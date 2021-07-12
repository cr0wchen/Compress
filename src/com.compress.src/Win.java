package com.compress.src;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.FontUIResource;
/*
 * Created by JFormDesigner on Sat Jul 10 09:34:49 CST 2021
 */


/**
 * @author �����
 */
public class Win extends JFrame {
    FileMgr fileMgr;

    public void flushShowList() {
        String[] list = new String[fileMgr.getCurrentPathList().length];
        String[] dirs = fileMgr.getDirs();
        String[] files = fileMgr.getFiles();
        int index = 0;
        for (String s : dirs) {
            list[index++] = "\\" + s;
        }
        for (String s : files) {
            list[index++] = s;
        }
        File[] allFiles = fileMgr.getFileList();
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
        flushShowList();
        this.setSize(800, 600);
    }

    private void menuItemSelectAllActionPerformed(ActionEvent e) {
        int len = fileMgr.getCurrentPathList().length;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = i;
        }
        showList.setSelectedIndices(arr);
        System.out.println("[debug] ѡ�е������У�" + showList.getSelectedIndices().length);

    }

    private void menuItemUnSelectActionPerformed(ActionEvent e) {
        showList.clearSelection();
        System.out.println("[debug] " + "ȡ��ѡ��");
    }

    private void menuItemReserveSelectActionPerformed(ActionEvent e) {
        int len = fileMgr.getCurrentPathList().length;
        int[] arr = showList.getSelectedIndices();
        int[] unArr = new int[len - arr.length];
        boolean[] allArr = new boolean[len];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            allArr[arr[i]] = true;
        }
        for (int i = 0; i < len; i++) {
            if (allArr[i] == false) {
                unArr[index++] = i;
            }
        }
        showList.setSelectedIndices(unArr);
        System.out.println("[debug] " + "��ѡ��" +"��ѡ������Ϊ ��" + String.format("%d - %d = %d",len,arr.length,unArr.length));
    }

    private void menuItemQuitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void menuItemAboutActionPerformed(ActionEvent e) {
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
// �����ı���ʾЧ��
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
        JOptionPane.showMessageDialog(this, "һ���򵥵Ľ�ѹѹ��С����", "����", JOptionPane.INFORMATION_MESSAGE);
    }

    private void menuItemOpenActionPerformed(ActionEvent e) {
        System.out.println("[debug] " + "open directory��" + showList.getSelectedValue());
        Object selectedValue = showList.getSelectedValue();
        JFile ob = (JFile) selectedValue;
        fileMgr.changeFile(ob.getAbsolutePath());
        fileMgr.flushInf();
        flushShowList();
        System.out.println("[debug] " + "���ļ��У�" + fileMgr.getCurrentPath());
    }

    private void showListValueChanged(ListSelectionEvent e) {
        Object ob = showList.getSelectedValue();
        JFile m = (JFile) ob;
        if (showList.isSelectionEmpty()) {
            menuItemOpen.setEnabled(false);
        } else if (m.isFile()) {
            menuItemOpen.setEnabled(false);
        } else if (showList.getSelectedIndices().length != 1) {
            menuItemOpen.setEnabled(false);
        } else if (m.isDirectory()) {
            menuItemOpen.setEnabled(true);
        }
    }

    private void showListFocusGained(FocusEvent e) {
        // TODO add your code here
    }

    private void showListPropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void menuItemMakeDirActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void menuItemDeleteActionPerformed(ActionEvent e) {
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
// �����ı���ʾЧ��
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));
        Object[] objects = showList.getSelectedValuesList().toArray();
        JFile[] ob = (JFile[]) objects;
        for (JFile jf : ob) {
            if (jf.exists()) {
                if (jf.delete()) {
                    JOptionPane.showMessageDialog(this, "ɾ���ɹ�");
                } else {
                    JOptionPane.showMessageDialog(this, "ɾ��ʧ��");
                }
            }
        }
    }

    private void showListMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2){
            Object selectedValue = showList.getSelectedValue();
            JFile ob = (JFile) selectedValue;
            if (ob.isDirectory()){
                fileMgr.changeFile(ob.getAbsolutePath());
                fileMgr.flushInf();
                flushShowList();
                System.out.println("[debug] " + "���ļ��У�" + fileMgr.getCurrentPath());
            }
        }
    }

    private void upPathActionPerformed(ActionEvent e) {
        fileMgr.upPath();
        fileMgr.flushInf();
        flushShowList();
        System.out.println("[debug] " + "�����ϲ��ļ��У�" + fileMgr.getCurrentPath());
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
        setIconImage(new ImageIcon(getClass().getResource("/com.compress.resource/rar.png")).getImage());
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
            panel1.add(unZip);

            //---- zip ----
            zip.setText("\u538b\u7f29");
            zip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
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
            showList.addListSelectionListener(e -> {
			showListValueChanged(e);
			showListValueChanged(e);
		});
            showList.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    showListFocusGained(e);
                }
            });
            showList.addPropertyChangeListener(e -> showListPropertyChange(e));
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
