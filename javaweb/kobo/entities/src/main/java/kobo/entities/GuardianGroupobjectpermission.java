package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the guardian_groupobjectpermission database table.
 * 
 */
@Entity
@Table(name="guardian_groupobjectpermission")
@NamedQuery(name="GuardianGroupobjectpermission.findAll", query="SELECT g FROM GuardianGroupobjectpermission g")
public class GuardianGroupobjectpermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="object_pk", nullable=false, length=255)
	private String objectPk;

	//bi-directional many-to-one association to AuthGroup
	@ManyToOne
	@JoinColumn(name="group_id", nullable=false)
	private AuthGroup authGroup;

	//bi-directional many-to-one association to AuthPermission
	@ManyToOne
	@JoinColumn(name="permission_id", nullable=false)
	private AuthPermission authPermission;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	public GuardianGroupobjectpermission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObjectPk() {
		return this.objectPk;
	}

	public void setObjectPk(String objectPk) {
		this.objectPk = objectPk;
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

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

}