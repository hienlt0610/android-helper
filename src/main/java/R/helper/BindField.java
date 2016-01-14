package R.helper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by duynk on 1/14/16.
 */

@Retention(RUNTIME) @Target(FIELD)
public @interface BindField {
    String value();
}
