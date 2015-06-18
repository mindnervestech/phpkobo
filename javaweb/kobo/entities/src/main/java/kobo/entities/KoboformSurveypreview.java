package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the koboform_surveypreview database table.
 * 
 */
@Entity
@Table(name="koboform_surveypreview")
@NamedQuery(name="KoboformSurveypreview.findAll", query="SELECT k FROM KoboformSurveypreview k")
public class KoboformSurveypreview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private String csv;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="unique_string", nullable=false, length=64)
	private String uniqueString;

	@Column(nullable=false)
	private String xml;

	public KoboformSurveypreview() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCsv() {
		return this.csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUniqueString() {
		return this.uniqueString;
	}

	public void setUniqueString(String uniqueString) {
		this.uniqueString = uniqueString;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

}