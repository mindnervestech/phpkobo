package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reversion_revision database table.
 * 
 */
@Entity
@Table(name="reversion_revision")
@NamedQuery(name="ReversionRevision.findAll", query="SELECT r FROM ReversionRevision r")
public class ReversionRevision implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private String comment;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="manager_slug", nullable=false, length=200)
	private String managerSlug;

	//bi-directional many-to-one association to AuthUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private AuthUser authUser;

	//bi-directional many-to-one association to ReversionVersion
	@OneToMany(mappedBy="reversionRevision")
	private List<ReversionVersion> reversionVersions;

	public ReversionRevision() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getManagerSlug() {
		return this.managerSlug;
	}

	public void setManagerSlug(String managerSlug) {
		this.managerSlug = managerSlug;
	}

	public AuthUser getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public List<ReversionVersion> getReversionVersions() {
		return this.reversionVersions;
	}

	public void setReversionVersions(List<ReversionVersion> reversionVersions) {
		this.reversionVersions = reversionVersions;
	}

	public ReversionVersion addReversionVersion(ReversionVersion reversionVersion) {
		getReversionVersions().add(reversionVersion);
		reversionVersion.setReversionRevision(this);

		return reversionVersion;
	}

	public ReversionVersion removeReversionVersion(ReversionVersion reversionVersion) {
		getReversionVersions().remove(reversionVersion);
		reversionVersion.setReversionRevision(null);

		return reversionVersion;
	}

}