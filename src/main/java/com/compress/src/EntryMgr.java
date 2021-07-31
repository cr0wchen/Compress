package com.compress.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.*;

/**
 * ��ѹ���ļ�����
 */
public class EntryMgr {
    private static byte[] buff = new byte[1024]; //������

    /**
     * ѹ��������Ϊ�ļ���Ŀ���ļ�
     *
     * @param srcFiles   ��ѹ�����ļ�
     * @param targetFile Ŀ���ļ�
     * @throws IOException
     */
    public static void zip(File[] srcFiles, String targetFile, int level) throws IOException {
        //�ļ������
        FileOutputStream fout = new FileOutputStream(targetFile);
        //д������У��͵��������У��Ϳ�����У��������ݵ�������
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fout, new CRC32());
        //zip��ʽ�������
        ZipOutputStream zout = new ZipOutputStream(checkedOutputStream, Charset.forName("gbk"));  //У�����������
        zout.setLevel(level);
        zip(zout, srcFiles, "");//���·��Ϊ��
        zout.close();
        fout.close();
    }

    /**
     * �ݹ��ѹ���ļ�
     *
     * @param zout         zip�����
     * @param srcArr       ��ѹ�����ļ�
     * @param zipEntryName ���·��
     * @throws IOException
     */
    private static void zip(ZipOutputStream zout, File[] srcArr, String zipEntryName) throws IOException {
        //ѹ��Ŀ¼������Ŀ¼����������ļ�
        for (File file : srcArr) {
            if (file.isFile()) { //������ļ�����ֱ�ӵ���ѹ���ļ��ķ�������ѹ��
                zipFile(zout, file, zipEntryName + File.separator + file.getName());
            } else {
                //˵�����ڵ�file��Ŀ¼������Ҫ����Ŀ¼�������ļ�ѹ��
                if (file.listFiles().length > 0) {  //�ǿ��ļ���
                    //�ݹ����ѹ�����ļ��еķ���
                    zip(zout, file.listFiles(), zipEntryName + File.separator + file.getName());
                } else {
                    //���ļ���
                    //��ѹ����Ŀд�뵽ѹ��������,������б�ߣ�֪ͨ������֪������Ŀ¼��
                    zout.putNextEntry(new ZipEntry(zipEntryName + File.separator + file.getName() + File.separator));
                    zout.closeEntry();
                }
            }
        }
    }


    /**
     * @param zout         zip��ʽ���ļ������
     * @param sourceFile   Դ�ļ�
     * @param zipEntryName ѹ����Ŀ��ʵ���Ͼ����ļ������·����
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //��һ����Ҫѹ�����ļ�д�뵽ѹ����Ŀ��
        zout.putNextEntry(new ZipEntry(zipEntryName));
        //���뽫Ҫѹ�����ļ�
        FileInputStream fin = new FileInputStream(sourceFile);
        int length;
        //��ԭ�ļ�д�뵽zip��ʽ�����
        while ((length = fin.read(buff)) > 0) {
            zout.write(buff, 0, length);
        }
        fin.close();
        zout.closeEntry();
    }

    /**
     * ��ѹ�ļ����ļ���
     *
     * @param sourcePath ԭ�ļ�����ѹ���ļ���·����
     * @param outPath    ����ļ�·��
     */
    public static void upZip(String sourcePath, String outPath) {
        try {
            //�ļ�������
            FileInputStream fin = new FileInputStream(sourcePath);
            //����ȡ����У��͵���������У��Ϳ�������֤�������ݵ�������
            CheckedInputStream checkedInputStream = new CheckedInputStream(fin, new CRC32());
            //zip��ʽ��������
            ZipInputStream zin = new ZipInputStream(checkedInputStream, Charset.forName("gbk"));   //У�����������룬�����ַ���ΪGBK��Ȼ��ѹ�����ļ������ļ�������
            ZipEntry zipEntry;
            //����ѹ���ļ��е�����ѹ����Ŀ
            while ((zipEntry = zin.getNextEntry()) != null) {
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
            System.out.println("У���Ϊ��" + checkedInputStream.getChecksum().getValue());
            checkedInputStream.close();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //ԭ�ļ�������ѹ�ļ���·����
        String sourcePath = "C:\\Users\\�����\\Desktop\\ls\\empty";
        //����ļ�·��
        String outPath = "C:\\Users\\�����\\Desktop\\ls\\ercha.zip";

        String curPath = "C:\\Users\\�����\\Desktop\\ls";

        File[] arr = new File[4];
        arr[0] = new File(curPath + File.separator + "A.zip");
        arr[1] = new File(curPath + File.separator + "B.zip");
        arr[2] = new File(curPath + File.separator + "C.zip");
        arr[3] = new File(curPath + File.separator + "workspace" + File.separator);
        System.out.println(arr[3].isDirectory());
//        System.out.println(arr[0].getName());
//        File[] arr = new File[1];
//        arr[0] = new File(curPath + File.separator + "A.zip");
        try {
            zip(arr, curPath + File.separator + "Digui_nameTest.zip",-1);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
}
