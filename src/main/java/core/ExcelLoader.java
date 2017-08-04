package core;

import database.DataProcessing;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelLoader {
    /*public static void main(String[] args) {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            for (int i = 4; i < 11; i++) {
                Sheet dataSheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = dataSheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    int number;
                    String name;
                    int price;
                    int curr;
                    int inv;
                    Cell cell = row.getCell(1);
                    Double a = cell.getNumericCellValue();
                    number = a.intValue();
                    name = row.getCell(2).getStringCellValue();
                    a = row.getCell(3).getNumericCellValue();
                    price = a.intValue();
                    a = row.getCell(4).getNumericCellValue();
                    curr = a.intValue();
                    a = row.getCell(5).getNumericCellValue();
                    inv = a.intValue();
                    DataProcessing.help(number, name, price, curr, inv);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void doStuff() {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            for (int i = 4; i < 11; i++) {
                Sheet dataSheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = dataSheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    int number;
                    String name;
                    int price;
                    int curr;
                    int inv;
                    Cell cell = row.getCell(0);
                    System.out.println(cell);
                    Double a = cell.getNumericCellValue();
                    number = a.intValue();
                    name = row.getCell(1).getStringCellValue();
                    a = row.getCell(2).getNumericCellValue();
                    price = a.intValue();
                    a = row.getCell(3).getNumericCellValue();
                    curr = a.intValue();
                    a = row.getCell(4).getNumericCellValue();
                    inv = a.intValue();
                    DataProcessing.help(number, name, price, curr, inv);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
