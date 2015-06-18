package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the auth_user_groups database table.
 * 
 */
@Entity
@Table(name="auth_user_groups")
@NamedQuery(name="AuthUserGroup.findAll", query="SELECT a FROM AuthUserGroup a")
public class AuthUserGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to AuthGroup
	@ManyToOne
	@JoinColumn(name="group_id", nullable=false)
	private AuthGroup authGroup;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public AuthUserGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthGroup getAuthGroup() {
		return this.authGroup;
	}

	public void setAuthGroup(AuthGroup authGroup) {
		this.authGroup = authGroup;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}