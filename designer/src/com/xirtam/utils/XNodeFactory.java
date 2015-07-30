package com.xirtam.utils;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.listener.XMouseListener;
import com.xirtam.ui.Launcher;
import com.xirtam.ui.XWidget;
import com.xirtam.ui.widget.XButton;
import com.xirtam.ui.widget.XListView;
import com.xirtam.ui.widget.XImageView;
import com.xirtam.ui.widget.XLabel;
import com.xirtam.ui.widget.XLayout;

public class XNodeFactory {

	private static int currentY = 0;
	private static Vector<XWidget> buttons = new Vector<XWidget>();
	public static volatile int widgetCount;
	public static XWidget copyBuffer;

	/**
	 * 生成一个控件
	 * 
	 * @param type
	 *            类型
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return 返回生成的数据
	 */
	public static XWidget generateButton(String type, int w, int h) {

		XWidget button = null;
		if (type.equals(NString.T_LABEL)) {
			button = new XLabel();
		} else if (type.equals(NString.T_LAYOUT)) {
			button = new XLayout();
		} else if (type.equals(NString.T_BUTTON)) {
			button = new XButton();
		} else if (type.equals(NString.T_ListView)) {
			button = new XListView();
		} else if (type.equals(NString.T_IMAGEVIEW)) {
			button = new XImageView();
		}

		button.setBounds(0, currentY * Config.GEN_BUTTON_H >> 1, w, h);
		currentY = currentY > 20 ? 0 : currentY + 1;
		button.setCPMouseListener(new XMouseListener(button));

		buttons.add(button);
		UIUtlis.setFocusNode(button);
		UIUtlis.getRootTreeNode().add(new DefaultMutableTreeNode(button));
		UIUtlis.saveMappingIB(button.getId(), button);
		UIUtlis.updateProperties(button.getJsonData());

		sortNodes();
		UIUtlis.reloadTree();
		UIUtlis.refreshWindow();
		UIUtlis.refreshProperties();
		widgetCount++;
		UIUtlis.setWidgetCount(widgetCount);
		return button;
	}

	// public static CPTreeNode getFocusButton() {
	// return focusButton;
	// }

	// public static void setFocusButton(CPTreeNode focusButton) {
	// CPNodeFactory.focusButton = focusButton;
	// }

	// private static void addButton(CPButton button) {
	// buttons.add(button);
	// CPLauncher.launcher.windowPanel.removeAll();
	//
	// for (int i = buttons.size() - 1; i >= 0; i--) {
	// CPLauncher.launcher.windowPanel.add(buttons.get(i));
	// }
	// }

	/**
	 * 将node和node的所有child置顶
	 * 
	 * @param node
	 */
	public static void moveToTop(XWidget node) {
		moveToTopImpl(node);
		sortNodes();
		UIUtlis.refreshWindow();
	}

	/**
	 * 移动至底层
	 * 
	 * @param node
	 */
	public static void moveToBottom(XWidget node) {
		moveToBottomImpl(node);
		sortNodes();
		UIUtlis.refreshWindow();
	}

	/**
	 * 深度优先遍历所有子节点
	 * 
	 * @param node
	 */
	private static void moveToTopImpl(XWidget node) {
		DefaultMutableTreeNode tNode = UIUtlis.getTreeNodeById(node.getId());
		if (tNode != null && tNode.getChildCount() > 0) {
			for (int i = 0; i < tNode.getChildCount(); i++) {
				tNode.getChildAt(i);
				moveToTopImpl(UIUtlis
						.getButtonById(((XWidget) ((DefaultMutableTreeNode) tNode
								.getChildAt(i)).getUserObject()).getId()));
			}
		}
	}

	/**
	 * 移动到底层实现，后续递归遍历
	 * 
	 * @param node
	 */
	private static void moveToBottomImpl(XWidget node) {
		DefaultMutableTreeNode tNode = UIUtlis.getTreeNodeById(node.getId());
		if (tNode != null && tNode.getChildCount() > 0) {
			for (int i = 0; i < tNode.getChildCount(); i++) {
				tNode.getChildAt(i);
				moveToBottomImpl(UIUtlis
						.getButtonById(((XWidget) ((DefaultMutableTreeNode) tNode
								.getChildAt(i)).getUserObject()).getId()));
			}
		}
	}

