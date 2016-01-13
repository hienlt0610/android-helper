package R.helper.test;

import android.test.suitebuilder.annotation.MediumTest;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by duynk on 1/13/16.
 */
public class Test {
    @MediumTest
    public static void testEntity() {
        Student a = new Student();
        a.set(Student.NAME, "Duy");
        a.set(Student.TAG, Arrays.asList("D1", "D2", "D3"));

        Clazz cc = new Clazz();
        cc.set(Clazz.STUDENT, Arrays.asList(a));

        School s = new School();
        s.set(School.NAME, "PTIT");
        s.set(School.LEVEL, 10);
        s.set(School.CLASS, Arrays.asList(cc));

        try {
            JSONObject j = s.exportToJsonObject();
            String jsonString = j.toString();
            System.out.println(jsonString);

            School s2 = new School();
            try {
                s2.importData(new JSONObject(jsonString));
                Clazz test_class = (Clazz)s2.getArray(School.CLASS).get(0);
                Student stu = (Student)test_class.getArray(Clazz.STUDENT).get(0);
                System.out.println(stu.get(Student.NAME, null));
            }catch (Exception E) {
                E.printStackTrace();
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
