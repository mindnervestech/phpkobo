package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the viewer_parsedinstance database table.
 * 
 */
@Entity
@Table(name="viewer_parsedinstance")
@NamedQuery(name="ViewerParsedinstance.findAll", query="SELECT v FROM ViewerParsedinstance v")
public class ViewerParsedinstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="end_time")
	private Timestamp endTime;

	private double lat;

	private double lng;

	@Column(name="start_time")
	private Timestamp startTime;

	//bi-directional many-to-one association to LoggerInstance
	@ManyToOne
	@JoinColumn(name="instance_id", nullable=false)
	private LoggerInstance loggerInstance;

	public ViewerParsedinstance() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return this.lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public LoggerInstance getLoggerInstance() {
		return this.loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}

}