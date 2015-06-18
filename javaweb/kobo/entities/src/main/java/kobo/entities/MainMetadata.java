package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the main_metadata database table.
 * 
 */
@Entity
@Table(name="main_metadata")
@NamedQuery(name="MainMetadata.findAll", query="SELECT m FROM MainMetadata m")
public class MainMetadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="data_file", length=100)
	private String dataFile;

	@Column(name="data_file_type", length=255)
	private String dataFileType;

	@Column(name="data_type", nullable=false, length=255)
	private String dataType;

	@Column(name="data_value", nullable=false, length=255)
	private String dataValue;

	@Column(name="file_hash", length=50)
	private String fileHash;

	//bi-directional many-to-one association to LoggerXform
	@ManyToOne
	@JoinColumn(name="xform_id", nullable=false)
	private LoggerXform loggerXform;

	public MainMetadata() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataFile() {
		return this.dataFile;
	}

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	public String getDataFileType() {
		return this.dataFileType;
	}

	public void setDataFileType(String dataFileType) {
		this.dataFileType = dataFileType;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataValue() {
		return this.dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getFileHash() {
		return this.fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public LoggerXform getLoggerXform() {
		return this.loggerXform;
	}

	public void setLoggerXform(LoggerXform loggerXform) {
		this.loggerXform = loggerXform;
	}

}