package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the corsheaders_corsmodel database table.
 * 
 */
@Entity
@Table(name="corsheaders_corsmodel")
@NamedQuery(name="CorsheadersCorsmodel.findAll", query="SELECT c FROM CorsheadersCorsmodel c")
public class CorsheadersCorsmodel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String cors;

	public CorsheadersCorsmodel() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCors() {
		return this.cors;
	}

	public void setCors(String cors) {
		this.cors = cors;
	}

}