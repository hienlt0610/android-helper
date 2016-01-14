package R.helper.test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by duynk on 1/13/16.
 */
public class Test {
    public static void testEntity() {
        Student a = new Student();
        a.setAge(10);
        a.setLivingAddress(new Address("abc", "xyz"));
        a.setPhone(new ArrayList<>(Arrays.asList("0123", "0234", "0124")));
        a.setOtherAddresses(new ArrayList<Address>(Arrays.asList(new Address("ad1", "ad1x"), new Address("ad2", "ad2x"))));
        System.out.println(a);

        Student b = new Student();
        try {
            b.importFromJson(a.exportToJson());
        } catch (Exception E) {
            E.printStackTrace();;
        }
        System.out.println(b);
    }
}
