package com.xirtam.common;

/**@formatter:off 此类不会被eclipse格式化代码
 * @author xirtam
 * @email 751348449@qq.com
 * 此类维护时，非必要情况不要修改字段顺序，新增字段请放在常量数组的倒数第二，childeren字段之前,因为程序会默认隐藏最后一个字段children
 * 如果需要修改字段顺序或者插入字段，可能需要同时修改{@com.xirtam.data.TablePropertiesModel.java 中setvalueAt方法，
 * 和{@com.cloudpower.ui.CPLauncher.java}中下拉相关配置}
 */
public class WidgetProperties {

	public static final String[] CONFIG_BUTTON_PROPERTIES = new String[] {
			NString.K_NAME,
			NString.K_TYPE,
			NString.K_PARENT,
			NString.K_ALIGN,
			NString.K_MARGIN_LEFT,
			NString.K_MARGIN_TOP,
			NString.K_MARGIN_RIGHT,
			NString.K_MARGIN_BOTTOM,
			NString.K_W,
			NString.K_H,
			NString.K_TEXT,
			NString.K_FONT_HEIGHT ,
			NString.K_FONT_COLOR ,
			NString.K_UNSEL_IMG,
			NString.K_SEL_IMG,
			NString.K_CHILDREN
	};
	
	public static final String[] CONFIG_LAYOUT_PROPERTIES = new String[] {
			NString.K_NAME,
			NString.K_TYPE,
			NString.K_PARENT,
			NString.K_BG_COLOR,
			NString.K_BG_SRC,
//			NString.K_ALIGN,
			NString.K_MARGIN_LEFT,
			NString.K_MARGIN_TOP,
			NString.K_MARGIN_RIGHT,
			NString.K_MARGIN_BOTTOM,
			NString.K_W,
			NString.K_H,
			NString.K_LAYOUT,
			NString.K_CHILDREN
	};
	
	public static final String[] CONFIG_LABEL_PROPERTIES = new String[] {
			NString.K_NAME,
			NString.K_TYPE,
			NString.K_PARENT,
			NString.K_BG_COLOR,
			NString.K_BG_SRC,
//			NString.K_ALIGN,
			NString.K_MARGIN_LEFT,
			NString.K_MARGIN_TOP,
			NString.K_MARGIN_RIGHT,
			NString.K_MARGIN_BOTTOM,
			NString.K_W,
			NString.K_H,
			NString.K_TEXT,
			NString.K_FONT_HEIGHT,
			NString.K_FONT_COLOR,
			NString.K_TEXT_ALIGN,
			NString.K_IS_MULTIINE,
			NString.K_CHILDREN
	};
	
	public static final String[] CONFIG_IMAGE_PROPERTIES = new String[] {
			NString.K_NAME,
			NString.K_TYPE,
			NString.K_PARENT,
//			NString.K_ALIGN,
			NString.K_MARGIN_LEFT,
			NString.K_MARGIN_TOP,
			NString.K_MARGIN_RIGHT,
			NString.K_MARGIN_BOTTOM,
			NString.K_W,
			NString.K_H,
			NString.K_IMAGE,
			NString.K_IS_STRETCH,
			NString.K_IS_ALL_PATH,
			NString.K_CHILDREN

	};
	
	public static final String[] CONFIG_CHECKBOX_PROPERTIES = new String[] {
			NString.K_NAME,
			NString.K_TYPE,
			NString.K_PARENT,
//			NString.K_ALIGN,
			NString.K_MARGIN_LEFT,
			NString.K_MARGIN_TOP,
			NString.K_MARGIN_RIGHT,
			NString.K_MARGIN_BOTTOM,
			NString.K_W,
			NString.K_H,
			NString.K_IS_CLICK,
			NString.K_UNSEL_IMG,
			NString.K_SEL_IMG,
			NString.K_CHILDREN
	};
}
