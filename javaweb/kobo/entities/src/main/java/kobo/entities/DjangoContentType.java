package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the django_content_type database table.
 * 
 */
@Entity
@Table(name="django_content_type")
@NamedQuery(name="DjangoContentType.findAll", query="SELECT d FROM DjangoContentType d")
public class DjangoContentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="app_label", nullable=false, length=100)
	private String appLabel;

	@Column(nullable=false, length=100)
	private String model;

	@Column(nullable=false, length=100)
	private String name;

	//bi-directional many-to-one association to AuthPermission
	@OneToMany(mappedBy="djangoContentType")
	private List<AuthPermission> authPermissions;

	//bi-directional many-to-one association to DjangoAdminLog
	@OneToMany(mappedBy="djangoContentType")
	private List<DjangoAdminLog> djangoAdminLogs;

	//bi-directional many-to-one association to GuardianGroupobjectpermission
	@OneToMany(mappedBy="djangoContentType")
	private List<GuardianGroupobjectpermission> guardianGroupobjectpermissions;

	//bi-directional many-to-one association to GuardianUserobjectpermission
	@OneToMany(mappedBy="djangoContentType")
	private List<GuardianUserobjectpermission> guardianUserobjectpermissions;

	//bi-directional many-to-one association to ReversionVersion
	@OneToMany(mappedBy="djangoContentType")
	private List<ReversionVersion> reversionVersions;

	//bi-directional many-to-one association to TaggitTaggeditem
	@OneToMany(mappedBy="djangoContentType")
	private List<TaggitTaggeditem> taggitTaggeditems;

	public DjangoContentType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppLabel() {
		return this.appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AuthPermission> getAuthPermissions() {
		return this.authPermissions;
	}

	public void setAuthPermissions(List<AuthPermission> authPermissions) {
		this.authPermissions = authPermissions;
	}

	public AuthPermission addAuthPermission(AuthPermission authPermission) {
		getAuthPermissions().add(authPermission);
		authPermission.setDjangoContentType(this);

		return authPermission;
	}

	public AuthPermission removeAuthPermission(AuthPermission authPermission) {
		getAuthPermissions().remove(authPermission);
		authPermission.setDjangoContentType(null);

		return authPermission;
	}

	public List<DjangoAdminLog> getDjangoAdminLogs() {
		return this.djangoAdminLogs;
	}

	public void setDjangoAdminLogs(List<DjangoAdminLog> djangoAdminLogs) {
		this.djangoAdminLogs = djangoAdminLogs;
	}

	public DjangoAdminLog addDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().add(djangoAdminLog);
		djangoAdminLog.setDjangoContentType(this);

		return djangoAdminLog;
	}

	public DjangoAdminLog removeDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().remove(djangoAdminLog);
		djangoAdminLog.setDjangoContentType(null);

		return djangoAdminLog;
	}

	public List<GuardianGroupobjectpermission> getGuardianGroupobjectpermissions() {
		return this.guardianGroupobjectpermissions;
	}

	public void setGuardianGroupobjectpermissions(List<GuardianGroupobjectpermission> guardianGroupobjectpermissions) {
		this.guardianGroupobjectpermissions = guardianGroupobjectpermissions;
	}

	public GuardianGroupobjectpermission addGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().add(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setDjangoContentType(this);

		return guardianGroupobjectpermission;
	}

	public GuardianGroupobjectpermission removeGuardianGroupobjectpermission(GuardianGroupobjectpermission guardianGroupobjectpermission) {
		getGuardianGroupobjectpermissions().remove(guardianGroupobjectpermission);
		guardianGroupobjectpermission.setDjangoContentType(null);

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
		guardianUserobjectpermission.setDjangoContentType(this);

		return guardianUserobjectpermission;
	}

	public GuardianUserobjectpermission removeGuardianUserobjectpermission(GuardianUserobjectpermission guardianUserobjectpermission) {
		getGuardianUserobjectpermissions().remove(guardianUserobjectpermission);
		guardianUserobjectpermission.setDjangoContentType(null);

		return guardianUserobjectpermission;
	}

	public List<ReversionVersion> getReversionVersions() {
		return this.reversionVersions;
	}

	public void setReversionVersions(List<ReversionVersion> reversionVersions) {
		this.reversionVersions = reversionVersions;
	}

	public ReversionVersion addReversionVersion(ReversionVersion reversionVersion) {
		getReversionVersions().add(reversionVersion);
		reversionVersion.setDjangoContentType(this);

		return reversionVersion;
	}

	public ReversionVersion removeReversionVersion(ReversionVersion reversionVersion) {
		getReversionVersions().remove(reversionVersion);
		reversionVersion.setDjangoContentType(null);

		return reversionVersion;
	}

	public List<TaggitTaggeditem> getTaggitTaggeditems() {
		return this.taggitTaggeditems;
	}

	public void setTaggitTaggeditems(List<TaggitTaggeditem> taggitTaggeditems) {
		this.taggitTaggeditems = taggitTaggeditems;
	}

	public TaggitTaggeditem addTaggitTaggeditem(TaggitTaggeditem taggitTaggeditem) {
		getTaggitTaggeditems().add(taggitTaggeditem);
		taggitTaggeditem.setDjangoContentType(this);

		return taggitTaggeditem;
	}

	public TaggitTaggeditem removeTaggitTaggeditem(TaggitTaggeditem taggitTaggeditem) {
		getTaggitTaggeditems().remove(taggitTaggeditem);
		taggitTaggeditem.setDjangoContentType(null);

		return taggitTaggeditem;
	}

}