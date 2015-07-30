package com.xirtam.data;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

import com.xirtam.common.NString;
import com.xirtam.ui.XWidget;
import com.xirtam.ui.widget.XLayout;
import com.xirtam.utils.IOUtils;
import com.xirtam.utils.StringUtils;
import com.xirtam.utils.UIUtlis;

public class TablePropertiesModel extends AbstractTableModel {

	private static final long serialVersionUID = 125331971932498728L;
	private int row, col;
	private String[] tableHeader = new String[] { NString.TITLE_NAME,
			NString.TITLE_VALUE };

	private static String modelType = NString.T_BUTTON;

	public void setModelType(String modelType) {
		TablePropertiesModel.modelType = modelType;
		initLeftData();
		fireTableStructureChanged();// 通知table结构改变
	}

	public void setData(JSONObject data) {
		if (data == null) {
			this.data.clear();
		} else {
			this.data.clear();
			for (int i = 0; i < list.size() - 1; i++) {
				try {
					String string = data.getString(list.get(i));
					this.data.add(string);
				} catch (Exception e) {
					this.data.add("");
				}
			}
		}
	}

	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> data = new ArrayList<String>();

	public TablePropertiesModel() {
		initLeftData();
	}

	private void initLeftData() {
		list.clear();
		data.clear();
		list.addAll(UIUtlis.addModel(modelType));
		col = 2;
		row = list.size() - 1;// 隐藏Json中最后的Children属性
	}

	@Override
	public int getColumnCount() {
		return col;
	}

	@Override
	public int getRowCount() {
		return row;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object o = null;
		if (data.size() > row)
			if (col == 0)// left data,key
				o = list.get(row);
			else if (col == 1) // right data ,value
				o = data.get(row);
		return o;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0 || rowIndex == 1 || rowIndex == 2) {// 设置不可修改
			return false;
		}
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		String text = value.toString();
		XWidget focusNode = UIUtlis.getFocusNode();
		if (focusNode == null)
			return;

		JSONObject jo = focusNode.getJsonData();// get json

		XWidget w = UIUtlis.getPreWidget(focusNode);
		String pLayout = w == null ? "" : ((XLayout) focusNode
				.getParentCPWidget()).getCPLayout();
		int preX = w == null ? 0 : NString.LAYOUT_H.equals(pLayout) ? (int) w
				.getLocation().getX() : 0;
		int preY = w == null ? 0 : NString.LAYOUT_V.equals(pLayout) ? (int) w
				.getLocation().getY() : 0;
		int preW = w == null ? 0 : NString.LAYOUT_H.equals(pLayout) ? (int) w
				.getWidth() : 0;
		int preH = w == null ? 0 : NString.LAYOUT_V.equals(pLayout) ? (int) w
				.getHeight() : 0;

