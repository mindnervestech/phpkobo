package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the oauth2_provider_accesstoken database table.
 * 
 */
@Entity
@Table(name="oauth2_provider_accesstoken")
@NamedQuery(name="Oauth2ProviderAccesstoken.findAll", query="SELECT o FROM Oauth2ProviderAccesstoken o")
public class Oauth2ProviderAccesstoken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Timestamp expires;

	@Column(nullable=false)
	private String scope;

	@Column(nullable=false, length=255)
	private String token;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to Oauth2ProviderApplication
	@ManyToOne
	@JoinColumn(name="application_id", nullable=false)
	private Oauth2ProviderApplication oauth2ProviderApplication;

	//bi-directional many-to-one association to Oauth2ProviderRefreshtoken
	@OneToMany(mappedBy="oauth2ProviderAccesstoken")
	private List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens;

	public Oauth2ProviderAccesstoken() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getExpires() {
		return this.expires;
	}

	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public Oauth2ProviderApplication getOauth2ProviderApplication() {
		return this.oauth2ProviderApplication;
	}

	public void setOauth2ProviderApplication(Oauth2ProviderApplication oauth2ProviderApplication) {
		this.oauth2ProviderApplication = oauth2ProviderApplication;
	}

	public List<Oauth2ProviderRefreshtoken> getOauth2ProviderRefreshtokens() {
		return this.oauth2ProviderRefreshtokens;
	}

	public void setOauth2ProviderRefreshtokens(List<Oauth2ProviderRefreshtoken> oauth2ProviderRefreshtokens) {
		this.oauth2ProviderRefreshtokens = oauth2ProviderRefreshtokens;
	}

	public Oauth2ProviderRefreshtoken addOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().add(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setOauth2ProviderAccesstoken(this);

		return oauth2ProviderRefreshtoken;
	}

	public Oauth2ProviderRefreshtoken removeOauth2ProviderRefreshtoken(Oauth2ProviderRefreshtoken oauth2ProviderRefreshtoken) {
		getOauth2ProviderRefreshtokens().remove(oauth2ProviderRefreshtoken);
		oauth2ProviderRefreshtoken.setOauth2ProviderAccesstoken(null);

		return oauth2ProviderRefreshtoken;
	}

}