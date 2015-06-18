package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the auth_group_permissions database table.
 * 
 */
@Entity
@Table(name="auth_group_permissions")
@NamedQuery(name="AuthGroupPermission.findAll", query="SELECT a FROM AuthGroupPermission a")
public class AuthGroupPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to AuthGroup
	@ManyToOne
	@JoinColumn(name="group_id", nullable=false)
	private AuthGroup authGroup;

	//bi-directional many-to-one association to AuthPermission
	@ManyToOne
	@JoinColumn(name="permission_id", nullable=false)
	private AuthPermission authPermission;

	public AuthGroupPermission() {
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

	public AuthPermission getAuthPermission() {
		return this.authPermission;
	}

	public void setAuthPermission(AuthPermission authPermission) {
		this.authPermission = authPermission;
	}

}