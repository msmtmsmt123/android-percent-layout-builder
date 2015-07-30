package com.xirtam.ui.widget;

import java.awt.Graphics;
import java.util.ArrayList;

import org.json.JSONObject;

import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.UIUtlis;

public class XLayout extends XWidget {

	private static final long serialVersionUID = -3659156866523344523L;
	private String layout;

	public XLayout() {
		super();
		this.setCPLayout(null);
		setName(UIUtlis.genName());
		setType(NString.T_LAYOUT);
		setBgSrc(NString.NULL);
		setCPLayout(NString.NULL);
		UI2Json();
	}

	public String getCPLayout() {
		return layout;
	}

	@Override
	public void Json2UI(JSONObject data) {

		this.setBgColorStr(data.getString(NString.K_BG_COLOR));
		this.setCPLayout(data.getString(NString.K_LAYOUT));
		if (!(this.getCPLayout() == null || this.getCPLayout().equals(
				NString.NULL)))
			pack();

		super.Json2UI(data);
	}

	public void setCPLayout(String layout) {
		this.layout = layout;
	}

	@Override
	protected void UI2Json() {
		jo.put(NString.K_BG_COLOR, String.format("%06x", getBgColor()));
		jo.put(NString.K_BG_SRC, getBgSrc());
		jo.put(NString.K_LAYOUT, layout);
		super.UI2Json();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// g.setColor(new Color(getBgColor()));
		// g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
		this.setBackground(bgColorObj);
		// int textSize = g.getFont().getSize();

		if (bgImg != null)
			g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
		drawRange(g);
		// if (cpNode != null && getText() != null) {
		// g.drawString(getText(), (getWidth() >> 1)
		// - (getText().length() >> 1) * textSize, getHeight() >> 1);
		// }
		// g.setColor(new Color(1, 1, 1));
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 3, 3);
		paintChildren(g);
	}

	/**
	 * 根据布局重排控件
	 */
	public void pack() {
		if (getCPLayout() != null && !getCPLayout().equals("null")) {
			ArrayList<XWidget> children = this.getChildren();
			int curPosition = 0;
			for (XWidget widget : children) {
				if (getCPLayout().equals(NString.LAYOUT_H)) {
					UIUtlis.moveCPButton(widget, -widget.getMarginLeft()
							+ curPosition, 0);
					curPosition += widget.getWidth() /* + Config.layoutInterval */;
				} else if (getCPLayout().equals(NString.LAYOUT_V)) {
					UIUtlis.moveCPButton(widget, 0, -widget.getMarginTop()
							+ curPosition);
					curPosition += widget.getHeight()/* + Config.layoutInterval */;
				}
				widget.onPropertiesChanged();
			}
		}
	}

	@Override
	public void test() {
	}

	@Override
	public void onPropertiesChanged() {
		super.onPropertiesChanged();
	}

}
