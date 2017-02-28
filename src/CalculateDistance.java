/**
 * Created by Administrator on 2017/2/24.
 */
public class CalculateDistance {
    public static void main(String[] args) throws  Exception {
        long startRun = System.currentTimeMillis();
        String[] trajectory={"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"   //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";
        for(int kNum=30;kNum<=1300;kNum=kNum+10) {
            System.out.printf("\n\n\n\n%d\n\n\n\n",kNum);
            Kanonymity k = new Kanonymity(kNum, DrawPoint.file(title,trajectory));
            Double[] writtenData = new Double[6];

            k.partitionRound(k.p, 1);
            k.calDistance();
            //new DrawSee(k, title + "取整划分");
            System.out.print("取整划分：");
            System.out.println(k.sumDistance);
            writtenData[0] = k.sumDistance;
            k.delete();

            k.partitionMedian(k.p, 1);
            k.calDistance();
           // new DrawSee(k, title + "数值中线");
            System.out.print("数值中线：");
            System.out.println(k.sumDistance);
            writtenData[1] = k.sumDistance;
            k.delete();

            k.partitionAverage(k.p, 1);
            k.calDistance();
            //new DrawSee(k, title + "平均划分");
            System.out.print("平均划分：");
            System.out.println(k.sumDistance);
            writtenData[2] = k.sumDistance;
            k.delete();

            k.partitionCentralLine(k.p, 1);
            k.calDistance();
           // new DrawSee(k, title + "地理中线");
            System.out.printf("点数不合格率：%f %%\n", (100 * (float) k.unqualifiedPointNum / (float) k.p.num));
            k.calDistance();
            System.out.printf("地理中线：%f\n", k.sumDistance);
            writtenData[3] = k.sumDistance;
            writtenData[4] = (double) k.unqualifiedPointNum / (double) k.p.num;
            k.delete();

            writtenData[5] = (double) k.k;
            WriteExcel.writeDistance(title, k, writtenData);
            long endRun = System.currentTimeMillis();
            System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start
        }
    }
}
