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
     * 压缩文件或者文件夹
     *
     * @param sourcePath (将要压缩的文件)
     * @param outPath    输出文件的路径
     */
    private static void zip(String sourcePath, String outPath) {
        try {
            //文件输出流
            FileOutputStream fout = new FileOutputStream(outPath);
            //写入数据校验和的输出流，校验和可用于校验输出数据的完整性
            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fout, new CRC32());
            //zip格式的输出流
            ZipOutputStream zout = new ZipOutputStream(checkedOutputStream);  //校验输出流传入
            //将一个原文件写入到一个压缩文件中
            File sourceFile = new File(sourcePath);
            //压缩条目
            String zipEntryName = sourceFile.getName();

            if (sourceFile.isFile()) {
                //压缩文件
                zipFile(zout, sourceFile, zipEntryName);
            } else {
                //压缩目录
                zipDirectory(zout, sourceFile, zipEntryName);
            }
            zout.close();
            System.out.println("校验和为：" + checkedOutputStream.getChecksum().getValue());
            checkedOutputStream.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件夹
     *
     * @param zout         zip格式的文件输出流
     * @param sourceFile   原文件路径（将要压缩的文件夹）
     * @param zipEntryName 压缩条目（实际上就是一个文件的相对路径）
     * @throws IOException
     */
    private static void zipDirectory(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //压缩目录，遍历目录里面的所有文件(当且仅当文件夹只有文件才能压缩，不含有其他文件夹)
        for (File file : sourceFile.listFiles()) {
            if (file.isFile()) { //如果是文件，则直接调用压缩文件的方法进行压缩
                zipFile(zout, file, zipEntryName + "/" + file.getName());
            } else {
                //说明现在的file是目录，则需要将该目录的所有文件压缩
                if (file.listFiles().length > 0) {  //非空文件夹
                    //递归调用压缩子文件夹的方法
                    zipDirectory(zout, file, zipEntryName + "/" + file.getName());   //内容是： MFC/新建文件夹
                } else {
                    //空文件夹
                    //将压缩条目写入到压缩对象中
                    zout.putNextEntry(new ZipEntry(zipEntryName + "/" + file.getName() + "/"));   //最后添加斜线（通知解释器知道这是目录）
                    zout.closeEntry();
                }
            }
        }
    }

    /**
     * @param zout         zip格式的文件输出流
     * @param sourceFile   原文件路径（将要压缩的文件）
     * @param zipEntryName 压缩条目（实际上就是文件的相对路径）
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zout, File sourceFile, String zipEntryName) throws IOException {
        //将一个将要压缩的文件写入到压缩条目中
        zout.putNextEntry(new ZipEntry(zipEntryName));
        //读入将要压缩的文件
        FileInputStream fin = new FileInputStream(sourceFile);
        byte[] buff = new byte[1024];
        int length;
        //将原文件写入到zip格式输出流
        while ((length = fin.read(buff)) > 0) {
            zout.write(buff, 0, length);
        }
        fin.close();
        zout.closeEntry();
    }
}

