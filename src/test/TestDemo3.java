import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;

import java.util.*;

/**
 * Created by PUNK on 2017/3/16.
 */
public class TestDemo3 {

    public static void main(String[] args) {


//        System.out.println(Encrypt.encrypt3DES(String.valueOf(100.0),Constants.ENCRYPTION_KEY));

        System.out.println(Encrypt.encrypt3DES("Dgut@1880",Constants.ENCRYPTION_KEY));
       /*String [] a = new String[]{"a","b","c"};
        String [] b = new String[]{"a","d"};
       List list1 = Arrays.asList(a);
        List list2 = Arrays.asList(b);

        List list = new ArrayList(list1);
        list.remove("b");
        System.out.println(list);*/

       /* System.out.println(Encrypt.encrypt3DES("0.0", Constants.ENCRYPTION_KEY));

        String balanceStr = Encrypt.decrypt3DES("0B826D5A53070F8A",Constants.ENCRYPTION_KEY);
        Double balance = Double.parseDouble(balanceStr);
        if(balance==0){
            System.out.println(true);
        }
        else{
            System.out.println(false);
        }*/

         //并集
        /*List list = new ArrayList(list1);
        list.removeAll(list2);
        list.addAll(list2);
        System.out.println(list);*/

        //交集
        /*List list = new ArrayList(list1);
        list.retainAll(list2);
        System.out.println(list);*/

        //差集
      /*  List list = new ArrayList(list1);
        list.retainAll(list2);
        List<String> result = new ArrayList<>(list1);
        result.removeAll(list);
        System.out.println(result);*/

    }
}
