package R.helper.test;

import java.util.ArrayList;

import R.helper.BindField;
import R.helper.EntityX;

/**
 * Created by duynk on 1/13/16.
 */
public class Student extends EntityX {
    @BindField("age")
    int age;

    @BindField("phone")
    ArrayList<String> phone;

    @BindField("living_address")
    Address livingAddress;

    @BindField("other_addresses")
    ArrayList<Address> otherAddresses;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public Address getLivingAddress() {
        return livingAddress;
    }

    public void setLivingAddress(Address livingAddress) {
        this.livingAddress = livingAddress;
    }

    public ArrayList<Address> getOtherAddresses() {
        return otherAddresses;
    }

    public void setOtherAddresses(ArrayList<Address> otherAddresses) {
        this.otherAddresses = otherAddresses;
    }

    public Student() {
        super();
    }
}