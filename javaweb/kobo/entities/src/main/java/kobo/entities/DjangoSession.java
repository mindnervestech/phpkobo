package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the django_session database table.
 * 
 */
@Entity
@Table(name="django_session")
@NamedQuery(name="DjangoSession.findAll", query="SELECT d FROM DjangoSession d")
public class DjangoSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="session_key", unique=true, nullable=false, length=40)
	private String sessionKey;

	@Column(name="expire_date", nullable=false)
	private Timestamp expireDate;

	@Column(name="session_data", nullable=false)
	private String sessionData;

	public DjangoSession() {
	}

	public String getSessionKey() {
		return this.sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Timestamp getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}

	public String getSessionData() {
		return this.sessionData;
	}

	public void setSessionData(String sessionData) {
		this.sessionData = sessionData;
	}

}