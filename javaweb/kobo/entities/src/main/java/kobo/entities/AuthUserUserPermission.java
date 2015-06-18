package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the auth_user_user_permissions database table.
 * 
 */
@Entity
@Table(name="auth_user_user_permissions")
@NamedQuery(name="AuthUserUserPermission.findAll", query="SELECT a FROM AuthUserUserPermission a")
public class AuthUserUserPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to AuthPermission
	@ManyToOne
	@JoinColumn(name="permission_id", nullable=false)
	private AuthPermission authPermission;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public AuthUserUserPermission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthPermission getAuthPermission() {
		return this.authPermission;
	}

	public void setAuthPermission(AuthPermission authPermission) {
		this.authPermission = authPermission;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}