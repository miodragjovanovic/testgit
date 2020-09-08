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
public class PlayerCreation {

	public void fillData(Sheet sheet1, Sheet sheet2, Sheet sheet3) {
		Scanner in = new Scanner(System.in);

		PlayerXlsx playerXlsx = new PlayerXlsx();
		TeamLink teamLink = new TeamLink();
		PlayerName playerName = new PlayerName();

		System.out.println("Is GK?");
		String s = in.nextLine();
		boolean isGK = Boolean.parseBoolean(s);

		System.out.println("firstname");
		playerName.setFirstname(in.nextLine());
		System.out.println("lastname");
		String lastName = in.nextLine();
		playerName.setPlayerjerseyname(lastName);
		playerName.setSurname(lastName);

		System.out.println("jerseynumber");
		teamLink.setJerseynumber(in.nextInt());
		System.out.println("teamid");
		teamLink.setTeamid(in.nextLong());

		System.out.println("headassetid");
		playerXlsx.setHeadassetid(in.nextLong());
		System.out.println("playerid");
		long playerID = in.nextLong();
		System.out.println("artificialkey");
		teamLink.setArtificialkey(in.nextLong());
		playerXlsx.setPlayerid(playerID);
		playerName.setPlayerid(playerID);
		teamLink.setPlayerid(playerID);

		System.out.println("lastnameid, or 0");
		long lastnameid = in.nextLong();
		if (lastnameid != 0) {
			playerXlsx.setLastnameid(lastnameid);
			playerXlsx.setPlayerjerseynameid(lastnameid);
			playerXlsx.setCommonnameid(lastnameid);
		}

		System.out.println("bodytypecode");
		playerXlsx.setBodytypecode(in.nextLong());
		System.out.println("skintonecode");
		playerXlsx.setSkintonecode(in.nextLong());
		System.out.println("height");
		playerXlsx.setHeight(in.nextLong());
		System.out.println("weight");
		playerXlsx.setWeight(in.nextLong());
		System.out.println("nationality");
		playerXlsx.setNationality(in.nextLong());
		System.out.println("preferredfoot");
		playerXlsx.setPreferredfoot(in.nextLong());
		System.out.println("weakfootabilitytypecode");
		playerXlsx.setWeakfootabilitytypecode(in.nextLong());

		System.out.println("birthdate day");
		int day = in.nextInt();
		System.out.println("birthdate month");
		int month = in.nextInt();
		System.out.println("birthdate year");
		int year = in.nextInt();

		playerXlsx.setBirthdate(generateDate(day, month, year));

		System.out.println("jerseystylecode");
		playerXlsx.setJerseystylecode(in.nextLong());
		System.out.println("jerseysleevelengthcode");
		playerXlsx.setJerseysleevelengthcode(in.nextLong());

		System.out.println("potential");
		playerXlsx.setPotential(in.nextLong());

		System.out.println("agility");
		playerXlsx.setAgility(in.nextLong());
		System.out.println("strength");
		playerXlsx.setStrength(in.nextLong());
		System.out.println("jumping");
		playerXlsx.setJumping(in.nextLong());
		System.out.println("stamina");
		playerXlsx.setStamina(in.nextLong());
		System.out.println("sprintspeed");
		playerXlsx.setSprintspeed(in.nextLong());
		System.out.println("acceleration");
		playerXlsx.setAcceleration(in.nextLong());
		System.out.println("balance");
		playerXlsx.setBalance(in.nextLong());

		System.out.println("positioning");
		playerXlsx.setPositioning(in.nextLong());
		System.out.println("reactions");
		playerXlsx.setReactions(in.nextLong());
		System.out.println("composure");
		playerXlsx.setComposure(in.nextLong());

		if (isGK) {
			System.out.println("Is GK");
			// prompt
			System.out.println("gkdiving");
			playerXlsx.setGkdiving(in.nextLong());
			System.out.println("gkreflexes");
			playerXlsx.setGkreflexes(in.nextLong());
			System.out.println("gkkicking");
			playerXlsx.setGkkicking(in.nextLong());
			System.out.println("gkhandling");
			playerXlsx.setGkhandling(in.nextLong());
			System.out.println("gkpositioning");
			playerXlsx.setGkpositioning(in.nextLong());
			// hardcode
			playerXlsx.setPreferredposition1(0);

			playerXlsx.setStandingtackle(30);
			playerXlsx.setSlidingtackle(30);
			playerXlsx.setInterceptions(30);
			playerXlsx.setMarking(30);

			playerXlsx.setDribbling(10);
			playerXlsx.setBallcontrol(20);

			playerXlsx.setSkillmoves(0);
			playerXlsx.setSkillmoveslikelihood(0);

			playerXlsx.setShortpassing(10);
			playerXlsx.setLongpassing(10);
			playerXlsx.setCrossing(10);
			playerXlsx.setVision(10);

			playerXlsx.setHeadingaccuracy(10);
			playerXlsx.setFinishing(10);
			playerXlsx.setShotpower(10);
			playerXlsx.setLongshots(10);
			playerXlsx.setVolleys(10);
			playerXlsx.setPenalties(10);
			playerXlsx.setFreekickaccuracy(10);
			playerXlsx.setCurve(10);

			playerXlsx.setAggression(10);
			playerXlsx.setAttackingworkrate(0);
			playerXlsx.setDefensiveworkrate(2);
		} else {
			System.out.println("Is not GK");
			//prompt
			System.out.println("preferredposition1");
			playerXlsx.setPreferredposition1(in.nextLong());

			System.out.println("standingtackle");
			playerXlsx.setStandingtackle(in.nextLong());
			System.out.println("slidingtackle");
			playerXlsx.setSlidingtackle(in.nextLong());
			System.out.println("interceptions");
			playerXlsx.setInterceptions(in.nextLong());
			System.out.println("marking");
			playerXlsx.setMarking(in.nextLong());

			System.out.println("dribbling");
			playerXlsx.setDribbling(in.nextLong());
			System.out.println("ballcontrol");
			playerXlsx.setBallcontrol(in.nextLong());

			System.out.println("skillmoves");
			playerXlsx.setSkillmoves(in.nextLong());
			System.out.println("skillmoveslikelihood");
			playerXlsx.setSkillmoveslikelihood(in.nextLong());

			System.out.println("shortpassing");
			playerXlsx.setShortpassing(in.nextLong());
			System.out.println("longpassing");
			playerXlsx.setLongpassing(in.nextLong());
			System.out.println("crossing");
			playerXlsx.setCrossing(in.nextLong());
			System.out.println("vision");
			playerXlsx.setVision(in.nextLong());

			System.out.println("headingaccuracy");
			playerXlsx.setHeadingaccuracy(in.nextLong());
			System.out.println("finishing");
			playerXlsx.setFinishing(in.nextLong());
			System.out.println("shotpower");
			playerXlsx.setShotpower(in.nextLong());
			System.out.println("longshots");
			playerXlsx.setLongshots(in.nextLong());
			System.out.println("volleys");
			playerXlsx.setVolleys(in.nextLong());
			System.out.println("penalties");
			playerXlsx.setPenalties(in.nextLong());
			System.out.println("freekickaccuracy");
			playerXlsx.setFreekickaccuracy(in.nextLong());
			System.out.println("curve");
			playerXlsx.setCurve(in.nextLong());

			System.out.println("aggression");
			playerXlsx.setAggression(in.nextLong());
			System.out.println("attackingworkrate");
			playerXlsx.setAttackingworkrate(in.nextLong());
			System.out.println("defensiveworkrate");
			playerXlsx.setDefensiveworkrate(in.nextLong());
			// hardcode
			playerXlsx.setGkdiving(10);
			playerXlsx.setGkreflexes(10);
			playerXlsx.setGkkicking(10);
			playerXlsx.setGkhandling(10);
			playerXlsx.setGkpositioning(10);
		}

		fillInPlayerSheet(sheet1, playerXlsx);
		fillInPlayerNameSheet(sheet2, playerName);
		fillInTeamLinkSheet(sheet3, teamLink);
	}
	
