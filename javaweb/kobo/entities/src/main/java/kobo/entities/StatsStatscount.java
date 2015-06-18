package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the stats_statscount database table.
 * 
 */
@Entity
@Table(name="stats_statscount")
@NamedQuery(name="StatsStatscount.findAll", query="SELECT s FROM StatsStatscount s")
public class StatsStatscount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_on", nullable=false)
	private Timestamp createdOn;

	@Column(nullable=false, length=150)
	private String key;

	@Column(name="modified_on", nullable=false)
	private Timestamp modifiedOn;

	@Column(nullable=false)
	private Integer value;

	public StatsStatscount() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Timestamp getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}