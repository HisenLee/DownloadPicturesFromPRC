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

public class Qihoo extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	public static final int PAGESIZE = 50;
	private int imgCount = 0;
	
	private IPlatformCallback iPlatformCallback;
	
	public Qihoo(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Qihoo");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Qihoo");
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
			String pageUrl = "http://image.so.com/j?q="+RequestUtils.urlEncode(keyword)
					+"&src=srp&correct="+RequestUtils.urlEncode(keyword)
					+"&sn="+(1+PAGESIZE*pageIndex)+"&pn="+PAGESIZE;
			this.iPlatformCallback.actionProcess(Source.Qihoo, "analyze imageurl from qihoo "+ pageUrl, false, -1);
			Log.I("analyze imageurl from qihoo "+ pageUrl);
			try {
				String html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				if(!StringUtils.isEmptyStr(html)) {
					JSONObject jsonObject = JSONObject.parseObject(html);
					if(jsonObject != null) {
						JSONArray jrrArray = jsonObject.getJSONArray("list");
						if(jrrArray.size() != 0) {
							for(int i=0; i<jrrArray.size(); i++) {
								JSONObject itemJsonObject = (JSONObject) jrrArray.get(i);
								if(itemJsonObject!=null) {
									mImgUrls.add(itemJsonObject.getString("_thumb"));
								}
							}
						} else {
							// 取完了所有数据
							imgCount = mImgUrls.size();
							Log.I("qihoo imageCount---> "+ imgCount);
							return;
						}
						
					}
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void generateExcel() {
		if(mImgUrls.size()<1) {
			this.iPlatformCallback.actionProcess(Source.Qihoo, "Qihoo: web page analysis failure...... ", false, -1);
			Log.I("Qihoo: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Qihoo);
		this.iPlatformCallback.actionProcess(Source.Qihoo, "generate QihooAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Qihoo");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Qihoo");
				iPlatformCallback.actionProcess(Source.Qihoo, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Qihoo, process, false, -1);
			}// end if finish
			
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
