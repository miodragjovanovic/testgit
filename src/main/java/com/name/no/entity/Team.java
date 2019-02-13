package com.name.no.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "team")
public class Team {

	@Id
	@Column(name = "team_name")
	private String name;
	private String country;
	private float coefficient;
	@ManyToOne
	@JoinColumn(name = "group_name")
	private Group group;
	transient private Map<String, Group> possibleGroups = new HashMap<String, Group>();

	public Team(String name, String country, float coefficient, Group group) {
		this.name = name;
		this.country = country;
		this.coefficient = coefficient;
		this.group = group;
	}

	public Team() {
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

	public Map<String, Group> getPossibleGroups() {
		return possibleGroups;
	}

	public void setPossibleGroups(Map<String, Group> possibleGroups) {
		this.possibleGroups = possibleGroups;
	}

}
