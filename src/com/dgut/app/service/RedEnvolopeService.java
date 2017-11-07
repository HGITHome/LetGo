package com.dgut.app.service;

import com.dgut.app.helper.ChatGroupWrapper;
import com.dgut.app.helper.RedEnvolopeWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.dao.MemberDao;
import com.dgut.main.member.dao.RedEnvolopeDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.RedEnvolopeReceiver;
import com.dgut.main.member.entity.base.BaseRedEnvolope;
import com.dgut.main.member.manager.RedEnvolopeMng;
import com.dgut.main.web.CmsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dgut.common.page.SimplePage.cpn;
import static com.dgut.common.page.SimplePage.cps;

/*
 * Created by PUNK on 2017/3/31.
 *
 **/

@Service
public class RedEnvolopeService {

    @Autowired
    private RedEnvolopeMng redBagMng;

    @Autowired
    private RedEnvolopeDao redEnvolopeDao;

    @Autowired
    private MemberDao memberDao;


    /**
     * opt=72 发红包
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String sendRedBag(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        String total = parameters.get("total");
        String num = parameters.get("num");
        String type = parameters.get("type");
        String users = parameters.get("memberList");
        String group_id = parameters.get("group_id");
        String content = parameters.get("content");

        Map<String, Object> jsonMap = new HashMap<String, Object>();


        Member member = CmsUtils.getMember(request);
        Double currentMoney = redEnvolopeDao.getTodaySenderMoney(member.getId());
        if(currentMoney==null){
            currentMoney = 0.0;
        }
        if(currentMoney+Double.parseDouble(total)>10000.0){
            jsonMap.put("state",-1);
            jsonMap.put("msg","当日红包不能超过10000");
            return JSONUtils.printObject(jsonMap);
        }


        Double balance = Double.parseDouble(Encrypt.decrypt3DES(member.getBalance(), Constants.ENCRYPTION_KEY));

        Double amount = Double.parseDouble(total);


        if (amount > balance) {
            jsonMap.put("state", -3);
            jsonMap.put("msg", "余额不足，请充值");
            return JSONUtils.printObject(jsonMap);
        }
        if(Integer.parseInt(num)>100){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "一次性最多只能发100个红包");
            return JSONUtils.printObject(jsonMap);
        }
        else if (amount / Integer.parseInt(num) < 0.01) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "单个红包不能低于0.01");
            return JSONUtils.printObject(jsonMap);
        }



        RedEnvolope bean = redBagMng.save(member, type, num, content,Double.parseDouble(total), balance, users,group_id);

        jsonMap.put("state", 0);
        jsonMap.put("state", "发红包成功");
        jsonMap.put("result", RedEnvolopeWrapper.converInfo(bean,member));


        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt=73 拆红包
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getRedBag(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        Map<String,Object> jsonMap = new HashMap<>();

        String id = parameters.get("id");
        String group_id = parameters.get("group_id");
        Member member = CmsUtils.getMember(request);
        if(StringUtils.isBlank(id)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","红包id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        id = Encrypt.decrypt3DES(id,Constants.ENCRYPTION_KEY);
        RedEnvolope bean = redBagMng.findById(Integer.parseInt(id));

        Double money = null;



        //红包是否超时
        if(bean.getIsInvalid()){
            jsonMap.put("state",-1);
            jsonMap.put("msg","该红包已超时，不可领取");
            return JSONUtils.printObject(jsonMap);
        }
       /* else if(bean.getIsPublic() && StringUtils.isBlank(group_id)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","公开红包缺乏群id");
            return JSONUtils.printObject(jsonMap);
        }*/

        List<RedEnvolopeReceiver> receivers = bean.getReceiverList();


