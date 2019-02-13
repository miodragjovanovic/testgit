package com.name.no.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.name.no.entity.Group;
import com.name.no.entity.Team;
import com.name.no.object.Pot;
import com.name.no.object.PotTO;
import com.name.no.object.TeamTO;
import com.name.no.repository.GroupRepository;
import com.name.no.repository.TeamRepository;

@Service
public class DrawService {
	
	private final TeamRepository teamRepository;
	private final GroupRepository groupRepository;
	
	Map<String, Integer> teamsFromCountry = new HashMap<String, Integer>();
	Map<String, Long> teamsFromCountryPerGroup = new HashMap<String, Long>();
	Map<Integer, Pot> pots = new HashMap<Integer, Pot>();
	
	@Autowired
	public DrawService(TeamRepository teamRepository, GroupRepository groupRepository) {
		this.teamRepository = teamRepository;
		this.groupRepository = groupRepository;
	}
	
	public void initialize() {
		long noOfGroups = groupRepository.count();
		List<Team> teams = teamRepository.findAllByOrderByCoefficientDesc();
		Integer potNo = 0;
		
		for (Team team : teams) {
			String country = team.getCountry();
			if (teamsFromCountry.containsKey(country)) {
				teamsFromCountry.put(country, teamsFromCountry.get(country) + 1);
			} else {
				teamsFromCountry.put(country, 1);
			}
			if (pots.get(potNo) != null && noOfGroups > pots.get(potNo).getTeams().size()) {
				pots.get(potNo).getTeams().add(team);
			} else {
				potNo++;
				List<Team> teamsInPot = new ArrayList<Team>();
				teamsInPot.add(team);
				Pot pot = new Pot(potNo.toString(), teamsInPot);
				pots.put(potNo, pot);
			}
		}
		for (Map.Entry<String, Integer> entry : teamsFromCountry.entrySet()) {
			long limit = entry.getValue() / noOfGroups;
			if (entry.getValue() % noOfGroups != 0) {
				limit++;
			}
			teamsFromCountryPerGroup.put(entry.getKey(), limit);
		}
	}
	
	public Map<Integer, Pot> listPots() {
		for (Map.Entry<Integer, Pot> entry : pots.entrySet()) {
//			System.out.println("POT" + entry.getValue().getName() + ":");
//			for (Team team : entry.getValue().getTeams()) {
//				System.out.println(team.getName() + " " + team.getCountry());
//			}
//			possibleGroups(pots.get(0));
//			filterPossibleGroups(pots.get(0));
		}
		return pots;
	}
	
	public void possibleGroups(Pot pot) {
		List<Team> teamsInPot = pot.getTeams();
		List<Group> groups = (List<Group>) groupRepository.findAll();
		for (Team selectedTeam : teamsInPot) {
			String country = selectedTeam.getCountry();
			Long limit = teamsFromCountryPerGroup.get(country);
			if (groupRepository.countGroupsWithCountry(country) < groups.size()) {
				limit = 1L;
			}
			for (Group group : groups) {
				List<Team> groupTeams = teamRepository.findByGroupName(group.getName());
				if(groupTeams.size() < Long.valueOf(pot.getName())) {
					long noOfTeamsFromTheDrawnCountry = 0;
					for (Team groupTeam : groupTeams) {
						if (groupTeam.getCountry().equalsIgnoreCase(country)) {
							noOfTeamsFromTheDrawnCountry++;
						}
					}
					
					if (noOfTeamsFromTheDrawnCountry < limit && selectedTeam.getPossibleGroups().size() < groups.size()) {
						selectedTeam.getPossibleGroups().put(group.getName(), group);
					}
					
					if (limit > 1 && groupRepository.countGroupsWithCountry(country) < groups.size() && noOfTeamsFromTheDrawnCountry > 0 && selectedTeam.getPossibleGroups().size() > 1)
						selectedTeam.getPossibleGroups().remove(group.getName());
				}
			}
			
			// budz
//			if (selectedTeam.getPossibleGroups().size() < 1 && teamsFromCountryPerGroup.get(country) != limit) {
//				limit = teamsFromCountryPerGroup.get(country);
//				for (Group group : groups) {
//					List<Team> groupTeams = teamRepository.findByGroupName(group.getName());
//					if(groupTeams.size() < Long.valueOf(pot.getName())) {
//						long noOfTeamsFromTheDrawnCountry = 0;
//						for (Team groupTeam : groupTeams) {
//							if (groupTeam.getCountry().equalsIgnoreCase(country)) {
//								noOfTeamsFromTheDrawnCountry++;
//							}
//						}
//						
//						if (noOfTeamsFromTheDrawnCountry < limit && selectedTeam.getPossibleGroups().size() < groups.size()) {
//							selectedTeam.getPossibleGroups().put(group.getName(), group);
//						}
//						
//						if (limit > 1 && groupRepository.countGroupsWithCountry(country) < groups.size() && noOfTeamsFromTheDrawnCountry > 0 && selectedTeam.getPossibleGroups().size() > 1)
//							selectedTeam.getPossibleGroups().remove(group.getName());
//					}
//				}
//			}
			// budz end
		}
		
	}
	
