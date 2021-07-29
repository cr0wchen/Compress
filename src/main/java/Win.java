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
 * @author �����
 */
public class Win extends JFrame {
    FileMgr fileMgr;//�ļ�������

    /**
     * ˢ�µ�ǰĿ¼����ʾ���ļ�
     */
    public void flushShowList() {
        File[] allFiles = fileMgr.listFiles();//��ȡ�ļ�������
        JFile[] arr = new JFile[allFiles.length];//����һ��JFile������
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new JFile(allFiles[i].getAbsolutePath());
        }
        Arrays.sort(arr);//���ļ�������˳������
        showList.setListData(arr);//����JList��ΪԪ��
    }

    public Win() {
        initComponents();//��ʼ���������
        fileMgr = new FileMgr();
        flushShowList();//ˢ�µ�ǰĿ¼��ʾ���ļ�
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));//���öԻ���ť�������ʽ
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Microsoft YaHei UI", Font.PLAIN, 16)));//���öԻ�����Ϣ������ʽ
        {//���ð����Ŀ�ݼ�
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
     * ȫѡ�������¼�����
     *
     * @param e
     */
    private void menuItemSelectAllActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        showList.setSelectionInterval(0, len - 1);//����ȫѡ����
        System.out.println(String.format("[%s] ", showList.getClass()) + "ѡ�����Ŀ����Ϊ��" + showList.getSelectedIndices().length);

    }

    /**
     * ��ѡ�¼�����
     *
     * @param e
     */
    private void menuItemUnSelectActionPerformed(ActionEvent e) {
        showList.clearSelection();
        System.out.println(String.format("[%s] ", showList.getClass()) + "ȡ��ѡ��");
    }

    private void menuItemReserveSelectActionPerformed(ActionEvent e) {
        int len = fileMgr.listFiles().length;
        int[] arr = showList.getSelectedIndices();//��ȡѡ�е���Ŀ�±�
        showList.clearSelection();//���ѡ��״̬
        if (arr.length == len) return;//�����ȫѡֱ�ӷ���
        int[] unArr = new int[len - arr.length];//����δѡ���±�����Ĵ�С
        boolean[] allArr = new boolean[len];//������飬��������Щ�±걻ѡ����
        int index = 0;
        for (int j : arr) {
            allArr[j] = true;//��ѡ��Ϊ�±���Ϊtrue
        }
        for (int i = 0; i < len; i++) {
            if (!allArr[i]) {//ֻҪ���±껹ûѡ�У����±�ŵ���ѡ������
                unArr[index++] = i;
            }
        }
        showList.setSelectedIndices(unArr);//���÷�ѡ����
        System.out.println(String.format("[%s] ", showList.getClass()) + "��ѡ" + "��ѡ������Ϊ��" + String.format("%d - %d = %d", len, arr.length, unArr.length));
    }

    /**
     * �˳�ʱ�����
     *
     * @param e
     */
    private void menuItemQuitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**
     * ���ڰ�ť���¼�������ʹ��html��ʵ�ֻ���
     *
     * @param e
     */
    private void menuItemAboutActionPerformed(ActionEvent e) {
        String info = "<html><body>һ���򵥵�ѹ�����\n����ʱ�䣺2021��7��10��\nС���Ա������塢��ѡ����콾\n���ܣ�zip�ļ��Ľ�ѹ��ѹ�����ļ���ɾ�������ơ����������ļ��е��½�";
        JOptionPane.showMessageDialog(this, info, "����", JOptionPane.INFORMATION_MESSAGE);//������Ϣ�Ի���
    }

    /**
     * ���ļ��е��¼�����
     *
     * @param e
     */
    private void menuItemOpenActionPerformed(ActionEvent e) {
        JFile ob = (JFile) showList.getSelectedValue();//��ȡ��ѡ�е��ļ��У�����ͨ�������ļ���״̬���Ʊ�֤һ������ѡ�е��ļ���
        fileMgr.changeFile(ob.getAbsolutePath());//�ı䵱ǰ�ļ���Ŀ¼
        flushShowList();//ˢ��
        System.out.println(String.format("[%s] ", showList.getClass()) + "�򿪵��ļ���Ϊ��" + fileMgr.getPath());
    }

    /**
     * ���ְ�ť�ļ���״̬����
     *
     * @param e
     */
    private void showListValueChanged(ListSelectionEvent e) {
        JFile m = (JFile) showList.getSelectedValue();
        if (showList.isSelectionEmpty()) {//һ��Ҳûѡ��ʱ
            menuItemOpen.setEnabled(false);
            unZip.setEnabled(false);
            zip.setEnabled(false);
            menuItemDelete.setEnabled(false);
            menuItemCopy.setEnabled(false);
            menuItemReName.setEnabled(false);
            menuItemInfo.setEnabled(false);
        } else {
            if (showList.getSelectedIndices().length != 1) {//ѡ��һ��ʱ
                menuItemOpen.setEnabled(false);
                zip.setEnabled(true);
                unZip.setEnabled(false);
                menuItemReName.setEnabled(false);
                menuItemInfo.setEnabled(false);
            } else {//ѡ�ж��ʱ
                if (m.isFile()) {
                    menuItemOpen.setEnabled(false);
                    zip.setEnabled(true);
                } else if (m.isDirectory()) {
                    menuItemOpen.setEnabled(true);
                    zip.setEnabled(false);
                }
                //ѡ�ж��ʱ��Ĭ�ϲ���
                zip.setEnabled(true);
                unZip.setEnabled(true);
                menuItemDelete.setEnabled(true);
                menuItemCopy.setEnabled(true);
                menuItemReName.setEnabled(true);
                menuItemInfo.setEnabled(true);
            }
        }
        if (fileMgr.hasCopyBuff()) {//���˸����Ժ����ճ��
            menuItemPaste.setEnabled(true);
        } else {
            menuItemPaste.setEnabled(false);
        }
    }


    /**
     * �����ļ����¼�����
     *
     * @param e
     */
    private void menuItemMakeDirActionPerformed(ActionEvent e) {
        String dir = JOptionPane.showInputDialog(Win.this, "�ļ�������", "�½��ļ���");//��ȡ������ļ�������
        if (dir == null) {//û�����ȡ������
            return;//ȡ������
        }
        File file = new File(fileMgr.getPath() + File.separator + dir);
        System.out.println(String.format("[%s] ", FileMgr.class) + "�����ļ��У�" + file.getPath());
        if (file.mkdir()) {//�ж��Ƿ񴴽��ɹ�
            JOptionPane.showMessageDialog(this, "�����ɹ���");
        } else {
            JOptionPane.showMessageDialog(this, "����ʧ�ܣ�");
        }
        flushShowList();//ˢ��
    }

    /**
     * ɾ���ļ����ļ����¼�����
     *
     * @param e
     */
    private void menuItemDeleteActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;//���ѡ��״̬Ϊ�գ�ֱ�ӷ���
        Object[] objects = showList.getSelectedValuesList().toArray();//��ȡ��ɾ�����ļ�
        String info = "<html><body>";
        for (Object itm : objects) {
            info += ((File) itm).getName() + "\n";//����������ļ��������ڵ���
        }
        int ans = JOptionPane.showConfirmDialog(Win.this, info, "ɾ�������ļ���", JOptionPane.OK_CANCEL_OPTION);//��ȡɾ����Ϣ
        if (ans == JOptionPane.CANCEL_OPTION) {
            System.out.println(String.format("[%s] ", FileMgr.class) + "ȡ��ɾ����");
            return;
        }
        boolean judgeAcc = true;//��¼ɾ���Ƿ�ɹ�����ֹ�����������
        for (Object itm : objects) {
            JFile jf = (JFile) itm;//��������ΪFile��
            FileMgr.deleteAll(jf);//ɾ��
            if (jf.exists()) {
                judgeAcc = false;
            } else {
                System.out.println(String.format("[%s] ", FileMgr.class) + "ɾ���ļ��У�" + jf.getPath());
            }
        }
        if (judgeAcc == false) {
            JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
        } else {
            JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
        }

        flushShowList();//ˢ��
    }

    /**
     * ���˫��ʱ�����������ͬ���ļ���
     *
     * @param e
     */
    private void showListMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            JFile ob = (JFile) showList.getSelectedValue();
            if (ob.isDirectory()) {
                fileMgr.changeFile(ob.getAbsolutePath());
                flushShowList();
                System.out.println(String.format("[%s] ", fileMgr.getClass()) + "�򿪵��ļ���Ϊ��" + fileMgr.getPath());
            }
        }
    }

    /**
     * �����ϲ�Ŀ¼
     *
     * @param e
     */
    private void upPathActionPerformed(ActionEvent e) {
        fileMgr.upPath();
        flushShowList();
        System.out.println(String.format("[%s] ", fileMgr.getClass()) + "�����ϼ�Ŀ¼��" + fileMgr.getPath());
    }

    /**
     * ��ѹ�ļ��¼�����
     *
     * @param e
     */
    private void unZipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;//ѡ���¼�Ϊ�գ��޴���ѹ���ļ�
        JFile curFile = (JFile) showList.getSelectedValue();//��ȡ����ѹ���ļ�
        String dest = curFile.getAbsolutePath();//��ȡ���ļ���·��
        dest = dest.substring(0, dest.lastIndexOf("."));//����Ŀ��·����Ĭ��ΪͳһĿ¼�µ�ͬ���ļ���
        String s = JOptionPane.showInputDialog("Ŀ��·����", dest);//�û�����Ŀ¼
        if (s == null) {//Ϊ��ʱȡ����ѹ
            System.out.println(String.format("[%s] ", showList.getClass()) + "ȡ����ѹ��");
            return;
        }
        System.out.println(String.format("[%s] ", EntryMgr.class) + "��ѹ��" + "Ŀ��·��Ϊ" + s);
        try {
            EntryMgr.upZip(curFile.getAbsolutePath(), s);//���þ�̬������ѹ�ļ�
            JOptionPane.showMessageDialog(this, "��ѹ�ɹ���");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "��ѹʧ�ܣ�");
            ee.printStackTrace();
        }
        flushShowList();
    }

    /**
     * ѹ���ļ����¼�����
     *
     * @param e
     */
    private void zipActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        List obList = showList.getSelectedValuesList();//��ȡѡ�е��ļ�List
        String dest = fileMgr.getPath() + JFile.separator + fileMgr.getName() + ".zip";//Ĭ��ѹ���ļ���
        String s = JOptionPane.showInputDialog("Ŀ���ļ���", dest);//��ȡ�û�������ļ���
        if (s == null) {
            System.out.println(String.format("[%s] ", showList.getClass()) + "ȡ��ѹ����");
            return;
        }
        System.out.println(String.format("[%s] ", EntryMgr.class) + "Ŀ��·��Ϊ" + s);
        try {
            File[] fList = new File[obList.size()];
            for (int i = 0; i < obList.size(); i++) {
                fList[i] = (File) obList.get(i);//ת��������Ϊ����
            }
            EntryMgr.zip(fList, s);//ѹ���ļ�
            JOptionPane.showMessageDialog(this, "ѹ���ɹ���");
        } catch (Exception ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(this, "ѹ��ʧ�ܣ�");
        }
        flushShowList();
    }

    /**
     * �������ļ����ļ��е��¼�����
     *
     * @param e
     */
    private void menuItemReNameActionPerformed(ActionEvent e) {
        if (showList.isSelectionEmpty()) return;
        File file = (File) showList.getSelectedValue();//��ȡѡ�е�File
        String fileName = JOptionPane.showInputDialog(Win.this, "������Ϊ��", file.getName());//��ȡ�û�������ļ���
        if (fileName == null) {
            return;//ȡ������
        }
        String preName = file.getPath();
        int lastIndex = preName.lastIndexOf(file.getName());//�Ӻ���ǰƥ��ԭ�ļ�����ֻ�������һ��ƥ�䵽���ַ���
        String newName = preName.substring(0, lastIndex) + fileName;//�������ļ���
        File newFile = new File(newName);
        System.out.println(String.format("[%s] ", FileMgr.class) + "������Ϊ��" + newFile.getPath());
        if (file.renameTo(newFile)) {
            JOptionPane.showMessageDialog(this, "�������ɹ���");
        } else {
            JOptionPane.showMessageDialog(this, "������ʧ�ܣ�");
        }
        flushShowList();
    }

    /**
     * ��ȡ�ļ������¼�����
     *
     * @param e
     */
    private void menuItemInfoActionPerformed(ActionEvent e) {
        File file = (File) showList.getSelectedValue();//��ȡ�ļ�
        String type = file.isFile() ? "�ļ�" : "�ļ���";//�ļ�����
        Date date = new Date(file.lastModified());//�޸�ʱ��
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");//��ʽ��ʱ��
        String lastModified = sdf.format(date);
        String hidden = file.isHidden() ? "��" : "��";//�ļ��Ƿ�����
        String info = String.format("<html><body>���ͣ�%s\nλ�ã�%s\n���ƣ�%s \n��С��%d�ֽ�\n����޸�ʱ�䣺%s\n�Ƿ����أ�%s", type, file.getAbsolutePath(), file.getName(), file.length(), lastModified, hidden);
        JOptionPane.showMessageDialog(this, info, "����", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * �����ļ��¼�����
     *
     * @param e
     */
    private void menuItemCopyActionPerformed(ActionEvent e) {
        Object[] objects = showList.getSelectedValuesList().toArray();//��ȡѡ�е��ļ�����
        File[] files = new File[objects.length];
        for (int i = 0; i < objects.length; i++) {
            files[i] = (File) objects[i];//ת��Object����ΪFile����
            System.out.println(String.format("[%s] ", FileMgr.class) + "�����ļ���" + files[i].getPath());
        }
        fileMgr.copy(files);//����
        menuItemPaste.setEnabled(true);//����ճ����ť
    }

    /**
     * ճ���¼�����
     *
     * @param e
     */
    private void menuItemPasteActionPerformed(ActionEvent e) {
        try {
            fileMgr.paste(fileMgr.getPath());//ճ������ǰĿ¼
        } catch (IOException ee) {
            ee.printStackTrace();
            System.out.println(String.format("[%s] ", FileMgr.class) + "ճ���ļ�ʧ�ܣ���");
        }
        flushShowList();
    }


    /**
     * ��������ĳ�ʼ������JFormDesigner�Զ�����
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
     * �����������������JFormDesigner�Զ�����
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
