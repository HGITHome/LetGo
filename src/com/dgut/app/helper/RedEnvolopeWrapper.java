package com.dgut.app.helper;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.RedEnvolopeReceiver;
import com.dgut.main.member.entity.base.BaseRedEnvolope;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by PUNK on 2017/4/4.
 */
public class RedEnvolopeWrapper {

    public static final DecimalFormat df = new DecimalFormat("0.00");


    public static Map<String, Object> converInfo(RedEnvolope bean,Member member) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", Encrypt.encrypt3DES(String.valueOf(bean.getId()), Constants.ENCRYPTION_KEY));
        result.put("sender", UserWrapper.convertMemberInfo(bean.getSender()));
        result.put("total", bean.getTotal());
        result.put("group", bean.getGroup()!=null?Encrypt.encrypt3DES(String.valueOf(bean.getGroup().getId()), Constants.ENCRYPTION_KEY):null);
        result.put("num", bean.getNum());
//        result.put("surplus",bean.getSurplus());
        result.put("current",getCurrent(bean.getIsPublic(),bean.getReceiverList()));
        result.put("type", bean.getType().ordinal());
        //随机红包，返回手气旺
        if(bean.getType().equals(BaseRedEnvolope.RedEnvolopeType.random)){
            result.put("luckyMan",bean.getLuckyMan());
        }
        result.put("sendTime", bean.getSendTime().getTime());
        result.put("content", bean.getContent());
        result.put("isPublic", bean.getIsPublic());
        result.put("memberList", convertMemberInfo(bean.getReceiverList(),member));

        //公有红包
        RedEnvolopeReceiver redEnvolopeReceiver = isInList(member,bean.getReceiverList());
        if(bean.getIsPublic()){
            if(redEnvolopeReceiver!=null){
                result.put("canOpen",false);
            }
            else{
                result.put("canOpen",true);
            }
        }
        //指定红包
        else{
            if(redEnvolopeReceiver!=null && redEnvolopeReceiver.getIsReceived()){
                result.put("canOpen",false);
            }
            else{
                result.put("canOpen",true);
            }
        }




        return result;
    }

    public static List<Map<String,Object>> convertList(List<RedEnvolope> list,Member member){
        List<Map<String,Object>> result = new ArrayList<>(list.size());
        for(RedEnvolope bean : list){
            result.add(converInfo(bean,member));
        }
        return result;
    }

    /**
     * 得到领取的人数
     * @param isPublic
     * @param receiverList
     * @return
     */
    private static int getCurrent(Boolean isPublic, List<RedEnvolopeReceiver> receiverList) {
        int current = 0;
        if(isPublic){
            current = receiverList==null?  0:receiverList.size();
        }
        else{
            for(RedEnvolopeReceiver receiver : receiverList){
                if(receiver.getIsReceived()){
                    current++;
                }
            }
        }
        return current;
    }

    /**
     * 得到领取人信息
     * @param list
     * @param member
     * @return
     */
    private static List<Map<String, Object>> convertMemberInfo(List<RedEnvolopeReceiver> list,Member member) {
        if(list!=null) {
        List<Map<String, Object>> result = new ArrayList<>(list.size());

            Map<String, Object> jsonMap = null;
            for (RedEnvolopeReceiver m : list) {
                jsonMap = new HashMap<>();
                jsonMap.put("member", FriendshipWrapper.convertMember(m.getReceiver(), member));
                jsonMap.put("amount", m.getAmount());
                jsonMap.put("isReceived", m.getIsReceived());
                jsonMap.put("receiveTime", m.getReceiveTime() == null ? null : m.getReceiveTime().getTime());
                result.add(jsonMap);
            }
            return result;
        }
        else {
            return null;
        }

    }

    /**
     * 是否在指定人内
     * @param member
     * @param receivers
     * @return
     */
    public static RedEnvolopeReceiver isInList(Member member, List<RedEnvolopeReceiver> receivers) {
        if(receivers!=null) {
            RedEnvolopeReceiver bean = null;
            for (RedEnvolopeReceiver rr : receivers) {
                if (rr.getReceiver().equals(member)) {
                    bean = rr;
                    break;
                }
            }
            return bean;
        }
        else{
            return null;
        }
    }

    /**
     * 手气红包
     * @param surplusMoney
     * @param surplusNum
     * @return
     */
    public  static Double randomAllocate(double surplusMoney, int surplusNum) {
        Random random = new Random();

        Double min = 0.01, max = 0.0;
        Double money =null;
        if(surplusNum!=1) {
            max = surplusMoney * 2 / surplusNum;
            money = Double.parseDouble(df.format(random.nextDouble() * max));
            if (money <= 0) {
                money = 0.01;
            }
        }
        else{
            money = Double.parseDouble(df.format(surplusMoney));
        }

       return money;
    }
}
