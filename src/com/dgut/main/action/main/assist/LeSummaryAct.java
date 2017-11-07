package com.dgut.main.action.main.assist;

import com.dgut.app.utils.JSONUtils;
import com.dgut.main.manager.assist.DataSummaryMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolopeReceiver;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.RedEnvolopeMng;
import com.dgut.main.member.manager.TalkMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PUNK on 2017/4/13.
 */
@Controller
@RequestMapping("summary")
public class LeSummaryAct {



    @Autowired
    private RedEnvolopeMng  redEnvolopeMng;

    @Autowired
    private TalkMng talkMng;

    @Autowired
    private DataSummaryMng dataSummaryMng;





    @RequestMapping("redEnvolope.do")
    public String redEnvolopeSummary(HttpServletRequest request,HttpServletResponse response,ModelMap model){

        //土豪榜（按次数）
        List<Member> richMenByCount = redEnvolopeMng.getTop3RichMen("0");

        //土豪榜（按金额）
        List<Member> richMenByTotal = redEnvolopeMng.getTop3RichMen("1");

        //手气大人
        List<Member> luckyMen = redEnvolopeMng.getLuckyMen();
        //饼状图
        Map<String,Object> dataMap = dataSummaryMng.getRedEnvolopeData();
        //折线图
        Map<String,Object> timeMap = dataSummaryMng.getRedEnvolopeTimeInfo();


        model.put("richMenByCount",richMenByCount);
        model.put("richMenByTotal",richMenByTotal);
        model.put("dataMap",dataMap);
        model.put("timeList",timeMap.get("timeList"));
        model.put("countList",timeMap.get("summaryList"));
        model.put("luckyMen",luckyMen);
        return "summary/redEnvolope";
    }

    @RequestMapping("talk.do")
    public String talkSummary(HttpServletRequest request,HttpServletResponse response,ModelMap model){

        //分享榜（按次数）
        List<Member> shareMen = talkMng.getTop3ShareMen();

        //点赞狂人（按金额）
        List<Member> tapLikeMen = talkMng.getTop3TapLikeMen();

        //饼状图
        Map<String ,Object> dataMap = dataSummaryMng.getTalkData();

        //折线图
        Map<String,Object> timeMap = dataSummaryMng.getTalkTimeInfo();


        model.put("shareMen",shareMen);
        model.put("tapLikeMen",tapLikeMen);
        model.put("dataMap",dataMap);
        model.put("timeList",timeMap.get("timeList"));
        model.put("countList",timeMap.get("summaryList"));

        return "summary/talk";
    }




    @RequestMapping("getSexSummary.do")
    @ResponseBody
    public String getSex(ModelMap model) throws IOException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("categories",new String[]{"boy","girl"});
        map.put("values",new Integer[]{10,20});
//        map.put("男",20);
//        map.put("女",40);
//        model.put("sexMap",map);
        return JSONUtils.printObject(map);
    }
}
