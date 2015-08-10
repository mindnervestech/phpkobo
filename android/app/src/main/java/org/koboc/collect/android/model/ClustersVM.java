package org.koboc.collect.android.model;

/**
 * Created by User on 01-06-2015.
 */
public class ClustersVM {

    public Long id;
    public String name;
    public String sector_name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }
}
