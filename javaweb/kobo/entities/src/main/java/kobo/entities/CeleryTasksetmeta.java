package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the celery_tasksetmeta database table.
 * 
 */
@Entity
@Table(name="celery_tasksetmeta")
@NamedQuery(name="CeleryTasksetmeta.findAll", query="SELECT c FROM CeleryTasksetmeta c")
public class CeleryTasksetmeta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_done", nullable=false)
	private Timestamp dateDone;

	@Column(nullable=false)
	private Boolean hidden;

	@Column(nullable=false)
	private String result;

	@Column(name="taskset_id", nullable=false, length=255)
	private String tasksetId;

	public CeleryTasksetmeta() {
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

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTasksetId() {
		return this.tasksetId;
	}

	public void setTasksetId(String tasksetId) {
		this.tasksetId = tasksetId;
	}

}