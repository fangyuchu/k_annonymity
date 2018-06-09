import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import java.math.*;
/**
 *
 * 程序入口
 *
 */
public class DrawPoint {
    public static void main(String[] args) throws  Exception{
        long startRun = System.currentTimeMillis();

        //Kanonymity k=new Kanonymity(13,100);
        String[] trajectory={"20081025000438"//,"20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833"   //要计算的轨迹
               };
        String title="20081025";
        //2008-10-23 8：00-12：00("000-20081023（08-12）","015-20081023（08-12）","011-20081023（08-12）","012-20081023（08-12）","013-20081023（08-12）","014-20081023（08-12）","001-20081023（08-12）" )
        //2008-12-3 0：00-12：00("001-0：00-12：00","002-0：00-12：00","003-0：00-12：00","006-0：00-12：00","011-2：00-11：00","012-1：00-12：00","013-0：00-11：00","014-0：00-12：00","015-4：00-12：00","016-9：00-12：00","017-5：00-12：00")
        //2008-12-14 5：00-16：00("002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00")
        //20081024("20081024000126","20081024000805","20081024002706","20081024004733","20081024010406","20081024011938","20081024015454","20081024020227","20081024020959","20081024041230","20081024080126")
        //20081025("20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833")
        //String[] trajectory={"001-20081023（08-12）"};
        Kanonymity k=new Kanonymity(100,importFile.file(title,trajectory));

        k.partitionCentroid(k.p,1);
        k.calDistance();
        k.calArea();
        new DrawSee(k,title+"质心");
        System.out.printf("质心:中点距离：%f,区域面积：%f,区域数量：%d\n",
                k.sumDistance,k.sumArea*1000000,k.region.size());
        k.delete();


        k.partitionCentralLineK(k.p,1);
        k.calDistance();
        k.calArea();
        new DrawSee(k,title+"地理中线+平衡");
        System.out.printf("地理中线+平衡:中点距离：%f,区域面积：%f,区域数量：%d\n",
                k.sumDistance,k.sumArea*1000000,k.region.size());
        k.delete();


        k.partitionCentralLine(k.p,1);
        k.calDistance();
        k.calArea();
        new DrawSee(k,title+"地理中线");
        System.out.printf("地理中线:中点距离：%f,区域面积：%f,区域数量：%d\n",
                k.sumDistance,k.sumArea*1000000,k.region.size());
        System.out.printf("点数不合格率：%f %%\n",(100*(float)k.unqualifiedPointNum/(float)k.p.num));
        k.calDistance();
        k.delete();

        k.partitionRound(k.p,1);
        k.calDistance();
        k.calArea();
        //WriteExcel.writeResult(title,k,"取整划分");
        new DrawSee(k,title+"取整划分");
        System.out.printf("取整划分:中点距离：%f,区域面积：%f,区域数量：%d\n",
                k.sumDistance,k.sumArea*1000000,k.region.size());
        k.delete();
/*
        k.partitionAverage(k.p,1);
        k.calDistance();
        //WriteExcel.writeResult(title,k,"平均划分");
        new DrawSee(k,title+"平均划分");
        System.out.print("平均划分：");
        System.out.println(k.sumDistance);
        k.delete();
*/
        /*k.partitionCentralLineRound(k.p,1);
        k.calDistance();
        new DrawSee(k,title+"地理中线+取整划分");
        System.out.print("地理中线+取整划分:");
        System.out.println(k.sumDistance);
        k.delete();*/


        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start
    }
}
class DrawSee extends JFrame {
    private static final int sx = 50;//小方格位置宽度
    private static final int sy = 50;//小方格位置高度
    //private static final int w = 10;
    private static final int rw = 750;
    private Graphics jg;
    private Color rectColor = new Color(0xf5f5f5);
    /**
     * DrawSee构造方法
     */

    Color[] color={Color.RED,Color.BLUE,Color.GREEN,Color.ORANGE
            ,Color.PINK,Color.YELLOW,Color.MAGENTA};


