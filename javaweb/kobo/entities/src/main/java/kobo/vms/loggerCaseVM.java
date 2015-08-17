package kobo.vms;

import java.sql.Timestamp;
import java.util.List;

import kobo.entities.LoggerCase;


public class loggerCaseVM  {

	private Integer id;

	private Timestamp dateCreated;

	private Timestamp dateModified;

	private String note;

	private String status;
	
	private double latitude;
	
	private double longitude;
	
	List<LoggerCase> inc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<LoggerCase> getInc() {
		return inc;
	}

	public void setInc(List<LoggerCase> inc) {
		this.inc = inc;
	}


	
}
