import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

/**
 * Created by Administrator on 2016/12/14.
 */
public class WriteExcel {
    public static int lastRowNum;

    //在doc文件中第rowNum+1行，第columnNum+1列中写入content内容
    public static void write(String doc,String content,int rowNum,int columnNum) throws Exception {
        InputStream inp = new FileInputStream(doc);//"C:/Users/Administrator/Desktop/test.xls"
        //根据上述创建的输入流 创建工作簿对象
        Workbook wb = WorkbookFactory.create(inp);
        //得到第一页 sheet
        //页Sheet是从0开始索引的
        Sheet sheet = wb.getSheetAt(0);
        // 创建一行，在页sheet上
        Row row;
        row = sheet.getRow((short) rowNum);
        if(row==null)row=sheet.createRow((short) rowNum);
        row.createCell(columnNum).setCellValue(content);

        FileOutputStream fileOut = new FileOutputStream(doc);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        fileOut.close();
        lastRowNum=row.getRowNum();
        inp.close();
    }
    public static void writeResult(String title,String method, double numK,
                                   double numPoints, double regionNum,double distance,
                                   double area)throws Exception{  //写划分后的效果
        //要写入的文件名
        String s="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/result.xls";
        //创建要读入的文件的输入流
        InputStream inp = new FileInputStream(s);
        //根据上述创建的输入流 创建工作簿对象
        Workbook wb = WorkbookFactory.create(inp);
        //得到第一页 sheet
        //页Sheet是从0开始索引的
        Sheet sheet = wb.getSheetAt(0);
        int row=sheet.getLastRowNum()+1;
        inp.close();
        int i=0;
        write(s,title,row,i++);
        write(s,String.valueOf(numPoints),row,i++);
        write(s,String.valueOf(numK),row,i++);
        write(s,method,row,i++);
        write(s,String.valueOf(regionNum),row,i++);
        write(s,String.valueOf(area*1000000),row,i++);
        write(s,String.valueOf(distance),row,i++);
    }
    public static void writePartition(String trajectory,Kanonymity k,String method)throws Exception{   //写划分后的点集

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
        String doc="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/result"+trajectory+"/"+"k="+String.valueOf(k.k)+method+".xls";
        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为 new sheet
        Sheet sheet = wb.createSheet(trajectory);
        //创建一个文件 命名为workbook.xls
        FileOutputStream fileOut = new FileOutputStream(doc);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        fileOut.close();
        InputStream inp = new FileInputStream(doc);
        lastRowNum=sheet.getLastRowNum();
        inp.close();
        WriteExcel.write(doc,"trajectory:",lastRowNum,0);
        WriteExcel.write(doc,trajectory,lastRowNum,1);
        WriteExcel.write(doc,method,lastRowNum+1,1);
        WriteExcel.write(doc,"k=",lastRowNum+1,0);
        WriteExcel.write(doc,String.valueOf(k.k),lastRowNum,1);
        WriteExcel.write(doc,"sumDistance:",lastRowNum,6);
        WriteExcel.write(doc,String.valueOf(k.sumDistance),lastRowNum,7);
        for(int i=0;i<k.regionNum;i++) {                                //i表示第几个区域
            WriteExcel.write(doc, "region:", lastRowNum +1, 0);
            WriteExcel.write(doc, String.valueOf(i+1), lastRowNum, 1);  //习惯中区域数从1开始
            WriteExcel.write(doc,"属于",lastRowNum,3);
            WriteExcel.write(doc,"total number:",lastRowNum,4);
            WriteExcel.write(doc,String.valueOf(k.region.get(i).num),lastRowNum,5);
            try{
                WriteExcel.write(doc,"distance:",lastRowNum,6);
                WriteExcel.write(doc,String.valueOf(k.distance[i]),lastRowNum,7);
            }catch (java.lang.NullPointerException e){

            }
            for(int j=0;j<k.region.get(i).num;j++){                              //j表示第几个点
                WriteExcel.write(doc,String.valueOf(k.region.get(i).getX(j)),lastRowNum+1,0);
                WriteExcel.write(doc,String.valueOf(k.region.get(i).getY(j)),lastRowNum,2);
                WriteExcel.write(doc,String.valueOf(k.region.get(i).assemble[j].belonging),lastRowNum,3);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        WriteExcel.write("C:/Users/Administrator/Desktop/a.xls","轨迹：20081024020959",1,0);//C:\Users\Administrator\Desktop\1.xls
    }
}
