package com.xirtam.ui;

import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;
import org.json.JSONArray;

import com.xirtam.common.Config;
import com.xirtam.common.NString;
import com.xirtam.data.AddIntoTree;
import com.xirtam.data.AnimatorTree;
import com.xirtam.data.XMessage;
import com.xirtam.data.TableComboCell;
import com.xirtam.data.TablePropertiesModel;
import com.xirtam.data.TreeNodeTransfer;
import com.xirtam.listener.AnimatorTreeListener;
import com.xirtam.ui.widget.XButton;
import com.xirtam.utils.XNodeFactory;
import com.xirtam.utils.IOUtils;
import com.xirtam.utils.UIUtlis;

public class Launcher extends KeyAdapter implements MouseListener,
        ActionListener {

    public JFrame rootFrame;
    public XTable table;
    private JRadioButton rdbtnDesign;
    private JRadioButton rdbtnJson;
    private JButton btnButton;
    private JButton btnImage;
    private JButton btnLabel;
    public JTree tree;
    public TablePropertiesModel tabModel;
    private JButton btnPanel;
    public static Launcher launcher;
    private DefaultMutableTreeNode dragTreeNode = null;
    public JScrollPane propertiesPanel;
    public JScrollPane treePanel;
    private JMenuItem itemEJD;
    public JPanel windowPanel;
    public DefaultTreeModel defaultTreeModel;
    private JPanel panel_1;

    private JMenuItem itemOpen;
    private JScrollPane textPanel;
    public JTextArea textArea;
    public JLabel lbl_wc, lbl_mouse, lbl_shift, lblCopyName;
    private JButton btnLayout;
    public DefaultMutableTreeNode windowTreenode;
    private JMenuItem itemCopy, itemPaste;
    private JMenuItem itemDelete;
    private JMenuItem itemClean;
    private JMenuItem itemPreferences;
    private JMenuItem itemContact;
    private Vector<Character> egg;
    public Container container;
    public Vector<XMessage> messages = new Vector<XMessage>();
    private JMenuItem itemOpenImg;
    private Image bgImg;

    // public CPTreeNode rootTreeNode;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    launcher = new Launcher();
                    launcher.rootFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Launcher() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        initLookAndFeel();
        initRootFrame();
        initMenu();
        initMainUI();
        initTree();
        initRadio();
        initTable();
        initTableCell();
        initChildrenUI();
        initDebugInfo();

        btnLayout.addMouseListener(this);
        rootFrame.addKeyListener(this);
        rootFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                rootFrame.requestFocus();
            }
        });
        egg = new Vector<Character>();
    }

    private void initDebugInfo() {
        lbl_wc = new JLabel(NString.LBL_WC);
        lbl_wc.setBounds(6, 202, 116, 15);
        lbl_wc.setText(NString.LBL_WC + " " + 0);
        panel_1.add(lbl_wc);

        lbl_mouse = new JLabel(NString.LBL_MOUSE);
        lbl_mouse.setBounds(6, 227, 116, 15);
        panel_1.add(lbl_mouse);

        lbl_shift = new JLabel(NString.LBL_SHIFT);
        lbl_shift.setBounds(6, 252, 116, 15);
        lbl_shift.setText(NString.LBL_SHIFT + " " + false);
        panel_1.add(lbl_shift);

        lblCopyName = new JLabel(NString.LBL_COPY_NAME);
        lblCopyName.setBounds(6, 177, 116, 15);
        panel_1.add(lblCopyName);
        if (!Config.debug) {
            lbl_wc.setVisible(false);
            lbl_mouse.setVisible(false);
            lbl_shift.setVisible(false);
            lblCopyName.setVisible(false);
        }
    }

    private void initChildrenUI() {
        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(Config.DEFAULT_INNER_COLOR));
        panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_2.setBounds(10, 7, 132, 195);
        container.add(panel_2);
        panel_2.setLayout(null);

        btnButton = new JButton(NString.T_BUTTON);
        btnButton.setBounds(10, 10, 104, 30);
        panel_2.add(btnButton);

        btnLabel = new JButton(NString.T_LABEL);
        btnLabel.setBounds(10, 50, 104, 30);
        panel_2.add(btnLabel);

        btnImage = new JButton(NString.T_IMAGEVIEW);
        btnImage.setBounds(10, 90, 104, 30);
        panel_2.add(btnImage);

        btnPanel = new JButton(NString.T_ListView);
        btnPanel.setBounds(10, 130, 104, 30);
        panel_2.add(btnPanel);

        btnPanel.addMouseListener(this);
        btnImage.addMouseListener(this);
        btnLabel.addMouseListener(this);
        btnButton.addMouseListener(this);

        panel_1 = new JPanel();
        panel_1.setBackground(new Color(Config.DEFAULT_INNER_COLOR));
        panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_1.setBounds(10, 206, 132, 281);
        container.add(panel_1);
        panel_1.setLayout(null);

        btnLayout = new JButton(NString.T_LAYOUT);
        btnLayout.setBounds(10, 10, 104, 30);
        panel_1.add(btnLayout);

    }

    private void initTableCell() {
        // 设置需要下拉的列
        UIUtlis.putEditorData(NString.K_LAYOUT, new TableComboCell(
                NString.VALUES_LAYOUT));
        UIUtlis.putEditorData(NString.K_ALIGN, new TableComboCell(
                NString.VALUES_ALIGN));
        UIUtlis.putEditorData(NString.K_IS_ALL_PATH, new TableComboCell(
                NString.VALUES_TRUE_OR_FALSE));
        UIUtlis.putEditorData(NString.K_IS_CLICK, new TableComboCell(
                NString.VALUES_TRUE_OR_FALSE));
        UIUtlis.putEditorData(NString.K_IS_MULTIINE, new TableComboCell(
                NString.VALUES_TRUE_OR_FALSE));
        UIUtlis.putEditorData(NString.K_IS_STRETCH, new TableComboCell(
                NString.VALUES_TRUE_OR_FALSE));
    }

    private void initTable() {
        tabModel = new TablePropertiesModel();
        table = new XTable(tabModel);
        table.putClientProperty("terminateEditOnFocusLost", true);

        propertiesPanel = new JScrollPane(table);
        propertiesPanel.setBounds(532, 275, 252, 241);
        container.add(propertiesPanel);
        // tabbedPane_1.addTab("Properties", null, table, null);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.addKeyListener(this);
    }

    private void initRadio() {
        rdbtnJson = new JRadioButton(NString.XML);
        rdbtnJson.setOpaque(false);
        rdbtnJson.setBounds(157, 493, 80, 23);
        container.add(rdbtnJson);
        rdbtnJson.addActionListener(this);
        rdbtnJson.addKeyListener(this);

        rdbtnDesign = new JRadioButton(NString.DESIGN);
        rdbtnDesign.setOpaque(false);
        rdbtnDesign.setBounds(239, 493, 80, 23);
        rdbtnDesign.setSelected(true);
        container.add(rdbtnDesign);
        rdbtnDesign.addActionListener(this);
        rdbtnDesign.addKeyListener(this);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnJson);
        bg.add(rdbtnDesign);
    }

    private void initTree() {
        XWidget button = new XButton();
        button.setType(NString.ROOTNODE_NAME);
        button.setBounds(0, 0, 320, 480);
        UIUtlis.saveMappingIB(button.getId(), button);

        windowTreenode = new DefaultMutableTreeNode(button);
        defaultTreeModel = new DefaultTreeModel(windowTreenode);

        UIUtlis.setTreeModel(defaultTreeModel);
        UIUtlis.setRootWidget(button);
        UIUtlis.setRootTreeNode(windowTreenode);
        tree = new JTree(defaultTreeModel);
        tree.addKeyListener(this);
        tree.addMouseListener(this);
        treePanel = new JScrollPane(tree);
        treePanel.setBounds(532, 7, 252, 255);
        container.add(treePanel);
        tree.setShowsRootHandles(true);
        tree.setModel(defaultTreeModel);
        tree.setBorder(new LineBorder(new Color(0, 0, 0)));

        // kinds of tree
        AnimatorTree animator = new AnimatorTree(tree);
        AddIntoTree addIntoTree = new AddIntoTree(tree);
        AnimatorTreeListener listener = new AnimatorTreeListener(animator,
                addIntoTree);
        tree.addMouseListener(listener);
        tree.addMouseMotionListener(listener);

        // tree.setDragEnabled(true);
        // DragSource dragSource = DragSource.getDefaultDragSource();
        // dragSource.createDefaultDragGestureRecognizer(tree,
        // DnDConstants.ACTION_MOVE, new DragAndDropDragGestureListener());
        // new DropTarget(tree, new DragAndDropDropTargetListener());
    }

    private void initMainUI() {

        textArea = new JTextArea();
        textArea.setEditable(false);
        // textArea.setLineWrap(true);
        textArea.setBounds(Config.PHONE_X, Config.PHONE_Y, Config.PHONE_W,
                Config.PHONE_H);
        textArea.addKeyListener(this);

        textPanel = new JScrollPane(textArea);
        textPanel.setBounds(175, 7, Config.PHONE_W, Config.PHONE_H);
        textPanel.setVisible(false);
        container.add(textPanel);

        windowPanel = new JPanel() {

            private static final long serialVersionUID = -5624517811276597686L;

            private Color backColor = new Color(0x99000000, true);
            private Color textColor = new Color(0xffffff);

            // 黑框提示
            public void paint(java.awt.Graphics g) {
                super.paint(g);
                if (bgImg != null) {
                    g.drawImage(bgImg, 0, 0, null);
                }
                if (messages.size() > 0) {
                    g.setColor(backColor);
                    int range = 5;
                    g.fillRoundRect(range, 380, 320 - range * 2, 30, 20, 20);
                    g.setColor(textColor);
                    String str = messages.get(0).toString();
                    g.drawString(str, (320 - range * 2 - str.length()
                            * g.getFont().getSize() / 2) / 2, 400);
                    if (System.currentTimeMillis()
                            - messages.get(0).getTimeStamp() > messages.get(0)
                            .getShowTime()) {
                        messages.remove(0);
                        repaint();
                    } else {
                        repaint();
                    }
                }
            }

            ;

        };
        windowPanel.setBackground(Color.white);
        windowPanel
                .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        windowPanel.setBounds(175, 7, Config.PHONE_W, Config.PHONE_H);
        // windowPanel.addMouseMotionListener(new MouseMotionListener() {
        //
        // @Override
        // public void mouseMoved(MouseEvent e) {
        // UIUtlis.setMousePosition(e.getX(), e.getY());
        // }
        //
        // @Override
        // public void mouseDragged(MouseEvent e) {
        //
        // }
        // });
        container.add(windowPanel);
        windowPanel.setLayout(null);
    }

    private void initRootFrame() {
        rootFrame = new JFrame();
        container = new Container() {
            private static final long serialVersionUID = -1155531814665589313L;
            private Color outColor = new Color(Config.DEFAULT_OUTER_COLOR);

            @Override
            public void paint(Graphics g) {
                g.setColor(outColor);
                g.fillRect(0, 0, 800, 600);
                super.paint(g);
            }
        };
        rootFrame.setContentPane(container);
        rootFrame.addMouseListener(this);
        rootFrame.setTitle(NString.TITLE);
        rootFrame.setBounds(100, 100, 800, 600);
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootFrame.setResizable(false);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        rootFrame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu(NString.FILE);
        menuBar.add(mnFile);

        itemOpen = new JMenuItem(NString.OPEN);
        itemOpen.addActionListener(this);
        mnFile.add(itemOpen);

        itemOpenImg = new JMenuItem(NString.OPEN_IMG);
        itemOpenImg.addActionListener(this);
        mnFile.add(itemOpenImg);

        itemEJD = new JMenuItem(NString.EXPORT_JSON_DATA);
        itemEJD.addActionListener(this);
        mnFile.add(itemEJD);

        JMenu mnEdit = new JMenu(NString.EDIT);
        menuBar.add(mnEdit);

        itemCopy = new JMenuItem(NString.COPY);
        itemCopy.addActionListener(this);
        mnEdit.add(itemCopy);

        itemPaste = new JMenuItem(NString.PASTE);
        itemPaste.addActionListener(this);
        mnEdit.add(itemPaste);

        itemDelete = new JMenuItem(NString.DELETE);
        itemDelete.addActionListener(this);
        mnEdit.add(itemDelete);

        itemClean = new JMenuItem(NString.CLEAR_PRO);
        itemClean.addActionListener(this);
        mnEdit.add(itemClean);

        JMenu mnOption = new JMenu(NString.OPTION);
        menuBar.add(mnOption);
        itemPreferences = new JMenuItem(NString.PREFERENCES);
        itemPreferences.addActionListener(this);
        mnOption.add(itemPreferences);

        JMenu mnAbout = new JMenu(NString.ABOUT);
        menuBar.add(mnAbout);
        container.setLayout(null);

        itemContact = new JMenuItem(NString.CONTACT);
        itemContact.addActionListener(this);
        mnAbout.add(itemContact);
    }

    private void initLookAndFeel() {
        // Nimbus style
        try {
            // for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            // {
            // if (NString.LOOK_AND_FEEL.equals(info.getName())) {
            // UIManager.setLookAndFeel(info.getClassName());
            // break;
            // }
            // }

            BeautyEyeLNFHelper.frameBorderStyle = FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);// 去掉设置按钮
        } catch (Exception e) {
            // If Nimbus is not available
            // you can set the GUI to another look and feel.
            // Metal,Nimbus,CDE/Motif,Windows,Windows Classic
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        rootFrame.requestFocus();
        int hashCode = e.getSource().hashCode();
        if (hashCode == btnButton.hashCode()) {
            XNodeFactory.generateButton(NString.T_BUTTON, Config.GEN_BUTTON_W,
                    Config.GEN_BUTTON_H);
        } else if (hashCode == btnLabel.hashCode()) {
            XNodeFactory.generateButton(NString.T_LABEL, Config.GEN_LABEL_W,
                    Config.GEN_LABEL_H);
        } else if (hashCode == btnImage.hashCode()) {
            XNodeFactory.generateButton(NString.T_IMAGEVIEW,
                    Config.GEN_IMAGE_W, Config.GEN_IMAGE_H);
        } else if (hashCode == btnPanel.hashCode()) {
            XNodeFactory.generateButton(NString.T_ListView,
                    Config.GEN_LABEL_W, Config.GEN_LABEL_H);
        } else if (hashCode == btnLayout.hashCode()) {
            XNodeFactory.generateButton(NString.T_LAYOUT, Config.GEN_LAYOUT_W,
                    Config.GEN_LAYOUT_H);
        } else if (hashCode == tree.hashCode()) {
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                if (UIUtlis
                        .isRootName(((XWidget) ((DefaultMutableTreeNode) path
                                .getLastPathComponent()).getUserObject())
                                .getType())) {
                    // 点击root节点
                } else {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                            .getLastPathComponent();
                    XWidget cpButton = (XWidget) node.getUserObject();
                    UIUtlis.setFocusNode(cpButton);
                    UIUtlis.updateProperties(cpButton.getJsonData());
                    UIUtlis.refreshProperties();
                    UIUtlis.refreshWindow();
                }
            } else {
                lostFocus();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int hashCode = e.getSource().hashCode();
        if (hashCode == rootFrame.hashCode()) {// 点击任意空白或非以上if中判断过的控件
            if (table.isEditing())
                table.getCellEditor().stopCellEditing();
            lostFocus();
        }
    }

    /**
     * 获取拖动的树节点
     *
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode getDragTreeNode() {
        return dragTreeNode;
    }

    /**
     * 设置拖动的树节点
     */
    public void setDragTreeNode(DefaultMutableTreeNode dragTreeNode) {
        this.dragTreeNode = dragTreeNode;
    }

    /**
     * 获取树
     *
     * @return JTree
     */
    public JTree getTree() {
        return tree;
    }

    // 因为要JTree既为拖动源又是放置目标，所以把DragGestureListener作为一个内部类比较好
    class DragAndDropDragGestureListener implements DragGestureListener {
        public void dragGestureRecognized(DragGestureEvent dge) {
            JTree tree = (JTree) dge.getComponent();
            TreePath path = tree.getSelectionPath();
            if (path != null) {
                dragTreeNode = (DefaultMutableTreeNode) path
                        .getLastPathComponent();
                if (dragTreeNode != null) {
                    TreeNodeTransfer dragAndDropTransferable = new TreeNodeTransfer(
                            dragTreeNode);
                    // 启动拖动操作，dragAndDropTransferable为封装移动、复制或连接的数据
                    // DragAndDropDragSourceListener实例十跟踪操作进程和完成操作启动者的任务
                    dge.startDrag(DragSource.DefaultMoveDrop,
                            dragAndDropTransferable,
                            new DragAndDropDragSourceListener());
                }
            }
        }
    }

    // 因为要JTree既为拖动源又是放置目标，所以把DragGestureListener作为一个内部类比较好
    class DragAndDropDragSourceListener implements DragSourceListener {

        // dragEnter(),dragExit(),dragOver(),dropActionChanged()这几个方法只有在调用放置目标监听器中
        // 的对应方法并且防止目标不拒绝操作后，才调用这个拖动源的方法。

        /**
         * 在光标进入放置组件的显示区时调用
         *
         * @param e DragSourceDragEvent
         */
        public void dragEnter(DragSourceDragEvent e) {
            // 设置光标
            DragSourceContext context = e.getDragSourceContext();
            int dropAction = e.getDropAction();
            if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
                context.setCursor(DragSource.DefaultCopyDrop);
            } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
                context.setCursor(DragSource.DefaultCopyDrop);
            } else {
                context.setCursor(DragSource.DefaultCopyNoDrop);
            }
        }

        /**
         * 在光标退出放置组件的显示区时发生
         *
         * @param e DragSourceEvent
         */
        public void dragExit(DragSourceEvent e) {
        }

        /**
         * 在光标进入放置组件显示区之后移动时调用
         *
         * @param e DragSourceDragEvent
         */
        public void dragOver(DragSourceDragEvent e) {
        }

        /**
         * 选择放置操作的修饰键的状态变化
         *
         * @param e DragSourceDragEvent
         */
        public void dropActionChanged(DragSourceDragEvent e) {

        }

        /**
         * 放置发生并调用DropTargetListener的drop()方法后，调用dragDropEnd()方法， 告诉拖动源放置已经完成
         *
         * @param e DragSourceDropEvent
         */
        public void dragDropEnd(DragSourceDropEvent e) {
            // getDropSuccess()对应DropTargetListener的drop()方法调用dropcomplete()时指目标指定的值
            // getDropAction()对应DropTargetListener的drop()方法调用acceptDrop()时指定的操作
            if (!e.getDropSuccess()
                    || e.getDropAction() != DnDConstants.ACTION_MOVE) {
                return;
            }

            DragSourceContext context = e.getDragSourceContext();
            Object comp = context.getComponent();
            if (comp == null || !(comp instanceof JTree)) {
                return;
            }
            DefaultMutableTreeNode dragTreeNode = getDragTreeNode();

            if (dragTreeNode != null) {
                ((DefaultTreeModel) tree.getModel())
                        .removeNodeFromParent(dragTreeNode);
                // UITools.removeTreeNode(UITools.getCPNodeByHashCode(dragTreeNode
                // .getUserObject().hashCode()));
                // 设置拖动的树节点为空
                setDragTreeNode(null);
                int id = ((XWidget) dragTreeNode.getUserObject()).getId();
                XWidget button = UIUtlis.getButtonById(id);
                button.onPropertiesChanged();
                UIUtlis.updateProperties(button.getJsonData());
                UIUtlis.refreshProperties();

            }

            // UITools.selectTreeNode(((CPNode)dragTreeNode.getUserObject()).getId());
            UIUtlis.refreshAllJsonNodeData();
        }
    }

    /**
     * 失去焦点时调用，清空focusNode相关显示数据
     */
    private void lostFocus() {
        UIUtlis.setFocusNode(null);
        UIUtlis.selectTreeNode(0);
        UIUtlis.updateProperties(null);
        UIUtlis.refreshProperties();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
            XNodeFactory.saveCopy();
        } else if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
            XNodeFactory.copyButton();
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE
                || (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown())) {
            XNodeFactory.destoryButton(UIUtlis.getFocusNode());
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT
                && !UIUtlis.isShiftPressing) {
            UIUtlis.isShiftPressing = true;
            UIUtlis.setIsShiftPress(true);
            rootFrame.setCursor(DragSource.DefaultCopyDrop);
        } else if (e.getKeyCode() == KeyEvent.VK_O && e.isControlDown()) {
            showOpen();
        } else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            showSave();
        } else if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
            showClear();
        } else if (e.getKeyCode() == KeyEvent.VK_I && e.isControlDown()) {
            showOpenImg();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        egg.add(e.getKeyChar());
        StringBuilder sb = new StringBuilder();
        if (egg.size() >= 6)
            for (int i = egg.size() - 1; i > egg.size() - 7; i--) {
                sb.append(egg.get(i));
            }
        try {
            if (sb.toString().equals(
                    new String(new byte[]{109, 97, 116, 114, 105, 120},
                            "utf-8"))) {
                JOptionPane.showMessageDialog(rootFrame,
                        Integer.parseInt("2cc8aae1", 16)
                                + new String(new byte[]{64, 113, 113, 46, 99,
                                111, 109,}), new String(new byte[]{
                                101, 109, 97, 105, 108,}),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        super.keyTyped(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT && UIUtlis.isShiftPressing) {
            UIUtlis.isShiftPressing = false;
            UIUtlis.setIsShiftPress(false);
            rootFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int hashCode = ae.getSource().hashCode();
        if (hashCode == rdbtnJson.hashCode()) {
            textPanel.setVisible(true);
            windowPanel.setVisible(false);
            UIUtlis.showJsonText();
        } else if (hashCode == rdbtnDesign.hashCode()) {
            textPanel.setVisible(false);
            windowPanel.setVisible(true);
        } else if (hashCode == itemEJD.hashCode()) {
            showSave();
        } else if (hashCode == itemOpen.hashCode()) {
            showOpen();
        } else if (hashCode == itemOpenImg.hashCode()) {
            showOpenImg();
        } else if (hashCode == itemCopy.hashCode()) {
            XNodeFactory.saveCopy();
        } else if (hashCode == itemPaste.hashCode()) {
            XNodeFactory.copyButton();
        } else if (hashCode == itemDelete.hashCode()) {
            XNodeFactory.destoryButton(UIUtlis.getFocusNode());
        } else if (hashCode == itemClean.hashCode()) {
            showClear();
        } else if (hashCode == itemPreferences.hashCode()) {
            showPreferences();
        } else if (hashCode == itemContact.hashCode()) {
            URI uri;
            try {
                uri = new java.net.URI(NString.COMPANY_HOME_PAGE);
                java.awt.Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPreferences() {
        JDialog dialog = new JDialog(rootFrame);
        dialog.setBounds(150, 150, 700, 500);
        dialog.setVisible(true);
        JCheckBox jCheckBox = new JCheckBox(NString.DEBUG);
        jCheckBox.setBounds(0, 0, 80, 80);
        dialog.add(jCheckBox);
    }

    private void showClear() {
        int showConfirmDialog = JOptionPane.showConfirmDialog(rootFrame,
                NString.CLEAR_THE_PROJECT, NString.WARNING,
                JOptionPane.YES_NO_OPTION);
        if (showConfirmDialog == JOptionPane.YES_OPTION)
            UIUtlis.clearScreen();
    }

    private void showSave() {
        JFileChooser chooser = new JFileChooser();
        chooser.setApproveButtonText(NString.SAVE);
        chooser.addChoosableFileFilter(new FileFilter() {// 添加文件过滤，保存文件格式为.json格式的文件

            public boolean accept(File f) {// 如果是目录或者为.json文件格式的文件就显示出来
                if (f.isDirectory()) {// 如果是目录就可以访问
                    return true;
                }
                if (f.getName().endsWith(NString.DATA_END)) {// 如果是.json文件格式的文件
                    // 就显示出来
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return NString.DATA_NAME;
            }

        });
        int returnVal = chooser.showDialog(rootFrame, NString.SAVE);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            File tmpFile = new File(selectedFile.getAbsolutePath()
                    + NString.DATA_END);
            if (selectedFile.exists() || tmpFile.exists()) {
                int showConfirmDialog = JOptionPane.showConfirmDialog(
                        rootFrame, selectedFile + NString.FILE_EXIST,
                        NString.FILE_EXIST_TITLE, JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);// 显示一个对话框来实现是否覆盖源文件
                if (showConfirmDialog == JOptionPane.YES_OPTION) {
                    String showJsonText = UIUtlis.showJsonText();
                    boolean result = IOUtils.save(showJsonText, selectedFile);
                    JOptionPane.showMessageDialog(rootFrame,
                            result ? NString.EXPORT_RESULT_SUCCESS
                                    : NString.EXPORT_RESULT_FAILED,
                            NString.EXPORT_RESULT,
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String showJsonText = UIUtlis.showJsonText();
                boolean result = IOUtils.save(showJsonText, selectedFile);
                JOptionPane.showMessageDialog(rootFrame,
                        result ? NString.EXPORT_RESULT_SUCCESS
                                : NString.EXPORT_RESULT_FAILED,
                        NString.EXPORT_RESULT, JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    private void showOpen() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                NString.XML_FILE, NString.XML);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(rootFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String load = IOUtils.load(selectedFile);
            try {
                JSONArray ja = new JSONArray(load);
                UIUtlis.clearScreen();
                XNodeFactory.file2UI(ja);
                UIUtlis.refreshAllJsonNodeData();
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(rootFrame,
                        NString.NOT_VALIED_JSON, NString.OPEN_RESULT,
                        JOptionPane.INFORMATION_MESSAGE);
                e2.printStackTrace();
            }
        }
    }

    private void showOpenImg() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                NString.IMG_FILE, NString.IMG_TYPE);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(rootFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            bgImg = new ImageIcon(selectedFile.getAbsolutePath()).getImage();
            windowPanel.repaint();
        }
    }
}