	public void filterPossibleGroups(Pot pot) {
		List<Team> teamsInPot = pot.getTeams();
		List<Group> unavailableGroups = new ArrayList<Group>();
		List<Team> skipTeams = new ArrayList<Team>();
		for (Team selectedTeam : teamsInPot) {
//			if (teamRepository.findOne(selectedTeam.getName()).getGroup() == null && selectedTeam.getPossibleGroups().size() == 1 && teamsLeft(pot) > 1) {
			if (!teamRepository.existsById(selectedTeam.getName()) && selectedTeam.getPossibleGroups().size() == 1 && teamsLeft(pot) > 1) {
				addUnavailbleGroups(unavailableGroups, selectedTeam.getPossibleGroups());
				skipTeams.add(selectedTeam);
			}
			if (!teamRepository.existsById(selectedTeam.getName()) && selectedTeam.getPossibleGroups().size() == 2 && teamsLeft(pot) > 2) {
				for (Team nextTeam : teamsInPot) {
					if (!nextTeam.getName().equals(selectedTeam) && nextTeam.getPossibleGroups().size() == 2) {
						boolean match = true;
						for (Map.Entry<String, Group> entry : selectedTeam.getPossibleGroups().entrySet()) {
							if (!nextTeam.getPossibleGroups().containsKey(entry.getKey())) {
								match = false;
							}
						}
						if (match) {
							addUnavailbleGroups(unavailableGroups, selectedTeam.getPossibleGroups());
							skipTeams.add(selectedTeam);
						}
					}
				}
			}
		}
		while (!unavailableGroups.isEmpty()) {
			for (Team selectedTeam : teamsInPot) {
				if (selectedTeam.getPossibleGroups().size() > 1 && unavailableGroups.size() == 1 || selectedTeam.getPossibleGroups().size() > 2 && unavailableGroups.size() == 2) {
					for (Group group : unavailableGroups) {
						selectedTeam.getPossibleGroups().remove(group.getName());
					}
				}
			}
			unavailableGroups = new ArrayList<Group>();
			for (Team selectedTeam : teamsInPot) {
				if (selectedTeam.getPossibleGroups().size() == 1 && !skipTeams.contains(selectedTeam)) {
					addUnavailbleGroups(unavailableGroups, selectedTeam.getPossibleGroups());
					skipTeams.add(selectedTeam);
				}
			}
		}
	}
	
	private void addUnavailbleGroups(List<Group> unavailableGroups, Map<String, Group> possibleGroups) {
		for (Map.Entry<String, Group> entry : possibleGroups.entrySet()) {
			unavailableGroups.add(entry.getValue());
		}
	}

	public void draw() throws InterruptedException {
		for (Map.Entry<Integer, Pot> entry : pots.entrySet()) {
			while (teamsLeft(entry.getValue()) > 0) {
				possibleGroups(entry.getValue());
				filterPossibleGroups(entry.getValue());
				Team drawnTeam = drawTeam(entry.getValue());
				System.out.println("Drawn Team: " + drawnTeam.getName());
				System.out.println("Possbile groups: ");
				for (Map.Entry<String, Group> groupEntry : drawnTeam.getPossibleGroups().entrySet()) {
					System.out.print(groupEntry.getValue().getName() + " ");
				}
				System.out.println();
//				Thread.sleep(2000);
				Group drawnGroup = drawGroup(drawnTeam);
				System.out.println("Drawn Group: " + drawnGroup.getName());
//				Thread.sleep(1000);
				drawnTeam.setGroup(drawnGroup);
				teamRepository.save(drawnTeam);
				removeDrawnGroup(entry.getValue(), drawnGroup);
			}
		}
	}
	
