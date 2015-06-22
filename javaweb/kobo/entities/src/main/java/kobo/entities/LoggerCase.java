package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


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

	private String note;

	private String status;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="consultant_id")
	private AuthUser consultant;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser owner;

	//bi-directional many-to-one association to LoggerInstance
	@ManyToOne
	@JoinColumn(name="instance_id")
	private LoggerInstance loggerInstance;

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

	public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}

}