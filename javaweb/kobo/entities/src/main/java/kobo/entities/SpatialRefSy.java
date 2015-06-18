package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the spatial_ref_sys database table.
 * 
 */
@Entity
@Table(name="spatial_ref_sys")
@NamedQuery(name="SpatialRefSy.findAll", query="SELECT s FROM SpatialRefSy s")
public class SpatialRefSy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer srid;

	@Column(name="auth_name", length=256)
	private String authName;

	@Column(name="auth_srid")
	private Integer authSrid;

	@Column(length=2048)
	private String proj4text;

	@Column(length=2048)
	private String srtext;

	public SpatialRefSy() {
	}

	public Integer getSrid() {
		return this.srid;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	public String getAuthName() {
		return this.authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public Integer getAuthSrid() {
		return this.authSrid;
	}

	public void setAuthSrid(Integer authSrid) {
		this.authSrid = authSrid;
	}

	public String getProj4text() {
		return this.proj4text;
	}

	public void setProj4text(String proj4text) {
		this.proj4text = proj4text;
	}

	public String getSrtext() {
		return this.srtext;
	}

	public void setSrtext(String srtext) {
		this.srtext = srtext;
	}

}