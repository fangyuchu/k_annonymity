import java.io.File;

/**
 * Created by fangyc on 17/04/2018.
 */
public class importFile {
    static String route="D:/实验室/k匿名划分数据/";
    //文件的系统路径
    importFile(String title,String trajectory){
    }
    public static String file(String title,String s){
        String f=route+title+"/"+s+".xls";
        return f;
    }
    public static String[] file(String title, String[] s){
        String[] f=new String[s.length];
        for(int i=0;i<s.length;i++){
            f[i]=route+title+"/"+s[i]+".xls";
        }
        return f;
    }
    public static String[] file(String title){                      //自动获取titile文件夹中的所有文件的文件名，在这个项目中要求这个文件夹中均为excel文件
        File t=new File(route+title);
        File []files = t.listFiles();
        String[] f=new String[files.length];
        for(int i=0;i<f.length;i++){
            f[i]=files[i].getAbsolutePath();
        }
        return f;
    }
}
