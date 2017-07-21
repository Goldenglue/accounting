package core;




import database.DataProcessing;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class ExcelLoader {
    /*public static void main(String[] args) {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataSheet = workbook.getSheetAt(2);
            for (int i = 2; i < 271 ; i++) {
                Cell cell = dataSheet.getRow(i).getCell(6);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    DataProcessing.insertRentorIntoDatabase(cell.getStringCellValue());
                }
            }
            System.out.println("end");
            /*while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                for (int i = 0; i < 7; i++) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        System.out.println("string cell number " + i + " " + cell);
                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.println("numeric cell number " + i + " " + cell);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void load() {
        FileInputStream excelFile;
        Set<String> set = new TreeSet<>();
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataSheet = workbook.getSheetAt(2);
            for (int i = 2; i < 271 ; i++) {
                Cell cell = dataSheet.getRow(i).getCell(6);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    set.add(cell.getStringCellValue());
                }
            }
            set.forEach(DataProcessing::insertRentorIntoDatabase);
            System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
