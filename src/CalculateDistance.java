/**
 * Created by Administrator on 2017/2/24.
 */
public class CalculateDistance {
    public static void main(String[] args) throws  Exception {
        long startRun = System.currentTimeMillis();
        String[] trajectory={    "001-0：00-12：00","002-0：00-12：00","003-0：00-12：00","006-0：00-12：00","011-2：00-11：00","012-1：00-12：00","013-0：00-11：00","014-0：00-12：00","015-4：00-12：00","016-9：00-12：00","017-5：00-12：00"
        };//"20081112023003"
        String title="2008-12-3 0：00-12：00";
        for(int kNum=1010;kNum<=1200;kNum=kNum+10) {
            Kanonymity k = new Kanonymity(kNum, DrawPoint.file(trajectory));
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
