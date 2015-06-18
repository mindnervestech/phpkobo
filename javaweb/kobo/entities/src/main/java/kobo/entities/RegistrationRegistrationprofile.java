package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the registration_registrationprofile database table.
 * 
 */
@Entity
@Table(name="registration_registrationprofile")
@NamedQuery(name="RegistrationRegistrationprofile.findAll", query="SELECT r FROM RegistrationRegistrationprofile r")
public class RegistrationRegistrationprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="activation_key", nullable=false, length=40)
	private String activationKey;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AuthUser authUser;

	public RegistrationRegistrationprofile() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivationKey() {
		return this.activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

}