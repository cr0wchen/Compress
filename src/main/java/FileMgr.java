import javax.swing.filechooser.FileSystemView;
import java.io.*;

/**
 * 用于管理同一目录下的文件及其对应操作
 */
public class FileMgr {
    private File com;//当前文件夹的实例
    private File[] copyArr;//复制文件时的File数组

    /**
     * 是否可以复制
     *
     * @return true时可以
     */
    public boolean hasCopyBuff() {
        return copyArr != null;
    }

    /**
     * 复制copyArr中为文件到path目录
     *
     * @param path 目标路径
     * @throws IOException
     */
    public void paste(String path) throws IOException {
        for (File f : this.copyArr) {
            int idx = path.indexOf(f.getAbsolutePath());
            if (idx != -1) {
                //此时path是待复制的文件夹的子文件夹，将父文件夹复制到子文件递归将结束不了
                return;
            }
        }
        if (copyArr == null) return;//不存在待复制的文件
        paste(this.copyArr, path);//递归地复制文件
        this.copyArr = null;//清空缓冲
    }

    /**
     * 递归地将srcArr中的文件复制到path路径
     *
     * @param srcArr 待复制的文件
     * @param path   目标路径
     * @throws IOException
     */
    private void paste(File[] srcArr, String path) throws IOException {
        for (File f : srcArr) {//遍历其中为每一个文件
            if (f.isFile()) {//如果是文件，直接复制
                copyFile(f.getAbsolutePath(), path + File.separator + f.getName());
            } else {//如果是文件夹，先创建文件夹再复制
                String fName = path + File.separator + f.getName();
                File file = new File(fName);
                file.mkdir();//创建文件夹
                paste(f.listFiles(), fName);//是文件夹，递归地复制下去
            }
        }
    }

    /**
     * 将待复制为文件数组引用赋值给copyArr
     *
     * @param copyArr 待复制File数组
     */
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

    /**
     * 递归地删除文件和文件夹
     *
     * @param file 待删除的文件或者文件夹
     */
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {//如果是文件或者空文件夹，直接删除
            file.delete();
        } else {//是非空文件夹的情况
            File[] files = file.listFiles();//获取当前非空文件夹下面的文件数组
            for (int i = 0; i < files.length; i++) {//遍历其中的每一个并删除
                deleteAll(files[i]);//递归地进行下去，先删除更深层的文件或文件夹
                files[i].delete();//再删除该文件夹本身
            }
            if (file.exists())         //如果文件本身就是目录 ，就要删除目录
                file.delete();
        }
    }

    /**
     * 构造函数，默认将文件路径设为桌面
     */
    public FileMgr() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        com = fsv.getHomeDirectory();
    }

    /**
     * 获取父目录的File
     *
     * @return
     */
    public File getParentFile() {
        return com.getParentFile();
    }

    /**
     * 得到当前文件夹的路径
     *
     * @return 当前文件夹的路径
     */
    public String getPath() {
        return com.getPath();
    }

    /**
     * 改变当前文件夹路径
     *
     * @param newPath 新路径
     * @return true代表成功
     */
    public boolean changeFile(String newPath) {
        String prePath = com.getPath();//获取当前路径
        com = new File(newPath);//创建新目录
        if (com == null) {//若失败，回复默认的桌面路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            com = fsv.getHomeDirectory();
            return false;
        }
        if (com.isFile()) {//如果是文件，恢复为原来的路径
            com = new File(prePath);
            return false;
        }
        return true;
    }

    /**
     * 返回上层目录
     *
     * @return true代表成功
     */
    public boolean upPath() {
        boolean result;
        String parentPath = com.getParent();//获取父文件夹的路径
        if (parentPath == null) {//获取失败，已经是根目录，无父目录
            result = false;
        } else {
            com = new File(parentPath);//成功则更改当前目录为父目录
            result = true;
        }
        return result;
    }

    /**
     * 获取父目录路径
     *
     * @return 父目录路径
     */
    public String getParent() {
        return com.getParent();
    }

    /**
     * 获取当前路径的文件夹名
     *
     * @return 当前路径的文件夹名
     */
    public String getName() {
        return com.getName();
    }

    /**
     * 获取当前文件夹下的文件
     *
     * @return 当前文件夹下的文件的路径
     */
    public String[] list() {
        return com.list();
    }

    /**
     * 获取当前文件夹下的文件
     *
     * @return 当前文件夹下的文件File
     */
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
