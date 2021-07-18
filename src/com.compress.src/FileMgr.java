package com.compress.src;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class FileMgr {
    private File com;//��ǰ�ļ��е�ʵ��
    private File[] copyArr;

    public boolean hasCopyBuff() {
        return copyArr != null;
    }

    public void paste(String path) throws IOException {
        for (File f : this.copyArr) {
            int idx = path.indexOf(f.getAbsolutePath());
            if (idx != -1) {
                //��ʱpath�Ǵ����Ƶ��ļ��е����ļ��У������ļ��и��Ƶ����ļ��ݹ齫��������
                return;
            }
        }
        if (copyArr == null) return;
        paste(this.copyArr, path);
        this.copyArr = null;//��ջ���
    }

    private void paste(File[] srcArr, String path) throws IOException {
        for (File f : srcArr) {
            if (f.isFile()) {
                copyFile(f.getAbsolutePath(), path + File.separator + f.getName());
            } else {
                String fName = path + File.separator + f.getName();
                File file = new File(fName);
                file.mkdir();
                paste(f.listFiles(), fName);//���ļ��У��ݹ�ظ�����ȥ
            }
        }
    }

    public void copy(File[] copyArr) {
        this.copyArr = copyArr;
    }

    /**
     * ���Ƶ����ļ�
     *
     * @param src Դ��ַ
     * @param des Ŀ�ĵ�ַ
     * @throws IOException
     */
    private static void copyFile(String src, String des) throws IOException {
        BufferedInputStream ibs = new BufferedInputStream(new FileInputStream(src));//������
        BufferedOutputStream obs = new BufferedOutputStream(new FileOutputStream(des));//�����
        byte[] buff = new byte[1024];//������
        int m;
        while ((m = ibs.read(buff)) != -1) {//�Ӷ������ж�ȡ���ݣ�ֱ��ĩβ
            obs.write(buff, 0, m);//������д�������
        }
        ibs.close();//�ر���
        obs.close();
    }

    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }
            if (file.exists())         //����ļ��������Ŀ¼ ����Ҫɾ��Ŀ¼
                file.delete();
        }
    }

    public FileMgr() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        com = fsv.getHomeDirectory();
    }

    public File getParentFile() {
        return com.getParentFile();
    }

    public String getPath() {
        return com.getPath();
    }

    public boolean changeFile(String newPath) {
        String prePath = com.getPath();
        com = new File(newPath);
        if (com == null) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            com = fsv.getHomeDirectory();
            return false;
        }
        if (com.isFile()) {
            com = new File(prePath);
        }
        return true;
    }

    public boolean upPath() {
        boolean result;
        String parentPath = com.getParent();
        if (parentPath == null) {
            result = false;
        } else {
            com = new File(parentPath);
            result = true;
        }
        return result;
    }

    public String getParent() {
        return com.getParent();
    }

    public String getName() {
        return com.getName();
    }

    public String[] list() {
        return com.list();
    }

    public File[] listFiles() {
        return com.listFiles();
    }

    public static void main(String[] args) {
        FileMgr fileMgr = new FileMgr();
        System.out.println(fileMgr.getPath());
//        fileMgr.upPath();
        System.out.println(fileMgr.getPath());
        for (String s : fileMgr.list()) {
            System.out.println(s);
        }
        fileMgr.upPath();
        System.out.println(fileMgr.getPath());
        fileMgr.upPath();
//        fileMgr.upPath();
        System.out.println(fileMgr.getPath());
    }
}
