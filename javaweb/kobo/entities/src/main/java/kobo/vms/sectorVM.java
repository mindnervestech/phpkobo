package kobo.vms;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;



public class sectorVM  {

	private Integer id;
	
	private String name;

	List<emergencyContactVM> inc;
	
	List<coordinateVM> coordinators;
	
	List<sanghniVM> sanghni;
	
	List<clusterVM> clustervm;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<emergencyContactVM> getInc() {
		return inc;
	}

	public void setInc(List<emergencyContactVM> inc) {
		this.inc = inc;
	}

	public List<coordinateVM> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<coordinateVM> coordinators) {
		this.coordinators = coordinators;
	}

	public List<sanghniVM> getSanghni() {
		return sanghni;
	}

	public void setSanghni(List<sanghniVM> sanghni) {
		this.sanghni = sanghni;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<clusterVM> getClustervm() {
		return clustervm;
	}

	public void setClustervm(List<clusterVM> clustervm) {
		this.clustervm = clustervm;
	}


}
