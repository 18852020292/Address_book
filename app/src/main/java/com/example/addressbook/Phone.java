package com.example.addressbook;

/**
 *
 * @author 刘杰
 */
public class Phone {
    private String name,
            phone1,
            phone2,
            houerPhone,
            officephone,
            address,
            remark;
    public Phone(){

    }
    public  String getName() {
        return name;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getHouerPhone() {
        return houerPhone;
    }

    public String getOfficephone() {
        return officephone;
    }

    public String getAddress() {
        return address;
    }

    public String getRemark() {
        return remark;
    }
    public Phone(String name, String phone1,String phone2,String houerPhone, String officephone,String address,String remark){
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.houerPhone = houerPhone;
        this.officephone = officephone;
        this.address = address;
        this.remark = remark;
    }

}
