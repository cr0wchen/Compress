import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URI;

/**
 * 文件类，重写了toString和compareTo方法，用于存放在Jlist中
 */
public class JFile extends File {

    public JFile(String s) {
        super(s);
    }

    /**
     * File类原本的构造方法，由IEDA自动生成
     */
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
     * 重写toString方法，使其在Jlist中显示的时候只显示当前的文件名
     *
     * @return 显示在JList中的String
     */
    @Override
    public String toString() {
        if (this.isFile())
            return getName();
        else
            return "/" + this.getName();//文件夹前面加个斜杠
    }

    /**
     * 重写compareTo方法，使其文件夹优先
     *
     * @param file 用于比较的对象
     * @return -1,0,1分别代表小于、等于、大于
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

    public static void main(String[] args) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        JFile itm = new JFile(com.getPath());
        System.out.println(itm);
    }
}
