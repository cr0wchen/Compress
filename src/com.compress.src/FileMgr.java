package com.compress.src;

import org.omg.PortableInterceptor.ServerRequestInfo;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class FileMgr {
    private File com;//当前文件夹的实例

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
//        System.out.println(com.getPath()); //这便是桌面的具体路径
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
