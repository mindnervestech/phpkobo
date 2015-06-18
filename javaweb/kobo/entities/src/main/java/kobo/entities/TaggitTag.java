package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the taggit_tag database table.
 * 
 */
@Entity
@Table(name="taggit_tag")
@NamedQuery(name="TaggitTag.findAll", query="SELECT t FROM TaggitTag t")
public class TaggitTag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=100)
	private String name;

	@Column(nullable=false, length=100)
	private String slug;

	//bi-directional many-to-one association to TaggitTaggeditem
	@OneToMany(mappedBy="taggitTag")
	private List<TaggitTaggeditem> taggitTaggeditems;

	public TaggitTag() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return this.slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<TaggitTaggeditem> getTaggitTaggeditems() {
		return this.taggitTaggeditems;
	}

	public void setTaggitTaggeditems(List<TaggitTaggeditem> taggitTaggeditems) {
		this.taggitTaggeditems = taggitTaggeditems;
	}

	public TaggitTaggeditem addTaggitTaggeditem(TaggitTaggeditem taggitTaggeditem) {
		getTaggitTaggeditems().add(taggitTaggeditem);
		taggitTaggeditem.setTaggitTag(this);

		return taggitTaggeditem;
	}

	public TaggitTaggeditem removeTaggitTaggeditem(TaggitTaggeditem taggitTaggeditem) {
		getTaggitTaggeditems().remove(taggitTaggeditem);
		taggitTaggeditem.setTaggitTag(null);

		return taggitTaggeditem;
	}

}