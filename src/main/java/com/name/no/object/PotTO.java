package com.name.no.object;

import java.util.ArrayList;
import java.util.List;

import com.name.no.entity.Team;

public class PotTO {

	private String name;
	private List<TeamTO> teams;

	public PotTO(String name, List<TeamTO> teams) {
		this.name = name;
		this.teams = teams;
	}
	
	public PotTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TeamTO> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamTO> teams) {
		this.teams = teams;
	}
	
	public static List<PotTO> translate(List<Pot> pots) {
		List<PotTO> newPots = new ArrayList<PotTO>();
		for (Pot pot : pots) {
			List<TeamTO> newTeams = new ArrayList<TeamTO>();
			for (Team team : pot.getTeams()) {
				newTeams.add(new TeamTO(team.getName(), team.getCountry(), team.getCoefficient(), team.getGroup(), team.getPossibleGroups()));
			}
			PotTO newPot = new PotTO(pot.getName(), newTeams);
			newPots.add(newPot);
		}
		return newPots;
	}
}
