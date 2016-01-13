package R.helper.test;

import R.helper.BaseEntity;
import R.helper.EntityArrayField;

/**
 * Created by duynk on 1/13/16.
 */
public class Clazz extends BaseEntity {
    @EntityArrayField(value = "student", type = Student.class) public static String STUDENT;
}
