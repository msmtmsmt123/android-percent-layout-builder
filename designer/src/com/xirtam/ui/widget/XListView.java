package com.xirtam.ui.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.UIUtlis;

public class XListView extends XWidget {

	private static final long serialVersionUID = -945110568293265403L;
	private boolean isPressing;
	private transient Color defSelColor, defUnSelColor;

	public XListView() {
		super();
		setName(UIUtlis.genName());
		setType(NString.T_ListView);
		setBgSrc(NString.NULL);
		defSelColor = new Color(Config.DEFAULT_CHECKBOX_SEL_COLOR);
		defUnSelColor = new Color(Config.DEFAULT_CHECKBOX_UNSEL_COLOR);
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
		this.isPressing = Boolean.parseBoolean(data
				.getString(NString.K_IS_CLICK));
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
		jo.put(NString.K_IS_CLICK, isPressing + "");
		super.UI2Json();
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
		if (isPressing) {
			g.drawImage(selImg, 0, 0, getWidth(), getHeight(), null);
		} else {
			g.drawImage(unSelImg, 0, 0, getWidth(), getHeight(), null);
		}
		drawRange(g);
		paintChildren(g);
	}

	@Override
	public void onMouseDown(MouseEvent e) {
		super.onMouseDown(e);
	}

	@Override
	public void onMouseUp(MouseEvent e) {
		super.onMouseUp(e);
		isPressing = !isPressing;
		onPropertiesChanged();
		UIUtlis.updateProperties(UIUtlis.getFocusNode().getJsonData());
		UIUtlis.refreshProperties();
	}
}
