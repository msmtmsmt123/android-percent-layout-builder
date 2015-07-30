package com.xirtam.ui.widget;

import java.awt.Color;
import java.awt.Graphics;

import org.json.JSONObject;

import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.UIUtlis;

public class XImageView extends XWidget {

	private static final long serialVersionUID = 4884450876586694182L;

	public XImageView() {
		super();
		setName(UIUtlis.genName());
		setType(NString.T_IMAGEVIEW);
		setBgSrc(NString.NULL);
		UI2Json();
	}

	@Override
	public void Json2UI(JSONObject data) {
		// this.setBounds(
		// (int) Float.parseFloat(data.getString(NString.K_MARGIN_LEFT)),
		// (int) Float.parseFloat(data.getString(NString.K_MARGIN_TOP)),
		// (int) Float.parseFloat(data.getString(NString.K_W)),
		// (int) Float.parseFloat(data.getString(NString.K_H)));
		// this.setName(data.getString(NString.K_NAME));
		// this.setAlign(data.getString(NString.K_ALIGN));
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
		super.UI2Json();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(0x999999));
		g.drawRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
		if (image != null)
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		drawRange(g);
		paintChildren(g);
	}

}
