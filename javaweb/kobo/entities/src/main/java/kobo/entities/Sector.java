package kobo.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


@Entity
@Table(name="sector")
@NamedQuery(name="Sector.findAll", query="SELECT l FROM Sector l")
public class Sector implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String name;

	@OneToMany(cascade=CascadeType.ALL)
	@Column(name = "emergencyContacts",unique=false)
	private List<EmergencyContact> emergencyContacts;
	
	@OneToMany(cascade=CascadeType.ALL)
	@Column(name = "areaCoordinator",unique=false)
	private List<AuthUser> areaCoordinator;
	
	@Column(name = "sanghani",unique=false)
	@OneToMany(cascade=CascadeType.ALL)
	private List<AuthUser> sanghani;
	
	@Column(name = "cluster",unique=false)
	@OneToMany(cascade=CascadeType.ALL)
	private List<Cluster> cluster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EmergencyContact> getEmergencyContacts() {
		return emergencyContacts;
	}

	public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}

	public List<AuthUser> getAreaCoordinator() {
		return areaCoordinator;
	}

	public void setAreaCoordinator(List<AuthUser> areaCoordinator) {
		this.areaCoordinator = areaCoordinator;
	}

	public List<AuthUser> getSanghani() {
		return sanghani;
	}

	public void setSanghani(List<AuthUser> sanghani) {
		this.sanghani = sanghani;
	}

	public List<Cluster> getCluster() {
		return cluster;
	}

	public void setCluster(List<Cluster> cluster) {
		this.cluster = cluster;
	}

}