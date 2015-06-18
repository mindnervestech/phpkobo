package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the oauth2_provider_grant database table.
 * 
 */
@Entity
@Table(name="oauth2_provider_grant")
@NamedQuery(name="Oauth2ProviderGrant.findAll", query="SELECT o FROM Oauth2ProviderGrant o")
public class Oauth2ProviderGrant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String code;

	@Column(nullable=false)
	private Timestamp expires;

	@Column(name="redirect_uri", nullable=false, length=255)
	private String redirectUri;

	@Column(nullable=false)
	private String scope;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	//bi-directional many-to-one association to Oauth2ProviderApplication
	@ManyToOne
	@JoinColumn(name="application_id", nullable=false)
	private Oauth2ProviderApplication oauth2ProviderApplication;

	public Oauth2ProviderGrant() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getExpires() {
		return this.expires;
	}

	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}

	public String getRedirectUri() {
		return this.redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
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

}