package com.xirtam.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class ColorPickerCombobox extends JComboBox {
	private static final long serialVersionUID = -458457106104406060L;
	public static final String SELECTEDCOLOR = "selectedcolor";

	public ColorPickerCombobox() {
		this.setEditable(false);
		this.setRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				ColorTextField textField = new ColorTextField();
				textField.setColor(getSelectedColor());
				return textField;
			}
		});
		this.setPreferredSize(new Dimension(150, 22));
	}

	public void updateUI() {
		this.setUI(new MetalDateComboBoxUI());
	}

	class MetalDateComboBoxUI extends MetalComboBoxUI {
		protected ComboPopup createPopup() {
			return new ColorPopup(comboBox);
		}
	}

	class ColorPopup extends BasicComboPopup implements PropertyChangeListener {
		private static final long serialVersionUID = -3854294365914175186L;
		private ColorPicker picker;

		public ColorPopup(JComboBox box) {
			super(box);
			picker = new ColorPicker();
			picker.addPropertyChangeListener(this);
			this.setLayout(new BorderLayout());
			this.add(picker, BorderLayout.CENTER);
			this.setBorder(BorderFactory.createEmptyBorder());
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName() == ColorPicker.SELECTEDCOLORCHANGE) {
				comboBox.putClientProperty(SELECTEDCOLOR,
						picker.getSelectedColor());
				comboBox.setPopupVisible(false);
			}
		}
	}

	public Color getSelectedColor() {
		Object obj = getClientProperty(SELECTEDCOLOR);
		if (obj != null && obj instanceof Color) {
			return (Color) obj;
		} else {
			return null;
		}
	}

	public void setSelectedColor(Color selectedColor) {
		putClientProperty(SELECTEDCOLOR, selectedColor);
	}
}
