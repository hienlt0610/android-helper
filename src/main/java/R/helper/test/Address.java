package R.helper.test;

import R.helper.BindField;
import R.helper.EntityX;

/**
 * Created by duynk on 1/14/16.
 */
public class Address extends EntityX {
    @BindField("street")
    String street;

    @BindField("ward")
    String ward;

    public Address() {}

    public Address(String street, String ward) {
        super();
        this.street = street;
        this.ward = ward;
    }
}
