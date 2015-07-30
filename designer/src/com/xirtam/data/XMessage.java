package com.xirtam.data;

import com.xirtam.common.Config;
import com.xirtam.ui.Launcher;

/**
 * 使用EventQueue.invokeLater();传入CPMessage对象即可提示
 * 
 * @author xirtam
 * 
 */
public class XMessage implements Runnable {
	private String msg;
	private long timeStamp;
	private int showTime;

	public long getTimeStamp() {
		return timeStamp;
	}

	public XMessage(String msg) {
		this(msg, Config.DEFAULT_MESSAGE_SHOW_TIME);
	}

	public XMessage(String msg, int showTime) {
		this.msg = msg;
		this.showTime = showTime;
	}

	public int getShowTime() {
		return showTime;
	}

	public void run() {
		timeStamp = System.currentTimeMillis();
		Launcher.launcher.messages.add(this);
		Launcher.launcher.container.repaint();
	}

	@Override
	public String toString() {
		return msg;
	}
}