package com.xirtam.ui.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.StringUtils;
import com.xirtam.utils.UIUtlis;

public class XButton extends XWidget {

	private static final long serialVersionUID = -2251329511898320730L;

	private transient Color defSelColor, defUnSelColor;
	private transient Font font;
	private transient Color fontColorObj;
	private int fontHeight;

	private boolean isPressing;

	private String text;

	public XButton() {
		super();
		setName(UIUtlis.genName());
		setType(NString.T_BUTTON);
		setBgSrc(NString.NULL);
		fontHeight = Config.DEFAULT_FONT_SIZE;
		text = NString.T_BUTTON;
		font = new Font("Dialog", 0, fontHeight);
		defSelColor = new Color(Config.DEFAULT_BUTTON_SEL_COLOR);
		defUnSelColor = new Color(Config.DEFAULT_BUTTON_UNSEL_COLOR);
		fontColorObj = new Color(fontColor);
		UI2Json();
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public String getText() {
		return text;
	}

	@Override
	public void Json2UI(JSONObject data) {
		// this.setBounds(
		// (int) Float.parseFloat(data.getString(NString.K_MARGIN_LEFT)),
		// (int) Float.parseFloat(data.getString(NString.K_MARGIN_TOP)),
		// (int) Float.parseFloat(data.getString(NString.K_W)),
		// (int) Float.parseFloat(data.getString(NString.K_H)));
		//
		// this.setName(data.getString(NString.K_NAME));
		// this.setAlign(data.getString(NString.K_ALIGN));
		this.setFontHeight(Integer.parseInt(data
				.getString(NString.K_FONT_HEIGHT)));
		this.setText(data.getString(NString.K_TEXT));
		// this.setMarginBottom(Integer.parseInt(data
		// .getString(NString.K_MARGIN_BOTTOM)));
		// this.setMarginRight(Integer.parseInt(data
		// .getString(NString.K_MARGIN_RIGHT)));
		// this.setMarginTop(Integer.parseInt(data.getString(NString.K_MARGIN_TOP)));
		// this.setMarginLeft(Integer.parseInt(data
		// .getString(NString.K_MARGIN_LEFT)));
		// onPropertiesChanged();
		super.Json2UI(data);
	}

	@Override
	public void onMouseDown(MouseEvent e) {
		super.onMouseDown(e);
		isPressing = true;
		repaint();
	}

	@Override
	public void onMouseUp(MouseEvent e) {
		super.onMouseUp(e);
		isPressing = false;
		repaint();
	}

	@Override
	public void onPropertiesChanged() {
		super.onPropertiesChanged();
		font = new Font(null, 0, fontHeight);
		fontColorObj = new Color(fontColor);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (isPressing) {
			g.setColor(defSelColor);
		} else {
			g.setColor(defUnSelColor);
		}
		g.drawRect(0, 0, getWidth(), getHeight());
		if (unSelImg != null && !isPressing) {
			g.drawImage(unSelImg, 0, 0, getWidth(), getHeight(), null);
		} else if (selImg != null && isPressing) {
			g.drawImage(selImg, 0, 0, getWidth(), getHeight(), null);
		}

		drawRange(g);

		g.setColor(fontColorObj);
		g.setFont(font);
		g.drawString(text,
				(getWidth() - (text.length() / 2 * fontHeight)) >> 1,
				(getHeight() + fontHeight) >> 1);
		paintChildren(g);
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void UI2Json() {

		jo.put(NString.K_FONT_HEIGHT, String.valueOf(fontHeight));
		jo.put(NString.K_TEXT, text);
		jo.put(NString.K_SEL_IMG, selImgSrc);
		jo.put(NString.K_UNSEL_IMG, unSelImgSrc);
		jo.put(NString.K_FONT_COLOR, StringUtils.getHexColor(getFontColor()));
		super.UI2Json();
	}
}
