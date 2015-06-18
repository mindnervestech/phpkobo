package kobo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the viewer_columnrename database table.
 * 
 */
@Entity
@Table(name="viewer_columnrename")
@NamedQuery(name="ViewerColumnrename.findAll", query="SELECT v FROM ViewerColumnrename v")
public class ViewerColumnrename implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="column_name", nullable=false, length=32)
	private String columnName;

	@Column(nullable=false, length=255)
	private String xpath;

	public ViewerColumnrename() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

}