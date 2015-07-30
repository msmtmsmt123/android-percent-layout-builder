package com.xirtam.ui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.xirtam.utils.UIUtlis;

public class XTable extends JTable {

	private static final long serialVersionUID = -976275145650982788L;

	public XTable(TableModel dm) {
		super(dm);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		if (UIUtlis.getFocusNode() != null
				&& UIUtlis.isSelInTable(UIUtlis.getFocusNodeProperties().get(
						row)))
			return UIUtlis.getEditor(UIUtlis.getFocusNodeProperties().get(row));
		return super.getCellEditor(row, column);
	}
}
