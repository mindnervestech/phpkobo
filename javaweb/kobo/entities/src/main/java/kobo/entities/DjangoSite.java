package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the django_site database table.
 * 
 */
@Entity
@Table(name="django_site")
@NamedQuery(name="DjangoSite.findAll", query="SELECT d FROM DjangoSite d")
public class DjangoSite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=100)
	private String domain;

	@Column(nullable=false, length=50)
	private String name;

	public DjangoSite() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}