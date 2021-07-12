package com.compress.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by PC on 2019/3/18.
 */
public class UnZipFileMgr {
    public static void main(String[] args) {
        //ԭ�ļ�������ѹ�ļ���·����
        String sourcePath = "E:\\CODE\\test\\MFC4.zip";
        //����ļ�·��
        String outPath = "E:\\CODE\\test2";

        upZip(sourcePath, outPath);

    }

    /**
     * ��ѹ�ļ����ļ���
     * @param sourcePath  ԭ�ļ�����ѹ���ļ���·����
     * @param outPath   ����ļ�·��
     */
    private static void upZip(String sourcePath, String outPath) {
        try {
            //�ļ�������
            FileInputStream fin = new FileInputStream(sourcePath);
            //zip��ʽ��������
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry zipEntry;
            //����ѹ���ļ��е�����ѹ����Ŀ
            while((zipEntry = zin.getNextEntry())!= null) {
                //��ѹ����Ŀ��������·��
                File targetFile = new File(outPath + File.separator + zipEntry.getName());
                //�ж�Ŀ������ļ��ĸ�Ŀ¼�Ƿ���ڣ���������ڵĻ�����Ҫ����һ�����ļ���
                if (!targetFile.getParentFile().exists()) {
                    //�����ڸ��ļ���
                    targetFile.getParentFile().mkdirs();
                }
                if (zipEntry.isDirectory()) {
                    //�жϵ�ǰ��ѹ����Ŀ�Ƿ����ļ��У�������ļ��еĻ���ֱ�������·�������ļ�����
                    targetFile.mkdirs();
                } else {
                    //��ǰ��ѹ����Ŀ��һ���ļ�����Ҫ�������ѹ���ļ�������������·��
                    FileOutputStream fout = new FileOutputStream(targetFile);
                    byte[] buff = new byte[1024];
                    int length;
                    while ((length = zin.read(buff)) > 0) {
                        fout.write(buff, 0, length);
                    }
                    fout.close();
                }
            }
            zin.close();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}