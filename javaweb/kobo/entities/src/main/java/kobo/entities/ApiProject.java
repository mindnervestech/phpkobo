package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the api_project database table.
 * 
 */
@Entity
@Table(name="api_project")
@NamedQuery(name="ApiProject.findAll", query="SELECT a FROM ApiProject a")
public class ApiProject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(nullable=false)
	@Transient
	private Object metadata;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false)
	private Boolean shared;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="created_by_id", nullable=false)
	private AuthUser authUser1;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="organization_id", nullable=false)
	private AuthUser authUser2;

	//bi-directional many-to-one association to ApiProjectUserStar
	@OneToMany(mappedBy="apiProject")
	private List<ApiProjectUserStar> apiProjectUserStars;

	//bi-directional many-to-one association to ApiProjectxform
	@OneToMany(mappedBy="apiProject")
	private List<ApiProjectxform> apiProjectxforms;

	//bi-directional many-to-one association to ApiTeamProject
	@OneToMany(mappedBy="apiProject")
	private List<ApiTeamProject> apiTeamProjects;

	public ApiProject() {
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

	public Object getMetadata() {
		return this.metadata;
	}

	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getShared() {
		return this.shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	public AuthUser getAuthUser1() {
		return this.authUser1;
	}

	public void setAuthUser1(AuthUser authUser1) {
		this.authUser1 = authUser1;
	}

	public AuthUser getAuthUser2() {
		return this.authUser2;
	}

	public void setAuthUser2(AuthUser authUser2) {
		this.authUser2 = authUser2;
	}

	public List<ApiProjectUserStar> getApiProjectUserStars() {
		return this.apiProjectUserStars;
	}

	public void setApiProjectUserStars(List<ApiProjectUserStar> apiProjectUserStars) {
		this.apiProjectUserStars = apiProjectUserStars;
	}

	public ApiProjectUserStar addApiProjectUserStar(ApiProjectUserStar apiProjectUserStar) {
		getApiProjectUserStars().add(apiProjectUserStar);
		apiProjectUserStar.setApiProject(this);

		return apiProjectUserStar;
	}

	public ApiProjectUserStar removeApiProjectUserStar(ApiProjectUserStar apiProjectUserStar) {
		getApiProjectUserStars().remove(apiProjectUserStar);
		apiProjectUserStar.setApiProject(null);

		return apiProjectUserStar;
	}

	public List<ApiProjectxform> getApiProjectxforms() {
		return this.apiProjectxforms;
	}

	public void setApiProjectxforms(List<ApiProjectxform> apiProjectxforms) {
		this.apiProjectxforms = apiProjectxforms;
	}

	public ApiProjectxform addApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().add(apiProjectxform);
		apiProjectxform.setApiProject(this);

		return apiProjectxform;
	}

	public ApiProjectxform removeApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().remove(apiProjectxform);
		apiProjectxform.setApiProject(null);

		return apiProjectxform;
	}

	public List<ApiTeamProject> getApiTeamProjects() {
		return this.apiTeamProjects;
	}

	public void setApiTeamProjects(List<ApiTeamProject> apiTeamProjects) {
		this.apiTeamProjects = apiTeamProjects;
	}

	public ApiTeamProject addApiTeamProject(ApiTeamProject apiTeamProject) {
		getApiTeamProjects().add(apiTeamProject);
		apiTeamProject.setApiProject(this);

		return apiTeamProject;
	}

	public ApiTeamProject removeApiTeamProject(ApiTeamProject apiTeamProject) {
		getApiTeamProjects().remove(apiTeamProject);
		apiTeamProject.setApiProject(null);

		return apiTeamProject;
	}

}