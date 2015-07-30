package com.xirtam.common;

public class NString {
    // application
    public static final String TITLE = "Percent Layout Designer";
    public static final String JSON = "xml";
    public static final String DATA_END = ".json";
    public static final String DATA_NAME = "*.json";
    public static final String JSON_FILE = "json file";
    public static final String LOOK_AND_FEEL = "Nimbus";
    public static final String COMPANY_HOME_PAGE = "http://www.lovegsm.com/";

    // Toolbar
    public static final String FILE = "File";
    public static final String OPEN = "Open(O)";
    public static final String COPY = "Copy(C)";
    public static final String PASTE = "Paste(V)";
    public static final String DELETE = "Delete(D)";
    public static final String PREFERENCES = "Preferences";
    public static final String CONTACT = "Contact us";
    public static final String CLEAR_PRO = "Clear(R)";
    public static final String EXPORT_JSON_DATA = "Save(S)";
    public static final String EDIT = "Edit";
    public static final String OPTION = "Option";
    public static final String ABOUT = "About";

    // UI
    public static final String ROOTNODE_NAME = "Application Window";
    public static final String DESIGN = "design";
    public static final String SAVE = "保存";
    public static final String TITLE_NAME = "Name";
    public static final String TITLE_VALUE = "Value";
    public static final String LBL_WC = "widget count:";
    public static final String LBL_MOUSE = "mouse:";
    public static final String LBL_COPY_NAME = "copy:";
    public static final String LBL_SHIFT = "shift:";

    // Widget Type
    public static final String T_BUTTON = "Button";
    public static final String T_LABEL = "Custom View";
    public static final String T_IMAGEVIEW = "ImageView";
    public static final String T_ListView = "ListView";
    public static final String T_LAYOUT = "Layout";

    // notice
    public static final String CLEAR_THE_PROJECT = "clear the project?";
    public static final String WARNING = "warning";
    public static final String FILE_EXIST = "The file already exists, overwrite?";
    public static final String FILE_EXIST_TITLE = "File exists";
    public static final String EXPORT_RESULT_SUCCESS = "Export successed";
    public static final String EXPORT_RESULT_FAILED = "Export failed";
    public static final String EXPORT_RESULT = "Export result";
    public static final String NOT_VALIED_JSON = "not valied json";
    public static final String OPEN_RESULT = "open result";
    public static final String NAME_DUPLICATED = "this name was uesd";
    public static final String MODIFY_PROPERTIES = "Modify properties";
    public static final String ERR_DROP = "you can only drop into Layout";

    // keys
    public static final String K_BG_COLOR = "BackgroundColor";
    public static final String K_NAME = "name";
    public static final String K_PARENT = "parent";
    public static final String K_LAYOUT = "layout";
    public static final String K_ALIGN = "align";
    public static final String K_MARGIN_LEFT = "marginLeft";
    public static final String K_MARGIN_TOP = "marginTop";
    public static final String K_MARGIN_RIGHT = "marginRight";
    public static final String K_MARGIN_BOTTOM = "marginBottom";
    public static final String K_W = "width";
    public static final String K_H = "height";
    public static final String K_CHILDREN = "children";
    public static final String K_TYPE = "type";
    public static final String K_BG_SRC = "backgroundImage";
    public static final String K_TEXT = "text";
    public static final String K_FONT_HEIGHT = "fontHeight";
    public static final String K_FONT_COLOR = "fontColor";
    public static final String K_UNSEL_IMG = "unselImage";
    public static final String K_SEL_IMG = "selImage";
    public static final String K_IS_MULTIINE = "isMultLine";
    public static final String K_TEXT_ALIGN = "textAlign";
    public static final String K_IS_CLICK = "isClick";
    public static final String K_IS_STRETCH = "isStretch";
    public static final String K_IS_ALL_PATH = "isAllPall";
    public static final String K_IMAGE = "image";

    // preference
    public static final String DEBUG = "Debug";

    // common
    public static final String NULL = "null";
    public static final String SPACE = "";

    public static final String LAYOUT_H = "horizontal";
    public static final String LAYOUT_V = "Vertial";

    public static final String ALIGN_LC = "leftCenter";
    public static final String ALIGN_RC = "rightCenter";
    public static final String ALIGN_TC = "topCenter";
    public static final String ALIGN_BC = "bottomCenter";
    public static final String ALIGN_LT = "leftTop";
    public static final String ALIGN_LB = "leftBottom";
    public static final String ALIGN_RT = "rightTop";
    public static final String ALIGN_RB = "rightBottom";
    public static final String ALIGN_CENTER = "center";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String[] types = new String[]{NString.T_BUTTON,
            NString.T_ListView, "galleryView", NString.T_IMAGEVIEW,
            NString.T_LABEL, "slideView", "textField", "webImage"};

    public static final String[] VALUES_LAYOUT = new String[]{
            NString.LAYOUT_H, NString.LAYOUT_V, NString.NULL};

    public static final String[] VALUES_ALIGN = new String[]{ALIGN_LC,
            ALIGN_RC, ALIGN_TC, ALIGN_BC, ALIGN_LT, ALIGN_LB, ALIGN_RT,
            ALIGN_RB, ALIGN_CENTER, NString.NULL};

    public static final String[] VALUES_TRUE_OR_FALSE = new String[]{
            NString.TRUE, NString.FALSE};

    // 正则判断正负自然数或者null
    public static final String REG_INT_NULL = "-?\\d+\\.*\\d*|null";

    public static final String TEST_MOUSE = "mouse press test";

}
