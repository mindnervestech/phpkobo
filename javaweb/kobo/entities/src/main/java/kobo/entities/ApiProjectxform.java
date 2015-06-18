package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the api_projectxform database table.
 * 
 */
@Entity
@Table(name="api_projectxform")
@NamedQuery(name="ApiProjectxform.findAll", query="SELECT a FROM ApiProjectxform a")
public class ApiProjectxform implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to ApiProject
	@ManyToOne
	@JoinColumn(name="project_id", nullable=false)
	private ApiProject apiProject;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="created_by_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id", nullable=false)
	private LoggerXform loggerXform;

	public ApiProjectxform() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApiProject getApiProject() {
		return this.apiProject;
	}

	public void setApiProject(ApiProject apiProject) {
		this.apiProject = apiProject;
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