import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Kanonymity {
    public int k;
    public int regionNum;               //区域数量
    public Points p;                         //点集合
    public ArrayList<Points> region;                //划分后的区域点集合数组
    public double distance[] = null;              //元素为某匿名区域中的点离该区域中点的欧式距离和
    public double sumDistance;
    public double area[];
    public double sumArea;
    Roads roads;
    public int[] unqualified;              //点数小于K的区域号（数组下标+1）
    public Integer unqualifiedRegionNum;
    public Integer unqualifiedPointNum;
    public Kanonymity(int k,Points p){
        this.p=p;
        this.k=k;
        this.regionNum=0;
        region=new ArrayList<>();
        roads =new Roads();
    }
    public Kanonymity(int k,int num){
        p=new Points(num);
        this.k=k;
        this.regionNum=0;
        region=new ArrayList<>();
        roads =new Roads();
    }
    public Kanonymity(int k,String s){
        try {
            p = new Points(ReadExcel.readCell(s));
            this.k=k;
            this.regionNum=0;
            region=new ArrayList<>();
            roads=new Roads();
        }catch(Exception e){}
    }
    public Kanonymity(int k, String  s[]){
        try{
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            this.k=k;
            this.regionNum=0;
            region=new ArrayList<>();
            roads=new Roads();
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void print(){//                       将刚刚划分好的点区域输出
        for(int i=0;i<regionNum;i++) {
            System.out.printf("region %d \n", i+1);
            try{
                System.out.printf("distance %f \n", distance[i]);
            }catch (java.lang.NullPointerException e){

            }
            region.get(i).output();
            System.out.printf("\n");
        }
    }
    public void copy(Points p){//将划分好的点对象赋给re数组
        region.add(p);
        regionNum++;
    }
    public void delete(){
        this.regionNum=0;
        region=new ArrayList<>();
        roads=new Roads();
        distance=null;
        sumDistance=0;
        area=null;
        sumArea=0;
        unqualified=null;
        unqualifiedRegionNum=null;
        unqualifiedPointNum=null;
    }
    public void partitionCircle(Points p){
        int way=new Random().nextInt(2);

    }
    public void partitionAverage(Points p,int numCut){ //平均分配的思想
        if(p.num<2*k){
            copy(p);
            return;
        }
        int reNum=p.num/k;                //将原点集划分为reNum个点集
        int extra=p.num-reNum*k;          //每个点集K个点时会剩余extra个点
        int start=0;
        char c;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)){
            p.quickSort(p,0,p.num-1,0);
            c='c';
        }
        else {
            p.quickSort(p,0,p.num-1,1);
            c='r';
        }
        if(extra>0){                      //有多余点时就将k+1个点切到点集中
            copy(p.cut(p,0,k));
            roads.getCuttingLine(p,k,k+1,c,numCut);
            start=k+1;
        }else{
            copy(p.cut(p,start,k-1));     //否则就切k个点
            roads.getCuttingLine(p,k-1,k,c,numCut);
            start=k;
        }
        partitionAverage(p.cut(p,start,p.num-1),numCut+1);
    }
    public void partitionMedian(Points p,int numCut){//沿点的中位数进行切割
        if(p.num<2*k){
            copy(p);
            return;
        }
        Points p1;
        Points p2;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            int i = p.num/2;
            p.quickSort(p,0,p.num-1,0);
            p1=p.cut(p,0,i);                          //处于中线上的分给前一个界面
            p2=p.cut(p,i+1,p.num-1);
            roads.getCuttingLine(p,i,i+1,'c',numCut);
        }else{
            int i = p.num/2;
            p.quickSort(p,0,p.num-1,1);
            p1=p.cut(p,0,i);                          //处于中线上的分给前一个界面
            p2=p.cut(p,i+1,p.num-1);
            roads.getCuttingLine(p,i,i+1,'r',numCut);
        }
        partitionMedian(p1,numCut+1);
        partitionMedian(p2,numCut+1);
    }
    public void partitionRound(Points p,int numCut){//分为⌈d/2⌉+⌈r/2⌉和⌊d/2⌋+⌊r/2⌋
        int d=p.num/k;
        int r=p.num-k*d;
        if(p.num<2*k){
            copy(p);
            return;
        }
        Points p1,p2;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            p.quickSort(p,0,p.num-1,0);

            p.detect(0);

            p1=p.cut(p,0,d/2*k+r/2-1);
            p2=p.cut(p,d/2*k+r/2,p.num-1);
            roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'c',numCut);
        }else{
            p.quickSort(p,0,p.num-1,1);

            p.detect(1);

            p1=p.cut(p,0,d/2*k+r/2-1);
            p2=p.cut(p,d/2*k+r/2,p.num-1);
            roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'r',numCut);
        }
        partitionRound(p1,numCut+1);
        partitionRound(p2,numCut+1);
    }
    public boolean partitionCentralLineRound(Points p,int numCut){
        if(p.num<2*k){
            if(p.num<k){
                return false;
            }
            copy(p);
            return true;
        }
        Points p1;//划分为p1,p2两个区域
        Points p2;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            int i = p.search((p.xmax+p.xmin)/2,0);
            p.quickSort(p,0,p.num-1,0);
            if(i!=p.num-1) {
                p1 = p.cut(p, 0, i);                          //处于中线上的分给前一个界面
                p2 = p.cut(p, i + 1, p.num - 1);
                roads.getCuttingLine(p,i,i+1,'c',numCut);//(p,i,i+1,'c');
            }else{
                p1=p.cut(p,0,i-1);                            //当切割线位于最后一个点之前时，将最后一个点划为新的聚集区
                p2=p.cut(p,i,p.num-1);
                roads.getCuttingLine(p,i-1,i,'c',numCut);
            }
        }else{
            int i = p.search((p.ymax+p.ymin)/2,1);
            p.quickSort(p,0,p.num-1,1);
            if(i!=p.num-1) {
                p1 = p.cut(p, 0, i);                          //处于中线上的分给前一个界面
                p2 = p.cut(p, i + 1, p.num - 1);
                roads.getCuttingLine(p,i,i+1,'r',numCut);
            }else{
                p1=p.cut(p,0,i-1);                            //当切割线位于最后一个点之前时，将最后一个点划为新的聚集区
                p2=p.cut(p,i,p.num-1);
                roads.getCuttingLine(p,i-1,i,'r',numCut);
            }
        }
        if(!partitionCentralLineRound(p1,numCut+1)||!partitionCentralLineRound(p2,numCut+1)){
            roads.deleteLast();
            int d=p.num/k;
            int r=p.num-k*d;
            if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
                p.quickSort(p,0,p.num-1,0);
                p1=p.cut(p,0,d/2*k+r/2-1);
                p2=p.cut(p,d/2*k+r/2,p.num-1);
                roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'c',numCut);
            }else{
                p.quickSort(p,0,p.num-1,1);
                p1=p.cut(p,0,d/2*k+r/2-1);
                p2=p.cut(p,d/2*k+r/2,p.num-1);
                roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'r',numCut);
            }
            partitionCentralLineRound(p1,numCut+1);
            partitionCentralLineRound(p2,numCut+1);
        }
        return true;
    }
    public void partitionCentroid(Points p,int numCut){             //在质心处划分
        if(p.num<2*k){
            copy(p);
            return;
        }
        Points p1;//划分为p1,p2两个区域
        Points p2;
        double sum=0;
        Points[] temp;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            for(int i=0;i<p.num;i++){
                sum+=p.getX(i);
            }
            double x=sum/p.num;
            temp=p.cutAt(p,x,0);
            p1=temp[0];
            p2=temp[1];
            temp=balance(p1,p2);
            p1=temp[0];
            p2=temp[1];
            roads.getCuttingLine(p,p1.num-1,p1.num+1,'c',numCut);
        }else{
            for(int i=0;i<p.num;i++){
                sum+=p.getY(i);
            }
            double y=sum/p.num;
            temp=p.cutAt(p,y,1);
            p1=temp[0];
            p2=temp[1];
            temp=balance(p1,p2);
            p1=temp[0];
            p2=temp[1];
            roads.getCuttingLine(p,p1.num-1,p1.num+1,'r',numCut);
        }
        partitionCentroid(p1,numCut+1);
        partitionCentroid(p2,numCut+1);
    }
    public void partitionCentralLineK(Points p,int numCut){
        double lat=40000/360;                                           //纬度一度的距离
        double lon=lat*Math.cos(2*Math.PI*40/360);                      //经度一度的距离
        if(p.num<2*k){
            copy(p);
            return;
        }
        Points p1;//划分为p1,p2两个区域
        Points p2;
        Points[] temp;
        if((p.xmax-p.xmin)*lat>(p.ymax-p.ymin)*lon) {
            temp=p.cutAt(p,(p.xmax+p.xmin)/2,0);
            p1=temp[0];
            p2=temp[1];
            temp=balance(p1,p2);
            p1=temp[0];
            p2=temp[1];
            roads.getCuttingLine(p,p1.num-1,p1.num+1,'c',numCut);
        }else{
            temp=p.cutAt(p,(p.ymax+p.ymin)/2,1);
            p1=temp[0];
            p2=temp[1];
            temp=balance(p1,p2);
            p1=temp[0];
            p2=temp[1];
            roads.getCuttingLine(p,p1.num - 1,p1.num + 1,'r',numCut);
        }
        partitionCentralLineK(p1,numCut+1);
        partitionCentralLineK(p2,numCut+1);
    }
    public Points[] balance(Points p1,Points p2){                      //确保p1,p2点数均大于k
        Points[] temp=new Points[2];
        try{
            if(p1.num+p2.num<2*k){                                     //若点数总和小于2k，则不可能切分出两个符合k匿名的区域
                throw new Exception("p1 and p2 have no more than 2*k points together");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        int i,j;
        if(p1.num<k&&p2.num>=k){
            for(i=0,j=k-p1.num;i<j;i++){
                p1.add(p2.assemble[i],"special");
            }
            p2=p2.cut(p2,j,p2.num-1);
            p1.reset();
        }else if(p1.num>=k&&p2.num<k){
            for(i=0,j=k-p2.num;i<j;i++){
                p2.add(p1.assemble[p1.num-1-i],"special");
            }
            p1=p1.cut(p1,0,p1.num-j-1);
            p2.reset();
        }
        temp[0]=p1;
        temp[1]=p2;
        return temp;
    }
    public void partitionCentralLine(Points p, int numCut){
        unqualified=new int[p.num/k];
        unqualifiedRegionNum=0;
        unqualifiedPointNum=0;
        doPartitionCentralLine(p,numCut);

    }
    public void doPartitionCentralLine(Points p,int numCut){//沿地理中线进行切割
        if(p.num<2*k){
            copy(p);
            if(p.num<k){
                //System.out.printf("region %d has less than %d points.\n",regionNum,k);
                unqualified[unqualifiedRegionNum]=regionNum;
                unqualifiedPointNum+=region.get(regionNum-1).num;
                unqualifiedRegionNum++;
            }
            return;
        }
        Points p1;//划分为p1,p2两个区域
        Points p2;
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            int i = p.search((p.xmax+p.xmin)/2,0);
            p.quickSort(p,0,p.num-1,0);
            if(i!=p.num-1) {
                p1 = p.cut(p, 0, i);                          //处于中线上的分给前一个界面
                p2 = p.cut(p, i + 1, p.num - 1);
                roads.getCuttingLine(p,i,i+1,'c',numCut);//(p,i,i+1,'c');
            }else{
                p1=p.cut(p,0,i-1);                            //当切割线位于最后一个点之前时，将最后一个点划为新的聚集区
                p2=p.cut(p,i,p.num-1);
                roads.getCuttingLine(p,i-1,i,'c',numCut);
            }
        }else{
            int i = p.search((p.ymax+p.ymin)/2,1);
            p.quickSort(p,0,p.num-1,1);
            if(i!=p.num-1) {
                p1 = p.cut(p, 0, i);                          //处于中线上的分给前一个界面
                p2 = p.cut(p, i + 1, p.num - 1);
                roads.getCuttingLine(p,i,i+1,'r',numCut);
            }else{
                p1=p.cut(p,0,i-1);                            //当切割线位于最后一个点之前时，将最后一个点划为新的聚集区
                p2=p.cut(p,i,p.num-1);
                roads.getCuttingLine(p,i-1,i,'r',numCut);
            }
        }
        doPartitionCentralLine(p1,numCut+1);
        doPartitionCentralLine(p2,numCut+1);
    }
    public void calDistance(){                                 //计算区域内所有点距离中点的距离和
        //改为实际算法
        //纬度1度，距离差40000/360（km）=111.111km
        //经度1度，距离差111.111*cos40(km),其中40为大约的纬度值
        double lat=40000/360;                                           //纬度一度的距离
        double lon=lat*Math.cos(2*Math.PI*40/360);                      //经度一度的距离
        distance=new double[regionNum];
        for(int i=0;i<regionNum;i++){
            double x=(region.get(i).xmax+region.get(i).xmin)/2;
            double y=(region.get(i).ymax+region.get(i).ymin)/2;
            distance[i]=0.0;
            for(int j=0;j<region.get(i).num;j++){
                distance[i]+=Math.pow(Math.pow((region.get(i).getX(j)-x)*lat,2)
                        +Math.pow((region.get(i).getY(j)-y)*lon,2),0.5);
            }
            sumDistance+=distance[i];
        }
    }
    public void calArea(){
        //改为实际算法
        //纬度1度，距离差40000/360（km）=111.111km
        //经度1度，距离差111.111*cos40(km),其中40为大约的纬度值
        area=new double[regionNum];
        double lat=40000/360;                                           //纬度一度的距离
        double lon=lat*Math.cos(2*Math.PI*40/360);                      //经度一度的距离
        for(int i=0;i<regionNum;i++){
            area[i]=(region.get(i).xmax-region.get(i).xmin)*lat
                    *(region.get(i).ymax-region.get(i).ymin)*lon;
            sumArea+=area[i];
        }
    }
    public void areaBUDE(){                             //为BUDE特加的
        double lat=40000/360;                                           //纬度一度的距离
        double lon=lat*Math.cos(2*Math.PI*40/360);                      //经度一度的距离
        regionNum=1;
        area=new double[1];
        region.add(p);
        sumArea=area[0]=(p.xmax-p.xmin)*(p.ymax-p.ymin)*lat*lon;
        //distance=new double[1];

    }
    public void distanceBUDE(){
        double lat=40000/360;                                           //纬度一度的距离
        double lon=lat*Math.cos(2*Math.PI*40/360);                      //经度一度的距离
        regionNum=1;
        distance=new double[1];
        for(int i=0;i<regionNum;i++){
            double x=(region.get(i).xmax+region.get(i).xmin)/2;
            double y=(region.get(i).ymax+region.get(i).ymin)/2;
            distance[i]=0.0;
            for(int j=0;j<region.get(i).num;j++){
                distance[i]+=Math.pow(Math.pow((region.get(i).getX(j)-x)*lat,2)
                        +Math.pow((region.get(i).getY(j)-y)*lon,2),0.5);
            }
            sumDistance+=distance[i];
        }
    }
    /*public void roundPartition2(Points p){//分为⌈d/2⌉+⌊r/2⌋和⌊d/2⌋+⌈r/2⌉
        int d=p.num/k;
        int r=p.num-k*d;
        if(p.num<2*k){
            copy(p);
            print(p);
            return;
        }
        if((p.xmax-p.xmin)>(p.ymax-p.ymin)) {
            p.quickSort(p,0,p.num-1,0);
            if(r%2==0){
                Points p1=p.cut(p,0,d/2*k+r/2-1);
                Points p2=p.cut(p,d/2*k+r/2,p.num-1);
                cuttingLine[numCuttingLine]=getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'c');
                partitionRound(p1);
                partitionRound(p2);
            }
            else {
                Points p1 = p.cut(p, 0,d/2*k+r/2);
                Points p2 = p.cut(p, d/2*k+r/2+1,p.num - 1);
                cuttingLine[numCuttingLine]=getCuttingLine(p,d/2*k+r/2,d/2*k+r/2+1,'c');
                partitionRound(p1);
                partitionRound(p2);
            }
        }else{
            if (r%2 == 0) {
                Points p1 = p.cut(p,0,d/2*k+r/2-1);
                Points p2 = p.cut(p,d/2*k+r/2,p.num-1);
                cuttingLine[numCuttingLine]=getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'r');
                partitionRound(p1);
                partitionRound(p2);
            } else {
                p.quickSort(p, 0, p.num - 1, 1);
                Points p1 = p.cut(p, 0, d / 2 * k + r / 2);
                Points p2 = p.cut(p, d / 2 * k + r / 2 + 1, p.num - 1);
                cuttingLine[numCuttingLine]=getCuttingLine(p,d/2*k+r/2,d/2*k+r/2+1,'r');
                partitionRound(p1);
                partitionRound(p2);
            }
        }
    }*/



    public static void main(String[] args){
        String[] trajectory = {"002-5：00-11：00","003-5：00-15：00","004-5：00-10：00","007-5：00-16：00","009-5：00-12：00","011-7：00-12：00","013-7：00-10：00","016-5：00-12：00","017-8：00-12：00","018-9：00-15：00"   //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";
        Kanonymity k = new Kanonymity(180, importFile.file(title,trajectory));
        k.p.screening(39.943623,39.969028,116.412167,116.455862);
        k.partitionCentralLineK(k.p, 0);
        k.calDistance();
        new DrawSee(k,title+"地理中线再平衡");
        //k.partitionAverage(k.p,1);
        /*
        System.out.println(k.regionNum);
        System.out.println(k.numCuttingLine);
        System.out.printf("%f,%f",k.cuttingLine[0][0],k.cuttingLine[0][1]);*/
        //k.region[1].output();
        //k.partitionCentralLine(p);
        //k.partitionRound(k.p);
        //k.partitionMedian(k.p);
        //new DrawSee(100,90);
    }
}
