import javax.swing.filechooser.FileSystemView;
import java.io.*;

/**
 * ���ڹ���ͬһĿ¼�µ��ļ������Ӧ����
 */
public class FileMgr {
    private File com;//��ǰ�ļ��е�ʵ��
    private File[] copyArr;//�����ļ�ʱ��File����

    /**
     * �Ƿ���Ը���
     *
     * @return trueʱ����
     */
    public boolean hasCopyBuff() {
        return copyArr != null;
    }

    /**
     * ����copyArr��Ϊ�ļ���pathĿ¼
     *
     * @param path Ŀ��·��
     * @throws IOException
     */
    public void paste(String path) throws IOException {
        for (File f : this.copyArr) {
            int idx = path.indexOf(f.getAbsolutePath());
            if (idx != -1) {
                //��ʱpath�Ǵ����Ƶ��ļ��е����ļ��У������ļ��и��Ƶ����ļ��ݹ齫��������
                return;
            }
        }
        if (copyArr == null) return;//�����ڴ����Ƶ��ļ�
        paste(this.copyArr, path);//�ݹ�ظ����ļ�
        this.copyArr = null;//��ջ���
    }

    /**
     * �ݹ�ؽ�srcArr�е��ļ����Ƶ�path·��
     *
     * @param srcArr �����Ƶ��ļ�
     * @param path   Ŀ��·��
     * @throws IOException
     */
    private void paste(File[] srcArr, String path) throws IOException {
        for (File f : srcArr) {//��������Ϊÿһ���ļ�
            if (f.isFile()) {//������ļ���ֱ�Ӹ���
                copyFile(f.getAbsolutePath(), path + File.separator + f.getName());
            } else {//������ļ��У��ȴ����ļ����ٸ���
                String fName = path + File.separator + f.getName();
                File file = new File(fName);
                file.mkdir();//�����ļ���
                paste(f.listFiles(), fName);//���ļ��У��ݹ�ظ�����ȥ
            }
        }
    }

    /**
     * ��������Ϊ�ļ��������ø�ֵ��copyArr
     *
     * @param copyArr ������File����
     */
    public void copy(File[] copyArr) {
        this.copyArr = copyArr;
    }

    /**
     * ���Ƶ����ļ�
     *
     * @param src Դ��ַ
     * @param des Ŀ�ĵ�ַ
     * @throws IOException
     */
    private static void copyFile(String src, String des) throws IOException {
        BufferedInputStream ibs = new BufferedInputStream(new FileInputStream(src));//������
        BufferedOutputStream obs = new BufferedOutputStream(new FileOutputStream(des));//�����
        byte[] buff = new byte[1024];//������
        int m;
        while ((m = ibs.read(buff)) != -1) {//�Ӷ������ж�ȡ���ݣ�ֱ��ĩβ
            obs.write(buff, 0, m);//������д�������
        }
        ibs.close();//�ر���
        obs.close();
    }

    /**
     * �ݹ��ɾ���ļ����ļ���
     *
     * @param file ��ɾ�����ļ������ļ���
     */
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {//������ļ����߿��ļ��У�ֱ��ɾ��
            file.delete();
        } else {//�Ƿǿ��ļ��е����
            File[] files = file.listFiles();//��ȡ��ǰ�ǿ��ļ���������ļ�����
            for (int i = 0; i < files.length; i++) {//�������е�ÿһ����ɾ��
                deleteAll(files[i]);//�ݹ�ؽ�����ȥ����ɾ���������ļ����ļ���
                files[i].delete();//��ɾ�����ļ��б���
            }
            if (file.exists())         //����ļ��������Ŀ¼ ����Ҫɾ��Ŀ¼
                file.delete();
        }
    }

    /**
     * ���캯����Ĭ�Ͻ��ļ�·����Ϊ����
     */
    public FileMgr() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        com = fsv.getHomeDirectory();
    }

    /**
     * ��ȡ��Ŀ¼��File
     *
     * @return
     */
    public File getParentFile() {
        return com.getParentFile();
    }

    /**
     * �õ���ǰ�ļ��е�·��
     *
     * @return ��ǰ�ļ��е�·��
     */
    public String getPath() {
        return com.getPath();
    }

    /**
     * �ı䵱ǰ�ļ���·��
     *
     * @param newPath ��·��
     * @return true����ɹ�
     */
    public boolean changeFile(String newPath) {
        String prePath = com.getPath();//��ȡ��ǰ·��
        com = new File(newPath);//������Ŀ¼
        if (com == null) {//��ʧ�ܣ��ظ�Ĭ�ϵ�����·��
            FileSystemView fsv = FileSystemView.getFileSystemView();
            com = fsv.getHomeDirectory();
            return false;
        }
        if (com.isFile()) {//������ļ����ָ�Ϊԭ����·��
            com = new File(prePath);
            return false;
        }
        return true;
    }

    /**
     * �����ϲ�Ŀ¼
     *
     * @return true����ɹ�
     */
    public boolean upPath() {
        boolean result;
        String parentPath = com.getParent();//��ȡ���ļ��е�·��
        if (parentPath == null) {//��ȡʧ�ܣ��Ѿ��Ǹ�Ŀ¼���޸�Ŀ¼
            result = false;
        } else {
            com = new File(parentPath);//�ɹ�����ĵ�ǰĿ¼Ϊ��Ŀ¼
            result = true;
        }
        return result;
    }

    /**
     * ��ȡ��Ŀ¼·��
     *
     * @return ��Ŀ¼·��
     */
    public String getParent() {
        return com.getParent();
    }

    /**
     * ��ȡ��ǰ·�����ļ�����
     *
     * @return ��ǰ·�����ļ�����
     */
    public String getName() {
        return com.getName();
    }

    /**
     * ��ȡ��ǰ�ļ����µ��ļ�
     *
     * @return ��ǰ�ļ����µ��ļ���·��
     */
    public String[] list() {
        return com.list();
    }

    /**
     * ��ȡ��ǰ�ļ����µ��ļ�
     *
     * @return ��ǰ�ļ����µ��ļ�File
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
