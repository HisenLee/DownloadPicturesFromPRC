package prc.image.platform;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import prc.image.action.DownloadAction;
import prc.image.action.DownloadAction.IDownloadCallBack;
import prc.image.entity.ImageBean;
import prc.image.utils.Config;
import prc.image.utils.ExcelUtil;
import prc.image.utils.FileUtils;
import prc.image.utils.Log;
import prc.image.utils.RequestUtils;

public class Yitu extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	private int imgCount = 0;
	private int pageCount = 0;

	private IPlatformCallback iPlatformCallback;
	
	public Yitu(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Yitu");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Yitu");
	}
	
	@Override
	public int getMaxCount() {
		// 区间起始索引位置
		int alreadyDone = 0; 
		if(new File(downloadDataExcel).exists()) {
			alreadyDone = ExcelUtil.readXlsxExcel(downloadDataExcel).size();
			int allSize = ExcelUtil.readXlsxExcel(allDataExcel).size();
			imgCount = allSize-alreadyDone;
		} else {
			int pageNum = 1;
			try {
				String pageOne = "http://www.1tu.com/search/?k="+RequestUtils.urlEncode(keyword)+"&skey=&page="+pageNum;
				String pageOneHtml = RequestUtils.getHtml(pageOne, isNeedProxy);
				Document document = Jsoup.parse(pageOneHtml);
//				imgCount = Integer.parseInt(document.getElementsByClass("fb filter-summary").get(0).child(0).text().trim());
				
				Elements temps = document.getElementsByClass("pagenavi-mini clearfix");
				if(temps.size()>0) {
					Elements temps2 = temps.get(0).getElementsByClass("text");
					if(temps2.size()>1) {
						String pCountTemp = temps2.get(1).text();
						int index1 = pCountTemp.indexOf("共")+1;
						int index2 = pCountTemp.lastIndexOf("页");
						if(index1>-1 && index2>-1 && index1<index2) {
							String pCount = pCountTemp.substring(index1, index2);
							pageCount = Integer.parseInt(pCount); 
						}
					
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			generateImages();
		}// end if
		return imgCount;
	}
	
	@Override
	public void generateImages() {
		int pCount = pageCount<500 ? pageCount : 500;
		for(int pageIndex=1; pageIndex<=pCount; pageIndex++) {
			String pageUrl = "http://www.1tu.com/search/?k="+RequestUtils.urlEncode(keyword)+"&skey=&page="+pageIndex;
			Log.I("analyze imageurl from YiTu " + pageUrl);
			this.iPlatformCallback.actionProcess(Source.Yitu, "analyze imageurl from YiTu " + pageUrl, false, -1);
			String html;
			try {
				html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				Document nowPageDocument = Jsoup.parse(html);
				Elements imgElements = nowPageDocument.getElementsByClass("tab");
				for (Element imgElement : imgElements) {
					String imgUrl = imgElement.child(0).attr("data-url");
					mImgUrls.add(imgUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		imgCount = mImgUrls.size();
	}
	
	@Override
	public void generateExcel() {
		if(mImgUrls.size()<1) {
			this.iPlatformCallback.actionProcess(Source.Yitu, "Yitu: web page analysis failure...... ", false, -1);
			Log.I("Yitu: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Yitu);
		this.iPlatformCallback.actionProcess(Source.Yitu, "generate YituAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Yitu");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Yitu");
				iPlatformCallback.actionProcess(Source.Yitu, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Yitu, process, false, -1);
			}// end if finish
			
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
