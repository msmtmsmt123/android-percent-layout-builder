package com.xirtam.data;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.ui.widget.XLayout;
import com.xirtam.utils.XNodeFactory;
import com.xirtam.utils.UIUtlis;

public class AddIntoTree {
	private JTree tree;
	private TreePath draggedPath, dropPath;

	// private Point origin;
	// private int insertionRow;

	public AddIntoTree(JTree tree) {
		this.tree = tree;
	}

	public void startDrag(Point where) {
		this.draggedPath = this.tree.getPathForLocation(where.x, where.y);
		// if ((this.draggedPath != null) && (canMove(this.draggedPath))) {
		// this.dragActive = true;
		// this.tree.collapsePath(this.draggedPath);// 确保折叠
		// this.origin = where;
		// this.insertionRow = this.tree.getRowForPath(this.draggedPath);
		// this.dragImage = new GhostedDragImage(this.draggedPath,
		// this.origin);
		// }
	}

	private boolean canMove() {
		if ((this.draggedPath != null) && (this.dropPath != null)) {
			// 放置目标是拖动源则退出
			if (draggedPath == dropPath) {
				return false;
			}

			String strDragPath = draggedPath.toString();
			String strDropPath = dropPath.toString();
			String subDragPath = strDragPath.substring(0,
					strDragPath.length() - 1);

			DefaultMutableTreeNode dragTreeNode = (DefaultMutableTreeNode) draggedPath
					.getLastPathComponent();
			DefaultMutableTreeNode dropTreeNode = (DefaultMutableTreeNode) dropPath
					.getLastPathComponent();
			XWidget node1 = (XWidget) dragTreeNode.getUserObject();
			XWidget node2 = (XWidget) dropTreeNode.getUserObject();

			if (!(node2 instanceof XLayout)) {
				EventQueue.invokeLater(new XMessage(NString.ERR_DROP, 2000));
				return false;
			}
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

			if (draggedPath.getParentPath() != null) {
				if (draggedPath.getParentPath().toString().equals(strDropPath)) {// 放置目标是拖动源的父节点
					return false;
				}

				// 放置目标是拖动源的父节点则退出
				if (dragTreeNode.getParent().equals(dropTreeNode)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void endDrag(Point where) {
		this.dropPath = this.tree.getPathForLocation(where.x, where.y);
		if ((this.draggedPath != null) && (this.dropPath != null)
				&& (canMove())) {
			DefaultMutableTreeNode drag = (DefaultMutableTreeNode) draggedPath
					.getLastPathComponent();
			DefaultMutableTreeNode drop = (DefaultMutableTreeNode) dropPath
					.getLastPathComponent();
			// CPLauncher.launcher.defaultTreeModel.insertNodeInto(drag, drop,
			// 0);
			drag.removeFromParent();
			drop.add(drag);
			XWidget dragButton = (XWidget) drag.getUserObject();
			XWidget dropButton = (XWidget) drop.getUserObject();
			XNodeFactory.addInto(dragButton, dropButton);
			tree.updateUI();
			dragButton.packParent();
			UIUtlis.refreshAllJsonNodeData();

		}
	}
}
