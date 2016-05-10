package org.koboc.collect.android.database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by User on 02-06-2015.
 */
public class AuthUser extends SugarRecord {

    private Long userId;
    private String api_token;
    private String role;
    private String logged;
    private String formListUser;
    private String username;
    private String password;
    private String first_name;
    private String last_name;



    public AuthUser(){}

    public AuthUser(Long userId, String api_token, String group, String logged) {
        this.userId = userId;
        this.api_token = api_token;
        this.role = group;
        this.logged = logged;
    }

    public String getLogged() {
        return logged;
    }

    public void setLogged(String logged) {
        this.logged = logged;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String group) {
        this.role = group;
    }

    public static AuthUser findLoggedInUser() {

        List<AuthUser> authUsers = AuthUser.listAll(AuthUser.class);//AuthUser.find(AuthUser.class,"logged = ?","true");
        if(authUsers != null && authUsers.size() == 1) {
            return authUsers.get(0);
        }
        return null;
    }

    public void setFormListUser(String formListUser) {
        this.formListUser = formListUser;
    }

    public String getFormListUser() {
        return this.formListUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
