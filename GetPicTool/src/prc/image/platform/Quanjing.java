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

public class Quanjing extends Platform {
	private String keyword;
	private boolean isNeedProxy;
	
	public static final int PAGESIZE  = 100;
	private int imgCount = 0;
	private int pageCount = 0;
	
	private IPlatformCallback iPlatformCallback;
	
	public Quanjing(String keyword, boolean isNeedProxy, int needNum, IPlatformCallback iPlatformCallback) {
		FileUtils.createDir(Config.BASIC_PATH+"\\"+keyword + "\\Quanjing");
		this.keyword = keyword;
		this.isNeedProxy = isNeedProxy;
		this.needNum = needNum;
		this.iPlatformCallback = iPlatformCallback;
		
		init(keyword, "Quanjing");
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
			String pageOne = "http://search.quanjing.com/search?key="+RequestUtils.urlEncode(keyword)+"&pageSize="+PAGESIZE+"&pageNum="+pageNum+"&imageType=2&sortType=1&callback=searchresult";
			String pageOneHtml;
			try {
				pageOneHtml = RequestUtils.getHtml(pageOne, isNeedProxy);
				String pageOneJson = pageOneHtml.substring("searchresult(".length(), pageOneHtml.length()-1);
				JSONObject pageOneData = JSONObject.parseObject(pageOneJson);
				if(pageOneData != null) {
					pageCount = pageOneData.getIntValue("pagecount");
//					imgCount = pageOneData.getIntValue("recordcount");// 实际的imgcount
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(JSONException e) {
				e.printStackTrace();
			} // end try catch
			generateImages();
		}// end if
		return imgCount;
	}
	
	@Override
	public void generateImages() {
		int pCount = pageCount<500 ? pageCount : 500;
		for(int pageIndex=1; pageIndex<=pCount; pageIndex++) {
			String pageUrl = "http://search.quanjing.com/search?key="
					+RequestUtils.urlEncode(keyword)+"&pageSize="+PAGESIZE
					+"&pageNum="+pageIndex+"&imageType=2&sortType=1&callback=searchresult";
			iPlatformCallback.actionProcess(Source.Quanjing, "analyze imageurl from quanjing "+ pageUrl, false, -1);
			Log.I("analyze imageurl from quanjing "+ pageUrl);
			String html;
			try {
				html = RequestUtils.getHtml(pageUrl, isNeedProxy);
				if(!StringUtils.isEmptyStr(html)) {
					int start = "searchresult(".length();
					int end = html.length() -1;
					if(start>0 && end>0 && end>start) {
						String json = html.substring(start, end);
						JSONObject jsonObject = JSONObject.parseObject(json);
						if(jsonObject != null) {
							JSONArray jrrArray = jsonObject.getJSONArray("imglist");
							if(jrrArray!=null) {
								for(int i=0; i<jrrArray.size(); i++) {
									JSONObject itemJsonObject = (JSONObject) jrrArray.get(i);
									if(itemJsonObject != null) {
										mImgUrls.add(itemJsonObject.getString("imgurl"));
									}
								}
							}
							
						}
					}
					
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
			this.iPlatformCallback.actionProcess(Source.Quanjing, "Quanjing: web page analysis failure...... ", false, -1);
			Log.I("Quanjing: web page analysis failure......");
			return;
		}
		generateAllExcel(Source.Quanjing);
		this.iPlatformCallback.actionProcess(Source.Quanjing, "generate QuanjingAll excel...", false, -1);
	}

	@Override
	public void download() {
		List<ImageBean> imageDatas = getDownloadData(keyword, "Quanjing");
		// 下载本次所需的数据
		DownloadAction downloadAction = new DownloadAction(imageDatas, isNeedProxy, downloadCallBack);
		downloadAction.download();
	}
	
	private IDownloadCallBack downloadCallBack = new IDownloadCallBack() {
		
		@Override
		public void downloadActionProcess(String process, boolean isFinish,
				boolean needProxy) {
			if(isFinish) {
				int missNum = checkDownloadFinish("Quanjing");
				Config.finishImgDatas.clear();// 清空之前存储的数据
				iPlatformCallback.actionProcess(Source.Quanjing, process, true, missNum);
			} else {
				iPlatformCallback.actionProcess(Source.Quanjing, process, false, -1);
			}// end if finish
			
			
		}// end downloadActionProcess
	};// end IDownloadCallBack
	
}
