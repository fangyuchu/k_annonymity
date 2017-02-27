/**
 * Created by Administrator on 2017/2/24.
 */
public class CalculateDistance {
    public static void main(String[] args) throws  Exception {
        long startRun = System.currentTimeMillis();
        String[] trajectory={    "011-20081023（08-12）","012-20081023（08-12）","013-20081023（08-12）","014-20081023（08-12）","001-20081023（08-12）",              //要计算的轨迹
                "20081103232153","20081110013637","20081111001704","20081112023003","20081113034608" };//"20081112023003"
        String title="10.23";
        Kanonymity k=new Kanonymity(50,DrawPoint.file(trajectory));
        Double[] writtenData=new Double[5];

        k.partitionRound(k.p,1);
        k.calDistance();
        new DrawSee(k,title+"取整划分");
        System.out.print("取整划分：");
        System.out.println(k.sumDistance);
        writtenData[0]=k.sumDistance;
        k.delete();

        k.partitionMedian(k.p,1);
        k.calDistance();
        new DrawSee(k,title+"数值中线");
        System.out.print("数值中线：");
        System.out.println(k.sumDistance);
        writtenData[1]=k.sumDistance;
        k.delete();

        k.partitionAverage(k.p,1);
        k.calDistance();
        new DrawSee(k,title+"平均划分");
        System.out.print("平均划分：");
        System.out.println(k.sumDistance);
        writtenData[2]=k.sumDistance;
        k.delete();

        k.partitionCentralLine(k.p,1);
        k.calDistance();
        new DrawSee(k,title+"地理中线");
        System.out.printf("点数不合格率：%f %%\n",(100*(float)k.unqualifiedPointNum/(float)k.p.num));
        k.calDistance();
        System.out.printf("地理中线：%f\n",k.sumDistance);
        writtenData[3]=k.sumDistance;
        writtenData[4]=(double)k.unqualifiedPointNum/(double) k.p.num;
        k.delete();

        WriteExcel.writeDistance(title,k,writtenData);
        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start
    }
}
