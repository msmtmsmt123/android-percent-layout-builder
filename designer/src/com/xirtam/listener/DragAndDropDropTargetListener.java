package com.xirtam.listener;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.xirtam.data.TreeNodeTransfer;
import com.xirtam.ui.XWidget;
import com.xirtam.utils.XNodeFactory;
import com.xirtam.utils.UIUtlis;

//* Drop Target Listener */   
public class DragAndDropDropTargetListener implements DropTargetListener {

	// e.rejectDrop()只可以在dragEnter()、dragOver()和dropActionChanged()中调用，不能在drop()中调用

	/**
	 * 在光标进入放置组件的显示区时调用
	 * 
	 * @param e
	 *            DropTargetDragEvent
	 */
	public void dragEnter(DropTargetDragEvent e) {

	}

	/**
	 * 在光标进入放置组件显示区之后移动时调用
	 * 
	 * @param e
	 *            DropTargetDragEvent
	 */
	public void dragOver(DropTargetDragEvent e) {
	}

	/**
	 * 选择放置操作的修饰键的状态变化
	 * 
	 * @param e
	 *            DropTargetDragEvent
	 */
	public void dropActionChanged(DropTargetDragEvent e) {
	}

	/**
	 * 在光标退出放置组件的显示区时发生
	 * 
	 * @param e
	 *            DropTargetEvent
	 */
	public void dragExit(DropTargetEvent e) {
	}

	/**
	 * 在发生放置时调用，负责接受或拒绝放置请求和处理放置的数据
	 * 
	 * @param e
	 *            DropTargetDropEvent
	 */
	public void drop(DropTargetDropEvent e) {
		// 获取要传递的数据
		Transferable transfer = e.getTransferable();
		DefaultMutableTreeNode dragSource = null;
		// 是否支持树节点数据的传递
		if (transfer.isDataFlavorSupported(TreeNodeTransfer.TREENODE_FLAVOR)) {
			try {
				// 设置接受移动的操作
				e.acceptDrop(DnDConstants.ACTION_MOVE);
				// 获取传递的数据
				// CPNode userObj = (CPNode) transfer
				// .getTransferData(TreeNodeTransfer.TREENODE_FLAVOR_USER_OBJ);
				dragSource = (DefaultMutableTreeNode) transfer
						.getTransferData(TreeNodeTransfer.TREENODE_FLAVOR);
				// dragSource.setUserObject(userObj);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (dragSource == null) { // 拖动的数据源为空则退出
			// 放置不成功
			e.dropComplete(false);
			return;
		}
		// 获取dropTo对象
		DropTarget dt = (DropTarget) e.getSource();
		Component comp = dt.getComponent();
		if (!(comp instanceof JTree)) {
			// 放置不成功
			e.dropComplete(false);
			return;
		}
		JTree jtr = (JTree) comp;
		TreePath treePath = jtr.getPathForLocation(e.getLocation().x,
				e.getLocation().y);
		if (treePath == null) {
			// 放置不成功
			e.dropComplete(false);
			return;
		}
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath
				.getLastPathComponent();
		if (!isCanDrop(dragSource, treeNode, jtr)) {
			// 放置不成功
			e.dropComplete(false);
			return;
		}

		// 修改数据树结构
		XWidget ff = (XWidget) treeNode.getUserObject();
		XWidget ss = (XWidget) dragSource.getUserObject();

		XNodeFactory.addInto(ss, ff);

		// 把节点添加到当前树节点下
		treeNode.add(dragSource);

		// parentCPButton = ss.getParentCPButton();
		// parentCPButton.onPropertiesChanged();
		// parentCPButton.repaint();
		// UIUtlis.setFocusNode(parentCPButton);
		// UIUtlis.selectTreeNode(parentCPButton.getCpNode().getId());

		// int x = UIUtlis.isRootName(f.getType()) ? 0 : f.getX();
		// int y = UIUtlis.isRootName(f.getType()) ? 0 : f.getY();
		// UITools.moveCPNode(s, x, y);
		// CPButton son = UITools.getButtonById(s.getId());
		// UITools.setFocusNode(son);
		// ff.add(ss);
		// ss.setBounds(ff.getX(),ff.getY(), ss.getWidth(), ss.getHeight());

		// 重新绑定数据对象和Button对象
		XWidget cpButton = UIUtlis.getButtonById(ss.getId());
		UIUtlis.moveCPButton(cpButton, -cpButton.getX(), -cpButton.getY());
		XNodeFactory.moveToTop(cpButton);
		// UITools.setFocusNode(cpButton);
		// UITools.updateProperties(cpButton.getJsonData());
		// UITools.refreshProperties();

		// f.addChind(s);
		// 更新树节点
		((DefaultTreeModel) jtr.getModel()).reload(treeNode);
		// 设置放置成功
		e.dropComplete(true);
		// UITools.refreshProperties();
	}

	/**
	 * 判断是否可以放置操作
	 * 
	 * @param dragTreeNode
	 *            DefaultMutableTreeNode 拖动源的树节点
	 * @param dropTreeNode
	 *            DefaultMutableTreeNode 放置目标的树节点
	 * @return boolean
	 */
	public boolean isCanDrop(DefaultMutableTreeNode dragTreeNode,
			DefaultMutableTreeNode dropTreeNode, JTree jtr) {
		if (dragTreeNode == null) { // 拖动源为空则退出
			return false;
		}

		// 设置放置目标为空时不可放置
		if (dropTreeNode == null) {
			return false;
		}
		// 放置目标是拖动源则退出
		if (dragTreeNode == dropTreeNode) {
			return false;
		}
		TreePath dragPath = new TreePath(
				((DefaultTreeModel) jtr.getModel()).getPathToRoot(dragTreeNode));
		TreePath dropPath = new TreePath(
				((DefaultTreeModel) jtr.getModel()).getPathToRoot(dropTreeNode));
		String strDragPath = dragPath.toString();
		String strDropPath = dropPath.toString();
		String subDragPath = strDragPath.substring(0, strDragPath.length() - 1);

		XWidget node1 = (XWidget) dragTreeNode.getUserObject();
		XWidget node2 = (XWidget) dropTreeNode.getUserObject();

		if (strDropPath.startsWith(subDragPath)) {
			if (strDragPath.equals(strDropPath)) {
				if (node1.getId() == node2.getId()) {// self
					return false;
				} else {// same name brother
					return true;
				}
			} else {// father and son
				return false;
			}
		}
		// else if (strDropPath.startsWith(subDragPath)) {// 放置目标是拖动源的子孙节点
		// return false;
		// }

		// CPNode a = UITools.getCPNodeByHashCode(dragTreeNode.hashCode());
		// CPNode b = UITools.getCPNodeByHashCode(dropTreeNode.hashCode());

		// if (UITools.getDragCacheNode() != null && dropTreeNode != null)
		// ;
		// if (UITools.getDragCacheNode().getTreeNode().getUserObject()
		// .hashCode() == dropTreeNode.getUserObject().hashCode()) {
		// return false;// same node
		// }

		if (dragPath.getParentPath() != null) {
			if (dragPath.getParentPath().toString().equals(strDropPath)) {// 放置目标是拖动源的父节点
				return false;
			}

			// 放置目标是拖动源的父节点则退出
			if (dragTreeNode.getParent().equals(dropTreeNode)) {
				return false;
			}
		}

		return true;
	}
}