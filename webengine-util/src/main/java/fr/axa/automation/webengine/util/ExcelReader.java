package fr.axa.automation.webengine.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ExcelReader {

    private ExcelReader() {
    }


    public static Workbook getWorkbook(String fileName) {
        Workbook wb = getWorkbookByHSSF( fileName );
        if ( wb == null ) {
            wb = getWorkbookByXSSF( fileName );
        }
        return wb;
    }

    public static Workbook getWorkbookByHSSF(String fileName) {
        try {
            return new HSSFWorkbook(new FileInputStream(fileName));
        } catch (IOException | OfficeXmlFileException e) {
            return null;
        }
    }

    public static Workbook getWorkbookByXSSF(String fileName) {
        try {
            return new XSSFWorkbook(new FileInputStream(fileName));
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> getAllSheetName(Workbook workbook){
        List<String> sheetNameList = new ArrayList();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++){
            sheetNameList.add(workbook.getSheetName( i ));
        }
        return sheetNameList;
    }


    public static String getCellValue(Sheet currentSheet, Integer rowIndex,int colIndex) {
        return getCellValue(currentSheet.getRow(rowIndex),colIndex);
    }

    public static String getCellValue(Row rowIndex, int colIndex) {
        try {
            CellType cellType = rowIndex.getCell(colIndex).getCellType();
            switch (cellType){
                case NUMERIC:
                    int numericCellValue = (int)rowIndex.getCell(colIndex).getNumericCellValue();
                    return String.valueOf(numericCellValue);
                default:
                    String cellValue = rowIndex.getCell(colIndex).getStringCellValue();
                    return cellValue.replaceFirst("^-*", "");
            }
            //For if/elseif/else/call/optional
        } catch (NullPointerException e) {
            return StringUtils.EMPTY;
        }
    }

}