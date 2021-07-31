package com.compress.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.*;

/**
 * 解压缩文件的类
 */
public class EntryMgr {
    private static byte[] buff = new byte[1024]; //缓冲区

    /**
     * 压缩数据中为文件到目标文件
     *
     * @param srcFiles   待压缩的文件
     * @param targetFile 目标文件
     * @throws IOException
     */
    public static void zip(File[] srcFiles, String targetFile, int level) throws IOException {
        //文件输出流
        FileOutputStream fout = new FileOutputStream(targetFile);
        //写入数据校验和的输出流，校验和可用于校验输出数据的完整性
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fout, new CRC32());
        //zip格式的输出流
        ZipOutputStream zout = new ZipOutputStream(checkedOutputStream, Charset.forName("gbk"));  //校验输出流传入
        zout.setLevel(level);
        zip(zout, srcFiles, "");//相对路径为空
        zout.close();
        fout.close();
    }

    /**
     * 递归地压缩文件
     *
     * @param zout         zip输出流
     * @param srcArr       待压缩的文件
     * @param zipEntryName 相对路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zout, File[] srcArr, String zipEntryName) throws IOException {
        //压缩目录，遍历目录里面的所有文件
        for (File file : srcArr) {
            if (file.isFile()) { //如果是文件，则直接调用压缩文件的方法进行压缩
                zipFile(zout, file, zipEntryName + File.separator + file.getName());
            } else {
                //说明现在的file是目录，则需要将该目录的所有文件压缩
                if (file.listFiles().length > 0) {  //非空文件夹
                    //递归调用压缩子文件夹的方法
                    zip(zout, file.listFiles(), zipEntryName + File.separator + file.getName());
                } else {
                    //空文件夹
                    //将压缩条目写入到压缩对象中,最后添加斜线（通知解释器知道这是目录）
                    zout.putNextEntry(new ZipEntry(zipEntryName + File.separator + file.getName() + File.separator));
                    zout.closeEntry();
                }
            }
        }
    }


    /**
     * @param zout         zip格式的文件输出流
     * @param sourceFile   源文件
     * @param zipEntryName 压缩条目（实际上就是文件的相对路径）
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //将一个将要压缩的文件写入到压缩条目中
        zout.putNextEntry(new ZipEntry(zipEntryName));
        //读入将要压缩的文件
        FileInputStream fin = new FileInputStream(sourceFile);
        int length;
        //将原文件写入到zip格式输出流
        while ((length = fin.read(buff)) > 0) {
            zout.write(buff, 0, length);
        }
        fin.close();
        zout.closeEntry();
    }

    /**
     * 解压文件或文件夹
     *
     * @param sourcePath 原文件（待压缩文件的路径）
     * @param outPath    输出文件路径
     */
    public static void upZip(String sourcePath, String outPath) {
        try {
            //文件输入流
            FileInputStream fin = new FileInputStream(sourcePath);
            //所读取数据校验和的输入流，校验和可用于验证输入数据的完整性
            CheckedInputStream checkedInputStream = new CheckedInputStream(fin, new CRC32());
            //zip格式的输入流
            ZipInputStream zin = new ZipInputStream(checkedInputStream, Charset.forName("gbk"));   //校验输入流传入，设置字符集为GBK不然解压中文文件名的文件有问题
            ZipEntry zipEntry;
            //遍历压缩文件中的所有压缩条目
            while ((zipEntry = zin.getNextEntry()) != null) {
                //将压缩条目输出到输出路径
                File targetFile = new File(outPath + File.separator + zipEntry.getName());
                //判断目标输出文件的父目录是否存在；如果不存在的话则需要创建一个父文件夹
                if (!targetFile.getParentFile().exists()) {
                    //不存在父文件夹
                    targetFile.getParentFile().mkdirs();
                }
                if (zipEntry.isDirectory()) {
                    //判断当前的压缩条目是否是文件夹，如果是文件夹的话，直接在输出路径创建文件即可
                    targetFile.mkdirs();
                } else {
                    //当前的压缩条目是一个文件，需要将输入的压缩文件内容输出到输出路径
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
            System.out.println("校验和为：" + checkedInputStream.getChecksum().getValue());
            checkedInputStream.close();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //原文件（将解压文件的路径）
        String sourcePath = "C:\\Users\\陈旭峰\\Desktop\\ls\\empty";
        //输出文件路径
        String outPath = "C:\\Users\\陈旭峰\\Desktop\\ls\\ercha.zip";

        String curPath = "C:\\Users\\陈旭峰\\Desktop\\ls";

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
