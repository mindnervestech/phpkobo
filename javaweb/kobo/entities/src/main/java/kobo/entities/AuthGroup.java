package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the auth_group database table.
 * 
 */
@Entity
@Table(name="auth_group")
@NamedQuery(name="AuthGroup.findAll", query="SELECT a FROM AuthGroup a")
public class AuthGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=80)
	private String name;

	//bi-directional one-to-one association to ApiTeam
	@OneToOne(mappedBy="authGroup")
	private ApiTeam apiTeam;

	//bi-directional many-to-one association to AuthGroupPermission
	@OneToMany(mappedBy="authGroup")
	private List<AuthGroupPermission> authGroupPermissions;

	//bi-directional many-to-one association to AuthUserGroup
	@OneToMany(mappedBy="authGroup")
	private List<AuthUserGroup> authUserGroups;

	//bi-directional many-to-one association to GuardianGroupobjectpermission
	@OneToMany(mappedBy="authGroup")
	private List<GuardianGroupobjectpermission> guardianGroupobjectpermissions;

	public AuthGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApiTeam getApiTeam() {
		return this.apiTeam;
	}

	public void setApiTeam(ApiTeam apiTeam) {
		this.apiTeam = apiTeam;
	}

	public List<AuthGroupPermission> getAuthGroupPermissions() {
		return this.authGroupPermissions;
	}

	public void setAuthGroupPermissions(List<AuthGroupPermission> authGroupPermissions) {
		this.authGroupPermissions = authGroupPermissions;
	}

	public AuthGroupPermission addAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().add(authGroupPermission);
		authGroupPermission.setAuthGroup(this);

		return authGroupPermission;
	}

	public AuthGroupPermission removeAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().remove(authGroupPermission);
		authGroupPermission.setAuthGroup(null);

		return authGroupPermission;
	}

	public List<AuthUserGroup> getAuthUserGroups() {
		return this.authUserGroups;
	}

	public void setAuthUserGroups(List<AuthUserGroup> authUserGroups) {
		this.authUserGroups = authUserGroups;
	}

	public AuthUserGroup addAuthUserGroup(AuthUserGroup authUserGroup) {
		getAuthUserGroups().add(authUserGroup);
		authUserGroup.setAuthGroup(this);

		return authUserGroup;
	}

	public AuthUserGroup removeAuthUserGroup(AuthUserGroup authUserGroup) {
		getAuthUserGroups().remove(authUserGroup);
		authUserGroup.setAuthGroup(null);

		return authUserGroup;
	}

	public List<GuardianGroupobjectpermission> getGuardianGroupobjectpermissions() {
		return this.guardianGroupobjectpermissions;
	}

	public void setGuardianGroupobjectpermissions(List<GuardianGroupobjectpermission> guardianGroupobjectpermissions) {
		this.guardianGroupobjectpermissions = guardianGroupobjectpermissions;
	}

	public GuardianGroupobjectpermission addGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().add(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setAuthGroup(this);

		return guardianGroupobjectpermission;
	}

	public GuardianGroupobjectpermission removeGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().remove(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setAuthGroup(null);

		return guardianGroupobjectpermission;
	}

}