/**
 * Created by fangyc on 19/04/2017.
 */
public class test {
    public static void main(String[] args){
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
        p.output();*/

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

        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");

    }





    public static String[] file(String title,String[] s){
        String[] temp=new String[s.length];
        for(int i=0;i<s.length;i++){
            //temp[i]="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/"+s[i]+".xls";
            temp[i]="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/"+title+"/"+s[i]+".xls";
        }
        return temp;
    }//
}
