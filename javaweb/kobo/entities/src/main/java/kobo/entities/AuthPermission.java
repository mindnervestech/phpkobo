package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the auth_permission database table.
 * 
 */
@Entity
@Table(name="auth_permission")
@NamedQuery(name="AuthPermission.findAll", query="SELECT a FROM AuthPermission a")
public class AuthPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=100)
	private String codename;

	@Column(nullable=false, length=50)
	private String name;

	//bi-directional many-to-one association to AuthGroupPermission
	@OneToMany(mappedBy="authPermission")
	private List<AuthGroupPermission> authGroupPermissions;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	//bi-directional many-to-one association to AuthUserUserPermission
	@OneToMany(mappedBy="authPermission")
	private List<AuthUserUserPermission> authUserUserPermissions;

	//bi-directional many-to-one association to GuardianGroupobjectpermission
	@OneToMany(mappedBy="authPermission")
	private List<GuardianGroupobjectpermission> guardianGroupobjectpermissions;

	//bi-directional many-to-one association to GuardianUserobjectpermission
	@OneToMany(mappedBy="authPermission")
	private List<GuardianUserobjectpermission> guardianUserobjectpermissions;

	public AuthPermission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AuthGroupPermission> getAuthGroupPermissions() {
		return this.authGroupPermissions;
	}

	public void setAuthGroupPermissions(List<AuthGroupPermission> authGroupPermissions) {
		this.authGroupPermissions = authGroupPermissions;
	}

	public AuthGroupPermission addAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().add(authGroupPermission);
		authGroupPermission.setAuthPermission(this);

		return authGroupPermission;
	}

	public AuthGroupPermission removeAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().remove(authGroupPermission);
		authGroupPermission.setAuthPermission(null);

		return authGroupPermission;
	}

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

	public List<AuthUserUserPermission> getAuthUserUserPermissions() {
		return this.authUserUserPermissions;
	}

	public void setAuthUserUserPermissions(List<AuthUserUserPermission> authUserUserPermissions) {
		this.authUserUserPermissions = authUserUserPermissions;
	}

	public AuthUserUserPermission addAuthUserUserPermission(AuthUserUserPermission authUserUserPermission) {
		getAuthUserUserPermissions().add(authUserUserPermission);
		authUserUserPermission.setAuthPermission(this);

		return authUserUserPermission;
	}

	public AuthUserUserPermission removeAuthUserUserPermission(AuthUserUserPermission authUserUserPermission) {
		getAuthUserUserPermissions().remove(authUserUserPermission);
		authUserUserPermission.setAuthPermission(null);

		return authUserUserPermission;
	}

	public List<GuardianGroupobjectpermission> getGuardianGroupobjectpermissions() {
		return this.guardianGroupobjectpermissions;
	}

	public void setGuardianGroupobjectpermissions(List<GuardianGroupobjectpermission> guardianGroupobjectpermissions) {
		this.guardianGroupobjectpermissions = guardianGroupobjectpermissions;
	}

	public GuardianGroupobjectpermission addGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().add(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setAuthPermission(this);

		return guardianGroupobjectpermission;
	}

	public GuardianGroupobjectpermission removeGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().remove(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setAuthPermission(null);

		return guardianGroupobjectpermission;
	}

	public List<GuardianUserobjectpermission> getGuardianUserobjectpermissions() {
		return this.guardianUserobjectpermissions;
	}

	public void setGuardianUserobjectpermissions(List<GuardianUserobjectpermission> guardianUserobjectpermissions) {
		this.guardianUserobjectpermissions = guardianUserobjectpermissions;
	}

	public GuardianUserobjectpermission addGuardianUserobjectpermission(GuardianUserobjectpermission guardianUserobjectpermission) {
		getGuardianUserobjectpermissions().add(guardianUserobjectpermission);
		guardianUserobjectpermission.setAuthPermission(this);

		return guardianUserobjectpermission;
	}

	public GuardianUserobjectpermission removeGuardianUserobjectpermission(GuardianUserobjectpermission guardianUserobjectpermission) {
		getGuardianUserobjectpermissions().remove(guardianUserobjectpermission);
		guardianUserobjectpermission.setAuthPermission(null);

		return guardianUserobjectpermission;
	}

}