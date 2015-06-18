package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the main_tokenstoragemodel database table.
 * 
 */
@Entity
@Table(name="main_tokenstoragemodel")
@NamedQuery(name="MainTokenstoragemodel.findAll", query="SELECT m FROM MainTokenstoragemodel m")
public class MainTokenstoragemodel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_id", unique=true, nullable=false)
	private Integer idId;

	@Column(nullable=false)
	private String token;

	//bi-directional one-to-one association to AuthUser
	@OneToOne
	@JoinColumn(name="id_id", nullable=false, insertable=false, updatable=false)
	private AuthUser authUser;

	public MainTokenstoragemodel() {
	}

	public Integer getIdId() {
		return this.idId;
	}

	public void setIdId(Integer idId) {
		this.idId = idId;
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

}