import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyc on 13/10/2017.
 */
public class RasterDraw {
    public static void main(String[] args) {
        String[] trajectory = {"20081025000438","20081025005444","20081025010205","20081025013736","20081025022807","20081025030906","20081025032809","20081025034918","20081025041051","20081025041134","20081025041708","20081025043904","20081025044159","20081025045755","20081025045800","20081025060840","20081025065431","20081025074142","20081025080705","20081025080833"

        };
        String title="20081025";
        //Raster test = new Raster(200, DrawPoint.file(title, trajectory));
        //test.screening(39.955,40.036,116.315,116.36);
        /*String title="random";
        String doc="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/random1.xls";

        Raster test=new Raster(15,doc);*/
        //test.screening(40.974,41.948,140.00,140.97);

        Raster test=new Raster(80, DrawPoint.file(title, trajectory));
        //test.print();
        test.partition();
        System.out.println((double)(test.mtk+test.ltk)/(double)test.ek);
        test.testShow();
        //System.out.println(test.p.num);
        new DrawRaster(test, title);
    }


}
class DrawRaster extends JFrame {
    private static final int sx = 50;//小方格位置宽度
    private static final int sy = 50;//小方格位置高度
    //private static final int w = 10;
    private static final int rw = 650;
    private Graphics jg;
    private Color rectColor = new Color(0xf5f5f5);
    /**
     * DrawSee构造方法
     */

    Color[] color = {Color.RED,Color.darkGray, Color.GREEN, Color.ORANGE
            , Color.PINK, Color.YELLOW, Color.MAGENTA};


    public DrawRaster( Raster r,String s) {
        this.setTitle(s + "k=" + String.valueOf(r.k));
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
        jg = this.getGraphics();
        paint(jg,r);
        //paintTest(jg,r);
        System.out.println("over");
    }
    public void paint(Graphics g,Raster r){
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            g.setFont(new Font("Arial",Font.BOLD,20));
           // g.drawRect(sx, sy, rw, rw);
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=50;
            xu=50+(int)((r.pixel[1].length*len)/xWidth*rw);
            yu=50+(int)((r.pixel.length*len)/yWidth*rw);
            g.setColor(Color.cyan);
            stokeLine   =   new   BasicStroke(  (float)0.5 );
            g2d.setStroke(   stokeLine   );
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rw);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[1].length;i++){
                x=50+(int)((i*len)/xWidth*rw);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
               g.setColor(color[(i)%color.length]);
              // g.drawString(Integer.toString(i),50+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
               for(int j=0;j<r.kResult.get(i).p.num;j++){
                   g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rw)-radius/2,radius,radius);
               }
            }
            for(int i=0;i<r.kResult.size();i++){
                for(int j=0;j<r.kResult.get(i).numRegion;j++){
                    paintKRegionRectangle(jg,r.kResult.get(i).region.get(j),r.p.xmax,r.p.xmin,r.p.ymax,r.p.ymin);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void paintTest(Graphics g,Raster r){             //论文作图用的，试一下就知道
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            g.setFont(new Font("Arial",Font.BOLD,20));
            // g.drawRect(sx, sy, rw, rw);
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=50;
            xu=50+(int)((r.pixel[1].length*len)/xWidth*rw);
            yu=50+(int)((r.pixel.length*len)/yWidth*rw);
            g.setColor(Color.black);
            stokeLine   =   new   BasicStroke(  (float)0.5 );
            g2d.setStroke(   stokeLine   );
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rw);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[1].length;i++){
                x=50+(int)((i*len)/xWidth*rw);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=10;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
                //g.setColor(color[(i)%color.length]);
                // g.drawString(Integer.toString(i),50+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
                for(int j=0;j<r.kResult.get(i).p.num;j++){
                    g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rw)-radius/2,radius,radius);
                }
                //System.out.print(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paintKRegionRectangle(Graphics g,Points p,double regionXmax,double regionXmin,double regionYmax,double regionYmin) {
        try{
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
            g.setColor(Color.BLACK);
            // 绘制外层矩形框
           // g.drawRect(sx, sy, rw, rw);
            /*int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                //g.setColor(Color.red);
                //g.setColor(color[(p.assemble[i].belonging)%color.length]);
                g.drawOval(50+((int)((p.getX(i)-regionXmin)/(xWidth)*rw))-r/2,50+(int)((p.getY(i)-regionYmin)/(yWidth)*rw)-r/2,r,r);//以点为圆心，r为半径画圆
            }*/
            g.setColor(Color.BLACK);
            g.drawLine(xmin,ymin,xmax,ymin);//划出边界线，线穿过边界点的圆心
            g.drawLine(xmax,ymin,xmax,ymax);
            g.drawLine(xmax,ymax,xmin,ymax);
            g.drawLine(xmin,ymax,xmin,ymin);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}