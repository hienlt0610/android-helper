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

        Student b = new Student();
        b.set(Student.NAME, "Khoa");

        Student c = new Student();
        c.set(Student.NAME, "Lan");

        School s = new School();
        s.set(School.NAME, "PTIT");
        s.set(School.LEVEL, 10);
        s.set(School.STUDENT, Arrays.asList(a, b, c));

        try {
            JSONObject j = s.exportToJsonObject();
            String jsonString = j.toString();

            School s2 = new School();
            try {
                s2.importData(new JSONObject(jsonString));
                System.out.println(s2);
            }catch (Exception E) {
                E.printStackTrace();
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
