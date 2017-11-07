package com.dgut.app.service;

import com.dgut.app.utils.JSONUtils;
import com.dgut.common.page.Pagination;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.entity.Accusation;
import com.dgut.main.entity.AccusationType;
import com.dgut.main.manager.AccusationMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PUNK on 2017/3/24.
 */
@Service
public class AccusationService {

    @Autowired
    private AccusationMng accusationMng;

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    @Autowired
    private MemberMng memberMng;

    /**
     * opt = 60 获得举报信息
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getData(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap();
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"category"});

        Map<String,Object> resultMap =  new HashMap<>();

        Pagination pagination = accusationMng.getCategory(null,1,Integer.MAX_VALUE);

        jsonMap.put("result",pagination.getList());

        jsonMap.put("state",0);
        jsonMap.put("msg","获取举报信息成功");


        return JSONObject.fromObject(jsonMap,config).toString();
    }

    /**
     * opt=61 举报用户
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String accuseUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        Member member = CmsUtils.getMember(request);

        String respondentId = parameters.get("respondent_id");
        String typeId = parameters.get("type_id");
        String content = parameters.get("content");
        if(StringUtils.isBlank(respondentId)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","被举报用户id不能为空");
            return  JSONUtils.printObject(jsonMap);
        }
        else if(StringUtils.isBlank(typeId)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","举报类型不能为空");
            return JSONUtils.printObject(jsonMap);
        }
        else if(StringUtils.isBlank(content)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","举报内容不能为空");
            return JSONUtils.printObject(jsonMap);
        }



        respondentId = Encrypt.decrypt3DES(respondentId, Constants.ENCRYPTION_KEY);
//        typeId = Encrypt.decrypt3DES(typeId,Constants.ENCRYPTION_KEY);


        AccusationType  type = accusationMng.findTypeById(Integer.parseInt(typeId));

        Member respondent = memberMng.findById(Integer.parseInt(respondentId));

        if(member.equals(respondent)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","不能举报自己");
            return JSONUtils.printObject(jsonMap);
        }



        accusationMng.addAccusation(member,respondent,content,type);

        jsonMap.put("state",0);
        jsonMap.put("msg","您的举报已受理");





        return JSONUtils.printObject(jsonMap);
    }
}
