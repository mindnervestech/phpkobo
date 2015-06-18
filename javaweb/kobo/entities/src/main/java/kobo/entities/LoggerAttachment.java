package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the logger_attachment database table.
 * 
 */
@Entity
@Table(name="logger_attachment")
@NamedQuery(name="LoggerAttachment.findAll", query="SELECT l FROM LoggerAttachment l")
public class LoggerAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="media_file", nullable=false, length=100)
	private String mediaFile;

	@Column(nullable=false, length=50)
	private String mimetype;

	//bi-directional many-to-one association to LoggerInstance
	@ManyToOne
	@JoinColumn(name="instance_id", nullable=false)
	private LoggerInstance loggerInstance;

	public LoggerAttachment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMediaFile() {
		return this.mediaFile;
	}

	public void setMediaFile(String mediaFile) {
		this.mediaFile = mediaFile;
	}

	public String getMimetype() {
		return this.mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}

}