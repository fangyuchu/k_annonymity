/**
 * Created by fangyc on 17/04/2018.
 */
public class importFile {
    static String route="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/";                                                   //文件的系统路径
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
    //test git on windows
}
