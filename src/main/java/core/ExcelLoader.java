package core;




import UI.CabinsTab;
import database.DataProcessing;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ExcelLoader {
    /*public static void main(String[] args) {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);

            for (int i = 4; i < 5; i++) {
                Sheet dataSheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = dataSheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    List<Cell> cellList = new ArrayList<>();
                    while (cellIterator.hasNext()){
                        cellList.add(cellIterator.next());
                    }
                    Double number;
                    Double rentPrice = cellList.get(2).getNumericCellValue();
                    Double currentPaymentAmount = cellList.get(3).getNumericCellValue();
                    Double invPrice = cellList.get(4).getNumericCellValue();
                    Date date = cellList.get(5).getDateCellValue();
                    LocalDate localDate = null;
                    if (date != null) {
                        Instant instant = date.toInstant();
                        localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    }

                    if (cellList.get(0).getCellTypeEnum() == CellType.NUMERIC) {
                        number = cellList.get(0).getNumericCellValue();
                    } else {
                        number = Double.valueOf(cellList.get(0).getStringCellValue());
                    }

                    CabinsTab.Cabin cabin = new CabinsTab.Cabin(number.intValue(),cellList.get(1).getStringCellValue(),currentPaymentAmount.intValue(),rentPrice.intValue(),invPrice.intValue(),localDate,cellList.get(6).getStringCellValue(),0,false,null);
                    CabinsTab.cabinObservableList.add(cabin);
                    //System.out.println("on row number: " + row.getRowNum() + " " + cabin.getName());
                }
            }
            /*for (int i = 2; i < 271 ; i++) {
                Cell cell = dataSheet.getRow(i).getCell(6);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    DataProcessing.insertRenterIntoDatabase(cell.getStringCellValue());
                }
            }
            System.out.println("end");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void load() {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);

            for (int i = 4; i < 5; i++) {
                Sheet dataSheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = dataSheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    List<Cell> cellList = new ArrayList<>();
                    while (cellIterator.hasNext()){
                        cellList.add(cellIterator.next());
                    }
                    Double number;
                    Double rentPrice = cellList.get(2).getNumericCellValue();
                    Double currentPaymentAmount = cellList.get(3).getNumericCellValue();
                    Double invPrice = cellList.get(4).getNumericCellValue();
                    Date date = cellList.get(5).getDateCellValue();
                    LocalDate localDate = null;
                    if (date != null) {
                        Instant instant = date.toInstant();
                        localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    }

                    if (cellList.get(0).getCellTypeEnum() == CellType.NUMERIC) {
                        number = cellList.get(0).getNumericCellValue();
                    } else {
                        number = Double.valueOf(cellList.get(0).getStringCellValue());
                    }

                    CabinsTab.Cabin cabin = new CabinsTab.Cabin(number.intValue(),cellList.get(1).getStringCellValue(),rentPrice.intValue(),currentPaymentAmount.intValue(),invPrice.intValue(),localDate,cellList.get(6).getStringCellValue(),0,false,null);
                    CabinsTab.cabinObservableList.add(cabin);
                    //System.out.println("on row number: " + row.getRowNum() + " " + cabin.getName());
                }
            }
            /*for (int i = 2; i < 271 ; i++) {
                Cell cell = dataSheet.getRow(i).getCell(6);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    DataProcessing.insertRenterIntoDatabase(cell.getStringCellValue());
                }
            }
            System.out.println("end");*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
