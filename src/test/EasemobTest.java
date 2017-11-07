import com.dgut.common.easemob.api.ChatGroupAPI;
import com.dgut.common.easemob.api.IMUserAPI;
import com.dgut.common.easemob.comm.ClientContext;
import com.dgut.common.easemob.comm.EasemobRestAPIFactory;
import com.dgut.common.easemob.comm.body.ChatGroupBody;
import com.dgut.common.easemob.comm.body.GroupOwnerTransferBody;
import com.dgut.common.easemob.comm.body.UserNamesBody;
import com.dgut.common.easemob.comm.wrapper.BodyWrapper;
import com.dgut.common.easemob.comm.wrapper.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by PUNK on 2017/2/22.
 */
public class EasemobTest {

    static EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

	static ChatGroupAPI group = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);

    static IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);

    static final ObjectMapper mapper = new ObjectMapper();



	public static void testCreateChatGroup() throws JsonProcessingException {

		BodyWrapper userBody = new ChatGroupBody("邀请","吹水群",true,6L,false,"jjzi5",null);

        ResponseWrapper result= (ResponseWrapper) group.createChatGroup(userBody);
//	 ResponseWrapper result=(ResponseWrapper) user.getFriends(username);
        ObjectMapper mapper = new ObjectMapper();
        String body=mapper.writeValueAsString(result.getResponseBody());

        JSONObject obj=JSONObject.fromObject(body);
        JSONObject data = (JSONObject) obj.get("data");
        System.out.println(data.get("groupid"));
    }


    public static void editChatGroup(){
        BodyWrapper userBody = new ChatGroupBody("09232","吹水群",6L);
        Object result = group.modifyChatGroup("8846999355393",userBody);
        System.out.println(result);
    }

    public static void changeOwner(){
        BodyWrapper body = new GroupOwnerTransferBody("csl");
        Object result = group.transferChatGroupOwner("8846999355393",body);
        System.out.println(result);
    }

    public static void getAllGroups() throws JsonProcessingException {
        ResponseWrapper result = (ResponseWrapper) user.getUserJoinedGroups("jjzi5");
        String body=mapper.writeValueAsString(result.getResponseBody());

        JSONObject obj=JSONObject.fromObject(body);
        JSONArray data = (JSONArray) obj.get("data");
        System.out.println(data);
    }

    public static void addBatch(){
        BodyWrapper body = new UserNamesBody(new String[]{"csl","mm123"});
        Object result = (ResponseWrapper) group.addBatchUsersToChatGroup("10934376529923",body);
        JSONObject obj = JSONObject.fromObject(result);
        System.out.println(obj.getInt("responseStatus"));
    }

    public static void removeBatch() {

    }

    public  static void deleteGroup(){
//        10929568808961
        Object result = group.deleteChatGroup("10929568808961");
        System.out.println(result);
    }







	public static void main(String []args) throws JsonProcessingException {
        testLogin();
	}

    private static void testLogin() {
    }
}
