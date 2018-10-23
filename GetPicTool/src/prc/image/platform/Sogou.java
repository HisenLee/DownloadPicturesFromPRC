package prc.image.platform;


import java.io.File;
import java.io.IOException;
import java.util.List;

import prc.image.action.DownloadAction;
import prc.image.action.DownloadAction.IDownloadCallBack;
import prc.image.entity.ImageBean;
import prc.image.utils.Config;
import prc.image.utils.ExcelUtil;
import prc.image.utils.FileUtils;
import prc.image.utils.Log;
import prc.image.utils.RequestUtils;
import prc.image.utils.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Sogou extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	private int imgCount = 0;
	
	private IPlatformCallback iPlatformCallback;
	
	public Sogou(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Sogou");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Sogou");
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
		for(int pageIndex=0; ; pageIndex++) {
			String pageUrl = "http://pic.sogou.com/pics?"
					+ "query="+RequestUtils.urlEncode(keyword)+"&mode=1"
					+ "&start="+(48*pageIndex)+"&reqType=ajax&reqFrom=result&tn=0";
			Log.I("analyze imageurl from sogou "+pageUrl);
			this.iPlatformCallback.actionProcess(Source.Sogou, "analyze imageurl from sogou "+ pageUrl, false, -1);
			try {
				String html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				if(!StringUtils.isEmptyStr(html)) {
					JSONObject jsonObject = JSONObject.parseObject(html);
					if(jsonObject != null) {
						JSONArray jrrArray = jsonObject.getJSONArray("items");
						if(jrrArray.size() != 0) {
							for(int i=0; i<jrrArray.size(); i++) {
								JSONObject itemJsonObject = (JSONObject) jrrArray.get(i);
								mImgUrls.add(itemJsonObject.getString("thumbUrl"));
							}
						} else {
							// 取完了所有数据
							imgCount = mImgUrls.size();
							Log.I("sogou imageCount---> "+ imgCount);
							return;
						}
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void generateExcel() {
		if(mImgUrls.size()<1) {
			this.iPlatformCallback.actionProcess(Source.Sogou, "Sogou: web page analysis failure...... ", false, -1);
			Log.I("Sogou: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Sogou);
		this.iPlatformCallback.actionProcess(Source.Sogou, "generate SogouAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Sogou");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Sogou");
				iPlatformCallback.actionProcess(Source.Sogou, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Sogou, process, false, -1);
			}// end if finish
			
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
