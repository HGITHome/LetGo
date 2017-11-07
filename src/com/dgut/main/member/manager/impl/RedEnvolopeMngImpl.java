package com.dgut.main.member.manager.impl;

import com.dgut.app.helper.RedEnvolopeWrapper;
import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.dao.MemberDao;
import com.dgut.main.member.dao.RedEnvolopeDao;
import com.dgut.main.member.dao.RedEnvolopeReceiverDao;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.RedEnvolopeReceiver;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.base.BaseRedEnvolope;
import com.dgut.main.member.manager.ChatGroupMng;
import com.dgut.main.member.manager.RedEnvolopeMng;
import com.dgut.main.util.InfoConvertor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * Created by PUNK on 2017/3/31.
 *
 **/


@Service
@Transactional
public class RedEnvolopeMngImpl implements RedEnvolopeMng {

    @Autowired
    private RedEnvolopeDao redBagDao;

    @Autowired
    private RedEnvolopeReceiverDao receiverDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ChatGroupMng groupMng;

    @Override
    public RedEnvolope save(Member member, String type, String content,String num, Double total, Double balance, String users, String group_id) {
        RedEnvolope bean = new RedEnvolope();
        bean.setNum(Integer.parseInt(num));

        bean.setSender(member);
        bean.setSendTime(new Date());
        bean.setTotal(total);
        bean.setSurplus(total);
        bean.setContent(content);
        if(bean.getNum()==1){
            bean.setType(BaseRedEnvolope.RedEnvolopeType.average);
        }
        else {
            bean.setType(BaseRedEnvolope.RedEnvolopeType.valueOf(Integer.parseInt(type)));
        }
        bean.setIsInvalid(false);
        if(StringUtils.isNotBlank(users)){
            bean.setReceiverList(getMembers(users,bean));
            bean.setIsPublic(false);
        }
        else{
            bean.setReceiverList(null);
            bean.setIsPublic(true);
        }

        if(StringUtils.isNotBlank(group_id)){
            ChatGroup group = groupMng.findById(Integer.parseInt(group_id));
            bean.setGroup(group);
        }


        member.setBalance(Encrypt.encrypt3DES(String.valueOf(balance - total), Constants.ENCRYPTION_KEY));
        return redBagDao.save(bean);
    }

    @Override
    public RedEnvolope findById(int id) {
        return redBagDao.findById(id);
    }

    @Override
    public void getRedEnvolopeMoney(RedEnvolopeReceiver receiver) {

        //修改红包剩余金额
        RedEnvolope redEnvolope = receiver.getRedEnvolope();
        Double surplus = redEnvolope.getSurplus();

        redEnvolope.setSurplus(Double.parseDouble(RedEnvolopeWrapper.df.format(surplus-receiver.getAmount())));
        Updater<RedEnvolope> redEnvolopeUpdater = new Updater<RedEnvolope>(redEnvolope);
        redBagDao.updateByUpdater(redEnvolopeUpdater);

        if(redEnvolope.getType().equals(BaseRedEnvolope.RedEnvolopeType.random) && redEnvolope.getSurplus()==0.0){
            Member luckyMan = redBagDao.getLuckyMan(redEnvolope.getId());
            redEnvolope.setLuckyMan(luckyMan);
        }



        //修改账户余额
        Member member = receiver.getReceiver();
        Double balance = Double.parseDouble(Encrypt.decrypt3DES(member.getBalance(), Constants.ENCRYPTION_KEY));
        member.setBalance(Encrypt.encrypt3DES(String.valueOf(balance+receiver.getAmount()),Constants.ENCRYPTION_KEY));
        Updater<Member> memberUpdater = new Updater<Member>(member);
        memberDao.updateByUpdater(memberUpdater);





    }

