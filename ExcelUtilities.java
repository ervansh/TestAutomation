package com.tos.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author vanshraj.singh
 */

public class ExcelUtilities {
	/**
	 * @param sheetName
	 * @param rowNum
	 * @param cellNum
	 * @return
	 */
	private static final String FILEPATH = ".\\test-data\\TosRel11-TestData.xlsx";
	public static FileInputStream fis;
	public static Workbook wb;

	public static String readData(String sheetName, int rowNum, int cellNum) {

		try {
			if (sheetName == null) {
				return "Enter Valid sheet name...";
			}
			fis = new FileInputStream(new File(FILEPATH));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			if (sheet == null)
				return "Sheet is empty...";
			Row row = sheet.getRow(rowNum);
			if (row == null)
				return "";
			Cell cell = row.getCell(cellNum);
			if (cell == null)
				return "";
			if (cell.getCellTypeEnum() == CellType.STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat datefarmate = new SimpleDateFormat("dd/MM/yyyy");
					return datefarmate.format(cell.getDateCellValue());
				} else
					return String.valueOf((int)cell.getNumericCellValue());
			} else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static int getRowNum(String sheetName) {
		int row = 0;
		try {
			fis = new FileInputStream(new File(FILEPATH));
			wb = WorkbookFactory.create(fis);
			if (wb != null) {
				Sheet sheet = wb.getSheet(sheetName);
				if (sheet != null) {
					row = sheet.getLastRowNum();
				}
			}
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			e.printStackTrace();
		}
		return row;
	}

	public static int getCellNum(String sheetName, int rowNum) {
		int cell = 0;
		try {
			fis = new FileInputStream(new File(FILEPATH));
			wb = WorkbookFactory.create(fis);
			if (wb != null) {
				Sheet sheet = wb.getSheet(sheetName);
				if (sheet != null) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						cell = row.getLastCellNum();
					} else {
						throw new NullPointerException("Rows in workbook are not provided.");
					}
				}
			}
		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			e.printStackTrace();
		}
		return cell;
	}

	public static void getRowData(String sheetName, int romNum) {

		try {
			fis = new FileInputStream(new File(FILEPATH));
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			sheet.getRow(romNum);

		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			e.getMessage();
		}
	}
}
