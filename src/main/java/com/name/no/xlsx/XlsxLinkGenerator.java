package com.name.no.xlsx;

import com.name.no.service.PlayerCreation;
import com.name.no.service.TeamLinkCreation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class XlsxLinkGenerator {

	TeamLinkCreation playerLinkCreation = new TeamLinkCreation();

	static long artifId = 22427;

	public XlsxLinkGenerator() throws Exception {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

		FileInputStream file = new FileInputStream(new File(fileLocation));
		Workbook workbook = new XSSFWorkbook(file);

		Sheet sheet3 = workbook.getSheetAt(2);

		boolean end = false;

		while (!end) {
			playerLinkCreation.fillData(sheet3, artifId++);

			Scanner in = new Scanner(System.in);

			System.out.println("end?");
			end = Boolean.parseBoolean(in.next());
		}

			try {
				FileOutputStream outputStream = new FileOutputStream(fileLocation);
				workbook.write(outputStream);
				workbook.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}

	public static void main(String[] args) throws Exception {
		new XlsxLinkGenerator();
	}


}
