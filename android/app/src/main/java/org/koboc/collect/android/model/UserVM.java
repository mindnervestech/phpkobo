package org.koboc.collect.android.model;

import java.util.List;

/**
 * Created by User on 01-06-2015.
 */
public class UserVM {

    private Long id;
    private String api_token;
    private List<String> groups;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