    public DrawSee(Kanonymity k,String s) {
        this.setTitle(s+"k="+String.valueOf(k.k));
        Container p = getContentPane();
        setBounds(100, 50, 1100, 1100);
        setVisible(true);
        p.setBackground(rectColor);
        setLayout(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取专门用于在窗口界面上绘图的对象
        jg =  this.getGraphics();

        paintCuttingLine(jg,k);
        for(int i=0;i<k.regionNum;i++) {
            paintRegionRectangle(jg, k.region.get(i),k.p.xmax,k.p.xmin,k.p.ymax,k.p.ymin);
        }
        try{
            for(int i=0;i<k.regionNum;i++){
                if(k.unqualified[i]==0){
                    break;
                }
                paintRegionUnqualified(jg, k.region.get(k.unqualified[i]-1),k.p.xmax,k.p.xmin,k.p.ymax,k.p.ymin);
            }
        }catch (java.lang.NullPointerException e){

        }
    }

    //paintCuttingLine用于画出分割线
    public void paintCuttingLine(Graphics g,Kanonymity k){
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            g.setFont(new Font("Arial",Font.BOLD,20));
            double xWidth=k.p.xmax-k.p.xmin;      //regionXmax-regionXmin;
            double yWidth=k.p.ymax-k.p.ymin;
            int x1,y1,x2,y2;
            for(int i=0;i<k.roads.numRow;i++){
                x1=50+(int)((k.roads.roadRow[i].line[0]-k.p.xmin)/xWidth*rw);
                y1=50+(int)((k.roads.roadRow[i].line[1]-k.p.ymin)/yWidth*rw);
                x2=50+(int)((k.roads.roadRow[i].line[2]-k.p.xmin)/xWidth*rw);
                y2=50+(int)((k.roads.roadRow[i].line[3]-k.p.ymin)/yWidth*rw);
                stokeLine   =   new   BasicStroke(   (float)(Math.pow(2,-k.roads.roadRow[i].numCut)*32)
                                                    // ,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER
                                                );
                g2d.setStroke(   stokeLine   );
                //g.setColor(color[(k.roads.roadRow[i].numCut-1)%color.length]);
                g.setColor(Color.CYAN);
                g.drawLine(x1,y1,x2,y2);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(k.roads.roadRow[i].numCut),(x1+x2)/2,y1+1);

            }
            for(int i=0;i<k.roads.numColumn;i++){
                x1=50+(int)((k.roads.roadColumn[i].line[0]-k.p.xmin)/xWidth*rw);
                y1=50+(int)((k.roads.roadColumn[i].line[1]-k.p.ymin)/yWidth*rw);
                x2=50+(int)((k.roads.roadColumn[i].line[2]-k.p.xmin)/xWidth*rw);
                y2=50+(int)((k.roads.roadColumn[i].line[3]-k.p.ymin)/yWidth*rw);
                stokeLine   =   new   BasicStroke(   (float)(Math.pow(2,-k.roads.roadColumn[i].numCut) *32)  );
                g2d.setStroke(   stokeLine   );
                //g.setColor(color[(k.roads.roadColumn[i].numCut-1)%9]);
                g.setColor(Color.CYAN);
                g.drawLine(x1,y1,x2,y2);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(k.roads.roadColumn[i].numCut)+",",x1+4,(y1+y2)/2);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //paintRegionRectangle用于画出点集合，包围线为黑色
    public void paintRegionRectangle(Graphics g,Points p,double regionXmax,double regionXmin,double regionYmax,double regionYmin) {
        try {
           /* int xmin=50+(int)(p.xmin*10);//区域边界的点,随机数的画法
            int ymin=50+(int)(p.ymin*10);
            int xmax=50+(int)(p.xmax*10);
            int ymax=50+(int)(p.ymax*10);
          //  p.output();
           // System.out.printf("xmin=%d\nxmax=%d\nymin=%d\nymax=%d\n",xmin,xmax,ymin,ymax);
            // 设置线条颜色为红色
            g.setColor(Color.RED);
            // 绘制外层矩形框
            g.drawRect(sx, sy, rw, rw);
            g.drawLine(xmin,ymin,xmax,ymin);//划出边界线，线穿过边界点的圆心
            g.drawLine(xmax,ymin,xmax,ymax);
            g.drawLine(xmax,ymax,xmin,ymax);
            g.drawLine(xmin,ymax,xmin,ymin);

            int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                g.drawOval(50+(int)(p.getX(i)*10)-r/2,50+(int)(p.getY(i)*10)-r/2,r,r);//以点为圆心，r为半径画圆
            }*/

            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            stokeLine   =   new   BasicStroke( (float)0.5  );
            g2d.setStroke(   stokeLine   );


            double xWidth=regionXmax-regionXmin;
            double yWidth=regionYmax-regionYmin;
            int xmin=50+(int)((p.xmin-regionXmin)/xWidth*rw);//区域边界的点，真实数据的画法
            int ymin=50+(int)((p.ymin-regionYmin)/yWidth*rw);
            int xmax=50+(int)((p.xmax-regionXmin)/xWidth*rw);
            int ymax=50+(int)((p.ymax-regionYmin)/yWidth*rw);
          //  p.output();
           // System.out.printf("xmin=%d\nxmax=%d\nymin=%d\nymax=%d\n",xmin,xmax,ymin,ymax);
            // 设置线条颜色为红色
            g.setColor(Color.BLACK);
            // 绘制外层矩形框
            g.drawRect(sx, sy, rw, rw);
            int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                //g.setColor(Color.red);
                g.setColor(color[(p.assemble[i].belonging)%color.length]);
                g.drawOval(50+((int)((p.getX(i)-regionXmin)/(xWidth)*rw))-r/2,50+(int)((p.getY(i)-regionYmin)/(yWidth)*rw)-r/2,r,r);//以点为圆心，r为半径画圆
            }
            g.setColor(Color.BLACK);
            g.drawLine(xmin,ymin,xmax,ymin);//划出边界线，线穿过边界点的圆心
            g.drawLine(xmax,ymin,xmax,ymax);
            g.drawLine(xmax,ymax,xmin,ymax);
            g.drawLine(xmin,ymax,xmin,ymin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void paintRegionUnqualified(Graphics g,Points p,double regionXmax,double regionXmin,double regionYmax,double regionYmin) {
        try {
            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            stokeLine   =   new   BasicStroke( (float)0.5  );
            g2d.setStroke(   stokeLine   );
            double xWidth=regionXmax-regionXmin;
            double yWidth=regionYmax-regionYmin;
            // 设置线条颜色为红色
            g.setColor(Color.BLACK);
            int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                g.drawOval(50+((int)((p.getX(i)-regionXmin)/(xWidth)*rw))-r/2,50+(int)((p.getY(i)-regionYmin)/(yWidth)*rw)-r/2,r,r);//以点为圆心，r为半径画圆
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void paintAssembleBoundary(Graphics g,Points p,double regionXmax,double regionXmin,double regionYmax,double regionYmin){
        try{
            double xWidth=regionXmax-regionXmin;
            double yWidth=regionYmax-regionYmin;
            int[] x=new int[p.num];
            int[] y=new int[p.num];
            g.setColor(Color.BLACK);
            // 绘制外层矩形框
            g.drawRect(sx, sy, rw, rw);
            g.setColor(Color.RED);
            int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                g.drawOval(50+((int)((p.getX(i)-regionXmin)/(xWidth)*900))-r/2,50+(int)((p.getY(i)-regionYmin)/(yWidth)*900)-r/2,r,r);//以点为圆心，r为半径画圆
                x[i]=50+((int)((p.getX(i)-regionXmin)/(xWidth)*900))-r/2;
                y[i]=50+(int)((p.getY(i)-regionYmin)/(yWidth)*900)-r/2;
            }
            g.setColor(Color.BLACK);
            g.drawPolygon(x,y,p.num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}