		if (row == 0) {// name
			if (data.get(0) != null && !data.get(0).equals(text))// 排除和自己重复
				if (UIUtlis.checkSame(text)) {// 有重复name
					JOptionPane.showMessageDialog(UIUtlis.getRootFrame(),
							NString.NAME_DUPLICATED, NString.MODIFY_PROPERTIES,
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
		}
		if (list.get(row).equals(NString.K_BG_COLOR)) {// bgColor
			if (StringUtils.checkHexColor(text))// 判断是否十六进制颜色
			{
				Integer color = Integer.parseInt(text, 16);
				focusNode.setBackground(color);
			} else {
				return;
			}
		}
		if (list.get(row).equals(NString.K_FONT_COLOR)) {// font Color
			if (StringUtils.checkHexColor(text))// 判断是否十六进制颜色
			{
				Integer color = Integer.parseInt(text, 16);
				focusNode.setFontColor(color);
				;
			} else {
				return;
			}
		}
		if (list.get(row).equals(NString.K_BG_SRC)) {// bgSrc

			if (text.equals(NString.NULL) || text.equals(NString.SPACE)) {
				focusNode.setbgImg(null);
				focusNode.setBgSrc(text);
			} else {
				URL resource = this.getClass().getClassLoader()
						.getResource(text);
				if (resource != null && focusNode != null) {
					Image read = IOUtils.loadImg(resource.getFile());
					if (read == null)
						return;
					focusNode.setbgImg(read);
					focusNode.setBgSrc(text);
				} else {
					return;
				}
			}
		}
		if (list.get(row).equals(NString.K_IMAGE)) {// imageview

			if (text.equals(NString.NULL) || text.equals(NString.SPACE)) {
				focusNode.setImage(null);
				focusNode.setImageSrc(text);
			} else {
				URL resource = this.getClass().getClassLoader()
						.getResource(text);
				if (resource != null && focusNode != null) {
					Image read = IOUtils.loadImg(resource.getFile());
					if (read == null)
						return;
					focusNode.setImage(read);
					focusNode.setImageSrc(text);
				} else {
					return;
				}
			}
		}
		if (list.get(row).equals(NString.K_UNSEL_IMG)) {// un sel img

			if (text.equals(NString.NULL) || text.equals(NString.SPACE)) {
				focusNode.setUnSelImg(null);
				focusNode.setUnSelImgSrc(text);
			} else {
				URL resource = this.getClass().getClassLoader()
						.getResource(text);
				if (resource != null && focusNode != null) {
					Image read = IOUtils.loadImg(resource.getFile());
					if (read == null)
						return;
					focusNode.setUnSelImg(read);
					focusNode.setUnSelImgSrc(text);
				} else {
					return;
				}
			}
		}
		if (list.get(row).equals(NString.K_SEL_IMG)) {// sel img

			if (text.equals(NString.NULL) || text.equals(NString.SPACE)) {
				focusNode.setSelImg(null);
				focusNode.setSelImgSrc(text);
			} else {
				URL resource = this.getClass().getClassLoader()
						.getResource(text);
				if (resource != null && focusNode != null) {
					Image read = IOUtils.loadImg(resource.getFile());
					if (read == null)
						return;
					focusNode.setSelImg(read);
					focusNode.setSelImgSrc(text);
				} else {
					return;
				}
			}
		}
		if (list.get(row).equals(NString.K_MARGIN_BOTTOM)
				|| list.get(row).equals(NString.K_MARGIN_LEFT)
				|| list.get(row).equals(NString.K_MARGIN_RIGHT)
				|| list.get(row).equals(NString.K_MARGIN_TOP)
				|| list.get(row).equals(NString.K_W)
				|| list.get(row).equals(NString.K_H)
				|| list.get(row).equals(NString.K_FONT_HEIGHT)) {
			if (!text.matches(NString.REG_INT_NULL))
				return;
			if (text.equals(NString.NULL)) {
				jo.put(list.get(row), text);
			} else {
				String old = String.valueOf(jo.get(list.get(row)));
				int oldValue = NString.NULL.equals(old) ? 0 : (int) Float
						.parseFloat(old);
				int curValue = (int) Float.parseFloat(text);
				if (list.get(row).equals(NString.K_MARGIN_LEFT)) {// x
					UIUtlis.moveCPButton(focusNode, preX + preW + curValue
							- oldValue, preY + preH + 0);
				} else if (list.get(row).equals(NString.K_MARGIN_TOP)) {// y
					UIUtlis.moveCPButton(focusNode, preX + preW + 0, preY
							+ preH + curValue - oldValue);
				}
			}

		}
		if (!(list.get(row).equals(NString.K_MARGIN_BOTTOM)
				|| list.get(row).equals(NString.K_MARGIN_LEFT)
				|| list.get(row).equals(NString.K_MARGIN_RIGHT) || list
				.get(row).equals(NString.K_MARGIN_TOP))) {
			if (!focusNode.isDragable()) {
				UIUtlis.moveCPButton(focusNode, preX + preW, preY + preH);
			}
		}

		focusNode.onPropertiesChanged();
//		 if (!(list.get(row).equals(NString.K_MARGIN_BOTTOM)
//		 || list.get(row).equals(NString.K_MARGIN_LEFT)
//		 || list.get(row).equals(NString.K_MARGIN_RIGHT) || list
//		 .get(row).equals(NString.K_MARGIN_TOP))) {
		jo.put(list.get(row), text);// 直接更新属性
//		 }
		UIUtlis.getFocusNode().Json2UI(jo);// update node ui
		UIUtlis.updateProperties(jo);
		UIUtlis.updateTree();
		focusNode.packParent();
		this.fireTableCellUpdated(row, col);
	}

	@Override
	public String getColumnName(int column) {
		return tableHeader[column];
	}

}
