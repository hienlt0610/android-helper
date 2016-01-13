package R.helper.test;

import R.helper.BaseEntity;
import R.helper.EntityArrayField;
import R.helper.EntityField;

/**
 * Created by duynk on 1/13/16.
 */
public class Student extends BaseEntity {
    @EntityField("name") public static String NAME;
    @EntityArrayField(value = "tag", type = String.class) public static String TAG;

    public Student() {
    }
}