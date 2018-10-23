package prc.image.utils;


import java.util.ArrayList;
import java.util.List;

import prc.image.entity.ImageBean;

public final class Config {
	public static String BASIC_PATH = ""; // Local Path
	
	public static final int DEF_NUM_BAIDU = 1000;
	public static final int DEF_NUM_QIHOO = 1000;
	public static final int DEF_NUM_SOGOU = 1500;
	public static final int DEF_NUM_BIYING = 800;
	public static final int DEF_NUM_QUANJING = 10000;
	public static final int DEF_NUM_YITU = 10000;
	
	public static List<ImageBean> finishImgDatas = new ArrayList<>();
}
