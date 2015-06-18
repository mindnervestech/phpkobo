package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the logger_ziggyinstance database table.
 * 
 */
@Entity
@Table(name="logger_ziggyinstance")
@NamedQuery(name="LoggerZiggyinstance.findAll", query="SELECT l FROM LoggerZiggyinstance l")
public class LoggerZiggyinstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="client_version")
	private Long clientVersion;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_deleted")
	private Timestamp dateDeleted;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="entity_id", nullable=false, length=249)
	private String entityId;

	@Column(name="form_instance", nullable=false)
	private String formInstance;

	@Column(name="form_version", nullable=false, length=10)
	private String formVersion;

	@Column(name="instance_id", nullable=false, length=249)
	private String instanceId;

	@Column(name="server_version", nullable=false)
	private Long serverVersion;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="reporter_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id")
	private LoggerXform loggerXform;

	public LoggerZiggyinstance() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getClientVersion() {
		return this.clientVersion;
	}

	public void setClientVersion(Long clientVersion) {
		this.clientVersion = clientVersion;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateDeleted() {
		return this.dateDeleted;
	}

	public void setDateDeleted(Timestamp dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getFormInstance() {
		return this.formInstance;
	}

	public void setFormInstance(String formInstance) {
		this.formInstance = formInstance;
	}

	public String getFormVersion() {
		return this.formVersion;
	}

	public void setFormVersion(String formVersion) {
		this.formVersion = formVersion;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Long getServerVersion() {
		return this.serverVersion;
	}

	public void setServerVersion(Long serverVersion) {
		this.serverVersion = serverVersion;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public LoggerXform getLoggerXform() {
		return this.loggerXform;
	}

	public void setLoggerXform(LoggerXform loggerXform) {
		this.loggerXform = loggerXform;
	}

}