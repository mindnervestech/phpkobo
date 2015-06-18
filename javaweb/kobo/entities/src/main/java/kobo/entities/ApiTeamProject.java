package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the api_team_projects database table.
 * 
 */
@Entity
@Table(name="api_team_projects")
@NamedQuery(name="ApiTeamProject.findAll", query="SELECT a FROM ApiTeamProject a")
public class ApiTeamProject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to ApiProject
	@ManyToOne
	@JoinColumn(name="project_id", nullable=false)
	private ApiProject apiProject;

	//bi-directional many-to-one association to ApiTeam
	@ManyToOne
	@JoinColumn(name="team_id", nullable=false)
	private ApiTeam apiTeam;

	public ApiTeamProject() {
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

	public ApiTeam getApiTeam() {
		return this.apiTeam;
	}

	public void setApiTeam(ApiTeam apiTeam) {
		this.apiTeam = apiTeam;
	}

}