package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reversion_version database table.
 * 
 */
@Entity
@Table(name="reversion_version")
@NamedQuery(name="ReversionVersion.findAll", query="SELECT r FROM ReversionVersion r")
public class ReversionVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String format;

	@Column(name="object_id", nullable=false)
	private String objectId;

	@Column(name="object_id_int")
	private Integer objectIdInt;

	@Column(name="object_repr", nullable=false)
	private String objectRepr;

	@Column(name="serialized_data", nullable=false)
	private String serializedData;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	//bi-directional many-to-one association to ReversionRevision
	@ManyToOne
	@JoinColumn(name="revision_id", nullable=false)
	private ReversionRevision reversionRevision;

	public ReversionVersion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Integer getObjectIdInt() {
		return this.objectIdInt;
	}

	public void setObjectIdInt(Integer objectIdInt) {
		this.objectIdInt = objectIdInt;
	}

	public String getObjectRepr() {
		return this.objectRepr;
	}

	public void setObjectRepr(String objectRepr) {
		this.objectRepr = objectRepr;
	}

	public String getSerializedData() {
		return this.serializedData;
	}

	public void setSerializedData(String serializedData) {
		this.serializedData = serializedData;
	}

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

	public ReversionRevision getReversionRevision() {
		return this.reversionRevision;
	}

	public void setReversionRevision(ReversionRevision reversionRevision) {
		this.reversionRevision = reversionRevision;
	}

}