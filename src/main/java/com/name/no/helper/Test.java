package com.name.no.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.name.no.entity.Group;
import com.name.no.entity.Team;
import com.name.no.repository.GroupRepository;
import com.name.no.repository.TeamRepository;

@Service
public class Test {
	
	private final GroupRepository groupRepository;
	
	private final TeamRepository teamRepository;
	
	@Autowired
	public Test(GroupRepository groupRepository, TeamRepository teamRepository) {
		this.groupRepository = groupRepository;
		this.teamRepository = teamRepository;
	}
	
	public List<Group> getGroups() {
		System.out.println("No of groups: " + groupRepository.count());
		List<Group> groups = (List<Group>) groupRepository.findAll();
		return groups;
	}
	
	public List<Team> getTeams() {
		System.out.println("No of teams: " + teamRepository.count());
		List<Team> teams = (List<Team>) teamRepository.findAll();
		return teams;
	}
	
}