            //指定人领取
            if(!bean.getIsPublic()){
                RedEnvolopeReceiver receiver=RedEnvolopeWrapper.isInList(member,receivers);
                //在名单内
                if(receiver!=null) {
                    //尚未领取
                    if (!receiver.getIsReceived()) {
                        //手气红包，需要记录此次红包金额
                        if (bean.getType().equals(BaseRedEnvolope.RedEnvolopeType.random)) {
                             money = RedEnvolopeWrapper.randomAllocate(bean.getSurplus(), getUnAllocatedNum(receivers));
                            receiver.setAmount(Double.parseDouble(RedEnvolopeWrapper.df.format(money)));
                        }
                        receiver.setIsReceived(true);
                        receiver.setReceiveTime(new Date());
                        redBagMng.getRedEnvolopeMoney(receiver);
                    }
                    else{
                        jsonMap.put("state",-1);
                        jsonMap.put("msg","你已经领取过该红包");
                        jsonMap.put("result",RedEnvolopeWrapper.converInfo(bean,member));
                        return JSONUtils.printObject(jsonMap);
                    }
                }
                else{
                    jsonMap.put("state",-1);
                    jsonMap.put("msg","你不在指定用户列表中，不能拆红包");
                    jsonMap.put("result",RedEnvolopeWrapper.converInfo(bean,member));
                    return JSONUtils.printObject(jsonMap);
                }

            }
            //群内随机抢
            else{
                //红包未领取完
                if(bean.getReceiverList().size()!=bean.getNum()) {
                    RedEnvolopeReceiver receiver1 = RedEnvolopeWrapper.isInList(member, receivers);
                    //判断是否领取
                    if (receiver1 != null) {
                        jsonMap.put("state", -1);
                        jsonMap.put("msg", "你已经领取过该红包");
                        jsonMap.put("result", RedEnvolopeWrapper.converInfo(bean,member));
                        return JSONUtils.printObject(jsonMap);
                    }
                    //待领取
                    else {
                        if(bean.getGroup()!=null) {
                            Boolean isExisted = ChatGroupWrapper.isInGroup(member, bean.getGroup());

                            if (!isExisted) {
                                jsonMap.put("state", -1);
                                jsonMap.put("msg","你不在该群组，不能领取该红包");
                                return  JSONUtils.printObject(jsonMap);
                            } else {
                                RedEnvolopeReceiver receiver2 = new RedEnvolopeReceiver();
                                receiver2.setRedEnvolope(bean);
                                receiver2.setReceiver(member);
                                receiver2.setIsReceived(true);
                                receiver2.setReceiveTime(new Date());
                                //普通红包
                                if (bean.getType().equals(BaseRedEnvolope.RedEnvolopeType.average)) {
                                    receiver2.setAmount(Double.parseDouble(RedEnvolopeWrapper.df.format(bean.getTotal() / bean.getNum())));
                                }
                                //手气红包
                                else if (bean.getType().equals(BaseRedEnvolope.RedEnvolopeType.random)) {
                                    money = RedEnvolopeWrapper.randomAllocate(bean.getSurplus(), bean.getNum() - bean.getReceiverList().size());
                                    receiver2.setAmount(Double.parseDouble(RedEnvolopeWrapper.df.format(money)));
                                }
                                bean.getReceiverList().add(receiver2);
                                redBagMng.getRedEnvolopeMoney(receiver2);
                            }
                        }
                    }
                }
            }

        jsonMap.put("state",0);
        jsonMap.put("msg","拆红包成功");
        jsonMap.put("result",RedEnvolopeWrapper.converInfo(bean,member));

        return JSONUtils.printObject(jsonMap);
    }

    /**
     * 得到指定人的待领取个数
     * @param receivers
     * @return
     */
    private int getUnAllocatedNum(List<RedEnvolopeReceiver> receivers) {
        int num = 0;
        for(RedEnvolopeReceiver receiver :receivers){
            if(!receiver.getIsReceived()){
                num++;
            }
        }
        return num;
    }

    /**
     * opt=74 查看红包记录
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getRecord(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Member member = CmsUtils.getMember(request);
        Map<String,Object> jsonMap = new HashMap<>();



        String isGet = parameters.get("isGet");
        String year = parameters.get("year");
        String pageNo = parameters.get("pageNo");
        String pageSize = parameters.get("pageSize");

        Pagination pagination = redBagMng.getRedEnvolopes(String.valueOf(member.getId()),null,null,isGet,year,null,null,cpn(pageNo),cps(pageSize));

        jsonMap.put("result",RedEnvolopeWrapper.convertList((List<RedEnvolope>) pagination.getList(),member));
        jsonMap.put("pageNo",cpn(pageNo));
        jsonMap.put("pageSize",cps(pageSize));
        jsonMap.put("totalCount",pagination.getTotalCount());
        jsonMap.put("state",0);
        jsonMap.put("msg","获取红包记录成功");
        return JSONUtils.printObject(jsonMap);
    }
}
