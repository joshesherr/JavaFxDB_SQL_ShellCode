package org.example.javafxdb_sql_shellcode;

public class Person {


    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private String photo;
    private String DoB;

    public Person() {
        this.id = -1;
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.phone = "";
        this.email = "";
        this.photo = "";
        this.DoB = "";
    }

    /**
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param phone
     * @param address
     */
    public Person(Integer id, String firstName, String lastName, String email, String phone, String address, String DoB) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.photo = "";
        this.DoB = DoB;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoto() {
        return photo==null?"":photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDoB() {
        return DoB==null?"":DoB;
    }
    public void setDoB(String doB) {this.DoB = doB;}

}