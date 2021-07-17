package com.compress.src;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.FontUIResource;

/*
 * Created by JFormDesigner on Sat Jul 10 09:34:49 CST 2021
 */


/**
 * @author 陈旭峰
 */
public class Win extends JFrame {
    FileMgr fileMgr;

    public void flushShowList() {
        File[] allFiles = fileMgr.listFiles();
        JFile[] arr = new JFile[allFiles.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new JFile(allFiles[i].getAbsolutePath());
        }
        Arrays.sort(arr);
        showList.setListData(arr);
    }

    public Win() {
        initComponents();
        fileMgr = new FileMgr();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        flushShowList();
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
    }

    private void menuItemSelectAllActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        showList.setSelectionInterval(0, len - 1);
        System.out.println(String.format("[%s] ", showList.getClass()) + "选择的项目数量为：" + showList.getSelectedIndices().length);

    }

    private void menuItemUnSelectActionPerformed(ActionEvent e) {
        showList.clearSelection();
        System.out.println(String.format("[%s] ", showList.getClass()) + "取消选择。");
    }

    private void menuItemReserveSelectActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        int[] arr = showList.getSelectedIndices();
        showList.clearSelection();
        if (arr.length == len) return;
        int[] unArr = new int[len - arr.length];
        boolean[] allArr = new boolean[len];
        int index = 0;
        for (int j : arr) {
            allArr[j] = true;
        }
        for (int i = 0; i < len; i++) {
            if (!allArr[i]) {
                unArr[index++] = i;
            }
        }
        showList.setSelectedIndices(unArr);
        System.out.println(String.format("[%s] ", showList.getClass()) + "反选" + "反选的数量为：" + String.format("%d - %d = %d", len, arr.length, unArr.length));
    }

    private void menuItemQuitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void menuItemAboutActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "一个简单的压缩解压软件", "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    private void menuItemOpenActionPerformed(ActionEvent e) {
        JFile ob = (JFile) showList.getSelectedValue();
        fileMgr.changeFile(ob.getAbsolutePath());
        flushShowList();
        System.out.println(String.format("[%s] ", showList.getClass()) + "打开的文件夹为：" + fileMgr.getPath());
    }

    private void showListValueChanged(ListSelectionEvent e) {
        JFile m = (JFile) showList.getSelectedValue();
        if (showList.isSelectionEmpty()) {
            menuItemOpen.setEnabled(false);
            unZip.setEnabled(false);
            zip.setEnabled(false);
            menuItemDelete.setEnabled(false);
        } else {
            if (showList.getSelectedIndices().length != 1) {
                menuItemOpen.setEnabled(false);
                zip.setEnabled(true);
                unZip.setEnabled(false);
            } else {
                if (m.isFile()) {
                    menuItemOpen.setEnabled(false);
                    zip.setEnabled(true);
                } else if (m.isDirectory()) {
                    menuItemOpen.setEnabled(true);
                    zip.setEnabled(false);
                }
                zip.setEnabled(true);
                unZip.setEnabled(true);
                menuItemDelete.setEnabled(true);
            }
        }
    }


    private void menuItemMakeDirActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void menuItemDeleteActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        Object[] objects = showList.getSelectedValuesList().toArray();
        boolean judgeAcc = true;
        for (Object itm : objects) {
            JFile jf = (JFile) itm;
            FileMgr.deleteAll(jf);
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

        flushShowList();

    }

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

    private void upPathActionPerformed(ActionEvent e) {
        fileMgr.upPath();
        flushShowList();
        System.out.println(String.format("[%s] ", fileMgr.getClass()) + "返回上级目录：" + fileMgr.getPath());
    }

    private void unZipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        JFile curFile = (JFile) showList.getSelectedValue();
        String dest = curFile.getAbsolutePath();
        dest = dest.substring(0, dest.lastIndexOf("."));
        String s = JOptionPane.showInputDialog("目标路径：", dest);
        if (s == null) {
            System.out.println(String.format("[%s] ", showList.getClass()) + "取消解压。");
            return;
        }
        System.out.println(String.format("[%s] ", EntryMgr.class) + "解压，" + "目标路径为" + s);
        try {
            EntryMgr.upZip(curFile.getAbsolutePath(), s);
            JOptionPane.showMessageDialog(this, "解压成功！");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "解压失败！");
            ee.printStackTrace();
        }
        flushShowList();
    }

    private void zipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        List obList = showList.getSelectedValuesList();
        System.out.println("开始");
        String dest = fileMgr.getPath() + JFile.separator + fileMgr.getName() + ".zip";
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
        String s = JOptionPane.showInputDialog("目标文件：", dest);
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
            EntryMgr.entryFileArray(fList, s);//压缩文件
            JOptionPane.showMessageDialog(this, "压缩成功！");
        } catch (Exception ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(this, "压缩失败！");
        }
        flushShowList();
    }


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
        button4 = new JButton();
        scrollPaneDirAndFile = new JScrollPane();
        showList = new JList();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/com.compress.resource/logo.png")).getImage());
        setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 16));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u5c0f\u538b\u7f29");
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
                menuFile.add(menuItemCopy);

                //---- menuItemPaste ----
                menuItemPaste.setText("\u7c98\u8d34");
                menuItemPaste.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                menuFile.add(menuItemPaste);

                //---- menuItemReName ----
                menuItemReName.setText("\u91cd\u547d\u540d");
                menuItemReName.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
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
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

            //---- upPath ----
            upPath.setText("\u8fd4\u56de");
            upPath.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            upPath.addActionListener(e -> upPathActionPerformed(e));
            panel1.add(upPath);

            //---- unZip ----
            unZip.setText("\u89e3\u538b");
            unZip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            unZip.setEnabled(false);
            unZip.addActionListener(e -> unZipActionPerformed(e));
            panel1.add(unZip);

            //---- zip ----
            zip.setText("\u538b\u7f29");
            zip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            zip.setEnabled(false);
            zip.addActionListener(e -> zipActionPerformed(e));
            panel1.add(zip);

            //---- button4 ----
            button4.setText("text");
            panel1.add(button4);
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== scrollPaneDirAndFile ========
        {
            scrollPaneDirAndFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            scrollPaneDirAndFile.setBorder(new EmptyBorder(5, 5, 5, 5));

            //---- showList ----
            showList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            showList.setBorder(new EmptyBorder(5, 5, 5, 5));
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
    private JButton button4;
    private JScrollPane scrollPaneDirAndFile;
    private JList showList;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
