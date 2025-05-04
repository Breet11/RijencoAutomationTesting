package TestNG.program;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import org.apache.poi.xssf.usermodel.XSSFCell;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {
    private static final String MATRICES_PATH = "matrices" + File.separator;
    private static final String TEST_DATA_SHEET_NAME = "test_data";
    private static final String REGISTRATION_PAGE_SPREADSHEET_NAME = "RegistrationPageTests.xlsx";

    @DataProvider(name= "RegistrationDataProvider")
    public static Object[][] registrationDataProvider(){
        return loadDataFromMatrix(REGISTRATION_PAGE_SPREADSHEET_NAME);
    }

    private static Object[][] loadDataFromMatrix(String matrixName){
        List<Map<String, String>> allTestData = new ArrayList<>();
        try(XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(MATRICES_PATH + matrixName)))){
            XSSFSheet testSheet = workbook.getSheet(TEST_DATA_SHEET_NAME);
            XSSFRow firstRow = testSheet.getRow(0);

            short firstRowColumnCount = firstRow.getLastCellNum();
            int rowsCount = testSheet.getLastRowNum();

            for(int rowNumber=1; rowNumber<=rowsCount; rowNumber++){
                XSSFRow testDataRow = testSheet.getRow(rowNumber);

                if(testDataRow != null){
                    Map<String,String> singleTestData = new HashMap<>();

                    for(int columnNumber = 0; columnNumber < firstRowColumnCount; columnNumber++){
                        XSSFCell keyCell = firstRow.getCell(columnNumber);
                        XSSFCell valueCell = valueCell = testDataRow.getCell(columnNumber);

                        if(keyCell != null && valueCell != null){
                            String key = keyCell.getStringCellValue();
                            String value = valueCell.getStringCellValue();

                            singleTestData.put(key, value);
                        }
                    }
                    allTestData.add(singleTestData);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object[][] data = new Object[allTestData.size()][];

        for(int i=0;i<allTestData.size();i++){
            Map<String, String> singeTestData = allTestData.get(i);
            data[i] = new Object[] {singeTestData};
        }
        return data;
    }
}
