package com.name.no.service;

import com.name.no.dto.PlayerName;
import com.name.no.dto.PlayerXlsx;
import com.name.no.dto.TeamLink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Scanner;

import static java.time.temporal.ChronoField.EPOCH_DAY;

@Service
public class TeamLinkCreation {

	public void fillData(Sheet sheet3, Long artifId) {
		Scanner in = new Scanner(System.in);

		TeamLink teamLink = new TeamLink();

		System.out.println("jerseynumber");
		teamLink.setJerseynumber(in.nextInt());
		System.out.println("teamid");
		teamLink.setTeamid(in.nextLong());

		System.out.println("playerid");
		long playerID = in.nextLong();
		teamLink.setArtificialkey(artifId);
		teamLink.setPlayerid(playerID);

		fillInTeamLinkSheet(sheet3, teamLink);

	}
	

	void fillInTeamLinkSheet(Sheet sheet, TeamLink teamLink) {
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.createRow(rowNum + 1);
		int cellCount = 0;

		for (int i = 0; i < 16; i++) {
			row.createCell(i);
		}

		row.getCell(cellCount++).setCellValue(teamLink.getLeaguegoals());
		row.getCell(cellCount++).setCellValue(teamLink.getIsamongtopscorers());
		row.getCell(cellCount++).setCellValue(teamLink.getYellows());
		row.getCell(cellCount++).setCellValue(teamLink.getIsamongtopscorersinteam());
		row.getCell(cellCount++).setCellValue(teamLink.getJerseynumber());
		row.getCell(cellCount++).setCellValue(teamLink.getPosition());
		row.getCell(cellCount++).setCellValue(teamLink.getArtificialkey());
		row.getCell(cellCount++).setCellValue(teamLink.getTeamid());
		row.getCell(cellCount++).setCellValue(teamLink.getLeaguegoalsprevmatch());
		row.getCell(cellCount++).setCellValue(teamLink.getInjury());
		row.getCell(cellCount++).setCellValue(teamLink.getLeagueappearances());
		row.getCell(cellCount++).setCellValue(teamLink.getIstopscorer());
		row.getCell(cellCount++).setCellValue(teamLink.getLeaguegoalsprevthreematches());
		row.getCell(cellCount++).setCellValue(teamLink.getPlayerid());
		row.getCell(cellCount++).setCellValue(teamLink.getForm());
		row.getCell(cellCount++).setCellValue(teamLink.getReds());

	}

}
