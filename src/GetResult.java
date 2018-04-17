/**
 * Created by Administrator on 2017/2/24.
 */

public class GetResult {
    public static void main(String[] args) throws  Exception {
        long startRun = System.currentTimeMillis();
        String[] trajectory={"20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833"   //要计算的轨迹
        };
        String title="20081025";
        //2008-10-23 8：00-12：00("000-20081023（08-12）","015-20081023（08-12）","011-20081023（08-12）","012-20081023（08-12）","013-20081023（08-12）","014-20081023（08-12）","001-20081023（08-12）" )
        //2008-12-3 0：00-12：00("001-0：00-12：00","002-0：00-12：00","003-0：00-12：00","006-0：00-12：00","011-2：00-11：00","012-1：00-12：00","013-0：00-11：00","014-0：00-12：00","015-4：00-12：00","016-9：00-12：00","017-5：00-12：00")
        //2008-12-14 5：00-16：00("002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00")
        //20081024("20081024000126","20081024000805","20081024002706","20081024004733","20081024010406","20081024011938","20081024015454","20081024020227","20081024020959","20081024041230","20081024080126")
        //20081025("20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833")
        for(int kNum=1300;kNum>=30;kNum=kNum-10) {
            System.out.printf("\n\n\n\n%d\n\n\n\n",kNum);

            Kanonymity k = new Kanonymity(kNum, importFile.file(title,trajectory));

            k.partitionRound(k.p, 1);
            k.calDistance();
            k.calArea();
            WriteExcel.writeResult(title,"取整划分",k.k,k.p.num, k.region.size(),k.sumDistance,k.sumArea);
            System.out.print("完成 取整划分 的计算\n");
            k.delete();

            k.partitionCentralLineK(k.p, 1);
            k.calDistance();
            k.calArea();
            WriteExcel.writeResult(title,"地理中线+平衡",k.k,k.p.num, k.region.size(),k.sumDistance,k.sumArea);
            System.out.print("完成 地理总线+平衡 的计算\n");
            k.delete();

            k.partitionCentroid(k.p, 1);
            k.calDistance();
            k.calArea();
            WriteExcel.writeResult(title,"质心划分",k.k,k.p.num, k.region.size(),k.sumDistance,k.sumArea);
            System.out.print("完成 质心划分 的计算\n");
            k.delete();

            k.partitionAverage(k.p, 1);
            k.calDistance();
            k.calArea();
            WriteExcel.writeResult(title,"平均划分",k.k,k.p.num, k.region.size(),k.sumDistance,k.sumArea);
            System.out.print("完成 平均划分 的计算\n");
            k.delete();

            //WriteExcel.writeDistance(title, k, writtenData);
            long endRun = System.currentTimeMillis();
            System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start
        }
    }
}
