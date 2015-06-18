package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the guardian_userobjectpermission database table.
 * 
 */
@Entity
@Table(name="guardian_userobjectpermission")
@NamedQuery(name="GuardianUserobjectpermission.findAll", query="SELECT g FROM GuardianUserobjectpermission g")
public class GuardianUserobjectpermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="object_pk", nullable=false, length=255)
	private String objectPk;

	//bi-directional many-to-one association to AuthPermission
	@ManyToOne
	@JoinColumn(name="permission_id", nullable=false)
	private AuthPermission authPermission;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	public GuardianUserobjectpermission() {
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

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

}