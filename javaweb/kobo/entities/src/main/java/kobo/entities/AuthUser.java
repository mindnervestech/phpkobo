package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the auth_user database table.
 * 
 */
@Entity
@Table(name="auth_user")
@NamedQuery(name="AuthUser.findAll", query="SELECT a FROM AuthUser a")
public class AuthUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="date_joined", nullable=false)
	private Timestamp dateJoined;

	@Column(nullable=false, length=75)
	private String email;

	@Column(name="first_name", nullable=false, length=30)
	private String firstName;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="is_staff", nullable=false)
	private Boolean isStaff;

	@Column(name="is_superuser", nullable=false)
	private Boolean isSuperuser;

	@Column(name="last_login", nullable=false)
	private Timestamp lastLogin;

	@Column(name="last_name", nullable=false, length=30)
	private String lastName;

	@Column(nullable=false, length=128)
	private String password;

	@Column(nullable=false, length=30)
	private String username;

	//bi-directional many-to-one association to ApiOrganizationprofile
	@OneToMany(mappedBy="authUser")
	private List<ApiOrganizationprofile> apiOrganizationprofiles;

	//bi-directional many-to-one association to ApiProject
	@OneToMany(mappedBy="authUser1")
	private List<ApiProject> apiProjects1;

	//bi-directional many-to-one association to ApiProject
	@OneToMany(mappedBy="authUser2")
	private List<ApiProject> apiProjects2;

	//bi-directional many-to-one association to ApiProjectUserStar
	@OneToMany(mappedBy="authUser")
	private List<ApiProjectUserStar> apiProjectUserStars;

	//bi-directional many-to-one association to ApiProjectxform
	@OneToMany(mappedBy="authUser")
	private List<ApiProjectxform> apiProjectxforms;

	//bi-directional many-to-one association to ApiTeam
	@OneToMany(mappedBy="authUser1")
	private List<ApiTeam> apiTeams1;

	//bi-directional many-to-one association to ApiTeam
	@OneToMany(mappedBy="authUser2")
	private List<ApiTeam> apiTeams2;

	//bi-directional many-to-one association to AuthUserGroup
	@OneToMany(mappedBy="authUser")
	private List<AuthUserGroup> authUserGroups;

	//bi-directional many-to-one association to AuthUserUserPermission
	@OneToMany(mappedBy="authUser")
	private List<AuthUserUserPermission> authUserUserPermissions;

	//bi-directional many-to-one association to AuthtokenToken
	@OneToMany(mappedBy="authUser")
	private List<AuthtokenToken> authtokenTokens;

	//bi-directional many-to-one association to DjangoAdminLog
	@OneToMany(mappedBy="authUser")
	private List<DjangoAdminLog> djangoAdminLogs;

	//bi-directional many-to-one association to DjangoDigestPartialdigest
	@OneToMany(mappedBy="authUser")
	private List<DjangoDigestPartialdigest> djangoDigestPartialdigests;

	//bi-directional many-to-one association to DjangoDigestUsernonce
	@OneToMany(mappedBy="authUser")
	private List<DjangoDigestUsernonce> djangoDigestUsernonces;

	//bi-directional many-to-one association to GuardianUserobjectpermission
	@OneToMany(mappedBy="authUser")
	private List<GuardianUserobjectpermission> guardianUserobjectpermissions;

	//bi-directional many-to-one association to KoboformSurveydraft
	@OneToMany(mappedBy="authUser")
	private List<KoboformSurveydraft> koboformSurveydrafts;

	//bi-directional many-to-one association to LoggerInstance
	@OneToMany(mappedBy="authUser")
	private List<LoggerInstance> loggerInstances;

	//bi-directional many-to-one association to LoggerXform
	@OneToMany(mappedBy="authUser")
	private List<LoggerXform> loggerXforms;

	//bi-directional many-to-one association to LoggerZiggyinstance
	@OneToMany(mappedBy="authUser")
	private List<LoggerZiggyinstance> loggerZiggyinstances;

	//bi-directional one-to-one association to MainTokenstoragemodel
	@OneToOne(mappedBy="authUser")
	private MainTokenstoragemodel mainTokenstoragemodel;

	//bi-directional many-to-one association to MainUserprofile
	@OneToMany(mappedBy="authUser1")
	private List<MainUserprofile> mainUserprofiles1;

	//bi-directional many-to-one association to MainUserprofile
	@OneToMany(mappedBy="authUser2")
	private List<MainUserprofile> mainUserprofiles2;

	//bi-directional many-to-one association to Oauth2ProviderAccesstoken
	@OneToMany(mappedBy="authUser")
	private List<Oauth2ProviderAccesstoken> oauth2ProviderAccesstokens;

	//bi-directional many-to-one association to Oauth2ProviderApplication
	@OneToMany(mappedBy="authUser")
	private List<Oauth2ProviderApplication> oauth2ProviderApplications;

	//bi-directional many-to-one association to Oauth2ProviderGrant
	@OneToMany(mappedBy="authUser")
	private List<Oauth2ProviderGrant> oauth2ProviderGrants;

	//bi-directional many-to-one association to Oauth2ProviderRefreshtoken
	@OneToMany(mappedBy="authUser")
	private List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens;

	//bi-directional many-to-one association to RegistrationRegistrationprofile
	@OneToMany(mappedBy="authUser")
	private List<RegistrationRegistrationprofile> registrationRegistrationprofiles;

	//bi-directional many-to-one association to ReversionRevision
	@OneToMany(mappedBy="authUser")
	private List<ReversionRevision> reversionRevisions;

	//bi-directional many-to-one association to ViewerInstancemodification
	@OneToMany(mappedBy="authUser")
	private List<ViewerInstancemodification> viewerInstancemodifications;

	public AuthUser() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateJoined() {
		return this.dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsStaff() {
		return this.isStaff;
	}

	public void setIsStaff(Boolean isStaff) {
		this.isStaff = isStaff;
	}

	public Boolean getIsSuperuser() {
		return this.isSuperuser;
	}

	public void setIsSuperuser(Boolean isSuperuser) {
		this.isSuperuser = isSuperuser;
	}

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public List<ApiOrganizationprofile> getApiOrganizationprofiles() {
		return this.apiOrganizationprofiles;
	}

	public void setApiOrganizationprofiles(List<ApiOrganizationprofile> apiOrganizationprofiles) {
		this.apiOrganizationprofiles = apiOrganizationprofiles;
	}

	public ApiOrganizationprofile addApiOrganizationprofile(ApiOrganizationprofile apiOrganizationprofile) {
		getApiOrganizationprofiles().add(apiOrganizationprofile);
		apiOrganizationprofile.setAuthUser(this);

		return apiOrganizationprofile;
	}

	public ApiOrganizationprofile removeApiOrganizationprofile(ApiOrganizationprofile apiOrganizationprofile) {
		getApiOrganizationprofiles().remove(apiOrganizationprofile);
		apiOrganizationprofile.setAuthUser(null);

		return apiOrganizationprofile;
	}

	@JsonIgnore
	public List<ApiProject> getApiProjects1() {
		return this.apiProjects1;
	}

	public void setApiProjects1(List<ApiProject> apiProjects1) {
		this.apiProjects1 = apiProjects1;
	}

	public ApiProject addApiProjects1(ApiProject apiProjects1) {
		getApiProjects1().add(apiProjects1);
		apiProjects1.setAuthUser1(this);

		return apiProjects1;
	}

	public ApiProject removeApiProjects1(ApiProject apiProjects1) {
		getApiProjects1().remove(apiProjects1);
		apiProjects1.setAuthUser1(null);

		return apiProjects1;
	}

	@JsonIgnore
	public List<ApiProject> getApiProjects2() {
		return this.apiProjects2;
	}

	public void setApiProjects2(List<ApiProject> apiProjects2) {
		this.apiProjects2 = apiProjects2;
	}

	public ApiProject addApiProjects2(ApiProject apiProjects2) {
		getApiProjects2().add(apiProjects2);
		apiProjects2.setAuthUser2(this);

		return apiProjects2;
	}

	public ApiProject removeApiProjects2(ApiProject apiProjects2) {
		getApiProjects2().remove(apiProjects2);
		apiProjects2.setAuthUser2(null);

		return apiProjects2;
	}

	@JsonIgnore
	public List<ApiProjectUserStar> getApiProjectUserStars() {
		return this.apiProjectUserStars;
	}

	public void setApiProjectUserStars(List<ApiProjectUserStar> apiProjectUserStars) {
		this.apiProjectUserStars = apiProjectUserStars;
	}

	public ApiProjectUserStar addApiProjectUserStar(ApiProjectUserStar apiProjectUserStar) {
		getApiProjectUserStars().add(apiProjectUserStar);
		apiProjectUserStar.setAuthUser(this);

		return apiProjectUserStar;
	}

	public ApiProjectUserStar removeApiProjectUserStar(ApiProjectUserStar apiProjectUserStar) {
		getApiProjectUserStars().remove(apiProjectUserStar);
		apiProjectUserStar.setAuthUser(null);

		return apiProjectUserStar;
	}

	@JsonIgnore
	public List<ApiProjectxform> getApiProjectxforms() {
		return this.apiProjectxforms;
	}

	public void setApiProjectxforms(List<ApiProjectxform> apiProjectxforms) {
		this.apiProjectxforms = apiProjectxforms;
	}

	public ApiProjectxform addApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().add(apiProjectxform);
		apiProjectxform.setAuthUser(this);

		return apiProjectxform;
	}

	public ApiProjectxform removeApiProjectxform(ApiProjectxform apiProjectxform) {
		getApiProjectxforms().remove(apiProjectxform);
		apiProjectxform.setAuthUser(null);

		return apiProjectxform;
	}

	@JsonIgnore
	public List<ApiTeam> getApiTeams1() {
		return this.apiTeams1;
	}

	public void setApiTeams1(List<ApiTeam> apiTeams1) {
		this.apiTeams1 = apiTeams1;
	}

	public ApiTeam addApiTeams1(ApiTeam apiTeams1) {
		getApiTeams1().add(apiTeams1);
		apiTeams1.setAuthUser1(this);

		return apiTeams1;
	}

	public ApiTeam removeApiTeams1(ApiTeam apiTeams1) {
		getApiTeams1().remove(apiTeams1);
		apiTeams1.setAuthUser1(null);

		return apiTeams1;
	}

	@JsonIgnore
	public List<ApiTeam> getApiTeams2() {
		return this.apiTeams2;
	}

	public void setApiTeams2(List<ApiTeam> apiTeams2) {
		this.apiTeams2 = apiTeams2;
	}

	public ApiTeam addApiTeams2(ApiTeam apiTeams2) {
		getApiTeams2().add(apiTeams2);
		apiTeams2.setAuthUser2(this);

		return apiTeams2;
	}

	public ApiTeam removeApiTeams2(ApiTeam apiTeams2) {
		getApiTeams2().remove(apiTeams2);
		apiTeams2.setAuthUser2(null);

		return apiTeams2;
	}

	@JsonIgnore
	public List<AuthUserGroup> getAuthUserGroups() {
		return this.authUserGroups;
	}

	public void setAuthUserGroups(List<AuthUserGroup> authUserGroups) {
		this.authUserGroups = authUserGroups;
	}

	public AuthUserGroup addAuthUserGroup(AuthUserGroup authUserGroup) {
		getAuthUserGroups().add(authUserGroup);
		authUserGroup.setAuthUser(this);

		return authUserGroup;
	}

	public AuthUserGroup removeAuthUserGroup(AuthUserGroup authUserGroup) {
		getAuthUserGroups().remove(authUserGroup);
		authUserGroup.setAuthUser(null);

		return authUserGroup;
	}

	@JsonIgnore
	public List<AuthUserUserPermission> getAuthUserUserPermissions() {
		return this.authUserUserPermissions;
	}

	public void setAuthUserUserPermissions(List<AuthUserUserPermission> authUserUserPermissions) {
		this.authUserUserPermissions = authUserUserPermissions;
	}

	public AuthUserUserPermission addAuthUserUserPermission(AuthUserUserPermission authUserUserPermission) {
		getAuthUserUserPermissions().add(authUserUserPermission);
		authUserUserPermission.setAuthUser(this);

		return authUserUserPermission;
	}

	public AuthUserUserPermission removeAuthUserUserPermission(AuthUserUserPermission authUserUserPermission) {
		getAuthUserUserPermissions().remove(authUserUserPermission);
		authUserUserPermission.setAuthUser(null);

		return authUserUserPermission;
	}

	@JsonIgnore
	public List<AuthtokenToken> getAuthtokenTokens() {
		return this.authtokenTokens;
	}

	public void setAuthtokenTokens(List<AuthtokenToken> authtokenTokens) {
		this.authtokenTokens = authtokenTokens;
	}

	public AuthtokenToken addAuthtokenToken(AuthtokenToken authtokenToken) {
		getAuthtokenTokens().add(authtokenToken);
		authtokenToken.setAuthUser(this);

		return authtokenToken;
	}

	public AuthtokenToken removeAuthtokenToken(AuthtokenToken authtokenToken) {
		getAuthtokenTokens().remove(authtokenToken);
		authtokenToken.setAuthUser(null);

		return authtokenToken;
	}

	@JsonIgnore
	public List<DjangoAdminLog> getDjangoAdminLogs() {
		return this.djangoAdminLogs;
	}

	public void setDjangoAdminLogs(List<DjangoAdminLog> djangoAdminLogs) {
		this.djangoAdminLogs = djangoAdminLogs;
	}

	public DjangoAdminLog addDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().add(djangoAdminLog);
		djangoAdminLog.setAuthUser(this);

		return djangoAdminLog;
	}

	public DjangoAdminLog removeDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().remove(djangoAdminLog);
		djangoAdminLog.setAuthUser(null);

		return djangoAdminLog;
	}

	@JsonIgnore
	public List<DjangoDigestPartialdigest> getDjangoDigestPartialdigests() {
		return this.djangoDigestPartialdigests;
	}

	public void setDjangoDigestPartialdigests(List<DjangoDigestPartialdigest> djangoDigestPartialdigests) {
		this.djangoDigestPartialdigests = djangoDigestPartialdigests;
	}

	public DjangoDigestPartialdigest addDjangoDigestPartialdigest(DjangoDigestPartialdigest djangoDigestPartialdigest) {
		getDjangoDigestPartialdigests().add(djangoDigestPartialdigest);
		djangoDigestPartialdigest.setAuthUser(this);

		return djangoDigestPartialdigest;
	}

	public DjangoDigestPartialdigest removeDjangoDigestPartialdigest(DjangoDigestPartialdigest djangoDigestPartialdigest) {
		getDjangoDigestPartialdigests().remove(djangoDigestPartialdigest);
		djangoDigestPartialdigest.setAuthUser(null);

		return djangoDigestPartialdigest;
	}

	@JsonIgnore
	public List<DjangoDigestUsernonce> getDjangoDigestUsernonces() {
		return this.djangoDigestUsernonces;
	}

	public void setDjangoDigestUsernonces(List<DjangoDigestUsernonce> djangoDigestUsernonces) {
		this.djangoDigestUsernonces = djangoDigestUsernonces;
	}

	public DjangoDigestUsernonce addDjangoDigestUsernonce(DjangoDigestUsernonce djangoDigestUsernonce) {
		getDjangoDigestUsernonces().add(djangoDigestUsernonce);
		djangoDigestUsernonce.setAuthUser(this);

		return djangoDigestUsernonce;
	}

	public DjangoDigestUsernonce removeDjangoDigestUsernonce(DjangoDigestUsernonce djangoDigestUsernonce) {
		getDjangoDigestUsernonces().remove(djangoDigestUsernonce);
		djangoDigestUsernonce.setAuthUser(null);

		return djangoDigestUsernonce;
	}

	@JsonIgnore
	public List<GuardianUserobjectpermission> getGuardianUserobjectpermissions() {
		return this.guardianUserobjectpermissions;
	}

	public void setGuardianUserobjectpermissions(List<GuardianUserobjectpermission> guardianUserobjectpermissions) {
		this.guardianUserobjectpermissions = guardianUserobjectpermissions;
	}

	public GuardianUserobjectpermission addGuardianUserobjectpermission(GuardianUserobjectpermission guardianUserobjectpermission) {
		getGuardianUserobjectpermissions().add(guardianUserobjectpermission);
		guardianUserobjectpermission.setAuthUser(this);

		return guardianUserobjectpermission;
	}

	public GuardianUserobjectpermission removeGuardianUserobjectpermission(GuardianUserobjectpermission guardianUserobjectpermission) {
		getGuardianUserobjectpermissions().remove(guardianUserobjectpermission);
		guardianUserobjectpermission.setAuthUser(null);

		return guardianUserobjectpermission;
	}

	@JsonIgnore
	public List<KoboformSurveydraft> getKoboformSurveydrafts() {
		return this.koboformSurveydrafts;
	}

	public void setKoboformSurveydrafts(List<KoboformSurveydraft> koboformSurveydrafts) {
		this.koboformSurveydrafts = koboformSurveydrafts;
	}

	public KoboformSurveydraft addKoboformSurveydraft(KoboformSurveydraft koboformSurveydraft) {
		getKoboformSurveydrafts().add(koboformSurveydraft);
		koboformSurveydraft.setAuthUser(this);

		return koboformSurveydraft;
	}

	public KoboformSurveydraft removeKoboformSurveydraft(KoboformSurveydraft koboformSurveydraft) {
		getKoboformSurveydrafts().remove(koboformSurveydraft);
		koboformSurveydraft.setAuthUser(null);

		return koboformSurveydraft;
	}

	@JsonIgnore
	public List<LoggerInstance> getLoggerInstances() {
		return this.loggerInstances;
	}

	public void setLoggerInstances(List<LoggerInstance> loggerInstances) {
		this.loggerInstances = loggerInstances;
	}

	public LoggerInstance addLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().add(loggerInstance);
		loggerInstance.setAuthUser(this);

		return loggerInstance;
	}

	public LoggerInstance removeLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().remove(loggerInstance);
		loggerInstance.setAuthUser(null);

		return loggerInstance;
	}

	@JsonIgnore
	public List<LoggerXform> getLoggerXforms() {
		return this.loggerXforms;
	}

	public void setLoggerXforms(List<LoggerXform> loggerXforms) {
		this.loggerXforms = loggerXforms;
	}

	public LoggerXform addLoggerXform(LoggerXform loggerXform) {
		getLoggerXforms().add(loggerXform);
		loggerXform.setAuthUser(this);

		return loggerXform;
	}

	public LoggerXform removeLoggerXform(LoggerXform loggerXform) {
		getLoggerXforms().remove(loggerXform);
		loggerXform.setAuthUser(null);

		return loggerXform;
	}

	@JsonIgnore
	public List<LoggerZiggyinstance> getLoggerZiggyinstances() {
		return this.loggerZiggyinstances;
	}

	public void setLoggerZiggyinstances(List<LoggerZiggyinstance> loggerZiggyinstances) {
		this.loggerZiggyinstances = loggerZiggyinstances;
	}

	public LoggerZiggyinstance addLoggerZiggyinstance(LoggerZiggyinstance loggerZiggyinstance) {
		getLoggerZiggyinstances().add(loggerZiggyinstance);
		loggerZiggyinstance.setAuthUser(this);

		return loggerZiggyinstance;
	}

	public LoggerZiggyinstance removeLoggerZiggyinstance(LoggerZiggyinstance loggerZiggyinstance) {
		getLoggerZiggyinstances().remove(loggerZiggyinstance);
		loggerZiggyinstance.setAuthUser(null);

		return loggerZiggyinstance;
	}

	@JsonIgnore
	public MainTokenstoragemodel getMainTokenstoragemodel() {
		return this.mainTokenstoragemodel;
	}

	public void setMainTokenstoragemodel(MainTokenstoragemodel mainTokenstoragemodel) {
		this.mainTokenstoragemodel = mainTokenstoragemodel;
	}

	@JsonIgnore
	public List<MainUserprofile> getMainUserprofiles1() {
		return this.mainUserprofiles1;
	}

	public void setMainUserprofiles1(List<MainUserprofile> mainUserprofiles1) {
		this.mainUserprofiles1 = mainUserprofiles1;
	}

	public MainUserprofile addMainUserprofiles1(MainUserprofile mainUserprofiles1) {
		getMainUserprofiles1().add(mainUserprofiles1);
		mainUserprofiles1.setAuthUser1(this);

		return mainUserprofiles1;
	}

	public MainUserprofile removeMainUserprofiles1(MainUserprofile mainUserprofiles1) {
		getMainUserprofiles1().remove(mainUserprofiles1);
		mainUserprofiles1.setAuthUser1(null);

		return mainUserprofiles1;
	}

	@JsonIgnore
	public List<MainUserprofile> getMainUserprofiles2() {
		return this.mainUserprofiles2;
	}

	public void setMainUserprofiles2(List<MainUserprofile> mainUserprofiles2) {
		this.mainUserprofiles2 = mainUserprofiles2;
	}

	public MainUserprofile addMainUserprofiles2(MainUserprofile mainUserprofiles2) {
		getMainUserprofiles2().add(mainUserprofiles2);
		mainUserprofiles2.setAuthUser2(this);

		return mainUserprofiles2;
	}

	public MainUserprofile removeMainUserprofiles2(MainUserprofile mainUserprofiles2) {
		getMainUserprofiles2().remove(mainUserprofiles2);
		mainUserprofiles2.setAuthUser2(null);

		return mainUserprofiles2;
	}

	@JsonIgnore
	public List<Oauth2ProviderAccesstoken> getOauth2ProviderAccesstokens() {
		return this.oauth2ProviderAccesstokens;
	}

	public void setOauth2ProviderAccesstokens(List<Oauth2ProviderAccesstoken> oauth2ProviderAccesstokens) {
		this.oauth2ProviderAccesstokens = oauth2ProviderAccesstokens;
	}

	public Oauth2ProviderAccesstoken addOauth2ProviderAccesstoken(Oauth2ProviderAccesstoken oauth2ProviderAccesstoken) {
		getOauth2ProviderAccesstokens().add(oauth2ProviderAccesstoken);
		oauth2ProviderAccesstoken.setAuthUser(this);

		return oauth2ProviderAccesstoken;
	}

	public Oauth2ProviderAccesstoken removeOauth2ProviderAccesstoken(Oauth2ProviderAccesstoken oauth2ProviderAccesstoken) {
		getOauth2ProviderAccesstokens().remove(oauth2ProviderAccesstoken);
		oauth2ProviderAccesstoken.setAuthUser(null);

		return oauth2ProviderAccesstoken;
	}

	@JsonIgnore
	public List<Oauth2ProviderApplication> getOauth2ProviderApplications() {
		return this.oauth2ProviderApplications;
	}

	public void setOauth2ProviderApplications(List<Oauth2ProviderApplication> oauth2ProviderApplications) {
		this.oauth2ProviderApplications = oauth2ProviderApplications;
	}

	public Oauth2ProviderApplication addOauth2ProviderApplication(Oauth2ProviderApplication oauth2ProviderApplication) {
		getOauth2ProviderApplications().add(oauth2ProviderApplication);
		oauth2ProviderApplication.setAuthUser(this);

		return oauth2ProviderApplication;
	}

	public Oauth2ProviderApplication removeOauth2ProviderApplication(Oauth2ProviderApplication oauth2ProviderApplication) {
		getOauth2ProviderApplications().remove(oauth2ProviderApplication);
		oauth2ProviderApplication.setAuthUser(null);

		return oauth2ProviderApplication;
	}

	@JsonIgnore
	public List<Oauth2ProviderGrant> getOauth2ProviderGrants() {
		return this.oauth2ProviderGrants;
	}

	public void setOauth2ProviderGrants(List<Oauth2ProviderGrant> oauth2ProviderGrants) {
		this.oauth2ProviderGrants = oauth2ProviderGrants;
	}

	public Oauth2ProviderGrant addOauth2ProviderGrant(Oauth2ProviderGrant oauth2ProviderGrant) {
		getOauth2ProviderGrants().add(oauth2ProviderGrant);
		oauth2ProviderGrant.setAuthUser(this);

		return oauth2ProviderGrant;
	}

	public Oauth2ProviderGrant removeOauth2ProviderGrant(Oauth2ProviderGrant oauth2ProviderGrant) {
		getOauth2ProviderGrants().remove(oauth2ProviderGrant);
		oauth2ProviderGrant.setAuthUser(null);

		return oauth2ProviderGrant;
	}

	@JsonIgnore
	public List<Oauth2ProviderRefreshtoken> getOauth2ProviderRefreshtokens() {
		return this.oauth2ProviderRefreshtokens;
	}

	public void setOauth2ProviderRefreshtokens(List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens) {
		this.oauth2ProviderRefreshtokens = oauth2ProviderRefreshtokens;
	}

	public Oauth2ProviderRefreshtoken addOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().add(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setAuthUser(this);

		return oauth2ProviderRefreshtoken;
	}

	public Oauth2ProviderRefreshtoken removeOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().remove(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setAuthUser(null);

		return oauth2ProviderRefreshtoken;
	}

	@JsonIgnore
	public List<RegistrationRegistrationprofile> getRegistrationRegistrationprofiles() {
		return this.registrationRegistrationprofiles;
	}

	public void setRegistrationRegistrationprofiles(List<RegistrationRegistrationprofile> registrationRegistrationprofiles) {
		this.registrationRegistrationprofiles = registrationRegistrationprofiles;
	}

	public RegistrationRegistrationprofile addRegistrationRegistrationprofile(RegistrationRegistrationprofile registrationRegistrationprofile) {
		getRegistrationRegistrationprofiles().add(registrationRegistrationprofile);
		registrationRegistrationprofile.setAuthUser(this);

		return registrationRegistrationprofile;
	}

	public RegistrationRegistrationprofile removeRegistrationRegistrationprofile(RegistrationRegistrationprofile registrationRegistrationprofile) {
		getRegistrationRegistrationprofiles().remove(registrationRegistrationprofile);
		registrationRegistrationprofile.setAuthUser(null);

		return registrationRegistrationprofile;
	}

	@JsonIgnore
	public List<ReversionRevision> getReversionRevisions() {
		return this.reversionRevisions;
	}

	public void setReversionRevisions(List<ReversionRevision> reversionRevisions) {
		this.reversionRevisions = reversionRevisions;
	}

	public ReversionRevision addReversionRevision(ReversionRevision reversionRevision) {
		getReversionRevisions().add(reversionRevision);
		reversionRevision.setAuthUser(this);

		return reversionRevision;
	}

	public ReversionRevision removeReversionRevision(ReversionRevision reversionRevision) {
		getReversionRevisions().remove(reversionRevision);
		reversionRevision.setAuthUser(null);

		return reversionRevision;
	}

	@JsonIgnore
	public List<ViewerInstancemodification> getViewerInstancemodifications() {
		return this.viewerInstancemodifications;
	}

	public void setViewerInstancemodifications(List<ViewerInstancemodification> viewerInstancemodifications) {
		this.viewerInstancemodifications = viewerInstancemodifications;
	}

	public ViewerInstancemodification addViewerInstancemodification(ViewerInstancemodification viewerInstancemodification) {
		getViewerInstancemodifications().add(viewerInstancemodification);
		viewerInstancemodification.setAuthUser(this);

		return viewerInstancemodification;
	}

	public ViewerInstancemodification removeViewerInstancemodification(ViewerInstancemodification viewerInstancemodification) {
		getViewerInstancemodifications().remove(viewerInstancemodification);
		viewerInstancemodification.setAuthUser(null);

		return viewerInstancemodification;
	}

}