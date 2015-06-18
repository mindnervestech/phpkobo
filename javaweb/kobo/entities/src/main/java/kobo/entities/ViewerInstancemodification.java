package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the viewer_instancemodification database table.
 * 
 */
@Entity
@Table(name="viewer_instancemodification")
@NamedQuery(name="ViewerInstancemodification.findAll", query="SELECT v FROM ViewerInstancemodification v")
public class ViewerInstancemodification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=50)
	private String action;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(nullable=false, length=50)
	private String xpath;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser authUser;

	//bi-directional many-to-one association to LoggerInstance
	@ManyToOne
	@JoinColumn(name="instance_id", nullable=false)
	private LoggerInstance loggerInstance;

	public ViewerInstancemodification() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}

}