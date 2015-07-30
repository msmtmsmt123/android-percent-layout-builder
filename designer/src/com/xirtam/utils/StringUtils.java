package com.xirtam.utils;

import java.awt.Font;
import java.util.Vector;

import com.xirtam.common.TextAligns;

public class StringUtils {
	/**
	 * 判断是否十六进制颜色
	 * 
	 * @param color
	 * @return
	 */
	public static boolean checkHexColor(String color) {
		return color.matches("[0-9a-fA-F]{6}$");
	}

	/**
	 * 检查是否是TextAlign
	 * 
	 * @param text
	 * @return
	 */
	public static boolean checkTextAlign(String text) {
		boolean result = false;
		for (TextAligns align : TextAligns.values()) {
			if (align.toString().equals(text)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 得到补全00的十六进制字符串
	 * 
	 * @param fontColor
	 * @return
	 */
	public static String getHexColor(int fontColor) {
		return String.format("%06x", fontColor);
	}

	/**
	 * 字符串切割,实现字符串自动换行
	 * 
	 * @param text
	 * @param maxWidth
	 * @param ft
	 * @return
	 */
	public static String[] format(String text, int maxWidth, Font ft) {
		String[] result = null;
		maxWidth = maxWidth - ft.getSize();// 矫正最后一个字符
		Vector<String> tempR = new Vector<String>();
		int lines = 0;
		int len = text.length();
		int index0 = 0;
		int index1 = 0;
		boolean wrap;
		while (true) {
			int widthes = 0;
			wrap = false;
			for (index0 = index1; index1 < len; index1++) {
				if (text.charAt(index1) == '\n') {
					index1++;
					wrap = true;
					break;
				}
				widthes = ft.getSize() / 2 + widthes;

				if (widthes > maxWidth) {
					break;
				}
			}
			lines++;

			if (wrap) {
				tempR.addElement(text.substring(index0, index1 - 1));
			} else {
				tempR.addElement(text.substring(index0, index1));
			}
			if (index1 >= len) {
				break;
			}
		}
		result = new String[lines];
		tempR.copyInto(result);
		return result;
	}
}
