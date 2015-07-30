package com.xirtam.data;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.ui.XComboBoxEditor;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.UIUtlis;

/**
 * table中下拉
 * 
 * @author xirtam
 * 
 */
public class TableComboCell implements ItemListener {
	private XComboBoxEditor myEditor;
	private String[] values;

	public TableCellEditor getEditor() {
		return myEditor;
	}

	public TableComboCell(String[] values) {
		super();
		this.values = values;
		this.myEditor = new XComboBoxEditor(values);
		JComboBox c = (JComboBox) myEditor.getC();
		c.addItemListener(this);
		// myEditor.addCellEditorListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		switch (event.getStateChange()) {
		case ItemEvent.SELECTED:
			if (UIUtlis.getFocusNode() == null)
				return;
			// "选中" + event.getItem());
			String value = (String) event.getItem();
			XWidget focusNode = UIUtlis.getFocusNode();
			DefaultMutableTreeNode tNode = UIUtlis.getTreeNodeById(focusNode
					.getId());

			int parentX,
			parentY,
			parentW,
			parentH,
			x = 0,
			y = 0;

			if (!((XWidget) tNode.getUserObject()).getType().equals(
					NString.ROOTNODE_NAME)) {
				XWidget parentCPNode = (XWidget) ((DefaultMutableTreeNode) tNode
						.getParent()).getUserObject();
				if (!UIUtlis.isRootName(parentCPNode.getType())) {
					XWidget parent = UIUtlis.getButtonById(parentCPNode
							.getId());
					parentX = 0;
					parentY = 0;
					parentW = parent.getWidth();
					parentH = parent.getHeight();
				} else {
					Container parent = focusNode.getParent();
					parentX = 0;
					parentY = 0;
					parentW = parent.getWidth();
					parentH = parent.getHeight();
				}
			} else {
				parentX = 0;
				parentY = 0;
				parentW = Config.PHONE_W;
				parentH = Config.PHONE_H;
			}

			if (values == NString.VALUES_ALIGN) {
				if (value.equals(NString.VALUES_ALIGN[0])) {
					x = parentX;
					y = parentY + (parentH - focusNode.getHeight()) / 2;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[1])) {
					x = parentX + parentW - focusNode.getWidth();
					y = parentY + (parentH - focusNode.getHeight()) / 2;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[2])) {
					x = parentX + (parentW - focusNode.getWidth()) / 2;
					y = parentY;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[3])) {
					x = parentX + (parentW - focusNode.getWidth()) / 2;
					y = parentY + parentH - focusNode.getHeight();
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[4])) {
					x = parentX;
					y = parentY;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[5])) {
					x = parentX;
					y = parentY + parentH - focusNode.getHeight();
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[6])) {
					x = parentX + parentW - focusNode.getWidth();
					y = parentY;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[7])) {
					x = parentX + parentW - focusNode.getWidth();
					y = parentY + parentH - focusNode.getHeight();
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[8])) {
					x = parentX + (parentW - focusNode.getWidth()) / 2;
					y = parentY + (parentH - focusNode.getHeight()) / 2;
					moveAlign(focusNode, x, y);
				} else if (value.equals(NString.VALUES_ALIGN[9])) {
					// focusNode.updateUI(focusNode.getCpNode().setXY(parentX,
					// parentY + (parentH - focusNode.getHeight()) / 2));

				}
			} else if (values == NString.VALUES_LAYOUT) {
				// ((CPLayout) focusNode).pack();
				if (value.equals(NString.LAYOUT_H)) {
					// focusNode.updateUI(focusNode.getCpNode().setXY(parentX,
					// parentY + (parentH - focusNode.getHeight()) / 2));

				} else if (value.equals(NString.LAYOUT_V)) {
					// focusNode.updateUI(focusNode.getCpNode().setXY(parentX,
					// parentY + (parentH - focusNode.getHeight()) / 2));

				} else if (value.equals(NString.NULL)) {
					// focusNode.updateUI(focusNode.getCpNode().setXY(parentX,
					// parentY + (parentH - focusNode.getHeight()) / 2));
				}
			}

			break;
		case ItemEvent.DESELECTED:
			// "取消选中" + event.getItem());
			break;
		}
	}

	private void moveAlign(XWidget focusNode, int x, int y) {
		UIUtlis.moveCPButton(focusNode, x - focusNode.getX(),
				y - focusNode.getY());
		focusNode.onPropertiesChanged();
		UIUtlis.updateProperties(focusNode.getJsonData());
		UIUtlis.refreshProperties();
	}
}
