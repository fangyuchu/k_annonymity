import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fangyc on 12/10/2017.
 */
public class Raster {
    public Points p;                                //总位置点集合
    public double density;                          //点密度
    public double pA;                               //像素面积(pixelArea)
    public int k;                                   //k值
    public Points[][] pixel;                        //像素矩阵
    public int[][] index;                           //指引合并后的结果，0表示未合并
    private int ind=1;                              //自增的索引
    boolean[][] visit;                              //是否处理过
    public ArrayList<Kanonymity> kResult;
    public ArrayList<Points> region;                //划分后的区域点集合数组
    public DrawSee draw;
    public double sumDistance=0;
    public double sumArea=0;
    public Raster(){}
    public Raster(int k,String  s[]){
        try{
            this.k=k;
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            density=(p.xmax-p.xmin)*(p.ymax-p.ymin)/p.num;
            pA=k*density; //pA=k*area/num; k/num需要取整
            double len=Math.sqrt(pA);
            int row=(int)Math.ceil((p.ymax-p.ymin)/len);
            int col=(int)Math.ceil((p.xmax-p.xmin)/len);
            pixel=new Points[row][];
            for(int i=0;i<row;i++){
                pixel[i]=new Points[col];
                for(int j=0;j<col;j++){
                    pixel[i][j]=new Points();
                }
            }
            for(Points.Point a:p.assemble){
                int r=(int)((a.y()-p.ymin)/len);
                int c=(int)((a.x()-p.xmin)/len);
                pixel[r][c].linkAdd(a);
            }
            visit=new boolean[pixel.length][pixel[1].length];
            index=new int[pixel.length][pixel[1].length];
            kResult=new ArrayList<>();
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void partition(){
        Kanonymity temp;
        for(int i=0;i<pixel.length;i++){
            for(int j=0;j<pixel[i].length;j++){
                if(visit[i][j]||pixel[i][j].num>=k||pixel[i][j].num==0)continue;
                visit[i][j]=true;
                reunion(i,j);
            }
        }
        //将合并后的各区域按其索引合并其点
        ArrayList<Points> tempPoints=new ArrayList<>();
        for(int i=0;i<pixel.length;i++) {
            for (int j = 0; j < pixel[i].length; j++) {
                if(index[i][j]==0)continue;
                //方法为，索引为i，就将其插入到tempPoints[i]，tP长度不够就填充空点集
                if(tempPoints.size()-1<index[i][j]){
                    for(int k=tempPoints.size();k<=index[i][j];k++){
                        tempPoints.add(new Points());
                    }
                }
                tempPoints.get(index[i][j]).add(pixel[i][j]);
            }
        }
        for(int i=0;i<tempPoints.size();i++){
            if(tempPoints.get(i).num==0)continue;
            kResult.add(new Kanonymity(k,tempPoints.get(i)));
        }
        for(int i=0;i<pixel.length;i++){
            for(int j=0;j<pixel[i].length;j++){
                if(visit[i][j]||pixel[i][j].num==0)continue;
                visit[i][j]=true;
                temp=new Kanonymity(k,pixel[i][j]);
                index[i][j]=ind++;
                kResult.add(temp);
            }
        }
        for(int i=0;i<kResult.size();i++){
            kResult.get(i).partitionCentralLineK(kResult.get(i).p,1);
            kResult.get(i).calDistance();
            sumDistance+=kResult.get(i).sumDistance;
            kResult.get(i).calArea();
            sumArea+=kResult.get(i).sumArea;
        }
    }
    public void reunion(int i,int j){         //error
        boolean find=false;
        boolean flag=true;
        int stCoor[]={i,j};                     //startCoordinate,左上角的坐标
        int row,col;
        row=col=1;
        int sum=pixel[i][j].num;
        if(i>0&&!visit[i-1][j]&&pixel[i-1][j].num!=0){                     //上层可合并
            row++;
            stCoor[0]--;
            find=true;
        }else if(j>0&&!visit[i][j-1]&&pixel[i][j-1].num!=0){                      //左侧可合并
            col++;
            stCoor[1]--;
            find=true;
        }else{
            while (!(stCoor[0] + row == pixel.length  && stCoor[1] + col == pixel[1].length )) {//扩展到右下角
                for (int z = 0; z < col; z++) {              //尝试和下层合并
                    if (stCoor[0]+row==visit.length||visit[stCoor[0] + row][stCoor[1] + z]) {     //stCoor[0]+(row-1)+1
                        flag = false;
                        break;
                    }
                    sum += pixel[stCoor[0] + row][stCoor[1] + z].num;
                }
                if (flag == true) row++;                  //下层可合并
                if (sum >= k) {
                    find = true;
                    break;
                }
                flag = true;
                for (int z = 0; z < row; z++) {             //尝试和右侧合并
                    if (stCoor[1]+col==visit[1].length||visit[stCoor[0] + z][stCoor[1] + col]) {
                        flag = false;
                        break;
                    }
                    sum += pixel[stCoor[0] + z][stCoor[1] + col].num;
                }
                if (flag == true) col++;                 //右侧可合并
                if (sum >= k) {
                    find = true;
                    break;
                }
                flag=true;
                if(stCoor[0] + row == pixel.length  &&stCoor[1]+col<pixel[0].length&&visit[stCoor[0]][stCoor[1]+col]){ //已到最底部，但右侧被占用，无法扩展
                    stCoor[0]=i;
                    stCoor[1]=j;
                    row=col=1;
                    break;
                }
            }
        }
        if(!find) {
            //假如加到了右下角的情况
            try {
                //把周边多余的点数为0的栅格去除
                for(int x=row-1;x>=0;x--){
                    boolean zero=true;
                    for(int y=col-1;y>=0;y--){
                        if(pixel[stCoor[0]+x][stCoor[1]+y].num!=0){
                            zero=false;
                            break;
                        }
                    }
                    if(zero)row--;
                }
                for(int y=col-1;y>=0;y--){
                    boolean zero=true;
                    for(int x=row-1;x>=0;x--){
                        if(pixel[stCoor[0]+x][stCoor[1]+y].num!=0){
                            zero=false;
                            break;
                        }
                    }
                    if(zero)col--;
                }
                //像左、上扩展
                int times=0;
                while(!checkSum(stCoor[0], stCoor[1], row, col)) {
                    int[] temp = times++ % 2 == 0 ?
                            checkIndex(stCoor[0], stCoor[1] - 1, row, col + 1) : //左边第一格开始
                            checkIndex(stCoor[0] - 1, stCoor[1], row + 1, col);    //上方第一格开始
                    while (temp[0] != stCoor[0] || temp[1] != stCoor[1]) {
                        row += stCoor[0] - temp[0];
                        col += stCoor[1] - temp[1];
                        stCoor[0] = temp[0];
                        stCoor[1] = temp[1];
                        temp = checkIndex(stCoor[0], stCoor[1], row, col);
                    }
                }
            }catch (Exception e){
                try {
                    while(!checkSum(stCoor[0], stCoor[1], row, col)) {
                        //右边无法扩展，且左上不能满足要求但情况
                        stCoor[0] = i;
                        stCoor[1] = j;
                        row = col = 1;
                        int y = 1;
                        while (!visit[i][j + y]) y++;
                        int[] temp = findIndex(i, j + y);
                        while(temp[0]!=stCoor[0]||temp[1]!=stCoor[1]||temp[2]!=stCoor[0]+row-1||temp[3]!=stCoor[1]+col-1) {
                            row = Math.max(stCoor[0] + row - 1, temp[2]) - Math.min(stCoor[0], temp[0]) + 1;
                            col = Math.max(stCoor[1] + col - 1, temp[3]) - Math.min(stCoor[1], temp[1]) + 1;
                            stCoor[0] = Math.min(stCoor[0], temp[0]);
                            stCoor[1] = Math.min(stCoor[1], temp[1]);
                            temp = checkIndex(stCoor[0], stCoor[1], row, col);
                        }
                    }
                }catch (Exception e1){
                    System.out.printf("oops");
                }
            }

        }
        for(int x=0;x<row;x++){
            for(int y=0;y<col;y++){
                index[stCoor[0]+x][stCoor[1]+y]=ind;
                visit[stCoor[0]+x][stCoor[1]+y]=true;
            }
        }
        ind++;
    }
    public int[] checkIndex(int i,int j,int row,int col){
        //检查（i,j）为左上角，row、col的矩形是否含有属于别的矩形的栅格
        //返回分别为最上、最左、最下、最右的坐标(不一定是同一个格子)
        //（res[0],res[1],res[2],res[3])
        int[] res=new int[4];
        res[0]=i;
        res[1]=j;
        for(int x=0;x<row;x++){
           for(int y=0;y<col;y++){
               int[] temp=findIndex(i+x,j+y);
               res[0]=Math.min(temp[0],res[0]);
               res[1]=Math.min(temp[1],res[1]);
               res[2]=Math.max(temp[2],res[2]);
               res[3]=Math.max(temp[3],res[3]);
           }
        }
        return res;
    }
    public boolean checkSum(int i,int j,int row,int col){
        //检查（i,j）为左上角，row、col的矩形所含点数是否大于k
        int sum=0;
        for(int x=0;x<row;x++){
            for(int y=0;y<col;y++){
                sum+=pixel[i+x][j+y].num;
            }
        }
        return sum>=k;
    }
    public int[] findIndex(int i,int j){                
        //找到（i,j）所属矩形左上角和右下角的坐标
        //左上角（res[0],res[1]）
        //右下角 (res[2],res[3])
        int i1=i;
        int j1=j;
        int[] res=new int[4];
        if(index[i][j]==0){
            res[0]=res[2]=i;
            res[1]=res[3]=j;
            return res;
        }
        while(i1>=0&&index[i1][j1]==index[i][j])i1--;
        res[0]=++i1;
       // System.out.printf("i1:%d,j1:%d\n",i1,j1);
        while(j1>=0&&index[i1][j1]==index[i][j])j1--;
        res[1]=++j1;
        i1=i;
        j1=j;
        while(i1<pixel.length&&index[i1][j1]==index[i][j])i1++;
        res[2]=--i1;
        while(j1<pixel[0].length&&index[i1][j1]==index[i][j])j1++;
        res[3]=--j1;
        return res;
    }
    public void testShow(){
        for(int i=0;i<index.length;i++){
            for(int j=0;j<index[1].length;j++){
                if(pixel[i][j].num==0){
                    System.out.printf("%dz\t\t",index[i][j]);
                }else if(pixel[i][j].num<k){
                    System.out.printf("%dn\t\t",index[i][j],pixel[i][j].num);
                }else{
                    System.out.printf("%d\t\t",index[i][j],pixel[i][j].num);
                }
            }
            System.out.println();
        }
    }
    public void Draw(String title){
        new DrawRaster(this,title);
    }
    /*public void writeResult(String trajectory){
        File file =new File("/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/result"+trajectory);
        //如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory())
        {
            System.out.println("//不存在");
            file .mkdir();
        } else
        {
            System.out.println("//目录存在");
            //return;
        }
        String doc="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/result"+trajectory+"/"+"k="+String.valueOf(k)+"栅格化.xls";
        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为 new sheet
        Sheet sheet = wb.createSheet(trajectory);
        //创建一个文件 命名为workbook.xls
        try {
            FileOutputStream fileOut = new FileOutputStream(doc);
            // 把上面创建的工作簿输出到文件中
            wb.write(fileOut);
            //关闭输出流
            fileOut.close();
            InputStream inp = new FileInputStream(doc);
            int lastRowNum=sheet.getLastRowNum();
            inp.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    public static void main(String[] args){
        String[] trajectory = {"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"   //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";
        //int k=200;
        for(int k=40;k<500;k=k+20) {
            Raster test = new Raster(k, DrawPoint.file(title, trajectory));
            test.partition();
            //test.testShow();
            System.out.println(k);
            System.out.printf("raster distance: %f area: %f\n", test.sumDistance, test.sumArea);

            Kanonymity testk = new Kanonymity(k, DrawPoint.file(title, trajectory));
            testk.partitionCentralLineK(testk.p, 0);
            testk.calDistance();
            testk.calArea();
            System.out.printf("central distance: %f area: %f\n\n", testk.sumDistance, testk.sumArea);
           // new DrawSee(testk, title + "中线");
        }

    }
}

