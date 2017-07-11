package core;




import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelLoader {
    public static void loadExcel() {
        FileInputStream excelFile;
        try {
            excelFile = new FileInputStream(new File("prog.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataSheet = workbook.getSheetAt(2);
            Iterator<Row> rowIterator = dataSheet.iterator();
            while (rowIterator.hasNext()) {
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


    }
}
