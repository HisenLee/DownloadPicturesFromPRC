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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Baidu extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	public static final int PAGESIZE  = 60;
	private int imgCount = 0;
	
	private IPlatformCallback iPlatformCallback;
	
	public Baidu(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Baidu");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Baidu");
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
		for(int pageIndex=0; ;pageIndex++) {
			int j = pageIndex*PAGESIZE;
			String pageUrl = "https://image.baidu.com/search/avatarjson?tn=baiduimage&fm=result&ie=utf-8&word="
						+RequestUtils.urlEncode(keyword)+"&rn="+PAGESIZE+"&pn="+j;
			this.iPlatformCallback.actionProcess(Source.Baidu, "analyze imageurl from baidu "+ pageUrl, false, -1);
			Log.I("analyze imageurl from baidu "+ pageUrl);
			try {
				String html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				if(html!=null) {
					JSONObject jsonObject = JSONObject.parseObject(html);
					if(jsonObject != null) {
						JSONArray jrrArray = jsonObject.getJSONArray("imgs");
						if(jrrArray.size() != 0) {
							for(int i=0; i<jrrArray.size(); i++) {
								JSONObject itemJsonObject = (JSONObject) jrrArray.get(i);
								mImgUrls.add(itemJsonObject.getString("thumbURL"));
							}
						} else {
							// 取完了所有数据
							imgCount = mImgUrls.size();
							Log.I("baidu imageCount---> "+ imgCount);
							return;
						}  // end if(jrrArray.size() != 0)
						
					}// end if(jsonObject != null)
				}// end if(html!=null) 
				
			} catch(JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	@Override
	public void generateExcel() {
		if(mImgUrls.size()<1) {
			this.iPlatformCallback.actionProcess(Source.Baidu, "baidu: web page analysis failure...... ", false, -1);
			Log.I("baidu: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Baidu);
		this.iPlatformCallback.actionProcess(Source.Baidu, "generate BaiduAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Baidu");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Baidu");
				iPlatformCallback.actionProcess(Source.Baidu, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Baidu, process, false, -1);
			}// end if finish
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
