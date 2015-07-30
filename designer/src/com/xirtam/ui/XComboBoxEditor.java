package com.xirtam.ui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class XComboBoxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 8381533035798283754L;

	public XComboBoxEditor(String[] items) {
		super(new JComboBox(items));
	}

	public Component getC() {
		return getComponent();
	}

}