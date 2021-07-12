package com.compress.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIpFileMgr {
    public static String inPath = "", outPath = "";
    private static byte[] buff = new byte[1024];

    /**
     * ѹ���ļ������ļ���
     *
     * @param sourcePath (��Ҫѹ�����ļ�)
     * @param outPath    ����ļ���·��
     */
    private static void zip(String sourcePath, String outPath) {
        try {
            //�ļ������
            FileOutputStream fout = new FileOutputStream(outPath);
            //д������У��͵��������У��Ϳ�����У��������ݵ�������
            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fout, new CRC32());
            //zip��ʽ�������
            ZipOutputStream zout = new ZipOutputStream(checkedOutputStream);  //У�����������
            //��һ��ԭ�ļ�д�뵽һ��ѹ���ļ���
            File sourceFile = new File(sourcePath);
            //ѹ����Ŀ
            String zipEntryName = sourceFile.getName();

            if (sourceFile.isFile()) {
                //ѹ���ļ�
                zipFile(zout, sourceFile, zipEntryName);
            } else {
                //ѹ��Ŀ¼
                zipDirectory(zout, sourceFile, zipEntryName);
            }
            zout.close();
            System.out.println("У���Ϊ��" + checkedOutputStream.getChecksum().getValue());
            checkedOutputStream.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ѹ���ļ���
     *
     * @param zout         zip��ʽ���ļ������
     * @param sourceFile   ԭ�ļ�·������Ҫѹ�����ļ��У�
     * @param zipEntryName ѹ����Ŀ��ʵ���Ͼ���һ���ļ������·����
     * @throws IOException
     */
    private static void zipDirectory(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //ѹ��Ŀ¼������Ŀ¼����������ļ�(���ҽ����ļ���ֻ���ļ�����ѹ���������������ļ���)
        for (File file : sourceFile.listFiles()) {
            if (file.isFile()) { //������ļ�����ֱ�ӵ���ѹ���ļ��ķ�������ѹ��
                zipFile(zout, file, zipEntryName + "/" + file.getName());
            } else {
                //˵�����ڵ�file��Ŀ¼������Ҫ����Ŀ¼�������ļ�ѹ��
                if (file.listFiles().length > 0) {  //�ǿ��ļ���
                    //�ݹ����ѹ�����ļ��еķ���
                    zipDirectory(zout, file, zipEntryName + "/" + file.getName());   //�����ǣ� MFC/�½��ļ���
                } else {
                    //���ļ���
                    //��ѹ����Ŀд�뵽ѹ��������
                    zout.putNextEntry(new ZipEntry(zipEntryName + "/" + file.getName() + "/"));   //������б�ߣ�֪ͨ������֪������Ŀ¼��
                    zout.closeEntry();
                }
            }
        }
    }

    /**
     * @param zout         zip��ʽ���ļ������
     * @param sourceFile   ԭ�ļ�·������Ҫѹ�����ļ���
     * @param zipEntryName ѹ����Ŀ��ʵ���Ͼ����ļ������·����
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //��һ����Ҫѹ�����ļ�д�뵽ѹ����Ŀ��
        zout.putNextEntry(new ZipEntry(zipEntryName));
        //���뽫Ҫѹ�����ļ�
        FileInputStream fin = new FileInputStream(sourceFile);
        byte[] buff = new byte[1024];
        int length;
        //��ԭ�ļ�д�뵽zip��ʽ�����
        while ((length = fin.read(buff)) > 0) {
            zout.write(buff, 0, length);
        }
        fin.close();
        zout.closeEntry();
    }
}

