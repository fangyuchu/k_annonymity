import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

/**
 * Created by fangyc on 19/04/2017.
 */
public class test {

    public  void getFileAndDirectory(File file){
        int countDirectory = 0;
        int countFile = 0;
        if(file.isDirectory()){
            File []files = file.listFiles();
            for(File fileIndex:files){
                if(fileIndex.isDirectory()){
                    countDirectory++;
                    getFileAndDirectory(fileIndex);
                }else {
                    countFile++;
                    System.out.println(fileIndex.getAbsolutePath());
                }
            }
        }
        System.out.println("目录文件数目为："+countDirectory);
        System.out.println("普通文件数目为："+countFile);
    }

    public static void main(String[] args){
        //HashMap<Integer,Integer> test=new HashMap<>();
        //test.put(1,2);
       // Integer a=test.get(1);
        //Integer b=test.get(2);
       // test a=new test();
        /*Raster t=new Raster(50,importFile.file("2008-12-14 5：00-16：00"));
        t.BUDE();
        t.testShow();
        System.out.println(t.successRateBUDE());*/

/*
        //对比dbscan效果的实验
        for(int j=0;j<importFile.files.length;j++) {
            String f=importFile.files[j];
            System.out.println(f);
            Raster t = new Raster(50, importFile.file(f));
            if(f.equals("20081025")){
                t.screen(30,90,116.28,200);
            }else if(f.equals("20081026")){
                t.screening(30,90,116.35,200);
            }
            long startTime = System.currentTimeMillis();
            t.dbscan(0.6, 10);
            long endTime = System.currentTimeMillis();
            System.out.printf("dbscan时间：");
            System.out.println(endTime - startTime);
            System.out.printf("k runTime sumArea sumDistance runTimeDB sumAreaDB sumDistanceDB\n");
            for (int k = 10; k < 200; k++) {
                t.k = k;
                t.init();
                t.stateCluster = false;
                startTime = System.currentTimeMillis();
                t.partition();
                endTime = System.currentTimeMillis();
                System.out.printf("%d ", k);
                System.out.print(endTime - startTime);
                System.out.printf(" %f %f ", t.sumArea, t.sumDistance);
                t.init();
                t.stateCluster = true;
                startTime = System.currentTimeMillis();
                t.partition();
                endTime = System.currentTimeMillis();
                System.out.print(endTime - startTime);
                System.out.printf(" %f %f\n", t.sumArea, t.sumDistance);

            }
        }
*/
        //改变栅格大小的实验
        for(int j=0;j<importFile.files.length;j++) {
            String f=importFile.files[j];
            System.out.println(f);
            Raster t = new Raster(50, importFile.file(f));
            if(f.equals("20081025")){
                t.screen(30,90,116.28,200);
            }else if(f.equals("20081026")){
                t.screening(30,90,116.35,200);
            }
            double density = (t.p.xmax - t.p.xmin)*Raster.lat * (t.p.ymax - t.p.ymin)*Raster.lon / t.p.num;
            double pA = 20 * density; //初始值为k=20时的面积
            System.out.println("originalGridSize");
            System.out.printf("%.10f\n",50*density);
            t.dbscan(0.6, 10);
            System.out.println("k regionNum gridSize sumArea averageArea sumDistance averageDistance");
            for (int i = 0; i < 20; i++) {
                double step = 0.1;        //每次增大step
                double gridSize = pA * Math.pow(1 + step, i);
                t.changeGridSize(gridSize);
                t.partition();
                System.out.printf("%d %d %.10f %.10f %.10f %.10f %.10f\n", t.k,t.regionNum, gridSize, t.sumArea, t.averageArea, t.sumDistance,t.averageDistance);
            }
        }





        //t.ExperimentOnGrid(0.1);


        //File f=new File("D:/实验室/k匿名划分数据/20081026");
        //a.getFileAndDirectory(f);
        /*ArrayList<Integer> t=new ArrayList<>();
        t.add(0,null);
        t.add(9,9);
        t.add(0);
        t.add(1);
        /*boolean[][] a;
        a=new boolean[3][3];
        a[0][1]=true;
       /* ArrayList<ArrayList<Points>> t=new ArrayList<>();
        ArrayList<Points> te=new ArrayList<>();
        Points a=new Points(3);
        Points b=new Points(5);
        te.add(a);
        te.add(b);
        a.deletePoint(a.assemble[0]);
        t.add(te);

       /* double[][] testPoints=new double[10][2];
        int i=0;
        testPoints[i][0]=49.234;
        testPoints[i++][1]=140.234;

        testPoints[i][0]=48.234 ;
        testPoints[i++][1]=146.657 ;

        testPoints[i][0]= 40.32;
        testPoints[i++][1]=149.89 ;

        testPoints[i][0]=41.653;
        testPoints[i++][1]= 142.683;

        testPoints[i][0]=48.26 ;
        testPoints[i++][1]=140.54 ;

        testPoints[i][0]=45.78 ;
        testPoints[i++][1]=142.57 ;

        testPoints[i][0]=46.64 ;
        testPoints[i++][1]=147.78 ;

        testPoints[i][0]=40.35 ;
        testPoints[i++][1]=142.75 ;

        testPoints[i][0]=41.06 ;
        testPoints[i++][1]=145.053 ;

        testPoints[i][0]=39.23 ;
        testPoints[i++][1]=142.96 ;

        Points p=new Points(testPoints);
        p.output();
        System.out.println();
        p.reset();
        p.output();

        long startRun = System.currentTimeMillis();

        String[] trajectory={"003-5：00-15：00"//,"004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"   //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";
        Kanonymity k=new Kanonymity(10,file(title,trajectory));
        //k.p.output();
        k.p.deletePoint(k.p.assemble[342]);
        k.p.quickSort(k.p,0,k.p.num-1,0);
        for(int i=0;i<k.p.num-1;i++){
            if(k.p.getX(i)>k.p.getY(i+1)){
                System.out.println("error");
            }
        }


        k.partitionCentralLineK(k.p,1);
        k.calArea();
        System.out.println(k.sumArea*1000000);
        k.sumArea=0;
        k.calArea();
        System.out.println(k.sumArea*1000000);

        Kanonymity k=new Kanonymity(2,5);
        k.partitionRound(k.p,1);
        new DrawSee(k,"取整划分");
        k.delete();

        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");*/

    }


}
