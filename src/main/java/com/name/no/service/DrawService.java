package com.name.no.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
			unavailableGroups = new ArrayList<>();
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
				Thread.sleep(2000);
				Group drawnGroup = drawGroup(drawnTeam);
				System.out.println("Drawn Group: " + drawnGroup.getName());
				Thread.sleep(1000);
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
		
		List<Group> groups = new ArrayList<>();
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
		List<Team> teams = new ArrayList<>();
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
		List<Group> groups = new ArrayList<>();
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
		List<TeamTO> teamsTO = new ArrayList<>();
		List<Team> teams = teamRepository.findByGroupNameOrderByCoefficientDesc(group);
		for (Team team : teams) {
			teamsTO.add(new TeamTO(team.getName(), team.getCountry(), team.getCoefficient(), team.getGroup(), team.getPossibleGroups()));
		}
		return teamsTO;
	}

	public static final void main(String[] args) {
		DrawService drawService = new DrawService(null, null);
		drawService.bla();
	}

	private void bla() {
		List<String> list = new ArrayList<>();
		File file = new File("D:/share/draw/allplayers.txt");
		if(file.exists()){
			try {
				list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if(list.isEmpty())
				return;
		}
		
//		List<String> list2 = new ArrayList<>();
//		File file2 = new File("D:/share/draw/test2.txt");
//		if(file2.exists()){
//			try {
//				list2 = Files.readAllLines(file2.toPath(), Charset.defaultCharset());
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//			if(list2.isEmpty())
//				return;
//		}
		
		List<Player> players = new ArrayList<>();
		int i = 0;
		while (i < list.size()) {
			String line = list.get(i++);
			String club = line;
			line = list.get(i++);
			while (!line.equals("") && i < list.size()) {
				String[] elements = line.split("\\t");
				Player player = new Player();
				player.setClub(club);
				player.setName(elements[0]);
				player.setGames(elements[1]);
				players.add(player);
				line = list.get(i++);
			}
		}
//		i = 0;
//		while (i < list2.size()) {
//			String line = list2.get(i++);
//			String club = line;
//			line = list2.get(i++);
//			while (!line.equals("") && i < list2.size()) {
//				String[] elements = line.split("\\t");
//				Player player = new Player();
//				player.setClub(club);
//				player.setName(elements[0]);
//				player.setGames(elements[1]);
//				players.add(player);
//				line = list2.get(i++);
//			}
//		}
		players.stream().forEach(player -> System.out.println(player));
		Map<String, Set<String>> map = new HashMap<>();
		Map<String, Set<String>> clubsWithDupPlayers = new HashMap<>();
		players.stream().forEach(player -> {
			Set<String> clubs = map.get(player.getName());
			if (clubs == null) {
				clubs = new HashSet<>();
			}
			clubs.add(player.getClub());
			map.put(player.getName(), clubs);

			clubs.stream().forEach(club -> {
				Set<String> clubPlayers = clubsWithDupPlayers.get(club);
				if (clubPlayers == null) {
					clubPlayers = new HashSet<>();
				}
				clubPlayers.add(player.getName());
				clubsWithDupPlayers.put(club, clubPlayers);
			});
		});
		
		
		
		Set<String> klubovi = players.stream().map(Player::getClub).collect(Collectors.toSet());
		Map<String, Map<String, Integer>> klubToKlub = new HashMap<>();
		klubovi.stream().forEach(klub -> klubToKlub.put(klub, new HashMap<>()));
		List<Player> skip = new ArrayList<>();
		players.stream().forEach(player -> {
			if (skip.stream().filter(skipi -> skipi.getName().equals(player.getName())).collect(Collectors.toList()).size() == 0) {
				List<Player> inAllClubs = players.stream().filter(player1 -> player.getName().equals(player1.getName())).collect(Collectors.toList());
				List<String> allTeams = inAllClubs.stream().map(Player::getClub).collect(Collectors.toList());
				allTeams.stream().forEach(team -> {
					Map<String, Integer> mapica = klubToKlub.get(team);
					List<String> restTeams = allTeams.stream().filter(item -> !item.equals(team)).collect(Collectors.toList());
					restTeams.stream().forEach(restTeam -> {
						Integer num = mapica.get(restTeam);
						if (num != null) {
							num = num + 1;
						} else {
							num = 1;
						}
						mapica.put(restTeam, num);
					});
				});
				skip.add(player);
			}
		});
		
		String[] klub2klub = new String[1];
		klub2klub[0] = "";
		klubToKlub.entrySet().forEach(element -> {
//			List<String> bljak = element.getValue().stream().filter(value -> map.get(value).size() > 1).collect(Collectors.toList());
			klub2klub[0] = klub2klub[0].concat(element.getKey() + "\t");
			klub2klub[0] = klub2klub[0].concat(element.getValue() + "\r\n"); 
	});
	try {
	      File myObj = new File("D:/share/draw/klub2klub.txt");
	      if (myObj.createNewFile());
	      FileWriter myWriter = new FileWriter("D:/share/draw/klub2klub.txt");
	      myWriter.write(klub2klub[0]);
	      myWriter.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }		
		
		String[] igraci = new String[1];
		igraci[0] = "";
		map.entrySet().forEach(element -> {
			if (element.getValue().size() > 1) {
				igraci[0] = igraci[0].concat(element.getValue().size() + "\t");
			}
			if (element.getValue().size() > 1) {
				System.out.println(element.getKey() + " " + element.getValue());
				igraci[0] = igraci[0].concat(element.getKey() + " " + element.getValue() + "\r\n");
			}
		});
		 try {
		      File myObj = new File("D:/share/draw/players2.txt");
		      if (myObj.createNewFile());
		      FileWriter myWriter = new FileWriter("D:/share/draw/players2.txt");
		      myWriter.write(igraci[0]);
		      myWriter.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		 String[] timovi = new String[1];
			timovi[0] = "";
		clubsWithDupPlayers.entrySet().forEach(element -> {
			if (element.getValue().size() > 1) {
				List<String> bljak = element.getValue().stream().filter(value -> map.get(value).size() > 1).collect(Collectors.toList());
				System.out.println(element.getKey() + " " + bljak);
				timovi[0] = timovi[0].concat(bljak.size() + "\t");
				timovi[0] = timovi[0].concat(element.getKey() + " " + bljak + "\r\n"); 
			}
		});
		try {
		      File myObj = new File("D:/share/draw/clubs2.txt");
		      if (myObj.createNewFile());
		      FileWriter myWriter = new FileWriter("D:/share/draw/clubs2.txt");
		      myWriter.write(timovi[0]);
		      myWriter.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}

	class Player {
		String name;
		String club;
		String games;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClub() {
			return club;
		}

		public void setClub(String club) {
			this.club = club;
		}

		public String getGames() {
			return games;
		}

		public void setGames(String games) {
			this.games = games;
		}

		public String toString() {
			return this.getName() + " " + this.getGames() + " " + this.getClub();
		}
	}

}
