package com.xirtam.ui.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.common.TextAligns;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.StringUtils;
import com.xirtam.utils.UIUtlis;

public class XLabel extends XWidget {

	private static final long serialVersionUID = 618265369418757134L;
	private String text;
	private transient Font font;
	private transient Color fontColorObj;
	private int fontHeight;

	private TextAligns textAlign;

	private boolean isMultLine;

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public XLabel() {
		super();
		setName(UIUtlis.genName());
		setType(NString.T_LABEL);
		setBgSrc(NString.NULL);
		text = NString.T_LABEL;
		fontHeight = Config.DEFAULT_FONT_SIZE;
		font = new Font("Dialog", 0, fontHeight);
		fontColorObj = new Color(fontColor);
		textAlign = TextAligns.center;
		color = Config.DEFAULT_LABEL_COLOR;
		UI2Json();
	}

	@Override
	public void Json2UI(JSONObject data) {
		this.setBgColorStr(data.getString(NString.K_BG_COLOR));
		this.setText(data.getString(NString.K_TEXT));
		this.setFontHeight(Integer.parseInt(data
				.getString(NString.K_FONT_HEIGHT)));
		String textAlignStr = data.getString(NString.K_TEXT_ALIGN);
		if (StringUtils.checkTextAlign(textAlignStr))
			this.textAlign = TextAligns.valueOf(textAlignStr);
		this.isMultLine = Boolean.parseBoolean(data
				.getString(NString.K_IS_MULTIINE));
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
	protected void UI2Json() {
		jo.put(NString.K_IS_MULTIINE, String.valueOf(isMultLine));
		jo.put(NString.K_TEXT_ALIGN, textAlign.toString());
		jo.put(NString.K_FONT_HEIGHT, String.valueOf(fontHeight));
		jo.put(NString.K_FONT_COLOR, StringUtils.getHexColor(getFontColor()));
		jo.put(NString.K_TEXT, getText());
		jo.put(NString.K_BG_COLOR, StringUtils.getHexColor(getBgColor()));
		super.UI2Json();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(bgColorObj);
		g.drawRect(0, 0, getWidth(), getHeight());
		if (bgImg != null)
			g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);

		g.setColor(fontColorObj);
		g.setFont(font);
		int x = 0, y = 0;
		switch (textAlign) {
		case right:
			x = getWidth() - (text.length() * fontHeight >> 1);
			y = (getHeight() + fontHeight) >> 1;
			break;
		case left:
			x = 0;
			y = (getHeight() + fontHeight) >> 1;
			break;
		case center:
			x = (getWidth() - (text.length() / 2 * fontHeight)) >> 1;
			y = (getHeight() + fontHeight) >> 1;
			break;
		default:
			break;
		}
		if (isMultLine) {
			String[] info_wrap = StringUtils.format(text, getWidth(), font);
			for (int i = 0; i < info_wrap.length; i++) {
				g.drawString(info_wrap[i], 5, (i + 1) * font.getSize());
			}
		} else {
			g.drawString(text, x, y);
		}

		drawRange(g);
		paintChildren(g);
	}

	@Override
	public void onPropertiesChanged() {
		super.onPropertiesChanged();
		font = new Font(null, 0, fontHeight);
		fontColorObj = new Color(fontColor);
	}
}
