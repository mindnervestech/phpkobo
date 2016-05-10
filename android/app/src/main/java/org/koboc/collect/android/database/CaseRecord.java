package org.koboc.collect.android.database;

import com.orm.SugarRecord;

/**
 * Created by User on 02-06-2015.
 */
public class CaseRecord extends SugarRecord {
    public double longitude;
    public double latitude;
    public String address;

    public String displayId;
    public long caseId;
    public long uid;
    public String status;
    public String dateCreated;
    public String dateModified;
    public boolean isSent;

    public CaseRecord() {
    }

    public CaseRecord(String displayId,double longitude, double latitude, String address, long caseId, String status, String dateCreated, String dateModified,boolean isSent) {
        this.displayId = displayId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.caseId = caseId;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.isSent = isSent;
    }
}