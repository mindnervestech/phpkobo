package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the logger_xform database table.
 * 
 */
@Entity
@Table(name="logger_xform")
@NamedQuery(name="LoggerXform.findAll", query="SELECT l FROM LoggerXform l")
public class LoggerXform implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="allows_sms", nullable=false)
	private Boolean allowsSms;

	@Column(name="bamboo_dataset", nullable=false, length=60)
	private String bambooDataset;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	private String description;

	@Column(nullable=false)
	private Boolean downloadable;

	@Column(nullable=false)
	private Boolean encrypted;

	@Column(name="has_start_time", nullable=false)
	private Boolean hasStartTime;

	@Column(name="id_string", nullable=false, length=100)
	private String idString;

	@Column(name="instances_with_geopoints", nullable=false)
	private Boolean instancesWithGeopoints;

	@Column(nullable=false)
	private String json;

	@Column(name="last_submission_time")
	private Timestamp lastSubmissionTime;

	@Column(name="num_of_submissions", nullable=false)
	private Integer numOfSubmissions;

	@Column(name="require_auth", nullable=false)
	private Boolean requireAuth;

	@Column(nullable=false)
	private Boolean shared;

	@Column(name="shared_data", nullable=false)
	private Boolean sharedData;

	@Column(name="sms_id_string", nullable=false, length=100)
	private String smsIdString;

	@Column(nullable=false, length=255)
	private String title;

	@Column(nullable=false, length=32)
	private String uuid;

	@Column(length=100)
	private String xls;

	@Column(nullable=false)
	private String xml;

	//bi-directional many-to-one association to ApiProjectxform
	@OneToMany(mappedBy="loggerXform")
	private List<ApiProjectxform> apiProjectxforms;

	//bi-directional many-to-one association to LoggerInstance
	@OneToMany(mappedBy="loggerXform")
	private List<LoggerInstance> loggerInstances;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser authUser;

	//bi-directional many-to-one association to LoggerZiggyinstance
	@OneToMany(mappedBy="loggerXform")
	private List<LoggerZiggyinstance> loggerZiggyinstances;

	//bi-directional many-to-one association to MainMetadata
	@OneToMany(mappedBy="loggerXform")
	private List<MainMetadata> mainMetadata;

	//bi-directional many-to-one association to RestserviceRestservice
	@OneToMany(mappedBy="loggerXform")
	private List<RestserviceRestservice> restserviceRestservices;

	//bi-directional many-to-one association to ViewerExport
	@OneToMany(mappedBy="loggerXform")
	private List<ViewerExport> viewerExports;

	public LoggerXform() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getAllowsSms() {
		return this.allowsSms;
	}

	public void setAllowsSms(Boolean allowsSms) {
		this.allowsSms = allowsSms;
	}

	public String getBambooDataset() {
		return this.bambooDataset;
	}

	public void setBambooDataset(String bambooDataset) {
		this.bambooDataset = bambooDataset;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDownloadable() {
		return this.downloadable;
	}

	public void setDownloadable(Boolean downloadable) {
		this.downloadable = downloadable;
	}

	public Boolean getEncrypted() {
		return this.encrypted;
	}

	public void setEncrypted(Boolean encrypted) {
		this.encrypted = encrypted;
	}

	public Boolean getHasStartTime() {
		return this.hasStartTime;
	}

	public void setHasStartTime(Boolean hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	public String getIdString() {
		return this.idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public Boolean getInstancesWithGeopoints() {
		return this.instancesWithGeopoints;
	}

	public void setInstancesWithGeopoints(Boolean instancesWithGeopoints) {
		this.instancesWithGeopoints = instancesWithGeopoints;
	}

	public String getJson() {
		return this.json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Timestamp getLastSubmissionTime() {
		return this.lastSubmissionTime;
	}

	public void setLastSubmissionTime(Timestamp lastSubmissionTime) {
		this.lastSubmissionTime = lastSubmissionTime;
	}

	public Integer getNumOfSubmissions() {
		return this.numOfSubmissions;
	}

	public void setNumOfSubmissions(Integer numOfSubmissions) {
		this.numOfSubmissions = numOfSubmissions;
	}

	public Boolean getRequireAuth() {
		return this.requireAuth;
	}

	public void setRequireAuth(Boolean requireAuth) {
		this.requireAuth = requireAuth;
	}

	public Boolean getShared() {
		return this.shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	public Boolean getSharedData() {
		return this.sharedData;
	}

	public void setSharedData(Boolean sharedData) {
		this.sharedData = sharedData;
	}

	public String getSmsIdString() {
		return this.smsIdString;
	}

	public void setSmsIdString(String smsIdString) {
		this.smsIdString = smsIdString;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getXls() {
		return this.xls;
	}

	public void setXls(String xls) {
		this.xls = xls;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<ApiProjectxform> getApiProjectxforms() {
		return this.apiProjectxforms;
	}

	public void setApiProjectxforms(List<ApiProjectxform> apiProjectxforms) {
		this.apiProjectxforms = apiProjectxforms;
	}

	public ApiProjectxform addApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().add(apiProjectxform);
		apiProjectxform.setLoggerXform(this);

		return apiProjectxform;
	}

	public ApiProjectxform removeApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().remove(apiProjectxform);
		apiProjectxform.setLoggerXform(null);

		return apiProjectxform;
	}

	public List<LoggerInstance> getLoggerInstances() {
		return this.loggerInstances;
	}

	public void setLoggerInstances(List<LoggerInstance> loggerInstances) {
		this.loggerInstances = loggerInstances;
	}

	public LoggerInstance addLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().add(loggerInstance);
		loggerInstance.setLoggerXform(this);

		return loggerInstance;
	}

	public LoggerInstance removeLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().remove(loggerInstance);
		loggerInstance.setLoggerXform(null);

		return loggerInstance;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public List<LoggerZiggyinstance> getLoggerZiggyinstances() {
		return this.loggerZiggyinstances;
	}

	public void setLoggerZiggyinstances(List<LoggerZiggyinstance> loggerZiggyinstances) {
		this.loggerZiggyinstances = loggerZiggyinstances;
	}

	public LoggerZiggyinstance addLoggerZiggyinstance(LoggerZiggyinstance loggerZiggyinstance) {
		getLoggerZiggyinstances().add(loggerZiggyinstance);
		loggerZiggyinstance.setLoggerXform(this);

		return loggerZiggyinstance;
	}

	public LoggerZiggyinstance removeLoggerZiggyinstance(LoggerZiggyinstance loggerZiggyinstance) {
		getLoggerZiggyinstances().remove(loggerZiggyinstance);
		loggerZiggyinstance.setLoggerXform(null);

		return loggerZiggyinstance;
	}

	public List<MainMetadata> getMainMetadata() {
		return this.mainMetadata;
	}

	public void setMainMetadata(List<MainMetadata> mainMetadata) {
		this.mainMetadata = mainMetadata;
	}

	public MainMetadata addMainMetadata(MainMetadata mainMetadata) {
		getMainMetadata().add(mainMetadata);
		mainMetadata.setLoggerXform(this);

		return mainMetadata;
	}

	public MainMetadata removeMainMetadata(MainMetadata mainMetadata) {
		getMainMetadata().remove(mainMetadata);
		mainMetadata.setLoggerXform(null);

		return mainMetadata;
	}

	public List<RestserviceRestservice> getRestserviceRestservices() {
		return this.restserviceRestservices;
	}

	public void setRestserviceRestservices(List<RestserviceRestservice> restserviceRestservices) {
		this.restserviceRestservices = restserviceRestservices;
	}

	public RestserviceRestservice addRestserviceRestservice(RestserviceRestservice restserviceRestservice) {
		getRestserviceRestservices().add(restserviceRestservice);
		restserviceRestservice.setLoggerXform(this);

		return restserviceRestservice;
	}

	public RestserviceRestservice removeRestserviceRestservice(RestserviceRestservice restserviceRestservice) {
		getRestserviceRestservices().remove(restserviceRestservice);
		restserviceRestservice.setLoggerXform(null);

		return restserviceRestservice;
	}

	public List<ViewerExport> getViewerExports() {
		return this.viewerExports;
	}

	public void setViewerExports(List<ViewerExport> viewerExports) {
		this.viewerExports = viewerExports;
	}

	public ViewerExport addViewerExport(ViewerExport viewerExport) {
		getViewerExports().add(viewerExport);
		viewerExport.setLoggerXform(this);

		return viewerExport;
	}

	public ViewerExport removeViewerExport(ViewerExport viewerExport) {
		getViewerExports().remove(viewerExport);
		viewerExport.setLoggerXform(null);

		return viewerExport;
	}

}