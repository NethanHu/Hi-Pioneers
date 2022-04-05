package com.jfinal.admin.common.kit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XLSFileKit {

    // 创建一个`excel`文件
    private HSSFWorkbook workBook;

    // `excel`文件保存路径
    private String filePath;

    public XLSFileKit(String filePath) {
        this.filePath = filePath;
        this.workBook = new HSSFWorkbook();
    }

    /**
     * 添加sheet
     *
     * @param content   数据
     * @param sheetName sheet名称
     * @param title     标题
     */
    public <T> void addSheet(List<List<T>> content, String sheetName, List<String> title) {
        HSSFSheet sheet = this.workBook.createSheet(sheetName);

        // `excel`中的一行
        HSSFRow row = null;

        // `excel`中的一个单元格
        HSSFCell cell = null;
        int i = 0, j = 0;

        // 创建第一行，添加`title`
        row = sheet.createRow(0);
        for (; j < title.size(); j++) {//添加标题
            cell = row.createCell(j);
            cell.setCellValue(title.get(j));
        }

        // 创建余下所有行
        i = 1;
        for (List<T> rowContent : content) {
            row = sheet.createRow(i);
            j = 0;
            for (Object cellContent : rowContent) {
                cell = row.createCell(j);
                cell.setCellValue(cellContent.toString());
                j++;
            }
            i++;
        }
    }

    /**
     * 保存
     *
     * @return
     */
    public boolean save() {
        try {
            FileOutputStream fos = new FileOutputStream(this.filePath);
            this.workBook.write(fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

