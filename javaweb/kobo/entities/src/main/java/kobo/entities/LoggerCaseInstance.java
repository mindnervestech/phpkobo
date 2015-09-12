package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the api_team_projects database table.
 * 
 */
@Entity
@Table(name="logger_case_instances")
@NamedQuery(name="LoggerCaseInstance.findAll", query="SELECT a FROM LoggerCaseInstance a")
public class LoggerCaseInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to LoggerCase
	@ManyToOne
	@JoinColumn(name="case_id", nullable=false)
	private LoggerCase loggerCase;

	//bi-directional many-to-one association to LoggerInstance
	@ManyToOne
	@JoinColumn(name="instance_id", nullable=false)
	private LoggerInstance loggerInstance;

	public LoggerCaseInstance() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public LoggerCase getLoggerCase() {
		return loggerCase;
	}

	public void setLoggerCase(LoggerCase loggerCase) {
		this.loggerCase = loggerCase;
	}
	
	public LoggerInstance getLoggerInstance() {
		return loggerInstance;
	}

	public void setLoggerInstance(LoggerInstance loggerInstance) {
		this.loggerInstance = loggerInstance;
	}
	
}