package org.koboc.collect.android.model;

import java.util.List;

/**
 * Created by User on 01-06-2015.
 */
public class SectorVM {

    public long id;
    public String name;

    public List<ClustersVM> clustervm;
    public List<EmergencyContactVM> inc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClustersVM> getClustervm() {
        return clustervm;
    }

    public void setClustervm(List<ClustersVM> clustervm) {
        this.clustervm = clustervm;
    }

    public List<EmergencyContactVM> getInc() {
        return inc;
    }

    public void setInc(List<EmergencyContactVM> inc) {
        this.inc = inc;
    }
}
