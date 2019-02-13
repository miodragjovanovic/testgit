package com.name.no.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.name.no.entity.Group;
import com.name.no.entity.Team;
import com.name.no.object.PostRequestWrapper;
import com.name.no.object.Pot;
import com.name.no.object.PotTO;
import com.name.no.object.TeamTO;
import com.name.no.service.DrawService;

@RequestMapping("/api/draw")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DrawController {
	
	@Autowired
	private DrawService drawService;
	
	@RequestMapping("/initialize")
	public void initialize() {
		drawService.initialize();
	}
	
	@RequestMapping("/listPots")
	public List<PotTO> listPots() {
		List<Pot> list = new ArrayList<Pot>(drawService.listPots().values());
		return PotTO.translate(list);
	}
	
	@RequestMapping("/draw")
	public void draw() throws InterruptedException {
		drawService.draw();
	}
	
	@RequestMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Group> test() {
		return drawService.getGroups();
	}
	
	@RequestMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Team> getTeams() {
		return drawService.getTeams();
	}
	
	@RequestMapping("/commandDrawTeam")
	public TeamTO commandDrawTeam(@RequestBody PotTO potTO) throws InterruptedException {
		return drawService.commandDrawTeam(potTO);
	}
	
	@RequestMapping("/commandDrawGroup")
	public Group commandDrawGroup(@RequestBody PostRequestWrapper postRequestWrapper) throws InterruptedException {
		return drawService.commandDrawGroup(postRequestWrapper.getPotTO(), postRequestWrapper.getTeamTO());
	}
	
	@RequestMapping(value = "/teamsFromGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TeamTO> getTeamsPerGroup(@RequestBody String group) {
		return drawService.getTeamsFromGroup(group);
	}

}
