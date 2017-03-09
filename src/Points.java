import java.awt.*;
import java.util.*;
import java.util.Arrays;
/**
 * Created by f456 on 16-11-30.
 */
public class Points {
    int num;
    //private Double[][] assemble;            //2行，num列,第一列为x,第二列为y. 不能直接再外部更改，否则其他域会出错
    public Point[]    assemble;
   // private Double[][] pointX;           //按x排序后的坐标
   // private Double[][] pointY;           //按y排序后的坐标
    public double xmin;
    public double xmax;
    public double ymin;
    public double ymax;
    public double signal;                  //0表示已按x排序，1表示已按y排序，2表示未排序
    public Points(){}
    /*public Points(int num)
    {
        this.num=num;
        assemble=new Double[num][2];
        for(int i=0;i<num;i++){
            assemble[i][0]=new Random().nextDouble()*90;                              //为方便画图暂时坐标最大值设置为90
            assemble[i][1]=new Random().nextDouble()*90;
        }
        reset();
    }*/
    public Points(double p[][]){
        assemble = new Point[p.length];
        this.num = p.length;
        System.out.println(p.length);
        for(int i=0;i<p.length;i++){
            System.out.println(i);
            assemble[i]=new Point().setPoint(p[i],0);
        }
        signal=2;
        reset();
    }
    public Points(double p[][],int b){
        assemble = new Point[p.length];
        this.num = p.length;
        for(int i=0;i<p.length;i++){
            assemble[i]=new Point().setPoint(p[i],b);
        }
        reset();
    }
    public double getX(int i){
        return assemble[i].point[0];
    }
    public double getY(int i){
        return assemble[i].point[1];
    }
    public void reset(){
        num=assemble.length;
        quickSort(this, 0, num - 1, 0);                                              //按x排序
        xmin = assemble[0].point[0];
        xmax = assemble[num-1].point[0];
        quickSort(this, 0, num - 1, 1);                                               //按y排序
        ymin = assemble[0].point[1];
        ymax = assemble[num-1].point[1];

    }
    public void add(Points.Point a){
        Point[] temp=new Point[num+1];
        for(int i=0;i<num;i++){
            temp[i]=assemble[i];
        }
        temp[temp.length-1]=a.copy(a);
        assemble=temp;
        signal=2;                                                              //排序被打乱
        reset();
    }
    public void add(double[][] p,int belonging){                                                    //将新的点坐标集加入数据集
        int i=0;
        Point[] temp=new Point[num+p.length-1];
        for(;i<num;i++){
            temp[i]=new Point().setPoint(assemble[i]);
        }
        for(int j=0;i<num+p.length-1;i++,j++){
            temp[i]=new Point().setPoint(p[j],belonging);
        }
        assemble=temp;
        signal=2;
        reset();
    }
    public Points cut(Points p,int start,int end) {//从下标start到end切割点集P
        Points a = new Points();
        a.num = end - start + 1;
        a.assemble = new Point[a.num];
        for (int i = start; i <= end; i++) {
            a.assemble[i-start]=new Point().setPoint(p.assemble[i].point,p.assemble[i].belonging);
        }
        quickSort(a,0,a.num-1,0);                                               //按x排序
        a.xmin=a.assemble[0].point[0];//pointX[0][0];
        a.xmax=a.assemble[a.num-1].point[0];
        quickSort(a,0,a.num-1,1);                                               //按y排序
        a.ymin=a.assemble[0].point[1];
        a.ymax=a.assemble[a.num-1].point[1];
        return a;
    }
    public Points copy(Points p){
        Points temp=cut(p,0,p.num-1);
        this.assemble=temp.assemble;
        this.xmax=temp.xmax;
        this.xmin=temp.xmin;
        this.ymax=temp.ymax;
        this.ymin=temp.ymin;
        this.num=temp.num;
        this.signal=temp.signal;
        return this;
    }
    public void exchangePoint(Point a,Point b){//交换点
        Point c=new Point();
        c.setPoint(b);
        b.setPoint(a);
        a.setPoint(c);
    }

