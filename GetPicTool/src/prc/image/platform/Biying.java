package prc.image.platform;


import java.io.File;
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

public class Biying extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	public static final int PAGESIZE = 50;
	private int imgCount = 0;
	
	private IPlatformCallback iPlatformCallback;
	
	public Biying(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Biying");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Biying");
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
			generateImages();
		}
		return imgCount;
	}
	
	@Override
	public void generateImages() {
		String lastHtml = "";
		for(int pageIndex=0; ; pageIndex++) {
			String pageUrl = "https://cn.bing.com/images/async?q="
					+RequestUtils.urlEncode(keyword)+"&first="
					+(pageIndex+PAGESIZE*pageIndex)+"&count="+PAGESIZE+"&mmasync=1";
			this.iPlatformCallback.actionProcess(Source.Biying, "analyze imageurl from biying "+ pageUrl, false, -1);
			Log.I("analyze imageurl from biying "+pageUrl);
			try {
				String html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				if(html.equals(lastHtml)) {
					// 取完了所有数据
					imgCount = mImgUrls.size();
					Log.I("biying imageCount---> "+ imgCount);
					return;
				} 
				
				Document document = Jsoup.parse(html);
				Elements divImgs = document.getElementsByClass("img_cont hoff");
				for (Element imgElement : divImgs) {
					String picUrl = imgElement.childNode(0).attr("src");
					mImgUrls.add(picUrl);
				}
				lastHtml = html;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void generateExcel() {
		if(mImgUrls.size()<1) {
			this.iPlatformCallback.actionProcess(Source.Biying, "Biying: web page analysis failure...... ", false, -1);
			Log.I("Biying: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Biying);
		this.iPlatformCallback.actionProcess(Source.Biying, "generate BiyingAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Biying");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Biying");
				iPlatformCallback.actionProcess(Source.Biying, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Biying, process, false, -1);
			}// end if finish
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