	public TeamTO commandDrawTeam(PotTO potTO) {
		Pot pot = pots.get(Integer.valueOf(potTO.getName()));
		if (teamsLeft(pot) > 0) {
			possibleGroups(pot);
			filterPossibleGroups(pot);
			Team drawnTeam = drawTeam(pot);
			System.out.println("Drawn Team: " + drawnTeam.getName());
			System.out.println("Possbile groups: ");
			for (Map.Entry<String, Group> groupEntry : drawnTeam.getPossibleGroups().entrySet()) {
				System.out.print(groupEntry.getValue().getName() + " ");
			}
			System.out.println();
			return new TeamTO(drawnTeam.getName(), drawnTeam.getCountry(), drawnTeam.getCoefficient(), drawnTeam.getGroup(), drawnTeam.getPossibleGroups());
		}
		return null;
	}
	
	public Group commandDrawGroup(PotTO potTO, TeamTO teamTO) {
		Pot pot = pots.get(Integer.valueOf(potTO.getName()));
		Team drawnTeam = null;
		for (Team team : pot.getTeams()) {
			if (teamTO.getName().equalsIgnoreCase(team.getName())) {
				drawnTeam = team;
			}
		}
		
		List<Group> groups = new ArrayList<Group>();
		for (Map.Entry<String, Group> entry : drawnTeam.getPossibleGroups().entrySet()) {
			groups.add(entry.getValue());
		}
		Integer index = (int) Math.round(Math.random() * groups.size());
		if (index == groups.size()) {
			index--;
		}
		Group drawnGroup = groups.get(index);
		
		drawnTeam.setGroup(drawnGroup);
		teamRepository.save(drawnTeam);
		removeDrawnGroup(pot, drawnGroup);
		return drawnGroup;
	}
	
	private int teamsLeft(Pot pot) {
		int count = 0;
		for (Team team : pot.getTeams()) {
			Team dbTeam = teamRepository.findById(team.getName()).get();
			if (dbTeam.getGroup() == null) {
				count++;
			}
		}
		return count;
	}
	
	private Team drawTeam(Pot pot) {
		possibleGroups(pot);
		filterPossibleGroups(pot);
		List<Team> teams = new ArrayList<Team>(); 
		for (Team potTeam : pot.getTeams()) {
			Team dbTeam = teamRepository.findById(potTeam.getName()).get();
			if (dbTeam.getGroup() == null) {
				teams.add(potTeam);
			}
		}
		Integer index = (int) Math.round(Math.random() * teams.size());
		if (index == teams.size()) {
			index--;
		}
		Team drawnTeam = teams.get(index);
		teams.remove(drawnTeam);
		return drawnTeam;
	}
	
	private Group drawGroup(Team team) {
		List<Group> groups = new ArrayList<Group>();
		for (Map.Entry<String, Group> entry : team.getPossibleGroups().entrySet()) {
			groups.add(entry.getValue());
		}
		Integer index = (int) Math.round(Math.random() * groups.size());
		if (index == groups.size()) {
			index--;
		}
		Group drawnGroup = groups.get(index);
		return drawnGroup;
	}
	
	private void removeDrawnGroup(Pot pot, Group group) {
		for (Team selectedTeam : pot.getTeams()) {
			selectedTeam.getPossibleGroups().remove(group.getName());
		}
	}
	
	public int test(String country) {
		return groupRepository.countGroupsWithCountry(country);
	}
	
	public List<Group> getGroups() {
		return (List<Group>) groupRepository.findAll();
	}
	
	public List<Team> getTeams() {
		return (List<Team>) teamRepository.findAll();
	}
	
	public List<TeamTO> getTeamsFromGroup(String group) {
		List<TeamTO> teamsTO = new ArrayList<TeamTO>();
		List<Team> teams = teamRepository.findByGroupNameOrderByCoefficientDesc(group);
		for (Team team : teams) {
			teamsTO.add(new TeamTO(team.getName(), team.getCountry(), team.getCoefficient(), team.getGroup(), team.getPossibleGroups()));
		}
		return teamsTO;
	}
	
}
