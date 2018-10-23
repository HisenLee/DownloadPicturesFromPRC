package prc.image.utils;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import prc.image.entity.LinkEntity;



public final class ExcelUtil {
	
	/**
	 * @param TAG
	 * @param fullPath
	 * @param sheetName
	 * @param apks
	 */
	public static void generateDownloadImgExcel(String fullPath,
			String sheetName, List<List<String>> list) {
		Log.I(".....generate excel "+sheetName+" ......");
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建表
		XSSFSheet sheet = workbook.createSheet(sheetName);
		// 创建表头
		String[] header = new String[] { "Rank", "Name", "Url"};
		XSSFRow row = workbook.getSheet(sheetName).createRow(0);
		for (int i = 0; i < header.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
		// 把集合转化为Map集合
		List<Map<String, String>> listRows = new ArrayList<Map<String, String>>();
		int rank = 1;
		for (List<String> itemList : list) {
			Map<String, String> itemData = new HashMap<String, String>();
			itemData.put(header[0], rank + "");
			itemData.put(header[1], itemList.get(0));
			itemData.put(header[2], itemList.get(1));
			rank++;
			listRows.add(itemData);
		}
		// 获取表头的列数
		int columnCount = sheet.getRow(0).getLastCellNum();
		// 填充具体数据
		for (int rowId = 0; rowId < listRows.size(); rowId++) { // 外层控制行数
			Map<String, String> itemData = listRows.get(rowId);
			XSSFRow newRow = sheet.createRow(rowId + 1);
			for (int i = 0; i < columnCount; i++) {// 内层控制列数
				XSSFCell cell = newRow.createCell(i);
				String key = header[i];
				cell.setCellValue(itemData.get(key));
			}
		}
		// 用流把表格写入文件
		try {
			FileOutputStream out = new FileOutputStream(fullPath);
			workbook.write(out);
			workbook.close(); // 切记关闭工作空间
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @param TAG
	 * @param fullPath
	 * @param sheetName
	 * @param apks
	 */
	public static void generateStoreExcel(String fullPath,
			String sheetName, List<LinkEntity> links) {
		Log.I(".....generate excel "+sheetName+" ......");
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建表
		XSSFSheet sheet = workbook.createSheet(sheetName);
		// 创建表头
		String[] header = null;
		header = new String[] { "Rank", "Url", "Platform"};
		
		XSSFRow row = workbook.getSheet(sheetName).createRow(0);
		for (int i = 0; i < header.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
		// 把集合转化为Map集合
		List<Map<String, String>> listRows = new ArrayList<Map<String, String>>();
		int rank = 1;
		for (LinkEntity linkEntity : links) {
			Map<String, String> itemData = new HashMap<String, String>();
			itemData.put(header[0], rank + "");
			itemData.put(header[1], linkEntity.getUrl());
			itemData.put(header[2], linkEntity.getPlatform().toString());
			rank++;
			listRows.add(itemData);
		}
		// 获取表头的列数
		int columnCount = sheet.getRow(0).getLastCellNum();
		// 填充具体数据
		for (int rowId = 0; rowId < listRows.size(); rowId++) { // 外层控制行数
			Map<String, String> itemData = listRows.get(rowId);
			XSSFRow newRow = sheet.createRow(rowId + 1);
			for (int i = 0; i < columnCount; i++) {// 内层控制列数
				XSSFCell cell = newRow.createCell(i);
				String key = header[i];
				cell.setCellValue(itemData.get(key));
			}
		}
		// 用流把表格写入文件
		try {
			FileOutputStream out = new FileOutputStream(fullPath);
			workbook.write(out);
			workbook.close(); // 切记关闭工作空间
			
			Log.I("......"+sheetName+" generate successfully......");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * readExcel
	 * 
	 * @param path
	 * @return
	 */
	public static List<List<String>> readXlsxExcel(String path) {
		List<List<String>> result = new ArrayList<List<String>>();
		XSSFWorkbook XSSFWorkbook;
		try {
			InputStream inStream = new FileInputStream(path);
			XSSFWorkbook = new XSSFWorkbook(inStream);
			// 遍历工作区间的每一个sheet
			for (int nowSheet = 0; nowSheet < XSSFWorkbook.getNumberOfSheets(); nowSheet++) {
				XSSFSheet sheet = XSSFWorkbook.getSheetAt(nowSheet);
				if (sheet == null) {
					continue;
				}
				// 遍历当前sheet的每一行
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					XSSFRow itemRow = sheet.getRow(rowNum);

					int minColIx = itemRow.getFirstCellNum();
					int maxColIx = itemRow.getLastCellNum();
					List<String> rowList = new ArrayList<String>();
					// 遍历当前行，获取每个Cell
					for (int colIx = minColIx; colIx < maxColIx; colIx++) {
						XSSFCell cell = itemRow.getCell(colIx);
						if (cell == null) {
							continue;
						}
						rowList.add(cell.toString());
					}
					result.add(rowList);
				}
			}
			XSSFWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
