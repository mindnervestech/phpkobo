package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the viewer_export database table.
 * 
 */
@Entity
@Table(name="viewer_export")
@NamedQuery(name="ViewerExport.findAll", query="SELECT v FROM ViewerExport v")
public class ViewerExport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_on", nullable=false)
	private Timestamp createdOn;

	@Column(name="export_type", nullable=false, length=10)
	private String exportType;

	@Column(name="export_url", length=200)
	private String exportUrl;

	@Column(length=255)
	private String filedir;

	@Column(length=255)
	private String filename;

	@Column(name="internal_status", nullable=false)
	private Integer internalStatus;

	@Column(name="task_id", length=255)
	private String taskId;

	@Column(name="time_of_last_submission")
	private Timestamp timeOfLastSubmission;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id", nullable=false)
	private LoggerXform loggerXform;

	public ViewerExport() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getExportType() {
		return this.exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getExportUrl() {
		return this.exportUrl;
	}

	public void setExportUrl(String exportUrl) {
		this.exportUrl = exportUrl;
	}

	public String getFiledir() {
		return this.filedir;
	}

	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getInternalStatus() {
		return this.internalStatus;
	}

	public void setInternalStatus(Integer internalStatus) {
		this.internalStatus = internalStatus;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Timestamp getTimeOfLastSubmission() {
		return this.timeOfLastSubmission;
	}

	public void setTimeOfLastSubmission(Timestamp timeOfLastSubmission) {
		this.timeOfLastSubmission = timeOfLastSubmission;
	}

	public LoggerXform getLoggerXform() {
		return this.loggerXform;
	}

	public void setLoggerXform(LoggerXform loggerXform) {
		this.loggerXform = loggerXform;
	}

}