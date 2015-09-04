package org.koboc.collect.android.model;

/**
 * Created by User on 01-06-2015.
 */
public class CaseVM {

    public long id;
    public String caseId;
    public String dateCreated;
    public String dateModified;
    public String note;
    public double longitude;
    public double latitude;

    public CaseVM(long id,String caseId, String dateCreated, String dateModified, String note, double longitude, double latitude) {
        this.id = id;
        this.caseId = caseId;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.note = note;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
