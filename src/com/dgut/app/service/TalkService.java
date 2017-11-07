package com.dgut.app.service;

import com.dgut.app.helper.*;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.*;
import com.dgut.main.member.entity.base.BaseFriendship;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.TalkCommentMng;
import com.dgut.main.member.manager.TalkLikeMng;
import com.dgut.main.member.manager.TalkMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dgut.common.page.SimplePage.cpn;
import static com.dgut.common.page.SimplePage.cps;

/**
 * Created by PUNK on 2017/1/31.
 */
@Service
public class TalkService {

    @Autowired
    private TalkMng talkMng;

    @Autowired
    private MemberMng memberMng;

    @Autowired
    private TalkLikeMng talkLikeMng;

    @Autowired
    private TalkCommentMng commentMng;

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    /**
     * opt=36 评论朋友圈
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String signTalk(HttpServletRequest request,
                           HttpServletResponse response, Map<String, String> parameters) throws IOException {

        Map<String ,Object> jsonMap=new HashMap<String,Object>();
        Member member= CmsUtils.getMember(request);
        //		Member member = memberMng.findById(1);
        Member receiver=null;
        String talk_id = parameters.get("talk_id");
        String content = parameters.get("content");
        String receiver_id = parameters.get("receiver_id");
        if(StringUtils.isBlank(talk_id) || StringUtils.isBlank(content)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "参数不完整");
            return JSONUtils.printObject(jsonMap);
        }

        if(StringUtils.isNotBlank(receiver_id)){
            if(member.getId().equals(receiver_id)){
                receiver_id=null;
            }
            else {
                receiver_id = Encrypt.decrypt3DES(receiver_id, Constants.ENCRYPTION_KEY);
                receiver = memberMng.findById(Integer.parseInt(receiver_id));
            }
        }

        talk_id=Encrypt.decrypt3DES(talk_id, Constants.ENCRYPTION_KEY);



        Talk talk=talkMng.findById(Integer.parseInt(talk_id));

        if(!member.equals(talk.getPublisher())){
            Friendship friendship= FriendshipWrapper.getFriendship(member, talk.getPublisher().getFriendships());
            if(friendship==null || !friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)){
                jsonMap.put("state", -1);
                jsonMap.put("msg", "非法操作，你无法评论该朋友圈");
                return JSONUtils.printObject(jsonMap);
            }
        }

        TalkComment talkComment=new TalkComment();
        talkComment.setPublisher(member);
        talkComment.setTalk(talk);
        talkComment.setMessage_time(new Date());
        talkComment.setContent(content);
        talkComment.setReceiver(receiver);
        talkComment.setDisabled(false);

        talk.getTalkComments().add(talkComment);
        talkMng.update(talk);

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> userMap = null,receiverMap = null;
        userMap= UserWrapper.convertMemberInfo(talk.getPublisher());
        userMap.put("alias",member.getUsername());

        if(receiver!=null){
            receiverMap  = UserWrapper.convertMemberInfo(receiver);
            Friendship friendship = FriendshipWrapper.getFriendship(receiver,member.getFriendships());
            if(friendship!=null && !friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST)){
                userMap.put("alias",friendship.getAlias());
            }
        }

        resultMap.put("user", userMap);
        resultMap.put("receiver", receiverMap);
        resultMap.put("message_time", talkComment.getMessage_time().getTime());


        resultMap.put("content", talkComment.getContent());
        resultMap.put("comment_id", Encrypt.encrypt3DES(talkComment.getId()+"", Constants.ENCRYPTION_KEY));


        jsonMap.put("result", resultMap);
        jsonMap.put("state", 0);
        jsonMap.put("msg", "评论成功");

        memberLogMng.operating(request,"cms.member.comment","说说id="+talk_id);

        return JSONUtils.printObject(jsonMap);
    }



    /**
     * opt = 37 删除朋友圈的评论
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String deleteComment(HttpServletRequest request,
                                HttpServletResponse response, Map<String, String> parameters) throws IOException {

        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member= CmsUtils.getMember(request);
//		Member member = memberMng.findById(1);
        String comment_id =parameters.get("comment_id");

        if(StringUtils.isBlank(comment_id)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "评论id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        comment_id =Encrypt.decrypt3DES(comment_id, Constants.ENCRYPTION_KEY);

        TalkComment comment=commentMng.findById(Integer.parseInt(comment_id));
        if(null!=comment){
            if(!member.equals(comment.getPublisher())){
                jsonMap.put("state", -1);
                jsonMap.put("msg", "你没有权限删除该评论");
                return JSONUtils.printObject(jsonMap);
            }
            commentMng.disabledComment(comment);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "删除评论成功");
            memberLogMng.operating(request,"cms.member.deleteComment","评论id="+comment_id);
        }
        else{
            jsonMap.put("state", -1);
            jsonMap.put("msg", "评论不存在");
        }
        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt 33 删除朋友圈
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String deleteTalk(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        String talk_id = parameters.get("talk_id");

        Member member=CmsUtils.getMember(request);
//		Member member = memberMng.findById(1);

        if (StringUtils.isBlank(talk_id)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "朋友圈id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        talk_id = Encrypt.decrypt3DES(talk_id, Constants.ENCRYPTION_KEY);

        Talk talk = talkMng.findById(Integer.parseInt(talk_id));
        if (!talk.getPublisher().equals(member)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "删除失败，你没有权限处理该朋友圈");
            return JSONUtils.printObject(jsonMap);
        }
        else if(talk.getDisabled()){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "非法操作");
            return JSONUtils.printObject(jsonMap);
        }

        talk = talkMng.disabledTalk(talk);

        jsonMap.put("state", 0);
        jsonMap.put("msg", "删除朋友圈成功");
        memberLogMng.operating(request,"cms.member.deleteTalk","说说id="+talk_id);

        return JSONUtils.printObject(jsonMap);
    }



    /**
     * opt=34 点赞
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String tapLike(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        Member member = CmsUtils.getMember(request);
        //				Member member = memberMng.findById(3);
        String talk_id = parameters.get("talk_id");
        if (StringUtils.isBlank(talk_id)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "说说id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        talk_id = Encrypt.decrypt3DES(talk_id, Constants.ENCRYPTION_KEY);

        Talk talk=talkMng.findById(Integer.parseInt(talk_id));
        Member publisher = talk.getPublisher();

        //非说说发布者
        if(!member.equals(talk.getPublisher())){
            // 得到点赞者和发布者的好友关系
            Friendship friendship=FriendshipWrapper.getNormalFriendship(member, publisher.getFriendships());
            if(friendship==null ){
                jsonMap.put("state", -1);
                jsonMap.put("msg", "非法操作，你无法点赞该朋友圈");
                return JSONUtils.printObject(jsonMap);
            }
        }

        List<TalkLike> talkLikeList=talk.getTalkLikes();
        TalkLike talkLike= TalkLikeWrapper.isInTalkLikeList(member, talkLikeList);


        if(talkLike!=null  ){
            if(talkLike.getDisabled()==false){
                jsonMap.put("state", -1);
                jsonMap.put("msg", "你已点赞该朋友圈，请勿重复点赞");
                return JSONUtils.printObject(jsonMap);
            }
            else{
                talkLike.setDisabled(false);
                talkLike.setMessage_time(new Date());
                talkLikeMng.update(talkLike);
            }

        }
        else {
            talkLike = new TalkLike();
            talkLike.setPublisher(member);
            talkLike.setTalk(talk);
            talkLike.setMessage_time(new Date());
            talkLike.setDisabled(false);
            talk.getTalkLikes().add(talkLike);
            talkMng.update(talk);
        }

        Map<String,Object> resultMap = null,userMap=null;
        userMap = UserWrapper.convertMemberInfo(member);
        userMap.put("alias",member.getUsername());

        resultMap = new HashMap<>();
        resultMap.put("user",userMap);
        resultMap.put("tap_time", talkLike.getMessage_time().getTime());
        resultMap.put("tap_id", Encrypt.encrypt3DES(talkLike.getId()+"", Constants.ENCRYPTION_KEY));

        jsonMap.put("result",resultMap);
        jsonMap.put("state", 0);
        jsonMap.put("msg", "点赞成功");

        memberLogMng.operating(request,"cms.member.tapLike","说说id="+talk_id);

        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt=35 取消点赞
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String cancelTapLike(HttpServletRequest request,
                                HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member =CmsUtils.getMember(request);
//				Member member = memberMng.findById(3);
        String tap_id = parameters.get("tap_id");

        if(StringUtils.isBlank(tap_id)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "点赞id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        tap_id=Encrypt.decrypt3DES(tap_id, Constants.ENCRYPTION_KEY);

        TalkLike talkLike= talkLikeMng.findById(Integer.parseInt(tap_id));

        if(!talkLike.getPublisher().equals(member)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "你没有权限处理该点赞信息");
            return JSONUtils.printObject(jsonMap);
        }

        talkLikeMng.disabledTapLike(talkLike);
        jsonMap.put("state", 0);
        jsonMap.put("mag", "取消点赞成功");

        memberLogMng.operating(request,"cms.member.cancelTapLike","说说id="+tap_id);

        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt=31 查看用户的朋友圈(陌生人之显示最新十条)
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String getUserTalk(HttpServletRequest request,
                              HttpServletResponse response, Map<String, String> parameters)
            throws IOException {
        JsonConfig config = new JsonConfig();
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        config.setExcludes(new String[] { "id", "member", "handler",
                "hibernateLazyInitializer" });

        Map<String, Object> jsonMap = new HashMap<String, Object>();


        Member member = CmsUtils.getMember(request);
        String userid = parameters.get("userid");

        if (StringUtils.isBlank(userid)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "用户id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        userid = Encrypt
                .decrypt3DES(userid, Constants.ENCRYPTION_KEY);

        String pageNo = parameters.get("pageNo");
        String pageSize = parameters.get("pageSize");

        Member user = memberMng.findById(Integer.parseInt(userid));


        Friendship friendship = FriendshipWrapper.getFriendship(user,member.getFriendships());
        Pagination pagination = null;



        if(!member.equals(user)) {
            //陌生人只显示最新十条
            if (friendship == null) {
                pagination = talkMng.findTalksByUser(user, 1, 10);
                jsonMap.put("result", TalkWrapper.covertTalkInfo(
                        (List<Talk>) pagination.getList(), member));
                jsonMap.put("totalCount", pagination.getTotalCount());
            }
            //黑名单用户不显示
            else if (friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST)) {
                jsonMap.put("result", null);
                jsonMap.put("totalCount", 0);
            }
            //本人查看
            else{
                pagination = talkMng.findTalksByUser(user,cpn(pageNo), cps(pageSize));
                jsonMap.put("result", TalkWrapper.covertTalkInfo(
                        (List<Talk>) pagination.getList(), member));
                jsonMap.put("totalCount", pagination.getTotalCount());
            }
        }
        else{
            //好友
            pagination = talkMng.findTalksByUser(
                    user, cpn(pageNo), cps(pageSize));
            jsonMap.put("result", TalkWrapper.covertTalkInfo(
                    (List<Talk>) pagination.getList(), member));
            jsonMap.put("totalCount", pagination.getTotalCount());
        }



        // boolean isMe=member.getId().equals(Integer.parseInt(searcher_id));

        jsonMap.put("state", 0);
        jsonMap.put("msg", "查看用户的朋友圈成功");
      /*  jsonMap.put("result", TalkWrapper.covertTalkInfo(
                (List<Talk>) pagination.getList(), member.getFriendships()));*/
        jsonMap.put("pageNo", cpn(pageNo));
        jsonMap.put("pageSize", cps(pageSize));
//        jsonMap.put("totalCount", pagination.getTotalCount());

        return JSONObject.fromObject(jsonMap, config).toString();
    }

    /**
     * OPT=32 首页朋友圈
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getTalkIndex(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        JsonConfig config = new JsonConfig();

        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        config.setExcludes(new String[]{"handler","hibernateLazyInitializer"});

        Map<String, Object> jsonMap = new HashMap<String, Object>();

        Member member = CmsUtils.getMember(request);
        //		 Member member=memberMng.findById(1);
        String pageNo = parameters.get("pageNo");
        String pageSize = parameters.get("pageSize");

        Pagination pagination = talkMng.getTalkList(member, cpn(pageNo),
                cps(pageSize));

        jsonMap.put("result", TalkWrapper.covertTalkInfo(
                (List<Talk>) pagination.getList(), member));
        jsonMap.put("pageNo", cpn(pageNo));
        jsonMap.put("pageSize", cps(pageSize));
        jsonMap.put("totalCount", pagination.getTotalCount());
        jsonMap.put("state", 0);
        jsonMap.put("msg", "查看首页朋友圈成功");
        System.out.println("首页朋友圈：" + JSONObject.fromObject(jsonMap,config));
//        return JSONUtils.printObject(jsonMap);
        return JSONObject.fromObject(jsonMap,config).toString();
    }
}