    public void quickSort(Points p,int first,int last,int w){//对下标从first到last的点进行快速排序      //w 0是x 1是y
        if(last-first<1)return;
        if(signal!=w) {
            Random random = new Random();
            int j = random.nextInt(last - first);
            p.exchangePoint(p.assemble[first], p.assemble[first + j]);
            Point base = new Point();
            base.setPoint(p.assemble[first]);
            int start = first;
            int end = last;
            int place = first;                      //base点最后所在的坐标
            while (start < end) {
                while (p.assemble[end].point[w] >= base.point[w] && start < end) {
                    end--;
                }
                p.exchangePoint(p.assemble[end], p.assemble[place]);
                place = end;
                while (p.assemble[start].point[w] <= base.point[w] && start < end) {
                    start++;
                }
                p.exchangePoint(p.assemble[start], p.assemble[place]);
                place = start;
            }
            p.quickSort(p, first, place - 1, w);
            p.quickSort(p, place + 1, last, w);
        }
        signal=w;
    }
    public int search(double aim,int w){  //查询。返回值为第一个大于所查询值的位置
        int i;
        int first=0;
        int last=num;
        quickSort(this,0,num-1,w);
        while (first < last) {
            i = (first + last) / 2;
            if (assemble[i].point[w] > aim){
                last = i;
            }
            else first = i + 1;
        }
        return first-1;
    }
    public int search(Point aim){                     //默认所有点纵坐标相同
        int place=search(aim.y(),1);
        if(!assemble[place].equal(aim)){
            throw new NoSuchElementException("no such point or more than one point share one y");
        }
        return place;
    }
    public void output(){//输出
        for(int i=0;i<num;i++){
            assemble[i].output();
        }
        System.out.printf("xmin is %f\n",xmin);
        System.out.printf("xmax is %f\n",xmax);
        System.out.printf("ymin is %f\n",ymin);
        System.out.printf("ymax is %f\n",ymax);
        System.out.printf("num is %d\n",num);
    }
    public void deletePoint(Point[] p){
        for(int i=0;i<p.length;i++){
            try{
                int j=search(p[i]);
                for(int k=j;k<num-1;k++){
                    assemble[k].setPoint(assemble[k+1]);
                }
                num--;
            }catch (NoSuchElementException e){
                System.out.println("no such point or more than one point share one y");
            }
        }
        copy(this.cut(this,0,num-1));
        reset();
    }
    public void deletePoint(Point p){
        Points.Point[] temp={p};
        deletePoint(temp);
    }
    public class Point{
        private double point[];                          //该点坐标
        public int     belonging;                        //该点属于第几个人,从第一个人开始
        public Point(){
            point=new double[2];
        }
        private Point setPoint(double[] p,int b){
            point[0]=p[0];
            point[1]=p[1];
            belonging=b;
            return this;
        }
        private Point setPoint(Point p){
            point[0]=p.x();
            point[1]=p.y();
            belonging=p.belonging;
            return this;
        }
        public Point copy(Point source){      //返回拷贝了source数据的点
            Point des=new Point();
            des.point[0]=source.x();
            des.point[1]=source.y();
            des.belonging=source.belonging;
            return des;
        }
        public double x(){
            return point[0];
        }
        public double y(){
            return point[1];
        }
        public void output(){
            System.out.print(point[0]);
            System.out.print("  ");
            System.out.print(point[1]);
            System.out.print("  ");
            System.out.printf("属于：%d\n",belonging);
        }
        public boolean equal(Point p){
            return (p.point[0]==point[0]&&p.point[1]==point[1]&&p.belonging==belonging);
        }
    }
    public static void main(String[] args)
    {
        long startRun = System.currentTimeMillis();
        String trajectory="011-20081023（08-12）";
        try {
            Points p = new Points(ReadExcel.readCell("C:/Users/Administrator/Desktop/实验室/k匿名划分数据/" + trajectory + ".xls"));
            p.quickSort(p,0,p.num-1,1);
            p.output();
           // p.deletePoint(p.cut(p,p.num-2,p.num-1).assemble);
            p.add(ReadExcel.readCell("C:/Users/Administrator/Desktop/实验室/k匿名划分数据/" + trajectory + ".xls"),1);
            p.output();
        }catch (Exception e){
            e.printStackTrace();
        }

    /*    int num=5;
        Points p=new Points(num);
        for(int i=0;i<5;i++){
            p.assemble[i][0]=(double)0;
        }
        p.quickSort(p,0,4,0);
        p.output();
        p.quickSort(p,0,num-1,0);
        System.out.println(p.search(30,0));*/


        long endRun = System.currentTimeMillis();
        System.out.println("运行时间：" + (endRun - startRun) + "ms");//应该是end - start
    }
}
