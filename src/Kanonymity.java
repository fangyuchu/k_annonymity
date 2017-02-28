import java.util.Random;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Kanonymity {
    public Integer k;
    public Integer numRegion;               //区域数量
    public Points p;                         //点集合
    public Points region[];                //划分后的区域点集合数组
    public Double distance[];              //元素为某匿名区域中的点离该区域中点的欧式距离和
    public Double sumDistance;
    Roads roads;

    public int[] unqualified;              //点数小于K的区域号（数组下标+1）
    public Integer unqualifiedRegionNum;
    public Integer unqualifiedPointNum;



   /* public Kanonymity(int k,int num){
        this.num=num;
        p=new Points(num);
        this.k=k;
        this.numRegion=0;
        region=new Points[num];
        roads =new Roads();
    }*/
    public Kanonymity(int k,String s){
        try {
            p = new Points(ReadExcel.readCell(s));
            //this.num=p.num;
            this.k=k;
            this.numRegion=0;
            region=new Points[p.num];
            roads=new Roads();
        }catch(Exception e){}
    }
    public Kanonymity(int k, String  s[]){
        try{
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
           // this.num=p.num;
            this.k=k;
            this.numRegion=0;
            region=new Points[p.num];
            roads=new Roads();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void print(){//                       将刚刚划分好的点区域输出
        for(int i=0;i<numRegion;i++) {
            System.out.printf("region %d \n", i+1);
            try{
                System.out.printf("distance %f \n", distance[i]);
            }catch (java.lang.NullPointerException e){

            }
            region[i].output();
            System.out.printf("\n");
        }
    }

    public void copy(Points p){//将划分好的点对象赋给re数组
        region[numRegion]=p;
        numRegion++;
    }

    public void delete(){
        this.numRegion=0;
        roads=new Roads();
        distance=null;
        sumDistance=null;
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
            //cuttingLine[numCuttingLine]=getCuttingLine(p,k,k+1,c,numCut++);
            start=k+1;
        }else{
            copy(p.cut(p,start,k-1));     //否则就切k个点
            roads.getCuttingLine(p,k-1,k,c,numCut);
            //cuttingLine[numCuttingLine]=getCuttingLine(p,k-1,k,c,numCut++);
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
            //cuttingLine[numCuttingLine]=getCuttingLine(p,i,i+1,'c');
        }else{
            int i = p.num/2;
            p.quickSort(p,0,p.num-1,1);
            p1=p.cut(p,0,i);                          //处于中线上的分给前一个界面
            p2=p.cut(p,i+1,p.num-1);
            roads.getCuttingLine(p,i,i+1,'r',numCut);
            //cuttingLine[numCuttingLine]=getCuttingLine(p,i,i+1,'r');
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
            p1=p.cut(p,0,d/2*k+r/2-1);
            p2=p.cut(p,d/2*k+r/2,p.num-1);
            roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'c',numCut);
        }else{
            p.quickSort(p,0,p.num-1,1);
            p1=p.cut(p,0,d/2*k+r/2-1);
            p2=p.cut(p,d/2*k+r/2,p.num-1);
            roads.getCuttingLine(p,d/2*k+r/2-1,d/2*k+r/2,'r',numCut);
        }
        partitionRound(p1,numCut+1);
        partitionRound(p2,numCut+1);
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
                //System.out.printf("region %d has less than %d points.\n",numRegion,k);
                unqualified[unqualifiedRegionNum]=numRegion;
                unqualifiedPointNum+=region[numRegion-1].num;
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

    public void calDistance(){
        distance=new Double[numRegion];
        sumDistance=new Double(0);
        for(int i=0;i<numRegion;i++){
            Double x=(region[i].xmax+region[i].xmin)/2;
            Double y=(region[i].ymax+region[i].ymin)/2;
            distance[i]=0.0;
            for(int j=0;j<region[i].num;j++){
                distance[i]+=Math.pow(Math.pow(region[i].getX(j)-x,2)+Math.pow(region[i].getY(j)-y,2),0.5);
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
        //Points p=new Points(500);
        String[] trajectory={ "000-20081023（08-12）","015-20081023（08-12）","011-20081023（08-12）","012-20081023（08-12）","013-20081023（08-12）","014-20081023（08-12）","001-20081023（08-12）"
        };//"20081112023003"
        String title="2008-10-23 8：00-12：00";
        Kanonymity k = new Kanonymity(20, DrawPoint.file(title,trajectory));
        k.p.quickSort(k.p,0,k.p.num-1,0);
        //k.partitionAverage(k.p,1);
        /*
        System.out.println(k.numRegion);
        System.out.println(k.numCuttingLine);
        System.out.printf("%f,%f",k.cuttingLine[0][0],k.cuttingLine[0][1]);*/
        //k.region[1].output();
        //k.partitionCentralLine(p);
        //k.partitionRound(k.p);
        //k.partitionMedian(k.p);
        //new DrawSee(100,90);
    }
}
