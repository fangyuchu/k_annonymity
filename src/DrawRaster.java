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

/*

        String[] trajectory={"002-5：00-11：00","003-5：00-15：00"  //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";*/

        /*Raster t=new Raster(space/2,importFile.file("20081024"));
        t.dbscan(0.0005,10);
        t.partition();
        new DrawRaster(t,"2008年10月24日");*/
        String title="2008-10-23 8：00-12：00";
        Raster t1 = new Raster(50, importFile.file(title));
       // t1.screen(30,90,116.27,200);
        //test.screening(30,90,116.35,200);
        //t1.dbscan(0.005,10);
        t1.dbscan(0.0005,10);
        System.out.println(1);
        t1.partition();
        new DrawRaster(t1,title);

        /*Raster t2=new Raster(space/2, importFile.file(title));
       // t2.screen(30,90,116.27,200);
        t2.dbscan(0.0005,10);
        t2.partition();
        System.out.printf("BUDE:%f\n",t1.averageArea);
        System.out.printf("my:%f\n",t2.averageArea);
        new DrawRaster(t2,title);*/
    }


    private Graphics jg;
    int height;                         //边框高度为屏幕高度的0.9
    int width;                          //边框宽度,为点框宽度的2倍
    int rwx;                            //点框宽度，根据rwy计算得出
    int rwy;                            //点框高度，为height-space
    int space;                          //点框和边框之间的距离，为边框高度的0.11
    /**
     * DrawSee构造方法
     */

    Color[] color = {Color.RED,Color.darkGray, Color.GREEN, Color.ORANGE
            , Color.PINK, Color.YELLOW, Color.MAGENTA};
    public DrawRaster( Raster r,String s) {
        this.setTitle(s + "  k=" + String.valueOf(r.k));
        int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height=(int)(screenHeight*0.9);                                                 //边框高度为屏幕高度的0.9
        space=(int)(height*0.11);
        rwy=(height-space);
        rwx=(int)(rwy/(r.p.ymax-r.p.ymin)*(r.p.xmax-r.p.xmin));
        width=rwx*2;
        Container p = getContentPane();
        String[] sg= {"位置分布","聚类结果","合并结果","分割结果"};
        JComboBox<String> jcb = new JComboBox<String>(sg);
        jcb.setBorder(BorderFactory.createLineBorder(Color.red, 3));
        JButton pButton=new JButton("paint");
        JFrame jf=this;
        ActionListener a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.update(jg);
                int i=jcb.getSelectedIndex();
                switch (i){
                    case 0:paintPicture1(jg,r);break;
                    case 1:paintCluster(jg,r);break;
                    case 2:paintPicture2(jg,r);break;
                    case 3:paint(jg,r);break;

                }

            }
        };
        pButton.addActionListener(a);
        JPanel jp = new JPanel();
        jp.add(jcb);
        jp.add(pButton);
        p.add(BorderLayout.EAST,jp);
        setBounds((screenWidth-width)/2,(screenHeight-height)/2,width,height);
        setVisible(true);
        p.setBackground(Color.white);
        p.setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 获取专门用于在窗口界面上绘图的对象
        jg = this.getGraphics();
        System.out.println("over");
    }
    public void paintCluster(Graphics g,Raster r){
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            g.setFont(new Font("Arial",Font.BOLD,11));
            BasicStroke stokeLine;
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=space/2;
            xu=space/2+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=space/2+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=space/2+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=space/2+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            stokeLine=new BasicStroke((float)0.5);
            g2d.setStroke(stokeLine);
            for(int i=0;i<r.p.num;i++){
                g.setColor(color[r.p.assemble[i].cluster%color.length]);
                g.drawOval(space/2+((int)((r.p.assemble[i].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,space/2+(int)((r.p.assemble[i].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paintPicture1(Graphics g,Raster r){             //论文图1
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            g.setFont(new Font("Arial",Font.BOLD,11));
            BasicStroke stokeLine;
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=space/2;
            xu=space/2+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=space/2+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=space/2+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=space/2+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            stokeLine=new BasicStroke((float)0.5);
            g2d.setStroke(stokeLine);
            for(int i=0;i<r.p.num;i++){
                g.drawOval(space/2+((int)((r.p.assemble[i].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,space/2+(int)((r.p.assemble[i].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
            }
            g.drawLine(space/2,space/2,space/2,space/2+rwy);                 //画出点的边界
            g.drawLine(space/2+rwx,space/2,space/2+rwx,space/2+rwy);
            g.drawLine(space/2,space/2,space/2+rwx,space/2);
            g.drawLine(space/2,space/2+rwy,space/2+rwx,space/2+rwy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paintPicture2(Graphics g,Raster r){     //论文图2，展示合并后的栅格
        try{
            Graphics2D   g2d   =   (   Graphics2D   )g;
            g.setFont(new Font("Arial",Font.BOLD,11));
            BasicStroke stokeLine;
            double xWidth=r.p.xmax-r.p.xmin;      //regionXmax-regionXmin;
            double yWidth=r.p.ymax-r.p.ymin;
            double len=Math.sqrt(r.pA);
            int x,y,xb,xu,yb,yu;
            xb=yb=space/2;
            xu=space/2+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=space/2+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=space/2+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=space/2+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=8;//点的半径r
            stokeLine=new BasicStroke((float)0.5);
            g2d.setStroke(stokeLine);
            g.setColor(Color.CYAN);
            for(int i=0;i<r.p.num;i++){
                g.drawOval(space/2+((int)((r.p.assemble[i].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,space/2+(int)((r.p.assemble[i].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
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
                            int y1=space/2+(int)((i*len)/yWidth*rwy);
                            int y2=space/2+(int)(((i+row)*len)/yWidth*rwy);
                            int x1=space/2+(int)((j*len)/xWidth*rwx);
                            int x2=space/2+(int)(((j+col)*len)/xWidth*rwx);
                            stokeLine=new BasicStroke((float)1);
                            g2d.setStroke(stokeLine);
                            g.setColor(Color.black);
                            g.drawLine(x1,y1,x1,y2);
                            g.drawLine(x1,y1,x2,y1);
                            g.drawLine(x1,y2,x2,y2);
                            g.drawLine(x2,y1,x2,y2);
                            g.setColor(Color.white);
                            stokeLine = new BasicStroke((float) 0.5); //实例化新画刷
                            g2d.setStroke(stokeLine); //设置新的画刷
                            for(int clear=1;clear<row;clear++){
                               // g.drawLine(x1,y1+(int)((clear*len)/yWidth*rwy),x2,y1+(int)((clear*len)/yWidth*rwy));
                                g.drawLine(x1,space/2+(int)((i+clear)*len/yWidth*rwy),x2,space/2+(int)((i+clear)*len/yWidth*rwy));
                            }
                            for(int clear=1;clear<col;clear++){
                                g.drawLine(space/2+(int)((j+clear)*len/xWidth*rwx),y1,space/2+(int)((j+clear)*len/xWidth*rwx),y2);
                            }
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
            xb=yb=space/2;
            xu=space/2+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=space/2+(int)((r.pixel.length*len)/yWidth*rwy);
            float[] dash={5,5}; //短划线图案
            stokeLine = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f,dash,0.0f); //实例化新画刷
            g2d.setStroke(stokeLine); //设置新的画刷
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=space/2+(int)((i*len)/yWidth*rwy);
                g.setColor(Color.cyan);
                g.drawLine(xb,y,xu,y);
                String sy=Double.toString(r.p.ymin+i*len);
                g.setColor(Color.black);
                g.drawString(sy.substring(0,Math.min(6,sy.length())),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=space/2+(int)((i*len)/xWidth*rwx);
                g.setColor(Color.cyan);
                g.drawLine(x,yb,x,yu);
                String sx=Double.toString(r.p.xmin + i * len);
                g.setColor(Color.black);
                g.drawString(sx.substring(0, Math.min(6,sx.length())), x - 20, yb - 5);
            }
            int radius=8;//点的半径r
            g.setColor(Color.cyan);
            for(int i=0;i<r.kResult.size();i++){
               for(int j=0;j<r.kResult.get(i).p.num;j++){
                   g.drawOval(space/2+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,space/2+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
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
            xb=yb=space/2;
            xu=space/2+(int)((r.pixel[0].length*len)/xWidth*rwx);
            yu=space/2+(int)((r.pixel.length*len)/yWidth*rwy);
            g.setColor(Color.black);
            stokeLine   =   new   BasicStroke(  (float)0.5 );
            g2d.setStroke(   stokeLine   );
            g.setFont(new Font("宋体",Font.BOLD,10));    //改变字体大小
            for(int i=0;i<=r.pixel.length;i++){
                y=space/2+(int)((i*len)/yWidth*rwy);
                g.drawLine(xb,y,xu,y);
                g.drawString(Double.toString(r.p.ymin+i*len).substring(0,6),xu+5,y);
            }
            for(int i=0;i<=r.pixel[0].length;i++){
                x=space/2+(int)((i*len)/xWidth*rwx);
                g.drawLine(x,yb,x,yu);
                g.drawString(Double.toString(r.p.xmin+i*len).substring(0,6),x-20,yb-5);
            }
            int radius=10;//点的半径r
            for(int i=0;i<r.kResult.size();i++){
                //g.setColor(color[(i)%color.length]);
                // g.drawString(Integer.toString(i),space/2+((int)((r.kResult.get(i).p.assemble[0].x()-r.p.xmin)/(xWidth)*rw))-radius/2,space/2+(int)((r.kResult.get(i).p.assemble[0].y()-r.p.ymin)/(yWidth)*rw)-radius/2);
                for(int j=0;j<r.kResult.get(i).p.num;j++){
                    g.drawOval(space/2+((int)((r.kResult.get(i).p.assemble[j].x()-r.p.xmin)/(xWidth)*rwx))-radius/2,space/2+(int)((r.kResult.get(i).p.assemble[j].y()-r.p.ymin)/(yWidth)*rwy)-radius/2,radius,radius);
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
            int xmin=space/2+(int)((p.xmin-regionXmin)/xWidth*rwx);//区域边界的点，真实数据的画法
            int ymin=space/2+(int)((p.ymin-regionYmin)/yWidth*rwy);
            int xmax=space/2+(int)((p.xmax-regionXmin)/xWidth*rwx);
            int ymax=space/2+(int)((p.ymax-regionYmin)/yWidth*rwy);
            g.setColor(Color.BLACK);
            // 绘制外层矩形框
           // g.drawRect(sx, sy, rw, rw);
            /*int r=8;//点的半径r
            for(int i=0;i<p.num;i++){
                //g.setColor(Color.red);
                //g.setColor(color[(p.assemble[i].belonging)%color.length]);
                g.drawOval(space/2+((int)((p.getX(i)-regionXmin)/(xWidth)*rw))-r/2,space/2+(int)((p.getY(i)-regionYmin)/(yWidth)*rw)-r/2,r,r);//以点为圆心，r为半径画圆
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