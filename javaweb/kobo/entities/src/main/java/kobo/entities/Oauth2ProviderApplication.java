package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the oauth2_provider_application database table.
 * 
 */
@Entity
@Table(name="oauth2_provider_application")
@NamedQuery(name="Oauth2ProviderApplication.findAll", query="SELECT o FROM Oauth2ProviderApplication o")
public class Oauth2ProviderApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="authorization_grant_type", nullable=false, length=32)
	private String authorizationGrantType;

	@Column(name="client_id", nullable=false, length=100)
	private String clientId;

	@Column(name="client_secret", nullable=false, length=255)
	private String clientSecret;

	@Column(name="client_type", nullable=false, length=32)
	private String clientType;

	@Column(nullable=false, length=255)
	private String name;

	@Column(name="redirect_uris", nullable=false)
	private String redirectUris;

	//bi-directional many-to-one association to Oauth2ProviderAccesstoken
	@OneToMany(mappedBy="oauth2ProviderApplication")
	private List<Oauth2ProviderAccesstoken> oauth2ProviderAccesstokens;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to Oauth2ProviderGrant
	@OneToMany(mappedBy="oauth2ProviderApplication")
	private List<Oauth2ProviderGrant> oauth2ProviderGrants;

	//bi-directional many-to-one association to Oauth2ProviderRefreshtoken
	@OneToMany(mappedBy="oauth2ProviderApplication")
	private List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens;

	public Oauth2ProviderApplication() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthorizationGrantType() {
		return this.authorizationGrantType;
	}

	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRedirectUris() {
		return this.redirectUris;
	}

	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}

	public List<Oauth2ProviderAccesstoken> getOauth2ProviderAccesstokens() {
		return this.oauth2ProviderAccesstokens;
	}

	public void setOauth2ProviderAccesstokens(List<Oauth2ProviderAccesstoken> oauth2ProviderAccesstokens) {
		this.oauth2ProviderAccesstokens = oauth2ProviderAccesstokens;
	}

	public Oauth2ProviderAccesstoken addOauth2ProviderAccesstoken(Oauth2ProviderAccesstoken oauth2ProviderAccesstoken) {
		getOauth2ProviderAccesstokens().add(oauth2ProviderAccesstoken);
		oauth2ProviderAccesstoken.setOauth2ProviderApplication(this);

		return oauth2ProviderAccesstoken;
	}

	public Oauth2ProviderAccesstoken removeOauth2ProviderAccesstoken(Oauth2ProviderAccesstoken oauth2ProviderAccesstoken) {
		getOauth2ProviderAccesstokens().remove(oauth2ProviderAccesstoken);
		oauth2ProviderAccesstoken.setOauth2ProviderApplication(null);

		return oauth2ProviderAccesstoken;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public List<Oauth2ProviderGrant> getOauth2ProviderGrants() {
		return this.oauth2ProviderGrants;
	}

	public void setOauth2ProviderGrants(List<Oauth2ProviderGrant> oauth2ProviderGrants) {
		this.oauth2ProviderGrants = oauth2ProviderGrants;
	}

	public Oauth2ProviderGrant addOauth2ProviderGrant(Oauth2ProviderGrant oauth2ProviderGrant) {
		getOauth2ProviderGrants().add(oauth2ProviderGrant);
		oauth2ProviderGrant.setOauth2ProviderApplication(this);

		return oauth2ProviderGrant;
	}

	public Oauth2ProviderGrant removeOauth2ProviderGrant(Oauth2ProviderGrant oauth2ProviderGrant) {
		getOauth2ProviderGrants().remove(oauth2ProviderGrant);
		oauth2ProviderGrant.setOauth2ProviderApplication(null);

		return oauth2ProviderGrant;
	}

	public List<Oauth2ProviderRefreshtoken> getOauth2ProviderRefreshtokens() {
		return this.oauth2ProviderRefreshtokens;
	}

	public void setOauth2ProviderRefreshtokens(List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens) {
		this.oauth2ProviderRefreshtokens = oauth2ProviderRefreshtokens;
	}

	public Oauth2ProviderRefreshtoken addOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().add(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setOauth2ProviderApplication(this);

		return oauth2ProviderRefreshtoken;
	}

	public Oauth2ProviderRefreshtoken removeOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().remove(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setOauth2ProviderApplication(null);

		return oauth2ProviderRefreshtoken;
	}

}