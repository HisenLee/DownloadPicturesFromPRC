package prc.image.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
	/**
	 * getTime
	 * 
	 * @return
	 */
	public static final String getNowTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
}
