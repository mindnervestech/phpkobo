package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the oauth2_provider_refreshtoken database table.
 * 
 */
@Entity
@Table(name="oauth2_provider_refreshtoken")
@NamedQuery(name="Oauth2ProviderRefreshtoken.findAll", query="SELECT o FROM Oauth2ProviderRefreshtoken o")
public class Oauth2ProviderRefreshtoken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String token;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to Oauth2ProviderAccesstoken
	@ManyToOne
	@JoinColumn(name="access_token_id", nullable=false)
	private Oauth2ProviderAccesstoken oauth2ProviderAccesstoken;

	//bi-directional many-to-one association to Oauth2ProviderApplication
	@ManyToOne
	@JoinColumn(name="application_id", nullable=false)
	private Oauth2ProviderApplication oauth2ProviderApplication;

	public Oauth2ProviderRefreshtoken() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Oauth2ProviderAccesstoken getOauth2ProviderAccesstoken() {
		return this.oauth2ProviderAccesstoken;
	}

	public void setOauth2ProviderAccesstoken(Oauth2ProviderAccesstoken oauth2ProviderAccesstoken) {
		this.oauth2ProviderAccesstoken = oauth2ProviderAccesstoken;
	}

	public Oauth2ProviderApplication getOauth2ProviderApplication() {
		return this.oauth2ProviderApplication;
	}

	public void setOauth2ProviderApplication(Oauth2ProviderApplication oauth2ProviderApplication) {
		this.oauth2ProviderApplication = oauth2ProviderApplication;
	}

}