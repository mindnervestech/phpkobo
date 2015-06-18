package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the api_team database table.
 * 
 */
@Entity
@Table(name="api_team")
@NamedQuery(name="ApiTeam.findAll", query="SELECT a FROM ApiTeam a")
public class ApiTeam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="group_ptr_id", unique=true, nullable=false)
	private Integer groupPtrId;

	@Column(name="date_created")
	private Timestamp dateCreated;

	@Column(name="date_modified")
	private Timestamp dateModified;

	//bi-directional one-to-one association to AuthGroup
	@OneToOne
	@JoinColumn(name="group_ptr_id", nullable=false, insertable=false, updatable=false)
	private AuthGroup authGroup;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="created_by_id")
	private AuthUser authUser1;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="organization_id", nullable=false)
	private AuthUser authUser2;

	//bi-directional many-to-one association to ApiTeamProject
	@OneToMany(mappedBy="apiTeam")
	private List<ApiTeamProject> apiTeamProjects;

	public ApiTeam() {
	}

	public Integer getGroupPtrId() {
		return this.groupPtrId;
	}

	public void setGroupPtrId(Integer groupPtrId) {
		this.groupPtrId = groupPtrId;
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

	public AuthGroup getAuthGroup() {
		return this.authGroup;
	}

	public void setAuthGroup(AuthGroup authGroup) {
		this.authGroup = authGroup;
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

	public List<ApiTeamProject> getApiTeamProjects() {
		return this.apiTeamProjects;
	}

	public void setApiTeamProjects(List<ApiTeamProject> apiTeamProjects) {
		this.apiTeamProjects = apiTeamProjects;
	}

	public ApiTeamProject addApiTeamProject(ApiTeamProject apiTeamProject) {
		getApiTeamProjects().add(apiTeamProject);
		apiTeamProject.setApiTeam(this);

		return apiTeamProject;
	}

	public ApiTeamProject removeApiTeamProject(ApiTeamProject apiTeamProject) {
		getApiTeamProjects().remove(apiTeamProject);
		apiTeamProject.setApiTeam(null);

		return apiTeamProject;
	}

}