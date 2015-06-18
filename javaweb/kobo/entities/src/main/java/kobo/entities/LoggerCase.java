package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


/**
 * The persistent class for the logger_note database table.
 * 
 */
@Entity
@Table(name="logger_case")
@NamedQuery(name="LoggerCase.findAll", query="SELECT l FROM LoggerCase l")
public class LoggerCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(nullable=false)
	private String note;

	//bi-directional many-to-one association to LoggerInstance
	@OneToOne
	@JoinColumn(name="instance_id")
	private LoggerInstance loggerInstance;
	
	@OneToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;
	
	@JsonIgnore
	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	@Column(name="longitude", nullable=false)
	private Double longitude;
	
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name="latitude", nullable=false)
	private Double latitude;


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

	public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}

}