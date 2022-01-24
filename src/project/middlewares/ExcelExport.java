package project.middlewares;

import javafx.scene.control.TableView;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExport<T> {

    public void export(TableView<T> tableView, String cfName, File file){
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet =  hssfWorkbook.createSheet();
        HSSFRow firstRow = hssfSheet.createRow(0);
        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        Font font = hssfWorkbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        ///set titles of columns
        for (int i = 0; i < tableView.getColumns().size(); i++){
            firstRow.setHeight((short) 700);
            firstRow.setRowStyle(cellStyle);
            firstRow.createCell(i).setCellValue(tableView.getColumns().get(i).getText());
        }


        for (int row=0; row<tableView.getItems().size(); row++){

            HSSFRow hssfRow = hssfSheet.createRow(row+1);

            for (int col=0; col<tableView.getColumns().size(); col++){

                Object celValue = tableView.getColumns().get(col).getCellObservableValue(row).getValue();

                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                        short s = 500;
                        hssfRow.setHeight(s);
                    }
                } catch (NumberFormatException e ){
                    hssfRow.createCell(col).setCellValue(celValue.toString());
                    short s = 500;
                    hssfRow.setHeight(s);
                }

            }

        }

        // save Excel file and close the workbook
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file+".xls");
            hssfWorkbook.write(fileOutputStream);
            System.out.println("Created");
            hssfWorkbook.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}