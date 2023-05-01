package com.thebitanpaul.cubuddy;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private  String Name, chn, croom, shn, droom, phone, email, userEmail;


    public User() {
    }

    public User(String Name, String chn, String croom, String shn, String droom, String phone, String email, String userEmail) {
        this.Name = Name;
        this.chn = chn;
        this.shn = shn;
        this.phone = phone;
        this.email = email;
        this.userEmail = userEmail;
        this.croom = croom;
        this.droom = droom;

    }


    public String getName() {
        return Name;
    }

    public String getChn() {
        return chn;
    }

    public String getShn() {
        return shn;
    }

    public String getCroom() {
        return croom;
    }

    public String getDroom() {
        return droom;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public void setShn(String shn) {
        this.shn = shn;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
