package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the restservice_restservice database table.
 * 
 */
@Entity
@Table(name="restservice_restservice")
@NamedQuery(name="RestserviceRestservice.findAll", query="SELECT r FROM RestserviceRestservice r")
public class RestserviceRestservice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=50)
	private String name;

	@Column(name="service_url", nullable=false, length=200)
	private String serviceUrl;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id", nullable=false)
	private LoggerXform loggerXform;

	public RestserviceRestservice() {
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

	public String getServiceUrl() {
		return this.serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public LoggerXform getLoggerXform() {
		return this.loggerXform;
	}

	public void setLoggerXform(LoggerXform loggerXform) {
		this.loggerXform = loggerXform;
	}

}