package kobo.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the api_organizationprofile database table.
 * 
 */
@Entity
@Table(name="api_organizationprofile")
@NamedQuery(name="ApiOrganizationprofile.findAll", query="SELECT a FROM ApiOrganizationprofile a")
public class ApiOrganizationprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userprofile_ptr_id", unique=true, nullable=false)
	private Integer userprofilePtrId;

	@Column(name="is_organization", nullable=false)
	private Boolean isOrganization;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="creator_id", nullable=false)
	private AuthUser authUser;

	//bi-directional one-to-one association to MainUserprofile
	@OneToOne
	@JoinColumn(name="userprofile_ptr_id", nullable=false, insertable=false, updatable=false)
	private MainUserprofile mainUserprofile;

	public ApiOrganizationprofile() {
	}

	public Integer getUserprofilePtrId() {
		return this.userprofilePtrId;
	}

	public void setUserprofilePtrId(Integer userprofilePtrId) {
		this.userprofilePtrId = userprofilePtrId;
	}

	public Boolean getIsOrganization() {
		return this.isOrganization;
	}

	public void setIsOrganization(Boolean isOrganization) {
		this.isOrganization = isOrganization;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public MainUserprofile getMainUserprofile() {
		return this.mainUserprofile;
	}

	public void setMainUserprofile(MainUserprofile mainUserprofile) {
		this.mainUserprofile = mainUserprofile;
	}

}