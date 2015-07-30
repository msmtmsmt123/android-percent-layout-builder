package com.xirtam.listener;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.UIUtlis;

public class XMouseListener extends MouseMotionAdapter implements
		MouseListener, Serializable {

	// private CPTreeNode dragBtn;

	private static final long serialVersionUID = -1289633907704843323L;
	private int pressW, pressH;
	private int pressX = 0, pressY = 0;
	private int pressXOS = 0, pressYOS = 0;
	private XWidget node;
	private int dragState = 0;

	// public CPMouseListener(CPTreeNode btnButton) {
	// this.dragBtn = btnButton;
	// }

	public XMouseListener(XWidget node) {
		this.node = node;
		node.onPropertiesChanged();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int offset = Config.DRAG_OFFSET;
		if (x < offset && y < offset) {
			dragState = Cursor.NW_RESIZE_CURSOR;
		} else if (x < offset && y > node.getHeight() - offset) {
			dragState = Cursor.SW_RESIZE_CURSOR;
		} else if (x > node.getWidth() - offset && y < offset) {
			dragState = Cursor.NE_RESIZE_CURSOR;
		} else if (x > node.getWidth() - offset
				&& y > node.getHeight() - offset) {
			dragState = Cursor.SE_RESIZE_CURSOR;
		} else if (x < offset && y > (node.getHeight() - offset) / 2
				&& y < (node.getHeight() + offset) / 2) {
			dragState = Cursor.W_RESIZE_CURSOR;
		} else if (x > node.getWidth() - offset
				&& y > (node.getHeight() - offset) / 2
				&& y < (node.getHeight() + offset) / 2) {
			dragState = Cursor.E_RESIZE_CURSOR;
		} else if (x > (node.getWidth() - offset) / 2
				&& x < (node.getWidth() + offset) / 2 && y < offset) {
			dragState = Cursor.N_RESIZE_CURSOR;
		} else if (x > (node.getWidth() - offset) / 2
				&& x < (node.getWidth() + offset) / 2
				&& y > node.getHeight() - offset) {
			dragState = Cursor.S_RESIZE_CURSOR;
		} else {
			dragState = Cursor.DEFAULT_CURSOR;
		}
		node.setCursor(new Cursor(dragState));
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		node.repaint();
		pressXOS = e.getXOnScreen();
		pressYOS = e.getYOnScreen();
		pressX = e.getX();
		pressY = e.getY();
		pressW = node.getWidth();
		pressH = node.getHeight();
		// updateProperties(e);
		UIUtlis.setFocusNode(node);
		UIUtlis.selectTreeNode(node.getId());
		updateData();
		node.onMouseDown(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (Config.debug)
			node.test();
		node.onMouseUp(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		if (node != null && node.isDragable())
			if (dragState == Cursor.DEFAULT_CURSOR) {
				node.getJsonData().put(NString.K_MARGIN_LEFT,
						String.valueOf(node.getX()));
				node.getJsonData().put(NString.K_MARGIN_TOP,
						String.valueOf(node.getY()));
				UIUtlis.moveCPButton(node, e.getX() - pressX, e.getY() - pressY);
			} else if (dragState == Cursor.NW_RESIZE_CURSOR) {// lt
				node.setSize(pressW - e.getXOnScreen() + pressXOS,
						pressH - e.getYOnScreen() + pressYOS);
				UIUtlis.moveCPButton(node, e.getX() - pressX, e.getY() - pressY);
			} else if (dragState == Cursor.SW_RESIZE_CURSOR) {// lb
				node.setSize(pressW - e.getXOnScreen() + pressXOS,
						pressH + e.getYOnScreen() - pressYOS);
				UIUtlis.moveCPButton(node, e.getX() - pressX, 0);
			} else if (dragState == Cursor.NE_RESIZE_CURSOR) {// rt
				node.setSize(pressW + e.getXOnScreen() - pressXOS,
						pressH - e.getYOnScreen() + pressYOS);
				UIUtlis.moveCPButton(node, 0, e.getY() - pressY);
			} else if (dragState == Cursor.SE_RESIZE_CURSOR) {// rb
				node.setSize(pressW + e.getXOnScreen() - pressXOS,
						pressH + e.getYOnScreen() - pressYOS);
			} else if (dragState == Cursor.S_RESIZE_CURSOR) {// b
				node.setSize(pressW, pressH + e.getYOnScreen() - pressYOS);
			} else if (dragState == Cursor.E_RESIZE_CURSOR) {// r
				node.setSize(pressW + e.getXOnScreen() - pressXOS, pressH);
			} else if (dragState == Cursor.N_RESIZE_CURSOR) {// t
				node.setSize(pressW, pressH - e.getYOnScreen() + pressYOS);
				UIUtlis.moveCPButton(node, 0, e.getY() - pressY);
			} else if (dragState == Cursor.W_RESIZE_CURSOR) {// l
				node.setSize(pressW - e.getXOnScreen() + pressXOS, pressH);
				UIUtlis.moveCPButton(node, e.getX() - pressX, 0);
			}
		updateData();
	}

	// private void updateProperties(MouseEvent e) {
	// JButton b = ((JButton) e.getSource());
	// CPTreeNode node = UITools.getMapping(b);
	// UITools.setFocusNode(node);
	// UITools.refreshProperties();
	// UITools.updateProperties(node.getJsonData());
	// }

	/**
	 * 更新自身数据
	 */
	private void updateData() {
		node.onPropertiesChanged();
		UIUtlis.updateProperties(node.getJsonData());
		UIUtlis.refreshProperties();
	}
}
