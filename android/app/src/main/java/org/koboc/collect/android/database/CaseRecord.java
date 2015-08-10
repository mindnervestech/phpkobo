package org.koboc.collect.android.database;

import com.orm.SugarRecord;

/**
 * Created by User on 02-06-2015.
 */
public class CaseRecord extends SugarRecord<CaseRecord> {
    public double longitude;
    public double latitude;
    public String address;

    public long caseId;
    public String status;
    public String dateCreated;
    public String dateModified;

    public CaseRecord() {
    }

    public CaseRecord(double longitude, double latitude, String address, long caseId, String status, String dateCreated, String dateModified) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.caseId = caseId;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }
}
