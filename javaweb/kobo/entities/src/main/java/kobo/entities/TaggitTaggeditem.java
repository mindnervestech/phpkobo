package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the taggit_taggeditem database table.
 * 
 */
@Entity
@Table(name="taggit_taggeditem")
@NamedQuery(name="TaggitTaggeditem.findAll", query="SELECT t FROM TaggitTaggeditem t")
public class TaggitTaggeditem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="object_id", nullable=false)
	private Integer objectId;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	//bi-directional many-to-one association to TaggitTag
	@ManyToOne
	@JoinColumn(name="tag_id", nullable=false)
	private TaggitTag taggitTag;

	public TaggitTaggeditem() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getObjectId() {
		return this.objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

	public TaggitTag getTaggitTag() {
		return this.taggitTag;
	}

	public void setTaggitTag(TaggitTag taggitTag) {
		this.taggitTag = taggitTag;
	}

}