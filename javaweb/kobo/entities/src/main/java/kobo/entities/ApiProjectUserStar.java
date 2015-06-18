package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the api_project_user_stars database table.
 * 
 */
@Entity
@Table(name="api_project_user_stars")
@NamedQuery(name="ApiProjectUserStar.findAll", query="SELECT a FROM ApiProjectUserStar a")
public class ApiProjectUserStar implements Serializable {
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
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public ApiProjectUserStar() {
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

}