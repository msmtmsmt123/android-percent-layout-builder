package com.xirtam.utils;

import com.xirtam.common.Config;

public class LogUtils {
	public static void print(String msg) {
		if (Config.debug)
			System.out.println(msg);
	}
}
