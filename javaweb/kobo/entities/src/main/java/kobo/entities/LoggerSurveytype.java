package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the logger_surveytype database table.
 * 
 */
@Entity
@Table(name="logger_surveytype")
@NamedQuery(name="LoggerSurveytype.findAll", query="SELECT l FROM LoggerSurveytype l")
public class LoggerSurveytype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=100)
	private String slug;

	//bi-directional many-to-one association to LoggerInstance
	@OneToMany(mappedBy="loggerSurveytype")
	private List<LoggerInstance> loggerInstances;

	public LoggerSurveytype() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSlug() {
		return this.slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<LoggerInstance> getLoggerInstances() {
		return this.loggerInstances;
	}

	public void setLoggerInstances(List<LoggerInstance> loggerInstances) {
		this.loggerInstances = loggerInstances;
	}

	public LoggerInstance addLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().add(loggerInstance);
		loggerInstance.setLoggerSurveytype(this);

		return loggerInstance;
	}

	public LoggerInstance removeLoggerInstance(LoggerInstance loggerInstance) {
		getLoggerInstances().remove(loggerInstance);
		loggerInstance.setLoggerSurveytype(null);

		return loggerInstance;
	}

}