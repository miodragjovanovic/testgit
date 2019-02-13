package com.name.no.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.name.no.entity.Group;
import com.name.no.entity.Team;
import com.name.no.helper.Test;

@RequestMapping("/api")
@RestController
public class TestController {

	@Autowired
	Test test;
	
	@RequestMapping(value="/test")
	public ResponseEntity<String> test() {
		for (Group group : test.getGroups()) {
			System.out.println(group.getName());
		}
		
		for (Team team : test.getTeams()) {
			System.out.println(team.getName() + " " + team.getCountry() + " " + team.getCoefficient());
		}
		
		return ResponseEntity.ok().body("OK");
	}
	
}
