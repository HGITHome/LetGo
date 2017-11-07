import com.dgut.common.pck.Encrypt;
import com.dgut.common.util.DateUtils;
import com.dgut.main.Constants;

import java.util.*;

/**
 * Created by PUNK on 2017/1/23.
 */
public class TestDemo2 {

    public static void main(String [] args){
        login();
        getRedRecords();

    }

    private static void getRedRecords() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("_t",t);
        map.put("isGet","1");
        map.put("opt","74");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void openRedBag() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("_t",t);
        map.put("id",Encrypt.encrypt3DES(String.valueOf(41),Constants.ENCRYPTION_KEY));
        map.put("opt","73");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void sendRedBag() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("total","1");
        map.put("num","2");
        map.put("type","0");
        map.put("_t",t);
        map.put("opt","72");
//        map.put("memberList",Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));
        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void accuseUser() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("respondent_id",Encrypt.encrypt3DES("1",Constants.ENCRYPTION_KEY));
        map.put("type_id","1");
        map.put("content","造谣");
        map.put("_t",t);
        map.put("opt","61");
        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void getAccusationData() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();


        map.put("_t",t);
        map.put("opt","60");


        String  _s =getMds(map);
        System.out.println(_s);
    }


    public static void deleteGroup() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("group_id",Encrypt.encrypt3DES("37",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","56");


        String  _s =getMds(map);
        System.out.println(_s);
    }


    public static void removerUserFromGroup() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("group_id",Encrypt.encrypt3DES("37",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","55");
        map.put("memberList",Encrypt.encrypt3DES("1",Constants.ENCRYPTION_KEY)+","+Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static void addUserToGroup() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("group_id",Encrypt.encrypt3DES("37",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","54");
        map.put("memberList",Encrypt.encrypt3DES("1",Constants.ENCRYPTION_KEY)+","+Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static void getGroups() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();


        map.put("_t",t);
        map.put("opt","57");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void changeOwner() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();

        map.put("group_id",Encrypt.encrypt3DES("11", Constants.ENCRYPTION_KEY));
        map.put("user_id",Encrypt.encrypt3DES("5",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","53");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void editGroup() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("groupName","呜呜开");
        map.put("isPublic","0");
        map.put("desc","吹水群");
        map.put("group_id",Encrypt.encrypt3DES("11", Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","52");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void createGroup(){
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("groupName","12603");
        map.put("approval","0");
        map.put("desc","吹水");
        map.put("memberList",Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","51");

        String  _s =getMds(map);
        System.out.println(_s);
    }


    private static void getCode(){
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("mobile","15818348376");

        map.put("_t",t);
        map.put("opt","0");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void regist() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("mobile","15818348376");
        map.put("pwd", Encrypt.encrypt3DES("123456", Constants.ENCRYPTION_KEY));
        map.put("username","小小");
        map.put("code","1388");
        map.put("_t",t);
        map.put("opt","1");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void indexTalk() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();


        map.put("_t",t);
        map.put("opt","32");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static void getUserTalk() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("userid",Encrypt.encrypt3DES("1",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","31");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void cancelLike() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("tap_id",Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","35");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void tapLike() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("talk_id",Encrypt.encrypt3DES("68",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","34");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void deleteComment() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("comment_id",Encrypt.encrypt3DES("4",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","37");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void deleteTalk() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("talk_id",Encrypt.encrypt3DES("69",Constants.ENCRYPTION_KEY));

        map.put("_t",t);
        map.put("opt","33");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void signTalk() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("talk_id",Encrypt.encrypt3DES("68",Constants.ENCRYPTION_KEY));
        map.put("content","赞");
        map.put("receiver_id","");

        map.put("_t",t);
        map.put("opt","36");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static void getUserList() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("easemob_names","csl,xwwu4");

        map.put("_t",t);
        map.put("opt","28");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void searchUser() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("mobile","15818401990");

        map.put("_t",t);
        map.put("opt","27");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static void getApplys() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("pageNo","1");
        map.put("pageSize","20");
        map.put("_t",t);
        map.put("opt","26");

        String  _s =getMds(map);
        System.out.println(_s);

    }

    private static void cancelFriendship() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("friendship_id",Encrypt.encrypt3DES("21",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","22");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void removeFromBlackList() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("friendship_id",Encrypt.encrypt3DES("21",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","24");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void getFriends() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("_t",t);
        map.put("opt","25");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void defriend() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("friendship_id",Encrypt.encrypt3DES("21",Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","23");

        String  _s =getMds(map);
        System.out.println(_s);
    }


    private static void handleApply() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("apply_id",Encrypt.encrypt3DES("21",Constants.ENCRYPTION_KEY));
        map.put("handle_flag", "1");
        map.put("_t",t);
        map.put("opt","21");

        String  _s =getMds(map);
        System.out.println(_s);
    }


    private static void addFriend() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("receiver_id",Encrypt.encrypt3DES("3",Constants.ENCRYPTION_KEY));
        map.put("apply_reason", "约?");
        map.put("_t",t);
        map.put("opt","20");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    private static void login() {
        String t= DateUtils.format1.format(new Date());
        Map<String,Object> map =new HashMap<>();
        map.put("mobile","15818348376");
        map.put("pwd", Encrypt.encrypt3DES("123456", Constants.ENCRYPTION_KEY));
//        map.put("pwd", Encrypt.encrypt3DES("123", Constants.ENCRYPTION_KEY));
        map.put("_t",t);
        map.put("opt","2");

        String  _s =getMds(map);
        System.out.println(_s);
    }

    public static String getMds(Map<String,Object> parameters){
        List<String> parameterNames = new ArrayList<String>(parameters.keySet());
        Collections.sort(parameterNames);


        StringBuffer signData = new StringBuffer();

        for (int i = 0; i < parameters.size(); i++) {
            signData.append((String)parameterNames.get(i) + "=" + (String)parameters.get(parameterNames.get(i)) + (i < parameters.size() - 1 ? "&" : ""));
        }

        System.out.println(signData);

        String result=Encrypt.MD5(signData + Constants.APP_ENCRYPTION_KEY, "utf-8");
        return result;

    }



}
