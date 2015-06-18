package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the hub_sitewidemessage database table.
 * 
 */
@Entity
@Table(name="hub_sitewidemessage")
@NamedQuery(name="HubSitewidemessage.findAll", query="SELECT h FROM HubSitewidemessage h")
public class HubSitewidemessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="_body_rendered", nullable=false)
	private String bodyRendered;

	@Column(nullable=false)
	private String body;

	@Column(nullable=false, length=50)
	private String slug;

	public HubSitewidemessage() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBodyRendered() {
		return this.bodyRendered;
	}

	public void setBodyRendered(String bodyRendered) {
		this.bodyRendered = bodyRendered;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSlug() {
		return this.slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

}