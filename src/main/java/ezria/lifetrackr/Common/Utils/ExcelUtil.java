package ezria.lifetrackr.Common.Utils;

import ezria.lifetrackr.Entity.Bill;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    public static List<Bill> readBillExcel(MultipartFile file, Long userid) throws Exception {
        List<Bill> BillList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 18; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Bill bill = new Bill();
            bill.setUserid(userid);
            bill.setCounterparty(getCellValue(row, 2));
            bill.setGoods(getCellValue(row, 3));
            bill.setIncome(getCellValue(row, 4));
            bill.setMoney(Float.parseFloat(getCellValue(row, 5)));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(getCellValue(row, 0), formatter);
            bill.setTradingHours(dateTime);
            BillList.add(bill);
        }
        workbook.close();
        return BillList;
    }

    private  static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";

        // 日期单元格：取原始值，避免中文展示格式
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            LocalDateTime dateTime = cell.getLocalDateTimeCellValue();
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return cell.toString().trim();
    }
}
