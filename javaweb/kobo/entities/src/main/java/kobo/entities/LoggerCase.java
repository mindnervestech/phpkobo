package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the logger_case database table.
 * 
 */
@Entity
@Table(name="logger_case")
@NamedQuery(name="LoggerCase.findAll", query="SELECT l FROM LoggerCase l")
public class LoggerCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="date_created")
	private Timestamp dateCreated;

	@Column(name="date_modified")
	private Timestamp dateModified;

	@Column(name="case_id")
	private String caseId;
	
	private String note;

	private String status;
	
	private double latitude;
	
	private double longitude;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="consultant_id")
	private AuthUser consultant;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser owner;

	//bi-directional many-to-one association to LoggerInstance
	/*@ManyToOne
	@JoinColumn(name="instance_id")
	private LoggerInstance loggerInstance;*/
	
	//bi-directional many-to-one association to LoggerCaseInstance
	@OneToMany(mappedBy="loggerCase", fetch = FetchType.EAGER)
	private List<LoggerCaseInstance> loggerCaseInstances;

	public LoggerCase() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AuthUser getOwner() {
		return this.owner;
	}

	public void setOwner(AuthUser authUser1) {
		this.owner = authUser1;
	}

	public AuthUser getConsultant() {
		return this.consultant;
	}

	public void setConsultant(AuthUser authUser2) {
		this.consultant = authUser2;
	}

	/*public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}*/

	public List<LoggerCaseInstance> getLoggerCaseInstances() {
		return loggerCaseInstances;
	}

	public void setLoggerCaseInstances(List<LoggerCaseInstance> loggerCaseInstances) {
		this.loggerCaseInstances = loggerCaseInstances;
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

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
}