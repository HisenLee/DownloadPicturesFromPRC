package prc.image.utils;


public final class StringUtils {
	/**
	 * checkEmptyStr
	 * @param str
	 * @return
	 */
	public static boolean isEmptyStr(String str) {
		if("".equals(str) || str==null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * downloadImageName
	 * @param i
	 * @param keyword
	 * @return
	 */
	public static String getName(int rank, String keyword, String platform) {
		String name="";
		if(rank<=9) {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" + "00000" + (rank) + ".jpg";
		} else if(rank<=99) {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" +  "0000" +(rank) + ".jpg";
		} else if(rank<=999) {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" +  "000" +(rank) + ".jpg";
		} else if(rank<=9999) {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" + "00" +(rank) + ".jpg";
		} else if(rank<=99999) {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" + "0" +(rank) + ".jpg";
		} else {
			name=Config.BASIC_PATH +  "\\"+ keyword + "\\"+ platform + "\\" + keyword + "_" + (rank) + ".jpg";
		}
		return name;
	}
	
}
