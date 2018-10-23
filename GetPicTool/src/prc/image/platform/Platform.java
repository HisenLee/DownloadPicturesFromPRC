package prc.image.platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import prc.image.entity.ImageBean;
import prc.image.entity.LinkEntity;
import prc.image.utils.Config;
import prc.image.utils.ExcelUtil;
import prc.image.utils.FileUtils;
import prc.image.utils.StringUtils;




public abstract class Platform {
	protected int needNum;
	protected String platformPath;
	protected String allDataExcel;
	protected String downloadDataExcel;
	protected List<String> mImgUrls;
	
	public enum Source {
		Baidu, Qihoo, Sogou, Biying, Quanjing, TuKuChina, Yitu
	}
	
	protected void init(String keyword, String platform) {
		mImgUrls = new ArrayList<String>();
		platformPath = Config.BASIC_PATH + "\\" + keyword + "\\" + platform;
		allDataExcel = Config.BASIC_PATH + "\\" + keyword + "\\" + platform + "\\" + platform + "All.xlsx";
		downloadDataExcel = Config.BASIC_PATH + "\\" + keyword + "\\" + platform + "\\" + platform + ".xlsx";
	}
	
	public abstract int getMaxCount();
	
	public abstract void generateImages();
	
	public abstract void generateExcel();
	
	public abstract void download();
	
	protected List<ImageBean> getDownloadData(String keyword, String platform) {
		// 可供下载的全部数据
		List<List<String>> allDatas =  ExcelUtil.readXlsxExcel(allDataExcel);
		int allRows = allDatas.size();
		// 从全部数据中取出本次下载所需的区间 
		// 区间起始索引位置
		int beginIndex = 0; 
		if(new File(downloadDataExcel).exists()) {
			beginIndex = ExcelUtil.readXlsxExcel(downloadDataExcel).size();
			
			List<String> downloadUrls = new ArrayList<>();
			for (List<String> list : ExcelUtil.readXlsxExcel(downloadDataExcel)) {
				downloadUrls.add(list.get(2));
			}
			
			while(true) {
				String urlFromAllExcel = allDatas.get(beginIndex).get(1); 
				if(!downloadUrls.contains(urlFromAllExcel)) {
					break;
				} else {
					beginIndex+=1;
				}
			}
			
		}
		// 区间结束索引位置
		int endIndex = beginIndex+needNum;
		endIndex = endIndex<allRows ? endIndex : allRows;
		// 取出区间内的数据(即本次下载所需的数据)
		List<ImageBean> imageDatas = new ArrayList<>();
		for(int i=beginIndex; i<endIndex; i++) {
			List<String> rowData = allDatas.get(i);
			String url = rowData.get(1);
			String name = StringUtils.getName(Integer.parseInt(rowData.get(0).toString()), 
					keyword, platform); // rank组成的name
			ImageBean imageBean = new ImageBean(name, url);
			imageDatas.add(imageBean);
		}
		return imageDatas;
	}
	
	public void generateAllExcel(Source platform) {
		String excelPath="", sheetName="";
		List<LinkEntity> links = new ArrayList<>();
		excelPath = allDataExcel;
		sheetName = platform.toString()+"All";
		for (String item : mImgUrls) {
			LinkEntity linkEntity = new LinkEntity();
			linkEntity.setUrl(item);
			linkEntity.setPlatform(platform);
			links.add(linkEntity);
		}
		ExcelUtil.generateStoreExcel(excelPath, sheetName, links);
	}
	
	protected int checkDownloadFinish(String platform) {
		// 检查是否下载完了所有数据
		int imgs = FileUtils.getImageNum(platformPath);
		int missNum = 0;
		if(new File(downloadDataExcel).exists()) {
			int theLast = ExcelUtil.readXlsxExcel(downloadDataExcel).size();
			missNum = needNum + theLast - imgs ;
		} else {
			missNum = needNum - imgs;
		}
		// 用来存储之前下载的和本次下载的数据总和
		List<ImageBean> imageBeans = new ArrayList<>();
		if(new File(downloadDataExcel).exists()) {
			List<List<String>> allDatas =  ExcelUtil.readXlsxExcel
					(downloadDataExcel);
			for (List<String> list : allDatas) {
				String url = list.get(2);
				String name = list.get(1);
				ImageBean imageBean = new ImageBean(name, url);
				imageBeans.add(imageBean); // 加上之前下载的数据
			}
			
		}
		imageBeans.addAll(Config.finishImgDatas);// 再加上本次下载的数据
		imageBeans.sort(null); // 对新数据排序
		List<List<String>> list = new ArrayList<List<String>>();
		for (ImageBean imageBean : imageBeans) {
			List<String> itemList = new ArrayList<>();
			itemList.add(imageBean.getName());
			itemList.add(imageBean.getUrl());
			list.add(itemList);
		}
		// 生成新数据的表格
		FileUtils.deleteFile(downloadDataExcel);
		ExcelUtil.generateDownloadImgExcel(downloadDataExcel, platform, list);
		Config.finishImgDatas.clear();// 清空之前存储的数据
		
		return missNum;
	}
	
}
