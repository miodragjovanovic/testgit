package com.name.no.object;

import java.util.List;

import com.name.no.entity.Team;

public class Pot {

	private String name;
	private List<Team> teams;

	public Pot(String name, List<Team> teams) {
		this.name = name;
		this.teams = teams;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

}