package com.name.no.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "group_draw")
public class Group {

	@Id
	@Column(name="group_name")
	private String name;
//	@OneToMany(mappedBy="group")
//	private List<Team> teams;

	public Group(String name) {
		this.name = name;
//		this.teams = teams;
	}
	
	public Group() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<Team> getTeams() {
//		return teams;
//	}
//
//	public void setTeams(List<Team> teams) {
//		this.teams = teams;
//	}

}
