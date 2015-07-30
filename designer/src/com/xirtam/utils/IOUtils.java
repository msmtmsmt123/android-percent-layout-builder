package com.xirtam.utils;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xirtam.common.NString;

public class IOUtils {

	/**
	 * 将UI保存为json文件
	 * 
	 * @param str
	 * @param selectedFile
	 * @return
	 */
	public static boolean save(String str, File selectedFile) {
		try {
			String path = selectedFile.getAbsolutePath();
			if (!path.endsWith(NString.DATA_END)) {
				selectedFile = new File(path + NString.DATA_END);
			}
			FileOutputStream fos = new FileOutputStream(selectedFile);
			fos.write(str.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 读取Json文件返回JsonString
	 * 
	 * @param selectedFile
	 * @return
	 */
	public static String load(File selectedFile) {
		byte[] b = null;
		try {
			FileInputStream fis = new FileInputStream(selectedFile);
			b = new byte[fis.available()];
			fis.read(b);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return b == null ? NString.SPACE : new String(b);
	}

	public static Image loadImg(String path) {
		Image image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
