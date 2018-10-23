package prc.image.action;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import prc.image.entity.ImageBean;

public final class DownloadAction {

	private boolean needProxy;
	private List<ImageBean> imageDatas;
	private IDownloadCallBack downloadCallBack;
	
	public interface IDownloadCallBack {
		public void downloadActionProcess(String process, boolean isFinish, boolean needProxy);
	}
	
	public DownloadAction(List<ImageBean> imageDatas, boolean needProxy, IDownloadCallBack downloadCallBack) {
		this.imageDatas = imageDatas;
		this.needProxy = needProxy;
		this.downloadCallBack = downloadCallBack;
	}
	
	public void download() {
		// download
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (ImageBean imageBean : imageDatas) {
			SingleTask singleTask = new SingleTask(imageBean.getUrl(), 
					imageBean.getName(),
					needProxy);
			executorService.submit(singleTask);
		}
		executorService.shutdown();
		while (true) {
			downloadCallBack.downloadActionProcess("download images...", false, needProxy);
			if (executorService.isTerminated()) {
				downloadCallBack.downloadActionProcess("download images finished...", true, needProxy);
				break;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end while
		
	}
	
}
