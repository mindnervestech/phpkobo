package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the django_digest_partialdigest database table.
 * 
 */
@Entity
@Table(name="django_digest_partialdigest")
@NamedQuery(name="DjangoDigestPartialdigest.findAll", query="SELECT d FROM DjangoDigestPartialdigest d")
public class DjangoDigestPartialdigest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Boolean confirmed;

	@Column(nullable=false, length=75)
	private String login;

	@Column(name="partial_digest", nullable=false, length=100)
	private String partialDigest;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public DjangoDigestPartialdigest() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPartialDigest() {
		return this.partialDigest;
	}

	public void setPartialDigest(String partialDigest) {
		this.partialDigest = partialDigest;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}