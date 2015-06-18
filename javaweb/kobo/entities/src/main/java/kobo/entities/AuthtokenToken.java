package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the authtoken_token database table.
 * 
 */
@Entity
@Table(name="authtoken_token")
@NamedQuery(name="AuthtokenToken.findAll", query="SELECT a FROM AuthtokenToken a")
public class AuthtokenToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=40)
	private String key;

	@Column(nullable=false)
	private Timestamp created;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public AuthtokenToken() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}