package com.example.project;

public class UserProfile {

    public String userName;
    public String userPhone;
    public String userPassword;
    public String userBloodGroup;
    public String userAddress;


    public UserProfile(String userName, String userPassword, String userPhone, String userBloodGroup, String userAddress) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userBloodGroup = userBloodGroup;
        this.userAddress = userAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName()
    {
        return userName;
    }
    public String getUserPassword()
    {
        return userPassword;
    }
    public String getUserPhone()
    {
        return userPhone;
    }

    public String getUserBloodGroup() {
        return userBloodGroup;
    }

    public void setUserBloodGroup(String userBloodGroup) {
        this.userBloodGroup = userBloodGroup;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
