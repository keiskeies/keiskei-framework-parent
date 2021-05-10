package top.keiskeiframework.common.util;

import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * <p>
 * Excel操作
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/12/14 14:59
 */
public class ExcelUtils {

    private final static String FILE_SUFFIX = ".xls";

    /**
     * 导出
     *
     * @param response 响应
     * @param dataList 数据
     */
    public static void createExcel(HttpServletResponse response, List<List<String>> dataList) {
        createExcel(response, dataList, null);
    }


    /**
     * 导出
     *
     * @param response 响应
     * @param dataList 数据
     * @param fileName 文件名
     */
    public static void createExcel(HttpServletResponse response, List<List<String>> dataList, String fileName) {

        response.setContentType("application/glob");
        response.setCharacterEncoding("UTF-8");

        // 设置工作表的标题 ,设置生成的文件名字
        if (!StringUtils.isEmpty(fileName)) {
            fileName = fileName.replace(FILE_SUFFIX, "") + "-" + DateTimeUtils.timeToString(LocalDateTime.now()) + FILE_SUFFIX;
        } else {
            fileName = DateTimeUtils.timeToString(LocalDateTime.now()) + FILE_SUFFIX;
        }
        response.setHeader("File-Name", fileName);
        response.setHeader("Access-Control-Allow-Origin", "*");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet();
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制

            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBottomBorderColor((short) 4);
            //声明列对象
            HSSFCell cell;
            for (int i = 0; i < dataList.size(); i++) {
                HSSFRow row = sheet.createRow(i);
                List<String> rowData = dataList.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                    if (i == 0) {
                        cell.setCellStyle(style);
                    }
                }
            }
            // 宽度自适应
            for (int i = 0; i < dataList.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Map<String, Object>> readExcel(String filePath, int sheetNum, int headerRowNum) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(filePath + " 文件不存在");
        }
        InputStream is;
        try {
            is = new FileInputStream(file);
            Workbook wb = getWorkbook(is);
            return readExcel(wb, sheetNum, headerRowNum);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BizException(BizExceptionEnum.ERROR.getCode(), "文件读取失败");
        }
    }

    public static List<Map<String, Object>> readExcel(InputStream is, int sheetNum, int headerRowNum) {
        Workbook wb = getWorkbook(is);
        return readExcel(wb, sheetNum, headerRowNum);
    }

    private static List<Map<String, Object>> readExcel(Workbook wb, int sheetNum, int headerRowNum) {

        List<Map<String, Object>> list = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(sheetNum);
        int rowNum = sheet.getPhysicalNumberOfRows();
        // 头部栏
        Row rowHeader = sheet.getRow(headerRowNum);
        int colNum = rowHeader.getPhysicalNumberOfCells();

        // 头部名称
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < colNum; i++) {
            headers.add(String.valueOf(rowHeader.getCell(i)));
        }
        wb.setForceFormulaRecalculation(true);

        for (int i = headerRowNum + 1; i < rowNum; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < colNum; j++) {

                    Cell cell = row.getCell(j);
                    Object cellValue = null;
                    if (cell != null) {
                        // 判断cell类型
                        switch (cell.getCellType()) {
                            case BOOLEAN:
                                cellValue = cell.getBooleanCellValue();
                                break;
                            case NUMERIC: {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = data2Local(cell.getDateCellValue());
                                } else {
                                    cellValue = String.valueOf(cell.getNumericCellValue());
                                }
                                break;
                            }
                            case FORMULA: {
                                try {
                                    cellValue = cell.getNumericCellValue();
                                } catch (IllegalStateException e) {
                                    try {
                                        cellValue = String.valueOf(cell.getRichStringCellValue());
                                    } catch (Exception ignore) {
                                    }
                                }
                                break;
                            }
                            default:
                                cellValue = String.valueOf(cell.getRichStringCellValue());
                                break;
                        }
                    }

                    map.put(headers.get(j), cellValue);
                }
                list.add(map);
            }
        }
        return list;

    }

    private static Workbook getWorkbook(InputStream is) {
        Workbook wb;
        try {
            wb = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException(BizExceptionEnum.ERROR.getCode(), "文件读取失败");
        }

        return wb;
    }

    private static LocalDateTime data2Local(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDateTime();
    }

}
