package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the celery_taskmeta database table.
 * 
 */
@Entity
@Table(name="celery_taskmeta")
@NamedQuery(name="CeleryTaskmeta.findAll", query="SELECT c FROM CeleryTaskmeta c")
public class CeleryTaskmeta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_done", nullable=false)
	private Timestamp dateDone;

	@Column(nullable=false)
	private Boolean hidden;

	private String meta;

	private String result;

	@Column(nullable=false, length=50)
	private String status;

	@Column(name="task_id", nullable=false, length=255)
	private String taskId;

	private String traceback;

	public CeleryTaskmeta() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateDone() {
		return this.dateDone;
	}

	public void setDateDone(Timestamp dateDone) {
		this.dateDone = dateDone;
	}

	public Boolean getHidden() {
		return this.hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getMeta() {
		return this.meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTraceback() {
		return this.traceback;
	}

	public void setTraceback(String traceback) {
		this.traceback = traceback;
	}

}