    @Override
    public Pagination getRedEnvolopes(String user_id,String username, String type, String isGet, String year, String isPublic,String isInvalid,Integer pageNo, Integer pageSize) {
        Pagination pagination = null;



        //领取的红包
        if(StringUtils.isNotBlank(isGet) && isGet.equals("1")){
            pagination = redBagDao.getReceivedEnvolopes(user_id,year,pageNo,pageSize);
        }
        //发出的红包
        else{
            pagination = redBagDao.getSendRedEnvolopes(user_id,username,type,year,isPublic,isInvalid,pageNo,pageSize);
        }


        return pagination;
    }

    @Override
    public RedEnvolope update(RedEnvolope bean) {
        Updater<RedEnvolope> updater = new Updater<RedEnvolope>(bean);
        redBagDao.updateByUpdater(updater);
        return bean;
    }

    @Override
    public List<Member> getTop3RichMen(String type) {
        List<Object[]> resultList = redBagDao.getTop3RichMen( type);

        List<Member> memberList = new ArrayList<>(resultList.size());
        Member m =null;
        Object[] result = null;
        //按次数
        if(type.equals("0")){
            for(int i=0;i<resultList.size();i++ ){
                result = resultList.get(i);
                m =(Member) result[0];
                m.setCount((Long) result[1]);
                memberList.add(m);
            }
        }
        else if(type.equals("1")){
            for(int i=0;i<resultList.size();i++ ){
                result = resultList.get(i);
                m =(Member) result[0];
                m.setMoney((Double) result[2]);
                memberList.add(m);
            }
        }

        return memberList;
    }

    @Override
    public List<Member> getLuckyMen() {
        List<Object[]> list = redBagDao.getLuckyMen();
        List<Member> results =  new ArrayList<>(list.size());
        Object[] result = null;
        Member m =null;
        for(int i=0;i<list.size();i++){
            result = list.get(i);
            m = (Member) result[0];
            m.setCount((Long) result[1]);
            results.add(m);

        }
        return results;
    }

    @Override
    public List<Object[]> getSexInfo() {
        List<Object[]> list=redBagDao.getSexInfo();

        list =InfoConvertor.convertSexInfo(list);
        return list;
    }

    @Override
    public List<Object[]> getTypeInfo() {
        List<Object[]> list=redBagDao.getTypeInfo();
        for(Object[] objs :list){
            if(null!=objs && String.valueOf(objs[0]).equals("random")){
                objs[0] ="手气红包";
            }
            else if(null!=objs && String.valueOf(objs[0]).equals("average")){
                objs[0] ="普通红包";
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getTimeInfo() {
        List<Object[]> list = redBagDao.getTimeInfo();
        List<String> timeList = new ArrayList(list.size());
        List<String > scoreList = new ArrayList(list.size());
        Map<String,Object> resultMap = new HashMap<>();
        for(Object[] objs : list){
            timeList.add(String.valueOf(objs[0]));
            scoreList.add(String.valueOf(objs[1]));
        }
        resultMap.put("timeList",timeList);
        resultMap.put("summaryList",scoreList);

        return resultMap;
    }

    private List<RedEnvolopeReceiver> getMembers(String users, RedEnvolope redEnvolope) {

        String[] members = users.split(",");
        List<RedEnvolopeReceiver> list = new ArrayList<>(members.length);
        String user_id = null;
        Member member = null;
        for (String s : members) {
            user_id = Encrypt.decrypt3DES(s, Constants.ENCRYPTION_KEY);
            member = memberDao.findById(Integer.parseInt(user_id));

            //过滤掉发红包人
            if(member.equals(redEnvolope.getSender())){
                continue;
            }

            RedEnvolopeReceiver bean = new RedEnvolopeReceiver();
            bean.setIsReceived(false);

            bean.setReceiver(member);
            bean.setRedEnvolope(redEnvolope);
            list.add(bean);
        }

        if(redEnvolope.getNum()==members.length && members.length==1){
            list.get(0).setAmount(redEnvolope.getTotal());
        }
        return list;

    }
}
