import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyc on 13/10/2017.
 */
public class RasterDraw {
    public static void main(String[] args) {
        String[] trajectory = {"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"  //要计算的轨迹

        };
        String title = "2008-12-14 5：00-16：00";
        Raster test = new Raster(180, DrawPoint.file(title, trajectory));
        test.partition();
        test.testShow();
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
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rw);
                g.drawLine(xb,y,xu,y);
            }
            for(int i=0;i<=r.pixel[1].length;i++){
                x=50+(int)((i*len)/xWidth*rw);
                g.drawLine(x,yb,x,yu);
            }
            int radius=8;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
               g.setColor(color[(i)%color.length]);
               g.drawString(Integer.toString(i),50+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
               for(int j=0;j<r.kResult.get(i).p.num;j++){

                   g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rw)-radius/2,radius,radius);
               }
               System.out.print(i);
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