	void fillInPlayerSheet(Sheet sheet, PlayerXlsx player) {
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.createRow(rowNum + 1);
		int cellCount = 0;
		
		for (int i = 0; i < 124; i++) {
			row.createCell(i);
		}
		
		row.getCell(cellCount++).setCellValue(player.getFirstnameid());
		row.getCell(cellCount++).setCellValue(player.getLastnameid());
		row.getCell(cellCount++).setCellValue(player.getPlayerjerseynameid());
		row.getCell(cellCount++).setCellValue(player.getCommonnameid());
		row.getCell(cellCount++).setCellValue(player.getSkintypecode());
		row.getCell(cellCount++).setCellValue(player.getTrait2());
		row.getCell(cellCount++).setCellValue(player.getBodytypecode());
		row.getCell(cellCount++).setCellValue(player.getHaircolorcode());
		row.getCell(cellCount++).setCellValue(player.getFacialhairtypecode());
		row.getCell(cellCount++).setCellValue(player.getCurve());
		row.getCell(cellCount++).setCellValue(player.getJerseystylecode());
		row.getCell(cellCount++).setCellValue(player.getAgility());
		row.getCell(cellCount++).setCellValue(player.getTattooback());
		row.getCell(cellCount++).setCellValue(player.getAccessorycode4());
		row.getCell(cellCount++).setCellValue(player.getGksavetype());
		row.getCell(cellCount++).setCellValue(player.getPositioning());
		row.getCell(cellCount++).setCellValue(player.getTattooleftarm());
		row.getCell(cellCount++).setCellValue(player.getHairtypecode());
		row.getCell(cellCount++).setCellValue(player.getStandingtackle());
		row.getCell(cellCount++).setCellValue(player.getPreferredposition3());
		row.getCell(cellCount++).setCellValue(player.getLongpassing());
		row.getCell(cellCount++).setCellValue(player.getPenalties());
		row.getCell(cellCount++).setCellValue(player.getAnimfreekickstartposcode());
		row.getCell(cellCount++).setCellValue(player.getAnimpenaltieskickstylecode());
		row.getCell(cellCount++).setCellValue(player.getIsretiring());
		row.getCell(cellCount++).setCellValue(player.getLongshots());
		row.getCell(cellCount++).setCellValue(player.getGkdiving());
		row.getCell(cellCount++).setCellValue(player.getInterceptions());
		row.getCell(cellCount++).setCellValue(player.getShoecolorcode2());
		row.getCell(cellCount++).setCellValue(player.getCrossing());
		row.getCell(cellCount++).setCellValue(player.getPotential());
		row.getCell(cellCount++).setCellValue(player.getGkreflexes());
		row.getCell(cellCount++).setCellValue(player.getFinishingcode1());
		row.getCell(cellCount++).setCellValue(player.getReactions());
		row.getCell(cellCount++).setCellValue(player.getComposure());
		row.getCell(cellCount++).setCellValue(player.getVision());
		row.getCell(cellCount++).setCellValue(player.getContractvaliduntil());
		row.getCell(cellCount++).setCellValue(player.getAnimpenaltiesapproachcode());
		row.getCell(cellCount++).setCellValue(player.getFinishing());
		row.getCell(cellCount++).setCellValue(player.getDribbling());
		row.getCell(cellCount++).setCellValue(player.getSlidingtackle());
		row.getCell(cellCount++).setCellValue(player.getAccessorycode3());
		row.getCell(cellCount++).setCellValue(player.getAccessorycolourcode1());
		row.getCell(cellCount++).setCellValue(player.getHeadtypecode());
		row.getCell(cellCount++).setCellValue(player.getSprintspeed());
		row.getCell(cellCount++).setCellValue(player.getHeight());
		row.getCell(cellCount++).setCellValue(player.getHasseasonaljersey());
		row.getCell(cellCount++).setCellValue(player.getTattoohead());
		row.getCell(cellCount++).setCellValue(player.getPreferredposition2());
		row.getCell(cellCount++).setCellValue(player.getStrength());
		row.getCell(cellCount++).setCellValue(player.getShoetypecode());
		row.getCell(cellCount++).setCellValue(player.getBirthdate());
		row.getCell(cellCount++).setCellValue(player.getPreferredposition1());
		row.getCell(cellCount++).setCellValue(player.getTattooleftleg());
		row.getCell(cellCount++).setCellValue(player.getBallcontrol());
		row.getCell(cellCount++).setCellValue(player.getShotpower());
		row.getCell(cellCount++).setCellValue(player.getTrait1());
		row.getCell(cellCount++).setCellValue(player.getSocklengthcode());
		row.getCell(cellCount++).setCellValue(player.getWeight());
		row.getCell(cellCount++).setCellValue(player.getHashighqualityhead());
		row.getCell(cellCount++).setCellValue(player.getGkglovetypecode());
		row.getCell(cellCount++).setCellValue(player.getTattoorightarm());
		row.getCell(cellCount++).setCellValue(player.getBalance());
		row.getCell(cellCount++).setCellValue(player.getGender());
		row.getCell(cellCount++).setCellValue(player.getHeadassetid());
		row.getCell(cellCount++).setCellValue(player.getGkkicking());
		row.getCell(cellCount++).setCellValue(player.getInternationalrep());
		row.getCell(cellCount++).setCellValue(player.getAnimpenaltiesmotionstylecode());
		row.getCell(cellCount++).setCellValue(player.getShortpassing());
		row.getCell(cellCount++).setCellValue(player.getFreekickaccuracy());
		row.getCell(cellCount++).setCellValue(player.getSkillmoves());
		row.getCell(cellCount++).setCellValue(player.getFaceposerpreset());
		row.getCell(cellCount++).setCellValue(player.getUsercaneditname());
		row.getCell(cellCount++).setCellValue(player.getAvatarpomid());
		row.getCell(cellCount++).setCellValue(player.getAttackingworkrate());
		row.getCell(cellCount++).setCellValue(player.getFinishingcode2());
		row.getCell(cellCount++).setCellValue(player.getAggression());
		row.getCell(cellCount++).setCellValue(player.getAcceleration());
		row.getCell(cellCount++).setCellValue(player.getHeadingaccuracy());
		row.getCell(cellCount++).setCellValue(player.getIscustomized());
		row.getCell(cellCount++).setCellValue(player.getEyebrowcode());
		row.getCell(cellCount++).setCellValue(player.getRunningcode2());
		row.getCell(cellCount++).setCellValue(player.getModifier());
		row.getCell(cellCount++).setCellValue(player.getGkhandling());
		row.getCell(cellCount++).setCellValue(player.getEyecolorcode());
		row.getCell(cellCount++).setCellValue(player.getJerseysleevelengthcode());
		row.getCell(cellCount++).setCellValue(player.getAccessorycode3());
		row.getCell(cellCount++).setCellValue(player.getAccessorycode1());
		row.getCell(cellCount++).setCellValue(player.getPlayerjointeamdate());
		row.getCell(cellCount++).setCellValue(player.getHeadclasscode());
		row.getCell(cellCount++).setCellValue(player.getDefensiveworkrate());
		row.getCell(cellCount++).setCellValue(player.getTattoofront());
		row.getCell(cellCount++).setCellValue(player.getNationality());
		row.getCell(cellCount++).setCellValue(player.getPreferredfoot());
		row.getCell(cellCount++).setCellValue(player.getSideburnscode());
		row.getCell(cellCount++).setCellValue(player.getWeakfootabilitytypecode());
		row.getCell(cellCount++).setCellValue(player.getJumping());
		row.getCell(cellCount++).setCellValue(player.getPersonality());
		row.getCell(cellCount++).setCellValue(player.getGkkickstyle());
		row.getCell(cellCount++).setCellValue(player.getStamina());
		row.getCell(cellCount++).setCellValue(player.getPlayerid());
		row.getCell(cellCount++).setCellValue(player.getMarking());
		row.getCell(cellCount++).setCellValue(player.getAccessorycolourcode4());
		row.getCell(cellCount++).setCellValue(player.getGkpositioning());
		row.getCell(cellCount++).setCellValue(player.getHeadvariation());
		row.getCell(cellCount++).setCellValue(player.getSkillmoveslikelihood());
		row.getCell(cellCount++).setCellValue(player.getSkintonecode());
		row.getCell(cellCount++).setCellValue(player.getShortstyle());
		row.getCell(cellCount++).setCellValue(player.getOverallrating());
		row.getCell(cellCount++).setCellValue(player.getSmallsidedshoetypecode());
		row.getCell(cellCount++).setCellValue(player.getEmotion());
		row.getCell(cellCount++).setCellValue(player.getRunstylecode());
		row.getCell(cellCount++).setCellValue(player.getJerseyfit());
		row.getCell(cellCount++).setCellValue(player.getAccessorycode2());
		row.getCell(cellCount++).setCellValue(player.getShoetypecode());
		row.getCell(cellCount++).setCellValue(player.getShoecolorcode1());
		row.getCell(cellCount++).setCellValue(player.getHairstylecode());
		row.getCell(cellCount++).setCellValue(player.getAnimpenaltiesstartposcode());
		row.getCell(cellCount++).setCellValue(player.getRunningcode1());
		row.getCell(cellCount++).setCellValue(player.getPreferredposition4());
		row.getCell(cellCount++).setCellValue(player.getVolleys());
		row.getCell(cellCount++).setCellValue(player.getAccessorycolourcode2());
		row.getCell(cellCount++).setCellValue(player.getTattoorightleg());
		row.getCell(cellCount++).setCellValue(player.getFacialhaircolorcode());
	}

	void fillInPlayerNameSheet(Sheet sheet, PlayerName playerName) {
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.createRow(rowNum + 1);
		int cellCount = 0;

		for (int i = 0; i < 5; i++) {
			row.createCell(i);
		}

		row.getCell(cellCount++).setCellValue(playerName.getFirstname());
		row.getCell(cellCount++).setCellValue(playerName.getCommonname());
		row.getCell(cellCount++).setCellValue(playerName.getPlayerjerseyname());
		row.getCell(cellCount++).setCellValue(playerName.getSurname());
		row.getCell(cellCount++).setCellValue(playerName.getPlayerid());
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

	public long generateDate(int day, int month, int year) {
		LocalDate date = LocalDate.of(year, month, day);
		LocalDate refDate = LocalDate.of(1984, 5, 11);
		long value = 146672;
		return date.getLong(EPOCH_DAY) - refDate.getLong(EPOCH_DAY) + value;
	}

}
