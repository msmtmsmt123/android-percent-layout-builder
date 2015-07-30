package com.xirtam.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.listener.XMouseListener;
import com.xirtam.ui.widget.XLayout;
import com.xirtam.utils.UIUtlis;

public abstract class XWidget extends JPanel {
	protected static final long serialVersionUID = -2593199961500729933L;
	protected String align;
	protected Color bgColorObj;
	protected String bgColorStr;
	protected transient Image bgImg, unSelImg, selImg;
	protected String bgSrc, unSelImgSrc, selImgSrc;
	protected int color, fontColor;

	public boolean isDragable() {
		return getParentCPWidget() == null
				|| !(getParentCPWidget() instanceof XLayout)
				|| (getParentCPWidget() instanceof XLayout && ((XLayout) getParentCPWidget())
						.getCPLayout().equals(NString.NULL));
	}

	protected XMouseListener cpMouseListener;
	protected int id;

	protected Image image;
	protected String imageSrc;
	protected JSONObject jo;

	protected int marginRight, marginBottom;
	protected int marginLeft, marginTop;

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	protected String name;

	protected String type;

	public XWidget() {
		jo = new JSONObject();
		id = hashCode();
		color = Config.DEFAULT_COLOR;

		this.setLayout(null);
		setName(UIUtlis.genName());
		setBgSrc(NString.NULL);
		bgColorObj = new Color(getBgColor());
		unSelImgSrc = "null";
		selImgSrc = "null";
	}

	/**
	 * 画四边四角的小点，表示可以拖动改变大小
	 * 
	 * @param g
	 */
	protected void drawRange(Graphics g) {
		if (UIUtlis.getFocusNode() != null)
			if (this.getId() == UIUtlis.getFocusNode().getId()) {
				Shape buffer = g.getClip();
				g.setColor(Color.gray);
				g.setClip(-1, -1, getWidth() + 5, getHeight() + 5);
				g.drawLine(-1, -1, -1, getHeight());
				g.drawLine(-1, -1, getWidth(), -1);
				g.drawLine(getWidth(), -1, getWidth(), getHeight());
				g.drawLine(-1, getHeight(), getWidth(), getHeight());

				g.fillRect(-1, -1, 5, 5);
				g.fillRect(getWidth() - 5, -1, 5, 5);
				g.fillRect(-1, getHeight() - 5, 5, 5);
				g.fillRect(getWidth() - 5, getHeight() - 5, 5, 5);

				g.fillRect(-1, (getHeight() - 5) >> 1, 5, 5);
				g.fillRect((getWidth() - 5) >> 1, -1, 5, 5);
				g.fillRect(getWidth() - 5, (getHeight() - 5) >> 1, 5, 5);
				g.fillRect((getWidth() - 5) >> 1, getHeight() - 5, 5, 5);

				g.setClip(buffer);
				UIUtlis.refreshWindow();
			}
	}

	public String getAlign() {
		return align;
	}

	public int getBgColor() {
		return color;
	}

	public String getBgColorStr() {
		return bgColorStr;
	}

	public Image getBgImg() {
		return bgImg;
	}

	public String getBgSrc() {
		return bgSrc;
	}

