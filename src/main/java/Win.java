import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
 * Created by JFormDesigner on Sat Jul 10 09:34:49 CST 2021
 */


/**
 * @author 陈旭峰
 */
public class Win extends JFrame {
    FileMgr fileMgr;//文件管理类

    /**
     * 刷新当前目录下显示的文件
     */
    public void flushShowList() {
        File[] allFiles = fileMgr.listFiles();//获取文件类数组
        JFile[] arr = new JFile[allFiles.length];//创建一个JFile类数据
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new JFile(allFiles[i].getAbsolutePath());
        }
        Arrays.sort(arr);//按文件夹优先顺序排序
        showList.setListData(arr);//设置JList中为元素
    }

    public Win() {
        initComponents();//初始化各种组件
        fileMgr = new FileMgr();
        flushShowList();//刷新当前目录显示的文件
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));//设置对话框按钮字体的样式
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));//设置对话框消息字体样式
        {//设置按键的快捷键
            menuFile.setMnemonic('F');
            menuEditor.setMnemonic('E');
            menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
            menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
            menuItemReName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
            menuItemMakeDir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
            menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
            menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
            menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            menuItemUnSelect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }
    }

    /**
     * 全选操作的事件监听
     *
     * @param e
     */
    private void menuItemSelectAllActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        showList.setSelectionInterval(0, len - 1);//设置全选区间
        System.out.println(String.format("[%s] ", showList.getClass()) + "选择的项目数量为：" + showList.getSelectedIndices().length);

    }

    /**
     * 反选事件监听
     *
     * @param e
     */
    private void menuItemUnSelectActionPerformed(ActionEvent e) {
        showList.clearSelection();
        System.out.println(String.format("[%s] ", showList.getClass()) + "取消选择。");
    }

    private void menuItemReserveSelectActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        int[] arr = showList.getSelectedIndices();//获取选中的项目下标
        showList.clearSelection();//清空选中状态
        if (arr.length == len) return;//如果是全选直接返回
        int[] unArr = new int[len - arr.length];//设置未选中下标数组的大小
        boolean[] allArr = new boolean[len];//标记数组，用来看哪些下标被选中了
        int index = 0;
        for (int j : arr) {
            allArr[j] = true;//将选中为下标置为true
        }
        for (int i = 0; i < len; i++) {
            if (!allArr[i]) {//只要该下标还没选中，将下标放到反选数组中
                unArr[index++] = i;
            }
        }
        showList.setSelectedIndices(unArr);//设置反选数组
        System.out.println(String.format("[%s] ", showList.getClass()) + "反选" + "反选的数量为：" + String.format("%d - %d = %d", len, arr.length, unArr.length));
    }

    /**
     * 退出时间监听
     *
     * @param e
     */
    private void menuItemQuitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**
     * 关于按钮的事件监听，使用html来实现换行
     *
     * @param e
     */
    private void menuItemAboutActionPerformed(ActionEvent e) {
        String info = "<html><body>一个简单的压缩软件\n创建时间：2021年7月10日\n小组成员：陈旭峰、李佳、李天骄\n功能：zip文件的解压和压缩，文件的删除、复制、重命名，文件夹的新建";
        JOptionPane.showMessageDialog(this, info, "关于", JOptionPane.INFORMATION_MESSAGE);//弹出消息对话框
    }

    /**
     * 打开文件夹的事件监听
     *
     * @param e
     */
    private void menuItemOpenActionPerformed(ActionEvent e) {
        JFile ob = (JFile) showList.getSelectedValue();//获取被选中的文件夹，由于通过按键的激活状态控制保证一定存在选中的文件夹
        fileMgr.changeFile(ob.getAbsolutePath());//改变当前文件夹目录
        flushShowList();//刷新
        System.out.println(String.format("[%s] ", showList.getClass()) + "打开的文件夹为：" + fileMgr.getPath());
    }

    /**
     * 各种按钮的激活状态设置
     *
     * @param e
     */
    private void showListValueChanged(ListSelectionEvent e) {
        JFile m = (JFile) showList.getSelectedValue();
        if (showList.isSelectionEmpty()) {//一个也没选中时
            menuItemOpen.setEnabled(false);
            unZip.setEnabled(false);
            zip.setEnabled(false);
            menuItemDelete.setEnabled(false);
            menuItemCopy.setEnabled(false);
            menuItemReName.setEnabled(false);
            menuItemInfo.setEnabled(false);
        } else {
            if (showList.getSelectedIndices().length != 1) {//选中一个时
                menuItemOpen.setEnabled(false);
                zip.setEnabled(true);
                unZip.setEnabled(false);
                menuItemReName.setEnabled(false);
                menuItemInfo.setEnabled(false);
            } else {//选中多个时
                if (m.isFile()) {
                    menuItemOpen.setEnabled(false);
                    zip.setEnabled(true);
                } else if (m.isDirectory()) {
                    menuItemOpen.setEnabled(true);
                    zip.setEnabled(false);
                }
                //选中多个时的默认操作
                zip.setEnabled(true);
                unZip.setEnabled(true);
                menuItemDelete.setEnabled(true);
                menuItemCopy.setEnabled(true);
                menuItemReName.setEnabled(true);
                menuItemInfo.setEnabled(true);
            }
        }
        if (fileMgr.hasCopyBuff()) {//点了复制以后才能粘贴
            menuItemPaste.setEnabled(true);
        } else {
            menuItemPaste.setEnabled(false);
        }
    }


    /**
     * 创建文件夹事件监听
     *
     * @param e
     */
    private void menuItemMakeDirActionPerformed(ActionEvent e) {
        String dir = JOptionPane.showInputDialog(Win.this, "文件夹名：", "新建文件夹");//获取输入的文件夹名字
        if (dir == null) {//没输入就取消创建
            return;//取消创建
        }
        File file = new File(fileMgr.getPath() + File.separator + dir);
        System.out.println(String.format("[%s] ", FileMgr.class) + "创建文件夹：" + file.getPath());
        if (file.mkdir()) {//判断是否创建成功
            JOptionPane.showMessageDialog(this, "创建成功！");
        } else {
            JOptionPane.showMessageDialog(this, "创建失败！");
        }
        flushShowList();//刷新
    }

    /**
     * 删除文件或文件夹事件监听
     *
     * @param e
     */
    private void menuItemDeleteActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;//如果选中状态为空，直接返回
        Object[] objects = showList.getSelectedValuesList().toArray();//获取待删除的文件
        String info = "<html><body>";
        for (Object itm : objects) {
            info += ((File) itm).getName() + "\n";//构造输出的文件名，用于调试
        }
        int ans = JOptionPane.showConfirmDialog(Win.this, info, "删除如下文件吗？", JOptionPane.OK_CANCEL_OPTION);//获取删除信息
        if (ans == JOptionPane.CANCEL_OPTION) {
            System.out.println(String.format("[%s] ", FileMgr.class) + "取消删除。");
            return;
        }
        boolean judgeAcc = true;//记录删除是否成功，防止弹窗弹出多次
        for (Object itm : objects) {
            JFile jf = (JFile) itm;//更改类型为File类
            FileMgr.deleteAll(jf);//删除
            if (jf.exists()) {
                judgeAcc = false;
            } else {
                System.out.println(String.format("[%s] ", FileMgr.class) + "删除文件夹：" + jf.getPath());
            }
        }
        if (judgeAcc == false) {
            JOptionPane.showMessageDialog(this, "删除失败！");
        } else {
            JOptionPane.showMessageDialog(this, "删除成功！");
        }

        flushShowList();//刷新
    }

    /**
     * 鼠标双击时间监听，功能同打开文件夹
     *
     * @param e
     */
    private void showListMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            JFile ob = (JFile) showList.getSelectedValue();
            if (ob.isDirectory()) {
                fileMgr.changeFile(ob.getAbsolutePath());
                flushShowList();
                System.out.println(String.format("[%s] ", fileMgr.getClass()) + "打开的文件夹为：" + fileMgr.getPath());
            }
        }
    }

    /**
     * 返回上层目录
     *
     * @param e
     */
    private void upPathActionPerformed(ActionEvent e) {
        fileMgr.upPath();
        flushShowList();
        System.out.println(String.format("[%s] ", fileMgr.getClass()) + "返回上级目录：" + fileMgr.getPath());
    }

    /**
     * 解压文件事件监听
     *
     * @param e
     */
    private void unZipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;//选中事件为空，无待解压的文件
        JFile curFile = (JFile) showList.getSelectedValue();//获取待解压的文件
        String dest = curFile.getAbsolutePath();//获取该文件的路径
        dest = dest.substring(0, dest.lastIndexOf("."));//构造目标路径，默认为统一目录下的同名文件夹
        String s = JOptionPane.showInputDialog("目标路径：", dest);//用户输入目录
        if (s == null) {//为空时取消解压
            System.out.println(String.format("[%s] ", showList.getClass()) + "取消解压。");
            return;
        }
        System.out.println(String.format("[%s] ", EntryMgr.class) + "解压，" + "目标路径为" + s);
        try {
            EntryMgr.upZip(curFile.getAbsolutePath(), s);//调用静态方法解压文件
            JOptionPane.showMessageDialog(this, "解压成功！");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "解压失败！");
            ee.printStackTrace();
        }
        flushShowList();
    }

    /**
     * 压缩文件的事件监听
     *
     * @param e
     */
    private void zipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        List obList = showList.getSelectedValuesList();//获取选中的文件List
        String dest = fileMgr.getPath() + JFile.separator + fileMgr.getName() + ".zip";//默认压缩文件名
        String s = JOptionPane.showInputDialog("目标文件：", dest);//获取用户输入的文件名
        if (s == null) {
            System.out.println(String.format("[%s] ", showList.getClass()) + "取消压缩。");
            return;
        }
        System.out.println(String.format("[%s] ", EntryMgr.class) + "目标路径为" + s);
        try {
            File[] fList = new File[obList.size()];
            for (int i = 0; i < obList.size(); i++) {
                fList[i] = (File) obList.get(i);//转换数组中为类型
            }
            EntryMgr.zip(fList, s);//压缩文件
            JOptionPane.showMessageDialog(this, "压缩成功！");
        } catch (Exception ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(this, "压缩失败！");
        }
        flushShowList();
    }

    /**
     * 重命名文件或文件夹的事件监听
     *
     * @param e
     */
    private void menuItemReNameActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        File file = (File) showList.getSelectedValue();//获取选中的File
        String fileName = JOptionPane.showInputDialog(Win.this, "重命名为：", file.getName());//获取用户输入的文件名
        if (fileName == null) {
            return;//取消创建
        }
        String preName = file.getPath();
        int lastIndex = preName.lastIndexOf(file.getName());//从后往前匹配原文件名，只更改最后一个匹配到的字符串
        String newName = preName.substring(0, lastIndex) + fileName;//构造新文件名
        File newFile = new File(newName);
        System.out.println(String.format("[%s] ", FileMgr.class) + "重命名为：" + newFile.getPath());
        if (file.renameTo(newFile)) {
            JOptionPane.showMessageDialog(this, "重命名成功！");
        } else {
            JOptionPane.showMessageDialog(this, "重命名失败！");
        }
        flushShowList();
    }

    /**
     * 获取文件属性事件监听
     *
     * @param e
     */
    private void menuItemInfoActionPerformed(ActionEvent e) {
        File file = (File) showList.getSelectedValue();//获取文件
        String type = file.isFile() ? "文件" : "文件夹";//文件类型
        Date date = new Date(file.lastModified());//修改时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");//格式化时间
        String lastModified = sdf.format(date);
        String hidden = file.isHidden() ? "是" : "否";//文件是否隐藏
        String info = String.format("<html><body>类型：%s\n位置：%s\n名称：%s \n大小：%d字节\n最后修改时间：%s\n是否隐藏：%s", type, file.getAbsolutePath(), file.getName(), file.length(), lastModified, hidden);
        JOptionPane.showMessageDialog(this, info, "属性", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 复制文件事件监听
     *
     * @param e
     */
    private void menuItemCopyActionPerformed(ActionEvent e) {
        Object[] objects = showList.getSelectedValuesList().toArray();//获取选中的文件数组
        File[] files = new File[objects.length];
        for (int i = 0; i < objects.length; i++) {
            files[i] = (File) objects[i];//转换Object类型为File类型
            System.out.println(String.format("[%s] ", FileMgr.class) + "复制文件：" + files[i].getPath());
        }
        fileMgr.copy(files);//复制
        menuItemPaste.setEnabled(true);//激活粘贴按钮
    }

    /**
     * 粘贴事件监听
     *
     * @param e
     */
    private void menuItemPasteActionPerformed(ActionEvent e) {
        try {
            fileMgr.paste(fileMgr.getPath());//粘贴到当前目录
        } catch (IOException ee) {
            ee.printStackTrace();
            System.out.println(String.format("[%s] ", FileMgr.class) + "粘贴文件失败！！");
        }
        flushShowList();
    }


    /**
     * 各种组件的初始化，由JFormDesigner自动生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menuFile = new JMenu();
        menuItemOpen = new JMenuItem();
        menuItemCopy = new JMenuItem();
        menuItemPaste = new JMenuItem();
        menuItemReName = new JMenuItem();
        menuItemMakeDir = new JMenuItem();
        menuItemDelete = new JMenuItem();
        menuItemInfo = new JMenuItem();
        menuItemQuit = new JMenuItem();
        menuEditor = new JMenu();
        menuItemSelectAll = new JMenuItem();
        menuItemReserveSelect = new JMenuItem();
        menuItemUnSelect = new JMenuItem();
        menuHelp = new JMenu();
        menuItemAbout = new JMenuItem();
        panel1 = new JPanel();
        upPath = new JButton();
        unZip = new JButton();
        zip = new JButton();
        scrollPaneDirAndFile = new JScrollPane();
        showList = new JList();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 16));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u5c0f\u538b\u7f29");
        setMinimumSize(new Dimension(800, 600));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {
            menuBar1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

            //======== menuFile ========
            {
                menuFile.setText("\u6587\u4ef6");
                menuFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

                //---- menuItemOpen ----
                menuItemOpen.setText("\u6253\u5f00\u6587\u4ef6\u5939");
                menuItemOpen.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemOpen.setEnabled(false);
                menuItemOpen.addActionListener(e -> menuItemOpenActionPerformed(e));
                menuFile.add(menuItemOpen);

                //---- menuItemCopy ----
                menuItemCopy.setText("\u590d\u5236");
                menuItemCopy.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemCopy.setEnabled(false);
                menuItemCopy.addActionListener(e -> menuItemCopyActionPerformed(e));
                menuFile.add(menuItemCopy);

                //---- menuItemPaste ----
                menuItemPaste.setText("\u7c98\u8d34");
                menuItemPaste.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemPaste.setEnabled(false);
                menuItemPaste.addActionListener(e -> menuItemPasteActionPerformed(e));
                menuFile.add(menuItemPaste);

                //---- menuItemReName ----
                menuItemReName.setText("\u91cd\u547d\u540d");
                menuItemReName.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemReName.setEnabled(false);
                menuItemReName.addActionListener(e -> menuItemReNameActionPerformed(e));
                menuFile.add(menuItemReName);

                //---- menuItemMakeDir ----
                menuItemMakeDir.setText("\u65b0\u5efa\u6587\u4ef6\u5939");
                menuItemMakeDir.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemMakeDir.addActionListener(e -> menuItemMakeDirActionPerformed(e));
                menuFile.add(menuItemMakeDir);

                //---- menuItemDelete ----
                menuItemDelete.setText("\u5220\u9664");
                menuItemDelete.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemDelete.setEnabled(false);
                menuItemDelete.addActionListener(e -> menuItemDeleteActionPerformed(e));
                menuFile.add(menuItemDelete);

                //---- menuItemInfo ----
                menuItemInfo.setText("\u5c5e\u6027");
                menuItemInfo.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemInfo.setEnabled(false);
                menuItemInfo.addActionListener(e -> menuItemInfoActionPerformed(e));
                menuFile.add(menuItemInfo);

                //---- menuItemQuit ----
                menuItemQuit.setText("\u9000\u51fa");
                menuItemQuit.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemQuit.addActionListener(e -> menuItemQuitActionPerformed(e));
                menuFile.add(menuItemQuit);
            }
            menuBar1.add(menuFile);

            //======== menuEditor ========
            {
                menuEditor.setText("\u7f16\u8f91");
                menuEditor.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

                //---- menuItemSelectAll ----
                menuItemSelectAll.setText("\u5168\u9009");
                menuItemSelectAll.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemSelectAll.addActionListener(e -> menuItemSelectAllActionPerformed(e));
                menuEditor.add(menuItemSelectAll);

                //---- menuItemReserveSelect ----
                menuItemReserveSelect.setText("\u53cd\u9009");
                menuItemReserveSelect.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemReserveSelect.addActionListener(e -> menuItemReserveSelectActionPerformed(e));
                menuEditor.add(menuItemReserveSelect);

                //---- menuItemUnSelect ----
                menuItemUnSelect.setText("\u5168\u90e8\u53d6\u6d88");
                menuItemUnSelect.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemUnSelect.addActionListener(e -> menuItemUnSelectActionPerformed(e));
                menuEditor.add(menuItemUnSelect);
            }
            menuBar1.add(menuEditor);

            //======== menuHelp ========
            {
                menuHelp.setText("\u5e2e\u52a9");
                menuHelp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));

                //---- menuItemAbout ----
                menuItemAbout.setText("\u5173\u4e8e");
                menuItemAbout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuItemAbout.addActionListener(e -> menuItemAboutActionPerformed(e));
                menuHelp.add(menuItemAbout);
            }
            menuBar1.add(menuHelp);
        }
        setJMenuBar(menuBar1);

        //======== panel1 ========
        {
            panel1.setBorder(null);
            panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

            //---- upPath ----
            upPath.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            upPath.setIcon(new ImageIcon(getClass().getResource("/back.png")));
            upPath.setToolTipText("\u56de\u5230\u7236\u76ee\u5f55");
            upPath.setBorder(new EtchedBorder());
            upPath.addActionListener(e -> upPathActionPerformed(e));
            panel1.add(upPath);

            //---- unZip ----
            unZip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            unZip.setEnabled(false);
            unZip.setIcon(new ImageIcon(getClass().getResource("/unZip.png")));
            unZip.setToolTipText("\u89e3\u538b");
            unZip.setBorder(new EtchedBorder());
            unZip.addActionListener(e -> unZipActionPerformed(e));
            panel1.add(unZip);

            //---- zip ----
            zip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            zip.setEnabled(false);
            zip.setIcon(new ImageIcon(getClass().getResource("/Zip.png")));
            zip.setToolTipText("\u538b\u7f29");
            zip.setBorder(new EtchedBorder());
            zip.addActionListener(e -> zipActionPerformed(e));
            panel1.add(zip);
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== scrollPaneDirAndFile ========
        {
            scrollPaneDirAndFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            scrollPaneDirAndFile.setBorder(new BevelBorder(BevelBorder.LOWERED));

            //---- showList ----
            showList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            showList.setBorder(new BevelBorder(BevelBorder.LOWERED));
            showList.addListSelectionListener(e -> showListValueChanged(e));
            showList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showListMouseClicked(e);
                }
            });
            scrollPaneDirAndFile.setViewportView(showList);
        }
        contentPane.add(scrollPaneDirAndFile, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    /**
     * 各种组件的声明，由JFormDesigner自动生成
     */
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menuFile;
    private JMenuItem menuItemOpen;
    private JMenuItem menuItemCopy;
    private JMenuItem menuItemPaste;
    private JMenuItem menuItemReName;
    private JMenuItem menuItemMakeDir;
    private JMenuItem menuItemDelete;
    private JMenuItem menuItemInfo;
    private JMenuItem menuItemQuit;
    private JMenu menuEditor;
    private JMenuItem menuItemSelectAll;
    private JMenuItem menuItemReserveSelect;
    private JMenuItem menuItemUnSelect;
    private JMenu menuHelp;
    private JMenuItem menuItemAbout;
    private JPanel panel1;
    private JButton upPath;
    private JButton unZip;
    private JButton zip;
    private JScrollPane scrollPaneDirAndFile;
    private JList showList;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
