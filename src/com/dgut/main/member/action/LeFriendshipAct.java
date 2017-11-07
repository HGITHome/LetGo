package com.dgut.main.member.action;


import com.dgut.app.service.ApplyService;
import com.dgut.app.service.EasemobService;
import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.base.BaseFriendship;
import com.dgut.main.member.manager.FriendshipMng;
import com.dgut.main.web.WebErrors;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/2/10.
 */
@Controller
@RequestMapping("friendship")
public class LeFriendshipAct {

    private static Logger log = LoggerFactory.getLogger(LeFriendshipAct.class);

    @Autowired
    private FriendshipMng friendshipMng;

    @Autowired
    private EasemobService easemobService;



    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    @RequestMapping("v_list.do")
    public String getFriendshipList(HttpServletRequest request,ModelMap model){

        String pageNo = request.getParameter("pageNo");
        String queryUsername = request.getParameter("queryUsername");
        String queryStatus =request.getParameter("queryStatus");

        Pagination pagination = friendshipMng.getList(queryUsername,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pageNo",pageNo);
        model.addAttribute("queryUsername",queryUsername);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("pagination",pagination);


        return "member/friendship/list";
    }

    @RequestMapping("v_update.do")
    public String update(HttpServletRequest request,ModelMap model){
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        WebErrors errors = validateUpdate(Integer.parseInt(id),type,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        Friendship bean = friendshipMng.findById(Integer.parseInt(id));

        JSONObject result = null;
        String user_easemobName = bean.getOwner().getEasemob_name();
        String friend_easemobName = bean.getFriend().getEasemob_name();
        //环信修改
        //解除好友
        if(type.equals("1")){
            result = easemobService.removeFriendship(user_easemobName,friend_easemobName);
        }
        //拉黑
        else if(type.equals("2")){
            result = easemobService.addToBlackList(user_easemobName,friend_easemobName);
        }
        //恢复好友关系
        else if(type.equals("3")){
            if(bean.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST)){
                result = easemobService.removeFromBlackList(user_easemobName,friend_easemobName);
            }
            else if(bean.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)){
                result = easemobService.addFriend(user_easemobName,friend_easemobName);
            }

        }
        if (result.getInt("responseStatus") != 200) {

            errors.addErrorCode("error.change","好友关系","环信服务器内部错误");
            return errors.showErrorPage(model);
        }
        else{
            //服务端修改
            friendshipMng.updateFriendship(bean,type);
            String updateContent = type.equals("1")?"解除关系":type.equals("2")? "拉黑好友":"恢复好友";
            adminLogMng.operating(request,"cms.friendship.update","更改好友关系为"+updateContent);
        }
        return "redirect:v_list.do";
    }

    private WebErrors validateUpdate(Integer id, String type,HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        Friendship bean = friendshipMng.findById(id);
        //type=1解除好友
        if(type.equals("1")){
            if(bean.getFriendship_status().equals(BaseFriendship.Friendship_status.DELETE)){
                errors.addErrorCode("error.change","好友关系","该状态不支持该操作");
                return errors;
            }
        }
        //type=2拉黑好友
        else if(type.equals("2")){
            if(!bean.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)){
                errors.addErrorCode("error.change","好友关系","该状态不支持该操作");
                return errors;
            }
        }
        //type=3恢复好友关系
        else if (type.equals("3")){
            if(bean.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)){
                errors.addErrorCode("error.change","好友关系","该状态不支持该操作");
                return errors;
            }
        }

        return errors;
    }

    private boolean vldExist(Integer id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        Friendship entity = friendshipMng.findById(id);
        if (errors.ifNotExist(entity, Friendship.class, id)) {
            return true;
        }
        return false;
    }
}
