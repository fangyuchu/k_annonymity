import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by fangyc on 13/10/2017.
 */



class DrawRaster extends JFrame {
    public static void main(String[] args) {


        String title="20081026";
        Raster test = new Raster(100, importFile.file(title));
        //test.screening(39.955,40.036,116.315,116.36);
        /*String title="random";
        String doc="/Users/fangyc/Documents/lab/trajectory/k匿名划分数据/random1.xls";

        Raster test=new Raster(15,doc);*/
        //test.screening(40.974,41.948,140.00,140.97);

        /*Raster test=new Raster(173, DrawPoint.file(title, trajectory));
        //test.print();
        test.partition();
        System.out.println((double)(test.mtk+test.ltk)/(double)test.ek);
        test.testShow();*/
        //System.out.println(test.p.num);
        //new DrawRaster(test, title);
        // Raster test=new Raster(10,200);             //论文图1
        test.p.screening(30,90,116.23,200);
        test.partition();
        test.testShow();
        System.out.println(1);
        new DrawRaster(test,"200个点，k为10");

    }


    private static final int sx = 50;//小方格位置宽度
    private static final int sy = 50;//小方格位置高度
    //private static final int w = 10;
    //private static final int rw = 650;
    private Graphics jg;
    private Color rectColor = new Color(0xf5f5f5);
    public JPanel j;
    /**
     * DrawSee构造方法
     */

    Color[] color = {Color.RED,Color.darkGray, Color.GREEN, Color.ORANGE
            , Color.PINK, Color.YELLOW, Color.MAGENTA};


    public DrawRaster( Raster r,String s) {
        this.setTitle(s + "k=" + String.valueOf(r.k));
        int rwx=800;                                                    //x的宽度
        int rwy=(int)(rwx/(r.p.xmax-r.p.xmin)*(r.p.ymax-r.p.ymin));     //同比例下的y的宽度
        Container p = getContentPane();
        setBounds(100, 50, 1100, 1100);
        setVisible(true);
        p.setBackground(rectColor);
        p.setBackground(Color.white);
        setLayout(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton rp=new JButton("repaint");
        rp.setBounds(950,55,100,50);
        ActionListener a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint(jg,r,rwx,rwy);
            }
        };
        rp.addActionListener(a);
        this.add(rp);
        // 获取专门用于在窗口界面上绘图的对象
        jg = this.getGraphics();
        //paintPicture1(jg,r,rwx,rwy);
        //paintPicture2(jg,r,rwx,rwy);
        paint(jg,r,rwx,rwy);
        //paintTest(jg,r);
        System.out.println("over");
    }
    public void paintPicture1(Graphics g,Raster r,int rwx,int rwy){             //论文图1
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            g.setFont(new Font("Arial",Font.BOLD,11));
            BasicStroke stokeLine;
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=50;
            xu=50+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=50+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=50+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            stokeLine=new BasicStroke((float)0.5);
            g2d.setStroke(stokeLine);
            for(int i=0;i<r.kResult.size();i++){
                for(int j=0;j<r.kResult.get(i).p.num;j++){
                    g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
                }
            }
            g.drawLine(50,50,50,50+rwy);                 //画出点的边界
            g.drawLine(50+rwx,50,50+rwx,50+rwy);
            g.drawLine(50,50,50+rwx,50);
            g.drawLine(50,50+rwy,50+rwx,50+rwy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paintPicture2(Graphics g,Raster r,int rwx,int rwy){     //论文图2，展示合并后的栅格
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            g.setFont(new Font("Arial",Font.BOLD,11));
            BasicStroke stokeLine;
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=50;
            xu=50+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=50+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=50+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            stokeLine=new BasicStroke((float)0.5);
            g2d.setStroke(stokeLine);
            for(int i=0;i<r.kResult.size();i++){
                for(int j=0;j<r.kResult.get(i).p.num;j++){
                    g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
                }
            }
            for(int ind=1;ind<r.ind;ind++){
                boolean find=false;
                for(int i=0;i<r.index.length;i++){
                    for(int j=0;j<r.index[0].length;j++){
                        if(r.index[i][j]==ind) {
                            find=true;
                            int row, col;
                            row = col = 1;
                            while (i+row<r.index.length&&r.index[i + row][j] == ind) row++;
                            while (j+col<r.index[0].length&&r.index[i][j + col] == ind) col++;
                            int y1=50+(int)((i*len)/yWidth*rwy);
                            int y2=50+(int)(((i+row)*len)/yWidth*rwy);
                            int x1=50+(int)((j*len)/xWidth*rwx);
                            int x2=50+(int)(((j+col)*len)/xWidth*rwx);
                            g.setColor(Color.white);
                            for(int clear=1;clear<row;clear++){
                                g.drawLine(x1,y1+(int)((clear*len)/yWidth*rwy),x2,y1+(int)((clear*len)/yWidth*rwy));
                            }
                            for(int clear=1;clear<col;clear++){
                                g.drawLine(x1+(int)((clear*len)/xWidth*rwx),y1,x1+(int)((clear*len)/xWidth*rwx),y2);
                            }
                            g.setColor(Color.black);
                            g.drawLine(x1,y1,x1,y2);
                            g.drawLine(x1,y1,x2,y1);
                            g.drawLine(x1,y2,x2,y2);
                            g.drawLine(x2,y1,x2,y2);
                            break;
                        }
                    }
                    if(find){
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void paint(Graphics g,Raster r,int rwx,int rwy){
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
            xu=50+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=50+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.cyan);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                String sy=Double.toString(r.p.ymin+i*len);
                g.drawString(sy.substring(0,Math.min(6,sy.length())),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=50+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                String sx=Double.toString(r.p.xmin + i * len);
                g.drawString(sx.substring(0, Math.min(6,sx.length())), x - 20, yb - 5);
            }
            int radius=8;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
               g.setColor(color[(i)%color.length]);
              // g.drawString(Integer.toString(i),50+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
               for(int j=0;j<r.kResult.get(i).p.num;j++){
                   g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
               }
            }
            for(int i=0;i<r.kResult.size();i++){
                for(int j=0;j<r.kResult.get(i).numRegion;j++){
                    paintKRegionRectangle(jg,r.kResult.get(i).region.get(j),r.p.xmax,r.p.xmin,r.p.ymax,r.p.ymin,rwx,rwy);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void paintTest(Graphics g,Raster r,int rwx,int rwy){             //论文作图用的，试一下就知道
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
            xu=50+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=50+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            stokeLine   =   new   BasicStroke(  (float)0.5 );
            g2d.setStroke(   stokeLine   );
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=50+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=50+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=10;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
                //g.setColor(color[(i)%color.length]);
                // g.drawString(Integer.toString(i),50+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,50+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
                for(int j=0;j<r.kResult.get(i).p.num;j++){
                    g.drawOval(50+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,50+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
                }
                //System.out.print(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paintKRegionRectangle(Graphics g,Points p,double regionXmax,double regionXmin,double regionYmax,double regionYmin,int rwx,int rwy) {
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            BasicStroke stokeLine;
            stokeLine   =   new   BasicStroke( (float)0.5  );
            g2d.setStroke(   stokeLine   );
            double xWidth=regionXmax-regionXmin;
            double yWidth=regionYmax-regionYmin;
            int xmin=50+(int)((p.xmin-regionXmin)/xWidth*rwx);//区域边界的点，真实数据的画法
            int ymin=50+(int)((p.ymin-regionYmin)/yWidth*rwy);
            int xmax=50+(int)((p.xmax-regionXmin)/xWidth*rwx);
            int ymax=50+(int)((p.ymax-regionYmin)/yWidth*rwy);
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