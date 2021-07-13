package com.compress.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by PC on 2019/3/18.
 */
public class UnZipFileMgr {
    public static void main(String[] args) {
        //原文件（将解压文件的路径）
        String sourcePath = "C:\\Users\\陈旭峰\\Desktop\\ls\\A";
        //输出文件路径
        String outPath = "C:\\Users\\陈旭峰\\Desktop\\ls";


        upZip(sourcePath, outPath);

    }

    /**
     * 解压文件或文件夹
     * @param sourcePath  原文件（待压缩文件的路径）
     * @param outPath   输出文件路径
     */
    public static void upZip(String sourcePath, String outPath) {
        try {
            //文件输入流
            FileInputStream fin = new FileInputStream(sourcePath);
            //zip格式的输入流
            ZipInputStream zin = new ZipInputStream(fin, Charset.forName("gbk"));
            ZipEntry zipEntry;
            //遍历压缩文件中的所有压缩条目
            while((zipEntry = zin.getNextEntry())!= null) {
                //将压缩条目输出到输出路径
                File targetFile = new File(outPath + File.separator + zipEntry.getName());
                System.out.println(targetFile.getName());
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
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}