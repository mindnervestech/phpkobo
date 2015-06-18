package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the south_migrationhistory database table.
 * 
 */
@Entity
@Table(name="south_migrationhistory")
@NamedQuery(name="SouthMigrationhistory.findAll", query="SELECT s FROM SouthMigrationhistory s")
public class SouthMigrationhistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="app_name", nullable=false, length=255)
	private String appName;

	@Column(nullable=false)
	private Timestamp applied;

	@Column(nullable=false, length=255)
	private String migration;

	public SouthMigrationhistory() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Timestamp getApplied() {
		return this.applied;
	}

	public void setApplied(Timestamp applied) {
		this.applied = applied;
	}

	public String getMigration() {
		return this.migration;
	}

	public void setMigration(String migration) {
		this.migration = migration;
	}

}