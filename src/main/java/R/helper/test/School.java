package R.helper.test;

import R.helper.BaseEntity;
import R.helper.EntityArrayField;
import R.helper.EntityField;

/**
 * Created by duynk on 1/13/16.
 */
public class School extends BaseEntity {
    @EntityField("name") public static String NAME;
    @EntityField(value = "level", type = Integer.class) public static String LEVEL;
    @EntityArrayField(value = "student", type = Student.class) public static String STUDENT;

    public School() {
    }
}
