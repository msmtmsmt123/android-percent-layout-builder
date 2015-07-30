package com.xirtam.data;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

/** Drop data Transferable */
public class TreeNodeTransfer implements Transferable {
	private DefaultMutableTreeNode treeNode;
	// DataFlavor
	public final static DataFlavor TREENODE_FLAVOR = new DataFlavor(
			DefaultMutableTreeNode.class, "TreeNode instance");
	public DataFlavor[] flavors = new DataFlavor[] { TREENODE_FLAVOR };

	public TreeNodeTransfer(DefaultMutableTreeNode treeNode) {
		this.treeNode = treeNode;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor df : flavors) {
			if (df.equals(flavor)) {
				return true;
			}
		}
		return false;
	}

	public Object getTransferData(DataFlavor df)
			throws UnsupportedFlavorException, IOException {
		if (df.equals(TREENODE_FLAVOR)) {
			return treeNode;
		}
		throw new UnsupportedFlavorException(df);
	}
}
