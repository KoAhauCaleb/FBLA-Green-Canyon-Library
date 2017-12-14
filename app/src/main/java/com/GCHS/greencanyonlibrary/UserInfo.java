package com.GCHS.greencanyonlibrary;

/**
 * Created by User on 11/11/2017.
 */

public class UserInfo {
    boolean teacher;
    private String firstName;
    private String lastName;

    public UserInfo(){

    }

    public UserInfo(String F, String L, int T){
        firstName = F;
        lastName = L;
        teacher = T == 1;
    }

    public UserInfo(String F, String L, boolean T){
        firstName = F;
        lastName = L;
        teacher = T;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean getTeacher() {
        return teacher;
    }
}
