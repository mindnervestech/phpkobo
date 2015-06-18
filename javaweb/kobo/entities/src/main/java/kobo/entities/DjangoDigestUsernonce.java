package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the django_digest_usernonce database table.
 * 
 */
@Entity
@Table(name="django_digest_usernonce")
@NamedQuery(name="DjangoDigestUsernonce.findAll", query="SELECT d FROM DjangoDigestUsernonce d")
public class DjangoDigestUsernonce implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	private Integer count;

	@Column(name="last_used_at", nullable=false)
	private Timestamp lastUsedAt;

	@Column(nullable=false, length=100)
	private String nonce;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public DjangoDigestUsernonce() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Timestamp getLastUsedAt() {
		return this.lastUsedAt;
	}

	public void setLastUsedAt(Timestamp lastUsedAt) {
		this.lastUsedAt = lastUsedAt;
	}

	public String getNonce() {
		return this.nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}