	/**
	 * 根据JTree的节点，广度优先遍历，重排Nodes
	 */
	public static void sortNodes() {

		// if (CPLauncher.launcher != null) {
		// CPLauncher.launcher.windowPanel.removeAll();
		//
		// for (int i = buttons.size() - 1; i >= 0; i--) {
		// CPLauncher.launcher.windowPanel.add(buttons.get(i));
		// }

		// @SuppressWarnings("rawtypes")
		// Enumeration e =
		// UIUtlis.getRootTreeNode().breadthFirstEnumeration();
		// while (e.hasMoreElements()) {
		// DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
		// .nextElement();
		// CPButton button = UIUtlis.getButtonById(((CPNode) node
		// .getUserObject()).getId());
		//
		// if (button != null) {
		// CPLauncher.launcher.windowPanel.add(button);
		// }
		// }
		// }
		buttons.clear();
		order(UIUtlis.getRootTreeNode());
		UIUtlis.refreshWindow();
	}

	private static void order(DefaultMutableTreeNode node) {
		int count = node.getChildCount();
		XWidget parent = UIUtlis.getButtonById(((XWidget) node
				.getUserObject()).getId());
		if (parent != null) {
			parent.removeAll();
			for (int i = count - 1; i >= 0; i--) {
				DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) node
						.getChildAt(i);
				order(childAt);
				XWidget button = UIUtlis.getButtonById(((XWidget) childAt
						.getUserObject()).getId());
				if (UIUtlis.isRootName(parent.getType())) {
					buttons.add(button);
					Launcher.launcher.windowPanel.add(button);
				} else {
					parent.add(button);
				}
			}
		}
	}

	/**
	 * 删除一个控件
	 * 
	 * @param id
	 */
	public static void destoryButton(XWidget widget) {
		if (widget == null)
			return;
		int id = widget.getId();
		destoryButtonImpl(id);
		UIUtlis.reloadTree();
		UIUtlis.refreshWindow();
		UIUtlis.removeMappingIB(id);
		UIUtlis.updateProperties(null);
		UIUtlis.refreshProperties();
		UIUtlis.setWidgetCount(widgetCount);
	}

	/**
	 * 移除一个控件
	 * 
	 * @param id
	 */
	@SuppressWarnings("rawtypes")
	private static void destoryButtonImpl(int id) {
		if (copyBuffer != null && id == copyBuffer.getId()) {
			copyBuffer = null;
			UIUtlis.setCopyName("");
		}

		DefaultMutableTreeNode tNode = UIUtlis.getTreeNodeById(id);
		Enumeration e = tNode.breadthFirstEnumeration();
		XWidget button = UIUtlis.getButtonById(id);
		button.removeCPMouseListner();

		while (e.hasMoreElements()) {
			DefaultMutableTreeNode nextElement = (DefaultMutableTreeNode) e
					.nextElement();
			widgetCount--;
			UIUtlis.removeMappingIB(((XWidget) nextElement.getUserObject())
					.getId());
		}

		if (button.getParentCPWidget() != null) {
			button.getParentCPWidget().remove(button);
		}
		buttons.remove(button);
		Launcher.launcher.windowPanel.remove(button);

		UIUtlis.setFocusNode(null);

		tNode.removeFromParent();
	}

	/**
	 * 复制一个控件
	 * 
	 * @param copyBuffer
	 */
	public static void copyButton() {
		XWidget button = null;
		if (copyBuffer == null || copyBuffer.getType() == null) {
			return;
		} else if (copyBuffer.getType().equals(NString.T_ListView)) {
			button = new XListView();
		} else if (copyBuffer.getType().equals(NString.T_IMAGEVIEW)) {
			button = new XImageView();
		} else if (copyBuffer.getType().equals(NString.T_LABEL)) {
			button = new XLabel();
		} else if (copyBuffer.getType().equals(NString.T_LAYOUT)) {
			button = new XLayout();
		} else if (copyBuffer.getType().equals(NString.T_BUTTON)) {
			button = new XButton();
		}
		// button.setBackground(Color.getHSBColor(((int) Math.random() * 256),
		// (int) (Math.random() * 256), (int) (Math.random() * 256)));
		button.Json2UI(copyBuffer.getJsonData());

		button.setBounds(copyBuffer.getX() + 20, copyBuffer.getY() + 20,
				copyBuffer.getWidth(), copyBuffer.getHeight());
		button.setName(UIUtlis.genName());
		currentY++;
		button.setCPMouseListener(new XMouseListener(button));
		buttons.add(button);
		UIUtlis.setFocusNode(button);
		UIUtlis.getRootTreeNode().add(new DefaultMutableTreeNode(button));
		UIUtlis.reloadTree();
		UIUtlis.refreshWindow();
		UIUtlis.saveMappingIB(button.getId(), button);
		UIUtlis.updateProperties(button.getJsonData());
		sortNodes();
		widgetCount++;
		UIUtlis.setWidgetCount(widgetCount);
	}

	/**
	 * 清除所有控件的时候调用，用来清除Factory中保存的变量
	 */
	public static void clearScreen() {
		currentY = 0;
		buttons.clear();
		widgetCount = 0;
		copyBuffer = null;
		UIUtlis.setCopyName("");
		UIUtlis.setWidgetCount(widgetCount);
	}

	/**
	 * 根据读取的json数据生成UI
	 * 
	 * @param ja
	 */
	public static void file2UI(JSONArray ja) {
		for (int i = 0; i < ja.length(); i++) {
			// 读取数据
			JSONObject jsonObject = ja.getJSONObject(i);
			String type = jsonObject.getString(NString.K_TYPE);
			String parentStr = jsonObject.getString(NString.K_PARENT);
			XWidget generateButton = generateButton(type, 1, 1);// 先生成一个button
			generateButton.Json2UI(jsonObject);

			XWidget parentBtn = UIUtlis.getWidgetByName(parentStr);
			if (parentBtn != null) {
				// 修改Jtree树结构
				DefaultMutableTreeNode tGenerate = UIUtlis
						.getTreeNodeById(generateButton.getId());
				DefaultMutableTreeNode tParent = UIUtlis
						.getTreeNodeById(parentBtn.getId());
				tParent.add(tGenerate);
				UIUtlis.updateTree();
				addInto(generateButton, parentBtn);
			}

			JSONArray children = null;
			if (!jsonObject.get(NString.K_CHILDREN).equals(NString.NULL))
				children = jsonObject.getJSONArray(NString.K_CHILDREN);
			if (children != null)
				file2UI(children);
		}
	}

	/**
	 * 控件放入另一个控件
	 * 
	 * @param ss
	 *            拖动的控件
	 * @param ff
	 *            目标控件
	 */
	public static void addInto(XWidget ss, XWidget ff) {
		if (UIUtlis.isRootName(ff.getType())) {
			XWidget p = ss.getParentCPWidget();
			p.remove(ss);
		}
		if (UIUtlis.isRootName(ff.getType())) {
			buttons.add(ss);
		} else {
			buttons.remove(ss);
			ff.add(ss);
			// sortNodes();
		}
		sortNodes();
		// UIUtlis.moveCPButton(ss, -ss.getMarginLeft(), -ss.getMarginTop());
		ss.onPropertiesChanged();
		// UIUtlis.moveCPButton(ss, 0, 0, ff.getXToParent(), ff.getYToParent());
		// ss.getCpNode().setXY(0, 0);
		// ss.onPropertiesChanged();

	}

	public static void saveCopy() {
		XNodeFactory.copyBuffer = UIUtlis.getFocusNode();
		if (copyBuffer != null)
			UIUtlis.setCopyName(copyBuffer.getName());
		else
			UIUtlis.setCopyName("");
	}
}
