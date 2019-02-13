package com.name.no.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.name.no.entity.Group;

public class TeamTO {

	private String name;
	private String country;
	private float coefficient;
	private Group group;
	transient private List<Group> possibleGroups = new ArrayList<Group>();

	public TeamTO(String name, String country, float coefficient, Group group, Map<String, Group> possibleGroups) {
		this.name = name;
		this.country = country;
		this.coefficient = coefficient;
		this.group = group;
		this.possibleGroups = new ArrayList<Group>(possibleGroups.values());
	}
	
	public TeamTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Group> getPossibleGroups() {
		return possibleGroups;
	}

	public void setPossibleGroups(List<Group> possibleGroups) {
		this.possibleGroups = possibleGroups;
	}

}
