package com.compress.src;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URI;

public class JFile extends File {

    public JFile(String s) {
        super(s);
    }

    /**
     * ��дcompareTo������ʹ���ļ�������
     *
     * @param file ���ڱȽϵĶ���
     * @return -1,0,1�ֱ����С�ڡ����ڡ�����
     */
    @Override
    public int compareTo(File file) {
        int result = 0;
        if (this.isDirectory() && file.isDirectory()) {
            result = this.getName().compareTo(file.getName());
        } else if (this.isDirectory() && file.isFile()) {
            result = -1;
        } else if (this.isFile() && file.isFile()) {
            result = this.getName().compareTo(file.getName());
        } else {
            result = 1;
        }
        return result;
    }

    public JFile(String s, String s1) {
        super(s, s1);
    }

    public JFile(File file, String s) {
        super(file, s);
    }

    public JFile(URI uri) {
        super(uri);
    }

    /**
     * ��дtoString������ʹ����Jlist����ʾ��ʱ��ֻ��ʾ��ǰ���ļ���
     *
     * @return
     */
    @Override
    public String toString() {
        if (this.isFile())
            return getName();
        else
            return "/" + this.getName();
    }

    public static void main(String[] args) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        JFile itm = new JFile(com.getPath());
        System.out.println(itm);
    }
}