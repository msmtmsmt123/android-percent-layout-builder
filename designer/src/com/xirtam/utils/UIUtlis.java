package com.xirtam.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.common.WidgetProperties;
import com.xirtam.data.TableComboCell;
import com.xirtam.ui.Launcher;
import com.xirtam.ui.XWidget;

public class UIUtlis {
	private static DefaultTreeModel defaultTreeModel;
	private static HashMap<String, TableComboCell> editorData = new HashMap<String, TableComboCell>();
	private static XWidget focusNode;
	public static volatile boolean isShiftPressing = false;

	// private static HashMap<JButton, CPTreeNode> button2Node = new
	// HashMap<JButton, CPTreeNode>();
	//
	// public static CPTreeNode getFocusNode() {
	// return focusNode;
	// }
	//
	// public static void saveMapping(JButton b, CPTreeNode c) {
	// button2Node.put(b, c);
	// }
	//
	// public static CPTreeNode getMapping(JButton b) {
	// return button2Node.get(b);
	// }

	// private static CPNode rootNode;

	private static Map<Integer, XWidget> mappingIB = new HashMap<Integer, XWidget>();
	/**
	 * JTree树根节点
	 */
	private static DefaultMutableTreeNode rootTreeNode;

	private static XWidget rootWidget;

	/**
	 * 根据类型为table赋值左列的key
	 * 
	 * @param modelType
	 *            控件的类型
	 * @return 对应的数据
	 */
	public static ArrayList<String> addModel(String modelType) {
		ArrayList<String> list = new ArrayList<String>();
		if (modelType.equals(NString.T_LAYOUT)) {
			for (String s : WidgetProperties.CONFIG_LAYOUT_PROPERTIES) {
				list.add(s);
			}
		} else if (modelType.equals(NString.T_BUTTON)) {
			for (String s : WidgetProperties.CONFIG_BUTTON_PROPERTIES) {
				list.add(s);
			}
		} else if (modelType.equals(NString.T_LABEL)) {
			for (String s : WidgetProperties.CONFIG_LABEL_PROPERTIES) {
				list.add(s);
			}
		} else if (modelType.equals(NString.T_ListView)) {
			for (String s : WidgetProperties.CONFIG_CHECKBOX_PROPERTIES) {
				list.add(s);
			}
		} else if (modelType.equals(NString.T_IMAGEVIEW)) {
			for (String s : WidgetProperties.CONFIG_IMAGE_PROPERTIES) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * 检查name是否已经存在
	 * 
	 * @param name
	 * @return 存在-->true
	 */
	public static boolean checkSame(String name) {
		boolean result = false;
		if (UIUtlis.getRootTreeNode() != null) {
			@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> en = UIUtlis.getRootTreeNode()
					.breadthFirstEnumeration();
			while (en.hasMoreElements()) {
				DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) en
						.nextElement();
				String tmp = ((XWidget) tNode.getUserObject()).getName();
				if (tmp != null && tmp.equals(name))
					result = true;
			}
		}
		return result;
	}

	/**
	 * 清除屏幕中所有控件
	 */
	public static void clearScreen() {
		XWidget rootButton = UIUtlis.getButtonById(UIUtlis.getRootWidget()
				.getId());
		mappingIB.clear();
		mappingIB.put(UIUtlis.getRootWidget().getId(), rootButton);
		XNodeFactory.clearScreen();
		Launcher.launcher.windowPanel.removeAll();
		rootTreeNode.removeAllChildren();
		UIUtlis.reloadTree();
		UIUtlis.setFocusNode(null);
		UIUtlis.updateProperties(null);
		UIUtlis.refreshProperties();
		UIUtlis.refreshWindow();
		Launcher.launcher.textArea.setText(NString.SPACE);
		Launcher.launcher.rootFrame.repaint();
	}

	/**
	 * Enumeration转换成List
	 * 
	 * @param e
	 * @return
	 */
	public static ArrayList<DefaultMutableTreeNode> enumToList(
			Enumeration<DefaultMutableTreeNode> e) {
		ArrayList<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
		while (e.hasMoreElements()) {
			list.add(e.nextElement());
		}
		// Collections.reverse(list);
		return list;
	}

	/**
	 * 根据系统时间生成一个name,不会和已有控件的name重复
	 * 
	 * @return
	 */
	public static String genName() {
		long time = System.currentTimeMillis();
		String name = null;
		do {
			name = Long.toHexString(time);
			time++;
		} while (checkSame(name));
		return name;
	}

	/**
	 * 根据id得到存储过映射的JButton对象
	 * 
	 * @param id
	 * @return
	 */
	public static XWidget getButtonById(int id) {
		return mappingIB.get(id);
	}

	/**
	 * 根据name得到控件
	 * 
	 * @param name
	 * @return
	 */
	public static XWidget getWidgetByName(String name) {
		Iterator<Entry<Integer, XWidget>> iterator = mappingIB.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, XWidget> entry = (Entry<Integer, XWidget>) iterator
					.next();
			if (entry.getValue().getName().equals(name)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * 根据key得到对应JTable中的editor(通常是下拉菜单)
	 * 
	 * @param key
	 * @return
	 */
	public static TableCellEditor getEditor(String key) {
		return editorData.get(key).getEditor();
	}

	/**
	 * @return 得到焦点JButton
	 */
	public static XWidget getFocusNode() {
		return focusNode;
	}

	/**
	 * 得到焦点控件的key list
	 * 
	 * @return
	 */
	public static ArrayList<String> getFocusNodeProperties() {
		return addModel(getFocusNode().getType());
	}

	/**
	 * @return 得到根JFrame对象
	 */
	public static JFrame getRootFrame() {
		return Launcher.launcher.rootFrame;
	}

	/**
	 * @return 得到JTree的根节点
	 */
	public static DefaultMutableTreeNode getRootTreeNode() {
		return rootTreeNode;
	}

	/**
	 * 得到根控件
	 * 
	 * @return
	 */
	public static XWidget getRootWidget() {
		return rootWidget;
	}

	/**
	 * 根据id得到JTree中的节点
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DefaultMutableTreeNode getTreeNodeById(int id) {
		DefaultMutableTreeNode treeNode = null;
		if (UIUtlis.getRootTreeNode() != null) {
			Enumeration<DefaultMutableTreeNode> e = UIUtlis.getRootTreeNode()
					.breadthFirstEnumeration();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode node = e.nextElement();
				int localId = ((XWidget) node.getUserObject()).getId();
				if (localId == id) {
					treeNode = (DefaultMutableTreeNode) new TreePath(
							node.getPath()).getLastPathComponent();
				}
			}
		}
		return treeNode;
	}

	/**
	 * 判断点是否在 window界面内
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isInPhone(float x, float y) {
		return isInPhone((int) x, (int) y);
	}

	/**
	 * 判断点是否在 window界面内
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isInPhone(int x, int y) {
		return x >= Config.PHONE_X && x <= Config.PHONE_X + Config.PHONE_W
				&& y >= Config.PHONE_Y && y <= Config.PHONE_Y + Config.PHONE_H;
	}

	/**
	 * 判断text和JTree的根text是否相同
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isRootName(String text) {
		return NString.ROOTNODE_NAME.equals(text);
	}

	/**
	 * 移动CPButton
	 * 
	 * @param node
	 * @param x
	 *            x distance
	 * @param y
	 *            y distance
	 */
	public static void moveCPButton(XWidget node, int x, int y) {
		node.setLocation(new Point(node.getLocation().x + x,
				node.getLocation().y + y));
		// node.setMarginLeft(node.getLocation().x);
		// node.setMarginTop(node.getLocation().y);
		// DefaultMutableTreeNode treeNode = UIUtlis.getTreeNodeById(node
		// .getCpNode().getId());
		// int childCount = treeNode.getChildCount();
		// if (childCount > 0) {
		// for (int i = 0; i < childCount; i++) {
		// moveCPButton(
		// UIUtlis.getButtonById(((CPNode) ((DefaultMutableTreeNode) treeNode
		// .getChildAt(i)).getUserObject()).getId()), x,
		// y, pressX, pressY);
		// }
		// }
	}

	/**
	 * 放置Editor的数据
	 * 
	 * @param key
	 * @param data
	 */
	public static void putEditorData(String key, TableComboCell data) {
		editorData.put(key, data);
	}

	/**
	 * key对应的table右列是否是下拉
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isSelInTable(String key) {
		boolean result = false;
		Set<String> keySet = editorData.keySet();
		for (String string : keySet) {
			if (string.equals(key))
				result = true;
		}
		return result;
	}

	/**
	 * 刷新所有界面
	 */
	public static void refreshAll() {
		Launcher.launcher.rootFrame.getContentPane().repaint();
	}

	/**
	 * 遍历所有节点，根据节点当前的属性刷新节点中的json数据
	 */
	public static void refreshAllJsonNodeData() {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = UIUtlis.getRootTreeNode()
				.breadthFirstEnumeration();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) en
					.nextElement();
			XWidget cb = UIUtlis.getButtonById(((XWidget) tNode
					.getUserObject()).getId());
			cb.onPropertiesChanged();
		}
	}

	/**
	 * 遍历所有节点，根据节点当前的json数据属性刷新节点的UI
	 */
	public static void refreshAllWidget() {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = UIUtlis.getRootTreeNode()
				.breadthFirstEnumeration();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) en
					.nextElement();
			XWidget cb = UIUtlis.getButtonById(((XWidget) tNode
					.getUserObject()).getId());
			cb.Json2UI(cb.getJsonData());
		}
	}

	/**
	 * 刷新properties界面
	 */
	public static void refreshProperties() {
		Launcher.launcher.propertiesPanel.repaint();
		showJsonText();
	}

	/**
	 * 刷新树界面
	 */
	public static void refreshTree() {
		Launcher.launcher.treePanel.repaint();
		// CPLauncher.launcher.tree.repaint();
	}

	/**
	 * 刷新window界面
	 */
	public static void refreshWindow() {
		Launcher.launcher.windowPanel.repaint();
	}

	/**
	 * reload Jtree
	 */
	public static void reloadTree() {
		// treeRootNode.removeAllChildren();
		// ArrayList<CPNode> roots = CPNodeFactory.getRoots();
		// for (int i = 0; i < roots.size(); i++) {
		// treeRootNode.add(roots.get(i).getTreeNode());
		// }
		defaultTreeModel.reload();
	}

	/**
	 * 移除保存的键值关系
	 * 
	 * @param id
	 */
	public static void removeMappingIB(int id) {
		mappingIB.remove(id);
	}

	/**
	 * 存储键值关系
	 * 
	 * @param id
	 *            CPNode数据中的id
	 * @param button
	 *            id 对应的JButton对象
	 * 
	 */
	public static void saveMappingIB(int id, XWidget button) {
		mappingIB.put(id, button);
	}

	/**
	 * 根据数据id 选中控件
	 * 
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public static void selectTreeNode(int id) {

		Enumeration<DefaultMutableTreeNode> e = UIUtlis.getRootTreeNode()
				.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			int localId = ((XWidget) node.getUserObject()).getId();
			if (localId == id) {
				Launcher.launcher.tree.setSelectionPath(new TreePath(node
						.getPath()));
				return;
			}
		}
		Launcher.launcher.tree.setSelectionPath(null);

	}

	/**
	 * 设置焦点JButton
	 * 
	 * @param focusNode
	 */
	public static void setFocusNode(XWidget focusNode) {
		UIUtlis.focusNode = focusNode;
		if (focusNode != null) {
			Launcher.launcher.tabModel.setData(focusNode.getJsonData());
			Launcher.launcher.tabModel.setModelType(focusNode.getType());
		}

		UIUtlis.refreshProperties();
	}

	/**
	 * 设置是否按下控制键
	 * 
	 * @param b
	 */
	public static void setIsShiftPress(boolean b) {
		Launcher.launcher.lbl_shift.setText(NString.LBL_SHIFT + " " + b);
		Launcher.launcher.lbl_shift.repaint();
	}

	public static void setCopyName(String name) {
		Launcher.launcher.lblCopyName.setText(NString.LBL_COPY_NAME + " "
				+ name);
		Launcher.launcher.lblCopyName.repaint();
	}

	/**
	 * 设置鼠标位置（未实现）
	 * 
	 * @param x
	 * @param y
	 */
	public static void setMousePosition(int x, int y) {
		Launcher.launcher.lbl_mouse.setText(NString.LBL_COPY_NAME + " " + x
				+ " " + y);
		Launcher.launcher.lbl_mouse.repaint();
	}

	/**
	 * 设置JTree树根节点
	 * 
	 * @param rootTreeNode
	 */
	public static void setRootTreeNode(DefaultMutableTreeNode rootTreeNode) {
		UIUtlis.rootTreeNode = rootTreeNode;
	}

	/**
	 * 设置根控件
	 * 
	 * @param rootWidget
	 */
	public static void setRootWidget(XWidget rootWidget) {
		UIUtlis.rootWidget = rootWidget;
	}

	/**
	 * 设置JTree的数据model
	 * 
	 * @param defaultTreeModel
	 */
	public static void setTreeModel(DefaultTreeModel defaultTreeModel) {
		UIUtlis.defaultTreeModel = defaultTreeModel;
	}

	/**
	 * 修改控件数量
	 * 
	 * @param count
	 */
	public static void setWidgetCount(int count) {
		Launcher.launcher.lbl_wc.setText(NString.LBL_WC + " " + count);
		Launcher.launcher.lbl_wc.repaint();
	}

	/**
	 * 显示当前的最终json数据
	 * 
	 * @return formatted json string
	 */
	public static String showJsonText() {
		// oldShow();
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = UIUtlis.getRootTreeNode()
				.breadthFirstEnumeration();
		ArrayList<DefaultMutableTreeNode> reverse = enumToList(e);
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		for (DefaultMutableTreeNode defaultMutableTreeNode : reverse) {
			XWidget node = (XWidget) defaultMutableTreeNode.getUserObject();
			XWidget button = UIUtlis.getButtonById(node.getId());
			if (button != null
					&& !button.getType().equals(NString.ROOTNODE_NAME)) {
				XWidget pNode = ((XWidget) ((DefaultMutableTreeNode) defaultMutableTreeNode
						.getParent()).getUserObject());
				if (UIUtlis.isRootName(pNode.getType())) {// 父节点是root
					result.add(button.getJsonData());
				} else {// 有父节点
					break;
				}
			}
		}
		JSONArray array = new JSONArray(result);
		String formatJson = JsonFormatTool.formatJson(array.toString(), "    ");
		Launcher.launcher.textArea.setText(formatJson);
		return formatJson;

	}

	/**
	 * 根据JsonData 更新properties界面
	 * 
	 * @param data
	 */
	public static void updateProperties(JSONObject data) {
		Launcher.launcher.tabModel.setData(data);
	}

	/**
	 * update Jtree
	 */
	public static void updateTree() {
		Launcher.launcher.tree.updateUI();
	}

	/**
	 * 得到控件在布局中的index
	 * 
	 * @param widget
	 * @return 不在布局中返回 -1
	 */
	public static int getIndexInLayout(XWidget widget) {
		int result = -1;
		if (widget == null || !widget.isDragable()) {
			int pid = widget.getParentCPWidget().getId();
			DefaultMutableTreeNode ptNode = UIUtlis.getTreeNodeById(pid);
			for (int i = 0; i < ptNode.getChildCount(); i++) {
				if (widget.getId() == ((XWidget) ((DefaultMutableTreeNode) ptNode
						.getChildAt(i)).getUserObject()).getId()) {
					result = i;
				}
			}
		}
		return result;
	}

	public static int getMarginInLayout(XWidget widget) {
		int result = 0;
		if (widget != null && !widget.isDragable()) {
			ArrayList<XWidget> children = widget.getParentCPWidget()
					.getChildren();
			int index = getIndexInLayout(widget);
			if (index <= 0) {
				result = 0;
			} else {
				result = (int) (widget.getLocation().getY()
						- children.get(index - 1).getLocation().getY() - children
						.get(index - 1).getHeight());
			}
		} else {
			result = Integer.MIN_VALUE;
		}
		return result;
	}

	public static XWidget getPreWidget(XWidget widget) {
		int index = getIndexInLayout(widget);
		if (index > 0) {
			return widget.getParentCPWidget().getChildren().get(index - 1);
		} else {
			return null;
		}

	}

	public static int getPreTop(XWidget widget) {
		return getMarginInLayout(getPreWidget(widget));
	}
}
