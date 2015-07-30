package com.xirtam.utils;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import com.xirtam.ui.XWidget;

public class TreePrinter {
	public static void PrintCPNodeTree() {
		DefaultMutableTreeNode cpRootNode = UIUtlis.getRootTreeNode();
		System.out.println("-----start to print tree-------");
		if (cpRootNode != null)
			printTree(cpRootNode, 0);
		else
			System.out.println("root is null,print your sister!!!");
		System.out.println("------print tree end-----------");
	}

	@SuppressWarnings("unchecked")
	private static void printTree(DefaultMutableTreeNode cpNode, int tabCount) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tabCount; i++) {
			builder.append("    ");
		}
		// CPNode userObject = (CPNode) cpNode.getUserObject();
		// System.out.println(builder.toString() + userObject.getText());
		Enumeration<DefaultMutableTreeNode> e = cpNode
				.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			XWidget userObject = (XWidget) node.getUserObject();
			System.out.println(builder.toString() + userObject.getType());
		}
	}
}
