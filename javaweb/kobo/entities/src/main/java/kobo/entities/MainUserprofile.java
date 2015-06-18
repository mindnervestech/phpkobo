package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the main_userprofile database table.
 * 
 */
@Entity
@Table(name="main_userprofile")
@NamedQuery(name="MainUserprofile.findAll", query="SELECT m FROM MainUserprofile m")
public class MainUserprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String address;

	@Column(nullable=false, length=255)
	private String city;

	@Column(nullable=false, length=2)
	private String country;

	@Column(nullable=false, length=255)
	private String description;

	@Column(name="home_page", nullable=false, length=255)
	private String homePage;

	@Column(nullable=false)
	@Transient
	private Object metadata;

	@Column(nullable=false, length=255)
	private String name;

	@Column(name="num_of_submissions", nullable=false)
	private Integer numOfSubmissions;

	@Column(nullable=false, length=255)
	private String organization;

	@Column(nullable=false, length=30)
	private String phonenumber;

	@Column(name="require_auth", nullable=false)
	private Boolean requireAuth;

	@Column(nullable=false, length=255)
	private String twitter;

	//bi-directional one-to-one association to ApiOrganizationprofile
	@OneToOne(mappedBy="mainUserprofile")
	private ApiOrganizationprofile apiOrganizationprofile;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="created_by_id")
	private AuthUser authUser1;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser2;

	public MainUserprofile() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHomePage() {
		return this.homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public Object getMetadata() {
		return this.metadata;
	}

	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumOfSubmissions() {
		return this.numOfSubmissions;
	}

	public void setNumOfSubmissions(Integer numOfSubmissions) {
		this.numOfSubmissions = numOfSubmissions;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPhonenumber() {
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Boolean getRequireAuth() {
		return this.requireAuth;
	}

	public void setRequireAuth(Boolean requireAuth) {
		this.requireAuth = requireAuth;
	}

	public String getTwitter() {
		return this.twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public ApiOrganizationprofile getApiOrganizationprofile() {
		return this.apiOrganizationprofile;
	}

	public void setApiOrganizationprofile(ApiOrganizationprofile apiOrganizationprofile) {
		this.apiOrganizationprofile = apiOrganizationprofile;
	}

	public AuthUser getAuthUser1() {
		return this.authUser1;
	}

	public void setAuthUser1(AuthUser authUser1) {
		this.authUser1 = authUser1;
	}

	public AuthUser getAuthUser2() {
		return this.authUser2;
	}

	public void setAuthUser2(AuthUser authUser2) {
		this.authUser2 = authUser2;
	}

}