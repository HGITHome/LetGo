package com.dgut.app.service;


import com.dgut.app.helper.ApplyWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseApplyFriend;
import com.dgut.main.member.manager.ApplyMng;
import com.dgut.main.member.manager.FriendshipMng;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.dgut.common.page.SimplePage.cpn;
import static com.dgut.common.page.SimplePage.cps;

/**
 * 好友申请service层
 * Created by PUNK on 2017/1/25.
 */
@Service
public class ApplyService {

    @Autowired
    private MemberMng memberMng;

    @Autowired
    private ApplyMng applyMng;

    @Autowired
    private EasemobService easemobService;

    @Autowired
    private FriendshipMng friendshipMng;

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    /**
     * OPT = 20 发起好友申请
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String addApplication(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        Member member = CmsUtils.getMember(request);
        //		Member member = memberMng.findById(1);
        String receiver_id = parameters.get("receiver_id");

        if (StringUtils.isBlank(receiver_id)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "目标用户id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        receiver_id = Encrypt
                .decrypt3DES(receiver_id, Constants.ENCRYPTION_KEY);
        String apply_reason = parameters.get("apply_reason");

        if (member.getId().equals(Integer.parseInt(receiver_id))) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "不能添加自己为好友");
            return JSONUtils.printObject(jsonMap);
        }
        // 判断是不是自己的好友
        Member receiver = memberMng.findById(Integer.parseInt(receiver_id));

        Set<Friendship> friendships = member.getFriendships();

        boolean flag = checkNotExist(receiver, friendships);
        if (flag) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "对方已是你的好友，添加失败");
            return JSONUtils.printObject(jsonMap);
        }

        // 刷新好友申请,更新申请理由
        applyMng.saveOrUpdate(member, receiver, apply_reason);

        jsonMap.put("state", 0);
        jsonMap.put("msg", "好友申请发送成功");
        memberLogMng.operating(request,"cms.member.applyFriend","username="+receiver.getUsername());

        return JSONUtils.printObject(jsonMap);
    }

    /**
     * 判断用户是否是自己好友
     *
     * @param receiver
     * @param friendships
     * @return
     */
    private boolean checkNotExist(Member receiver, Set<Friendship> friendships) {
        Member friend = null;
        for (Friendship f : friendships) {
            friend = f.getFriend();
            if (friend.equals(receiver)) {
                return true;
            }
        }
        return false;

    }

    /**
     * opt=21 处理好友申请
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String handleApply(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        String apply_id = parameters.get("apply_id");
        String handle_flag = parameters.get("handle_flag");
        String alias = parameters.get("alias");

        if (StringUtils.isBlank(apply_id) || StringUtils.isBlank(handle_flag)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "参数不完整");
            return JSONUtils.printObject(jsonMap);
        }

        Member member = CmsUtils.getMember(request);

        apply_id = Encrypt.decrypt3DES(apply_id, Constants.ENCRYPTION_KEY);
        ApplyFriend apply = applyMng.findById(Integer.parseInt(apply_id));

        // 若该申请不是待处理状态下，则不支持好友操作
        if (apply.getHandle_flag()!=ApplyFriend.ApplyFlag.UNHANDLED) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "当前状态下不支持该操作");
            return JSONUtils.printObject(jsonMap);

        }

        //是否有权限去处理申请
        if(!apply.getReceiver().equals(member)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "你没有权限去处理该申请");
            return JSONUtils.printObject(jsonMap);
        }

        apply.setIsRead(true);

        // 若通过(handle_flag=1)，加入好友圈
        if (Integer.parseInt(handle_flag)== ApplyFriend.ApplyFlag.ACCEPT.ordinal()) {

            // 环信保存
            JSONObject result = easemobService.addFriend(member
                    .getEasemob_name(), apply.getPublisher().getEasemob_name());
            if (result.getInt("responseStatus") != 200) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", "服务器内部错误");
            } else {
                apply.setReplyTime(new Date());
                apply.setHandle_flag(BaseApplyFriend.ApplyFlag.ACCEPT);
                applyMng.update(apply);
                // 服务端保存
                Member publisher = apply.getPublisher();


                //接受者添加好友关系
                friendshipMng.save(member,publisher , publisher.getUsername());

                //申请者添加好友关系
                friendshipMng.save(publisher,member, member.getUsername());

                //				friendMng.save(member, af.getReceiver());
                jsonMap.put("state", 0);
                jsonMap.put("msg", "处理好友申请成功");

            }
        }
        //拒绝申请
        else{
            apply.setReplyTime(new Date());
            apply.setHandle_flag(BaseApplyFriend.ApplyFlag.FAILURE);
            applyMng.update(apply);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "处理好友申请成功");
        }
        memberLogMng.operating(request, "cms.member.handleApply",handle_flag.equals("1")? "同意":"拒绝"+"friendship_id="+apply_id);
        return JSONUtils.printObject(jsonMap);

    }

    /**
     * opt=26 好友申请列表
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getApplications(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member=CmsUtils.getMember(request);
        //		Member member=memberMng.findById(3);
        String pageNo=parameters.get("pageNo");
        String pageSize=parameters.get("pageSize");
        Pagination pagination=applyMng.findByUser(member.getId(),cpn(pageNo),cps(pageSize));

        jsonMap.put("result", ApplyWrapper.convertApplys((List<ApplyFriend>) pagination.getList(),member));
        jsonMap.put("state", 0);
        jsonMap.put("msg", "获取好友申请列表成功");
        jsonMap.put("pageNo", cpn(pageNo));
        jsonMap.put("pageSize", cps(pageSize));
        jsonMap.put("totalCount", pagination.getTotalCount());

        return JSONUtils.printObject(jsonMap);

    }
}
