package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the logger_instance database table.
 * 
 */
@Entity
@Table(name="logger_instance")
@NamedQuery(name="LoggerInstance.findAll", query="SELECT l FROM LoggerInstance l")
public class LoggerInstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="deleted_at")
	private Timestamp deletedAt;

	@Transient
	private Object geom;

	//@Transient
	@Column(nullable=false)
	private String json;

	@Column(nullable=false, length=20)
	private String status;

	@Column(nullable=false, length=249)
	private String uuid;

	@Column(nullable=false)
	private String xml;

	//bi-directional many-to-one association to LoggerAttachment
	@OneToMany(mappedBy="loggerInstance")
	private List<LoggerAttachment> loggerAttachments;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser authUser;

	//bi-directional many-to-one association to LoggerSurveytype
	@ManyToOne
	@JoinColumn(name="survey_type_id", nullable=false)
	private LoggerSurveytype loggerSurveytype;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id")
	private LoggerXform loggerXform;

	//bi-directional many-to-one association to LoggerInstancehistory
	@OneToMany(mappedBy="loggerInstance")
	private List<LoggerInstancehistory> loggerInstancehistories;

	//bi-directional many-to-one association to LoggerNote
	@OneToMany(mappedBy="loggerInstance")
	private List<LoggerNote> loggerNotes;

	//bi-directional many-to-one association to ViewerInstancemodification
	@OneToMany(mappedBy="loggerInstance")
	private List<ViewerInstancemodification> viewerInstancemodifications;

	//bi-directional many-to-one association to ViewerParsedinstance
	@OneToMany(mappedBy="loggerInstance")
	private List<ViewerParsedinstance> viewerParsedinstances;

	//bi-directional many-to-one association to LoggerCaseInstance
	@OneToMany(mappedBy="loggerInstance" , fetch = FetchType.EAGER)
	private List<LoggerCaseInstance> loggerCaseInstances;
	
	public LoggerInstance() {
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

	public Timestamp getDeletedAt() {
		return this.deletedAt;
	}

	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getGeom() {
		return this.geom;
	}

	public void setGeom(Object geom) {
		this.geom = geom;
	}

	public String getJson() {
		return this.json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	@JsonIgnore
	public List<LoggerAttachment> getLoggerAttachments() {
		return this.loggerAttachments;
	}

	public void setLoggerAttachments(List<LoggerAttachment> loggerAttachments) {
		this.loggerAttachments = loggerAttachments;
	}

	public LoggerAttachment addLoggerAttachment(LoggerAttachment loggerAttachment) {
		getLoggerAttachments().add(loggerAttachment);
		loggerAttachment.setLoggerInstance(this);

		return loggerAttachment;
	}

	public LoggerAttachment removeLoggerAttachment(LoggerAttachment loggerAttachment) {
		getLoggerAttachments().remove(loggerAttachment);
		loggerAttachment.setLoggerInstance(null);

		return loggerAttachment;
	}

	@JsonIgnore
	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	@JsonIgnore
	public LoggerSurveytype getLoggerSurveytype() {
		return this.loggerSurveytype;
	}

	public void setLoggerSurveytype(LoggerSurveytype loggerSurveytype) {
		this.loggerSurveytype = loggerSurveytype;
	}

	@JsonIgnore
	public LoggerXform getLoggerXform() {
		return this.loggerXform;
	}

	public void setLoggerXform(LoggerXform loggerXform) {
		this.loggerXform = loggerXform;
	}

	@JsonIgnore
	public List<LoggerInstancehistory> getLoggerInstancehistories() {
		return this.loggerInstancehistories;
	}

	public void setLoggerInstancehistories(List<LoggerInstancehistory> loggerInstancehistories) {
		this.loggerInstancehistories = loggerInstancehistories;
	}

	public LoggerInstancehistory addLoggerInstancehistory(LoggerInstancehistory loggerInstancehistory) {
		getLoggerInstancehistories().add(loggerInstancehistory);
		loggerInstancehistory.setLoggerInstance(this);

		return loggerInstancehistory;
	}

	public LoggerInstancehistory removeLoggerInstancehistory(LoggerInstancehistory loggerInstancehistory) {
		getLoggerInstancehistories().remove(loggerInstancehistory);
		loggerInstancehistory.setLoggerInstance(null);

		return loggerInstancehistory;
	}

	@JsonIgnore
	public List<LoggerNote> getLoggerNotes() {
		return this.loggerNotes;
	}

	public void setLoggerNotes(List<LoggerNote> loggerNotes) {
		this.loggerNotes = loggerNotes;
	}

	public LoggerNote addLoggerNote(LoggerNote loggerNote) {
		getLoggerNotes().add(loggerNote);
		loggerNote.setLoggerInstance(this);

		return loggerNote;
	}

	public LoggerNote removeLoggerNote(LoggerNote loggerNote) {
		getLoggerNotes().remove(loggerNote);
		loggerNote.setLoggerInstance(null);

		return loggerNote;
	}

	@JsonIgnore
	public List<ViewerInstancemodification> getViewerInstancemodifications() {
		return this.viewerInstancemodifications;
	}

	public void setViewerInstancemodifications(List<ViewerInstancemodification> viewerInstancemodifications) {
		this.viewerInstancemodifications = viewerInstancemodifications;
	}

	public ViewerInstancemodification addViewerInstancemodification(ViewerInstancemodification viewerInstancemodification) {
		getViewerInstancemodifications().add(viewerInstancemodification);
		viewerInstancemodification.setLoggerInstance(this);

		return viewerInstancemodification;
	}

	public ViewerInstancemodification removeViewerInstancemodification(ViewerInstancemodification viewerInstancemodification) {
		getViewerInstancemodifications().remove(viewerInstancemodification);
		viewerInstancemodification.setLoggerInstance(null);

		return viewerInstancemodification;
	}

	@JsonIgnore
	public List<ViewerParsedinstance> getViewerParsedinstances() {
		return this.viewerParsedinstances;
	}

	public void setViewerParsedinstances(List<ViewerParsedinstance> viewerParsedinstances) {
		this.viewerParsedinstances = viewerParsedinstances;
	}

	public ViewerParsedinstance addViewerParsedinstance(ViewerParsedinstance viewerParsedinstance) {
		getViewerParsedinstances().add(viewerParsedinstance);
		viewerParsedinstance.setLoggerInstance(this);

		return viewerParsedinstance;
	}

	public ViewerParsedinstance removeViewerParsedinstance(ViewerParsedinstance viewerParsedinstance) {
		getViewerParsedinstances().remove(viewerParsedinstance);
		viewerParsedinstance.setLoggerInstance(null);

		return viewerParsedinstance;
	}

}