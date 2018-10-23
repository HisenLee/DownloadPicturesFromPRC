package prc.image.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public final class FileUtils {
	
	/**
	 * count Jpg Number
	 * @param path
	 * @return
	 */
	public static int getImageNum(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		int alreadDone = 0;
		for (File item : files) {
			if(item.getName().endsWith(".jpg")) {
				alreadDone++;
			}
		}
		return alreadDone;
	}
	/**
	 * createFolder
	 * @param destDirName
	 * @return
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) { 
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {
			System.out.println("create dir successfully£¡" + destDirName);
			return true;
		} else {
			System.out.println("create dir failure£¡");
			return false;
		}
	}
	
	/**
	 * delete File
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * checkDir
	 * @param dir
	 * @return
	 */
	public static boolean checkDir(String dir) {
		if(dir==null || "null".equals(dir) || "".equals(dir)) {
			DialogUtils.showErrorMsg("Please select the correct directory to save images");
			return false;
		}
		
		File file = new File(dir);
		if(!file.isDirectory()) {
			DialogUtils.showErrorMsg("Please select the correct directory to save images");
			return false;
		}
        return true;
	}
	
	/**
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static long copyFile(File f1, File f2) throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		byte[] buffer = new byte[length];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				return new Date().getTime() - time;
			} else
				out.write(buffer, 0, ins);
		}
	}
}
