package com.compress.src;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class FileMgr {
    private File com;//当前文件夹的实例
    private File[] copyArr;

    public boolean hasCopyBuff() {
        return copyArr != null;
    }

    public void paste(String path) throws IOException {
        for (File f : this.copyArr) {
            int idx = path.indexOf(f.getAbsolutePath());
            if (idx != -1) {
                //此时path是待复制的文件夹的子文件夹，将父文件夹复制到子文件递归将结束不了
                return;
            }
        }
        if (copyArr == null) return;
        paste(this.copyArr, path);
        this.copyArr = null;//清空缓冲
    }

    private void paste(File[] srcArr, String path) throws IOException {
        for (File f : srcArr) {
            if (f.isFile()) {
                copyFile(f.getAbsolutePath(), path + File.separator + f.getName());
            } else {
                String fName = path + File.separator + f.getName();
                File file = new File(fName);
                file.mkdir();
                paste(f.listFiles(), fName);//是文件夹，递归地复制下去
            }
        }
    }

    public void copy(File[] copyArr) {
        this.copyArr = copyArr;
    }

    /**
     * 复制单个文件
     *
     * @param src 源地址
     * @param des 目的地址
     * @throws IOException
     */
    private static void copyFile(String src, String des) throws IOException {
        BufferedInputStream ibs = new BufferedInputStream(new FileInputStream(src));//读入流
        BufferedOutputStream obs = new BufferedOutputStream(new FileOutputStream(des));//输出流
        byte[] buff = new byte[1024];//缓冲区
        int m;
        while ((m = ibs.read(buff)) != -1) {//从读入流中读取数据，直到末尾
            obs.write(buff, 0, m);//将数据写入输出流
        }
        ibs.close();//关闭流
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
            if (file.exists())         //如果文件本身就是目录 ，就要删除目录
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
