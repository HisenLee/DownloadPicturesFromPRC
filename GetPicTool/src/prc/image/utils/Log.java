package prc.image.utils;



public final class Log {

	public static final String I(String message) {
		System.out.println("****** "+DateUtils.getNowTime()+"--->" + message);
		return "****** "+DateUtils.getNowTime()+"--->" + message;
	}
}
