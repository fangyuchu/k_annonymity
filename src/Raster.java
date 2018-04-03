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
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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
    public double sumArea=0;                        //划分后区域的面积和
    public int regionNum=0;                         //划分后区域的数量
    int mtk;                                      //more than 2k,多于2k的栅格个数
    int ltk;                                      //less than k
    int ek;                                       //equal k，在k到2k之间的栅格个数
    int cutNum=0;                                 //分割的次数
    int unionNum=0;                               //合并的栅格数(先合并，后又被吞并的栅格算多次)
    public Raster(int k,String  s[]){
        try{
            this.k=k;
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            init();
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public Raster(int k,String s){
        try{
            this.k=k;
            p=new Points(ReadExcel.readCell(s),0);
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Raster(int k,int num){
        this.k=k;
        p=new Points(num);
        init();
    }
    public void init(){
        density=(p.xmax-p.xmin)*(p.ymax-p.ymin)/p.num;
        pA=k*density; //pA=k*area/num; k/num需要取整
        double len=Math.sqrt(pA);
        int row=(int)Math.ceil((p.ymax-p.ymin)/len);
        int col=(int)Math.ceil((p.xmax-p.xmin)/len);
        pixel=null;
        pixel=new Points[row][];
        for(int i=0;i<row;i++){
            pixel[i]=new Points[col];
            for(int j=0;j<col;j++){
                pixel[i][j]=new Points();
            }
        }
        ArrayList<ArrayList<ArrayList<Double[]>>> tempPixel=new ArrayList<>();
        for(int i=0;i<row;i++){
            tempPixel.add(new ArrayList<>());
            for(int j=0;j<col;j++){
                tempPixel.get(i).add(new ArrayList<>());
            }
        }
        for(int i=0;i<p.num;i++){
            int r=(int)((p.assemble[i].y()-p.ymin)/len);
            int c=(int)((p.assemble[i].x()-p.xmin)/len);
            Double[] temp={p.assemble[i].x(),p.assemble[i].y()};
            tempPixel.get(r).get(c).add(temp);
        }
        for(int i=0;i<pixel.length;i++){
            for(int j=0;j<pixel[0].length;j++){
                pixel[i][j].linkAdd(tempPixel.get(i).get(j));
            }
        }
        visit=new boolean[pixel.length][pixel[1].length];
        index=new int[pixel.length][pixel[1].length];
        kResult=new ArrayList<>();
        mtk=ltk=ek=0;
        for(int i=0;i<pixel.length;i++){
            for(int j=0;j<pixel[0].length;j++){
                if(pixel[i][j].num>2*k)mtk++;
                else if(pixel[i][j].num!=0&&pixel[i][j].num<k)ltk++;
                else if(pixel[i][j].num>=k&&pixel[i][j].num<=2*k)ek++;
            }
        }
    }
    public void print(){
        for(int i=0;i<p.num;i++){
            System.out.printf("%f\n%f\n",p.assemble[i].x(),p.assemble[i].y());
        }
    }
    public void findK(int min,int max){                     //给定k值范围[min,max]
        double T=5;                //初始温度
        double delta=0.6;           //降温速度,0.7时为15次
        double endT=0.000001;               //结束温度
        int step=20;                  //每次下降的范围为[1,1+step)
        int sol[]=new int[4];        //分4个范围进行退火尝试
        int range=(max-min)/4;                //每个范围大小
        //int min=10;                  //最小k定为10
        int repeat[]=new int[4];     // 已迭代失败的次数
        int stopTime=200;              //已迭代次数超过stopTime就停止迭代
        double res[]=new double[4];
        for(int i=0;i<4;i++){
            sol[i]=(i+1)*range-range/2+min;         //每range为一个范围，初始值为中间值
        }
        for(int i=0;i<4;i++){
            res[i]=getKResult(sol[i]);
        }
        while(T>endT){
            for(int i=0;i<4;i++){
                //int temp=(int)((new Random().nextDouble()-0.5)*step);
                int newSol=sol[i]-(int)((new Random().nextDouble()-0.5)*step);
                if(newSol==sol[i])newSol--;
                if(repeat[i]<=stopTime&&newSol>i*range+min&&newSol<=(i+1)*range+min){            //确保不会迭代到上下一个范围内
                    double newRes=getKResult(newSol);
                    if(newRes-res[i]<0){
                        sol[i]=newSol;
                        res[i]=newRes;
                        repeat[i]=0;
                    }else{
                        if(new Random().nextDouble()< Math.exp(-(newRes-res[i]) / T)){
                            sol[i]=newSol;
                            res[i]=newRes;
                            repeat[i]=0;
                        }else {
                            repeat[i]++;
                        }
                    }
                }
            }
            T=T*delta;
            step=(int)(step*delta)>2?(int)(step*delta):3;
        }
        int minK=sol[0];
        double minRes=res[0];
        for(int i=1;i<4;i++){
            if(res[i]<minRes){
                minK=sol[i];
                minRes=res[i];
            }
        }
        this.k=minK;
        init();
    }
    public double getKResult(int k){        //获得评价指标，指标需要越小越好
        /*this.k=k;
        init();
        return (double)(mtk+ltk)/(double)ek;*/  //评价指标为栅格数为k的个数的多少
        this.k=k;
        init();
        partition();
        return calAreaCV();                     //评价指标为划分完后的差异系数
    }
    public void screening(double xmi,double xma,double ymi,double yma){
        //通过raster.screening调用，不得直接调用Points.screening
        p.screening(xmi,xma,ymi,yma);
        init();
    }
    public void partition(){        //对栅格化后的数据进行处理，栅格点数不足k进行合并。对处理后对数据默认用地理中线划分
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
            cutNum+=kResult.get(i).roads.numCuttingLine;
            regionNum+=kResult.get(i).region.size();
            sumArea+=kResult.get(i).sumArea;
        }
    }
    public void reunion(int i,int j){
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
                }
                if (flag == true) {                                     //下层可合并
                    for(int z=0;z<col;z++){
                        sum+=pixel[stCoor[0] + row][stCoor[1] + z].num;
                    }
                    row++;
                }

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
                }
                if (flag == true){                          //右侧可合并
                    for(int z=0;z<row;z++){
                        sum += pixel[stCoor[0] + z][stCoor[1] + col].num;
                    }
                    col++;
                }
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
            //直接从单个栅格开始，而不是去掉0，是不是更好？
            stCoor[0]=i;
            stCoor[1]=j;
            row=col=1;
            try {
                /*//把周边多余的点数为0的栅格去除
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
                }*/
                //像左、上扩展
                int times=0;
                while(!checkSum(stCoor[0], stCoor[1], row, col)) {
                    int[] temp;
                    if(stCoor[0]==0){
                        temp=checkIndex(stCoor[0], stCoor[1] - 1, row, col + 1); //左边第一格开始
                    }else if(stCoor[1]==0){
                        temp=checkIndex(stCoor[0] - 1, stCoor[1], row + 1, col);    //上方第一格开始
                    }else{
                        temp= times++ % 2 == 0 ?
                                checkIndex(stCoor[0], stCoor[1] - 1, row, col + 1) : //左边第一格开始
                                checkIndex(stCoor[0] - 1, stCoor[1], row + 1, col);    //上方第一格开始*/
                    }
                    while (temp[0] != stCoor[0] || temp[1] != stCoor[1]) {
                        row += (stCoor[0] - temp[0]);
                        col += (stCoor[1] - temp[1]);
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
                        //向右下搜索，先下再右
                        int x = i;
                        int y = j+1;
                        boolean bingo=false;
                        for(;y<pixel.length&&!bingo;y++) {
                            for(x=i;x>=0;x--) {
                                if(visit[x][y]||pixel[x][y].num+pixel[i][j].num>=k){
                                    bingo=true;
                                    break;
                                }
                            }
                        }
                        int[] temp = findIndex(x,y);
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
                unionNum++;
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
    public void testShow(){                         //展示合并后栅格的情况
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
    public double calAreaCV(){          //计算划分后各匿名区域面积之间的差异系数
        ArrayList<Double>areaResult=new ArrayList<>();
        double sumArea=0;
        for(int i=0;i<kResult.size();i++){
            for(int j=0;j<kResult.get(i).area.length;j++) {
                areaResult.add(kResult.get(i).area[j]);
                sumArea+=kResult.get(i).area[j];
            }
        }
        double averageArea=sumArea/areaResult.size();
        double areaVariance=0;
        for(int i=0;i<areaResult.size();i++){
            areaVariance+=Math.pow(areaResult.get(i)-averageArea,2);
        }
        double DV=Math.pow(areaVariance/areaResult.size(),0.5);         //标准差
        double CV=DV/averageArea;                                       //差异系数

        return CV;
    }
    public void Draw(String title){
        new DrawRaster(this,title);
    }
    /*public void testShowTrend(){
    ArrayList<Double> kTrend=new ArrayList<>();
    for(int l=10;l<210;l++){
        this.k=l;
        init();
        System.out.printf("%d %f\n",l,(double)(mtk+ltk)/(double)ek);
        mtk=ek=ltk=0;
    }
}*/
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
        //2008-10-23 8：00-12：00("000-20081023（08-12）","015-20081023（08-12）","011-20081023（08-12）","012-20081023（08-12）","013-20081023（08-12）","014-20081023（08-12）","001-20081023（08-12）" )
        //2008-12-3 0：00-12：00("001-0：00-12：00","002-0：00-12：00","003-0：00-12：00","006-0：00-12：00","011-2：00-11：00","012-1：00-12：00","013-0：00-11：00","014-0：00-12：00","015-4：00-12：00","016-9：00-12：00","017-5：00-12：00")
        //2008-12-14 5：00-16：00("002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00")
        //20081024("20081024000126","20081024000805","20081024002706","20081024004733","20081024010406","20081024011938","20081024015454","20081024020227","20081024020959","20081024041230","20081024080126")
        //20081025("20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833")
        long startRun = System.currentTimeMillis();
        String[] trajectory = {"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"   //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";
        //第一个试验的"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"
        //
       /* String[] trajectory = {"20081024000126","20081024000805","20081024002706","20081024004733","20081024010406","20081024011938","20081024015454","20081024020227","20081024020959","20081024041230","20081024080126"
        };
        String title="20081024";*/
        /*String[] trajectory = {"20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833"

        };
        String title="20081025";*/
        /*Raster test=new Raster(200, DrawPoint.file(title, trajectory));
        test.findK(10,200);
        System.out.println(test.k);*/
        //需不需要记录分割的数量和合并的数量，栅格面积小并没有明显改变运行速度
        for(int k=10;k<=200;k++){
            long start=System.currentTimeMillis();
            Raster test = new Raster(k, DrawPoint.file(title, trajectory));
            test.partition();
            long time=System.currentTimeMillis()-start;
            double eqNum=(double)(test.mtk+test.ltk)/(double)test.ek;
            double cv=test.calAreaCV();
            System.out.printf("%d,%f,%f,%d,%d,%d,%d,%d\n",k,eqNum,cv,test.cutNum,test.unionNum,test.cutNum+test.unionNum,test.regionNum,test.pixel.length*test.pixel[0].length);
        }
        //test.testShow();



        //int k=200;
        /*for(int k=40;k<500;k=k+20) {
            Raster test = new Raster(k, DrawPoint.file(title, trajectory));
            test.partition();
            //test.testShow();
            System.out.println(k);
            System.out.printf("raster distance: %f area: %f\n", test.sumDistance, test.sumArea);

            Kanonymity testk = new Kanonymity(test.k, DrawPoint.file(title, trajectory));
            testk.partitionCentralLineK(testk.p, 0);
            testk.calDistance();
            testk.calArea();
            System.out.printf("central distance: %f area: %f\n\n", testk.sumDistance, testk.sumArea);
            new DrawSee(testk, title + "中线");
        //}
*/
        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start

    }
}