	/**
	 * 取得当前节点的子节点集合
	 * 
	 * @return
	 */
	public ArrayList<XWidget> getChildren() {
		ArrayList<XWidget> buttons = new ArrayList<XWidget>();
		int id = this.getId();
		DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) UIUtlis
				.getTreeNodeById(id);
		if (tNode != null && tNode.getChildCount() > 0) {
			for (int i = 0; i < tNode.getChildCount(); i++) {
				XWidget no = (XWidget) ((DefaultMutableTreeNode) tNode
						.getChildAt(i)).getUserObject();
				int sid = no.getId();
				buttons.add(UIUtlis.getButtonById(sid));
			}
		}
		return buttons;
	}

	public int getFontColor() {
		return fontColor;
	}

	public int getId() {
		return id;
	}

	public Image getImage() {
		return image;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public JSONObject getJsonData() {
		return jo;
	}

	public int getMarginBottom() {
		return marginBottom;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public String getName() {
		return name;
	}

	public XWidget getParentCPWidget() {
		int id = this.getId();
		DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) UIUtlis
				.getTreeNodeById(id);
		if (tNode != null) {
			DefaultMutableTreeNode parent2 = (DefaultMutableTreeNode) tNode
					.getParent();
			if (parent2 != null) {
				int pid = ((XWidget) parent2.getUserObject()).getId();
				return UIUtlis.getButtonById(pid);
			}
		}
		return null;
	}

	public int getParentX() {
		int offset = 0;
		if (getParentCPWidget() != null) {
			offset = getParentCPWidget().getX();
		}
		return offset;
	}

	public int getParentY() {
		int offset = 0;
		if (getParentCPWidget() != null) {
			offset = getParentCPWidget().getY();
		}
		return offset;
	}

	public Image getSelImg() {
		return selImg;
	}

	public String getSelImgSrc() {
		return selImgSrc;
	}

	public String getType() {
		return type;
	}

	public Image getUnSelImg() {
		return unSelImg;
	}

	public String getUnSelImgSrc() {
		return unSelImgSrc;
	}

	/**
	 * 根据data为UI赋值各个属性
	 * 
	 * @param data
	 */
	public void Json2UI(JSONObject data) {
		String left = data.getString(NString.K_MARGIN_LEFT);
		String top = data.getString(NString.K_MARGIN_TOP);
		String right = data.getString(NString.K_MARGIN_RIGHT);
		String bottom = data.getString(NString.K_MARGIN_BOTTOM);
		int w = (int) Float.parseFloat(data.getString(NString.K_W));
		int h = (int) Float.parseFloat(data.getString(NString.K_H));
		int leftValue = 0, topValue = 0;
		int rightValue = NString.NULL.equals(right) ? 0 : Integer
				.parseInt(right);
		int bottomValue = NString.NULL.equals(bottom) ? 0 : Integer
				.parseInt(bottom);

		XWidget pWidget = getParentCPWidget();
		if (NString.NULL.equals(left)) {
			leftValue = pWidget.getWidth() - rightValue - w;
		} else {
			leftValue = (int) Float.parseFloat(data
					.getString(NString.K_MARGIN_LEFT));
		}
		if (NString.NULL.equals(top)) {
			topValue = pWidget.getHeight() - bottomValue - h;
		} else {
			topValue = (int) Float.parseFloat(data
					.getString(NString.K_MARGIN_TOP));
		}

		this.setName(data.getString(NString.K_NAME));
		this.setAlign(data.getString(NString.K_ALIGN));
		if (NString.NULL.equals(right)) {
			if (NString.NULL.equals(left)) {
				leftValue = 0;
			}
		} else {
			this.setMarginRight(Integer.parseInt(data
					.getString(NString.K_MARGIN_RIGHT)));
		}
		if (NString.NULL.equals(bottom)) {
			if (NString.NULL.equals(top)) {
				topValue = 0;
			}
		} else {
			this.setMarginBottom(Integer.parseInt(data
					.getString(NString.K_MARGIN_BOTTOM)));
		}
		this.setBounds(leftValue, topValue, w, h);
		onPropertiesChanged();
	};

	/**
	 * 用于子类选择重写，在鼠标在控件中按下时会调用
	 * 
	 * @param e
	 */
	public void onMouseDown(MouseEvent e) {
	}

	/**
	 * 用于子类选择重写，在鼠标在控件中抬起时会调用
	 * 
	 * @param e
	 */
	public void onMouseUp(MouseEvent e) {
	}

	/**
	 * 控件属性发生改变时回调
	 */
	public void onPropertiesChanged() {
		UI2Json();
		bgColorObj = new Color(getBgColor());
	}

	/**
	 * 移除监听
	 */
	public void removeCPMouseListner() {
		this.removeMouseMotionListener(cpMouseListener);
		this.removeMouseListener(cpMouseListener);
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setBackground(int color) {
		setBgColor(color);
	}

	public void setBgColor(int color) {
		this.color = color;
	}

	public void setBgColorStr(String text) {
		this.bgColorStr = text;
	}

	public void setbgImg(Image img) {
		this.bgImg = img;
	}

	public void setBgSrc(String bgSrc) {
		this.bgSrc = bgSrc;
	}

	public void setCPMouseListener(XMouseListener cpMouseListener) {
		this.cpMouseListener = cpMouseListener;
		this.addMouseMotionListener(cpMouseListener);
		this.addMouseListener(cpMouseListener);
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSelImg(Image selImg) {
		this.selImg = selImg;
	}

	public void setSelImgSrc(String selImgSrc) {
		this.selImgSrc = selImgSrc;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUnSelImg(Image unSelImg) {
		this.unSelImg = unSelImg;
	}

	public void setUnSelImgSrc(String unSelImgSrc) {
		this.unSelImgSrc = unSelImgSrc;
	}

	@Override
	public String toString() {
		return UIUtlis.isRootName(type) ? NString.ROOTNODE_NAME : type + " "
				+ name;
	};

	/**
	 * 根据当前控件属性更新json数据
	 */
	protected void UI2Json() {
		String left = jo.has(NString.K_MARGIN_LEFT) ? jo
				.getString(NString.K_MARGIN_LEFT) : "0";
		String top = jo.has(NString.K_MARGIN_TOP) ? jo
				.getString(NString.K_MARGIN_TOP) : "0";
		String right = jo.has(NString.K_MARGIN_RIGHT) ? jo
				.getString(NString.K_MARGIN_RIGHT) : "0";
		String bottom = jo.has(NString.K_MARGIN_BOTTOM) ? jo
				.getString(NString.K_MARGIN_BOTTOM) : "0";
		XWidget w = UIUtlis.getPreWidget(this);
		String pLayout = w == null ? "" : ((XLayout) this.getParentCPWidget())
				.getCPLayout();
		int preX = w == null ? 0 : NString.LAYOUT_H.equals(pLayout) ? (int) w
				.getLocation().getX() : 0;
		int preY = w == null ? 0 : NString.LAYOUT_V.equals(pLayout) ? (int) w
				.getLocation().getY() : 0;
		int preW = w == null ? 0 : NString.LAYOUT_H.equals(pLayout) ? (int) w
				.getWidth() : 0;
		int preH = w == null ? 0 : NString.LAYOUT_V.equals(pLayout) ? (int) w
				.getHeight() : 0;
		String name = getName();
		String align = getAlign() == null ? NString.NULL : getAlign();

		jo.put(NString.K_TYPE, getType());
		if (NString.NULL.equals(left)) {
			jo.put(NString.K_MARGIN_LEFT, NString.NULL);
		} else {
			jo.put(NString.K_MARGIN_LEFT,
					String.valueOf(marginLeft - preX - preW));
		}
		if (NString.NULL.equals(top)) {
			jo.put(NString.K_MARGIN_TOP, NString.NULL);
		} else {
			jo.put(NString.K_MARGIN_TOP,
					String.valueOf(marginTop - preY - preH));
		}
		if (NString.NULL.equals(right)) {
			jo.put(NString.K_MARGIN_RIGHT, NString.NULL);
		} else {
			jo.put(NString.K_MARGIN_RIGHT, String.valueOf(marginRight));
		}
		if (NString.NULL.equals(bottom)) {
			jo.put(NString.K_MARGIN_BOTTOM, NString.NULL);
		} else {
			jo.put(NString.K_MARGIN_BOTTOM, String.valueOf(marginBottom));
		}
		jo.put(NString.K_W, String.valueOf(this.getWidth()));
		jo.put(NString.K_H, String.valueOf(this.getHeight()));
		jo.put(NString.K_NAME, name);
		jo.put(NString.K_ALIGN, align);
		jo.put(NString.K_PARENT,
				getParentCPWidget() == null ? UIUtlis.getRootWidget() == null ? NString.NULL
						: UIUtlis.getRootWidget().getName()
						: getParentCPWidget().getName());
		if (getChildren().size() == 0) {
			jo.put(NString.K_CHILDREN, NString.NULL);
		} else {
			ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
			for (XWidget button : getChildren()) {
				arrayList.add(button.getJsonData());
			}
			JSONArray jsonArray = new JSONArray(arrayList);
			jo.put(NString.K_CHILDREN, jsonArray);
		}
		// if (getParentCPWidget() != null)
		// getParentCPWidget().onPropertiesChanged();
	};

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	/**
	 * 测试用，点击控件鼠标释放时会调用
	 */
	public void test() {

	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.setMarginLeft(x);
		this.setMarginTop(y);
	}

	public void packParent() {
		XWidget pWidget = getParentCPWidget();
		if (pWidget != null && pWidget.getType().equals(NString.T_LAYOUT)) {
			((XLayout) pWidget).pack();
		}
	}

}
