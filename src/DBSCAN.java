import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by fangyc on 03/03/2017.
 */
public class DBSCAN {
    public Points p;
    public Double E;                                       //领域半径
    public Integer minPts;
    public ArrayList<Points> cluster;
    public double[][] distance;                            //上半三角有数据
    //public Integer[] cluster;                              //和点集中的点一一对应，0表示噪声点
    //public Integer clusterNum;

    DBSCAN(String s[], Double E, Integer minPts){
        try{
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            //p.signal=2;
            //p.quickSort(p,0,p.num-1,1);
            this.E=E;
            this.minPts=minPts;
            distance=new double[p.num][p.num];
            cluster=new ArrayList<>();
            //calDistance();
            //cluster=new Integer[p.num];
            //clusterNum=0;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Double calDistance(Points.Point a, Points.Point b){                 //翻译两点之间点欧氏距离
        return Math.pow(Math.pow(a.x()-b.x(),2)+Math.pow(a.y()-b.y(),2),0.5);
    }
    public void calDistance(){
        for(int i=0;i<p.num;i++){
            for(int j=0;j<p.num;j++){
                //System.out.printf(" %d ",j);
                distance[i][j]=Math.pow(Math.pow(p.getX(i)-p.getX(j),2)+Math.pow(p.getY(i)-p.getY(j),2),0.5);
                //distance[i][j]=1.0;
            }
        }
    }

    /*public void run(){
        Points unvisited=new Points();
        unvisited.copy(p);
        while(unvisited.num!=0){
            Points.Point center=unvisited.assemble[0];
            unvisited.deletePoint(unvisited.assemble[0]);
            Points candidate=new Points();
            ArrayList<Integer> temp=findPoint(0);
            if(temp.size()>=minPts) {
                for (Integer x : temp) {
                    candidate.add(unvisited.assemble[x]);                          //将可达的点加入候选集
                    unvisited.deletePoint(unvisited.assemble[x]);
                }
            }
        }
    }*/
    public void run(){                                           //有问题
        Points unvisited=new Points();
        unvisited.copy(p);
        while(unvisited.num!=0){
            Points.Point center=new Points().new Point().copy(unvisited.assemble[0]);
            unvisited.deletePoint(unvisited.assemble[0]);
            Points candidate=new Points();
            //unvisited.quickSort(unvisited,0,unvisited.num-1,1);
            ArrayList<Integer> temp=findPoint(center,unvisited);
            if(temp.size()>=minPts) {
                for (int x=0;x<temp.size();x++) {
                    candidate.add(unvisited.assemble[temp.get(x)]);                          //将可达的点加入候选集
                }
                //unvisited.detect(1);
                unvisited.deletePoint(candidate.assemble);
                Points tempCluster = new Points();
                tempCluster.add(center);
                cluster.add(tempCluster);
            }else{                                                                 //是噪声点就直接略过
                continue;
            }
            while(candidate.num!=0){
                center=candidate.assemble[0];                                      //将候选集第一个点从候选集移入簇内
                cluster.get(0).add(center);                                        //并将其可达点加入候选集
                candidate.deletePoint(center);
                temp=findPoint(center,unvisited);
                if(temp.size()>=minPts){
                    for(Integer x:temp){
                        candidate.add(unvisited.assemble[x]);
                        unvisited.deletePoint(unvisited.assemble[x]);
                    }
                }
            }
        }
    }
    public ArrayList<Integer> findPoint(Points.Point center,Points unvisited){     //找到center点直接密度可达的点并
        ArrayList<Integer> result=new ArrayList<>();
        for(int i=0;i<unvisited.num;i++){
            if(calDistance(unvisited.assemble[i],center)<=E&&!center.equal(unvisited.assemble[i])){
                result.add(i);
            }
        }
        return result;
    }
    /*public ArrayList<Integer> findPoint(int i) {                                     //查找下标为i的点的可达点，返回可达点的下标数组
        ArrayList<Integer> result=new ArrayList<>();
        for(int j=0;j<p.num;j++){
            if(distance[i][j]<=E&&j!=i){
                result.add(j);
            }
        }
        return result;
    }*/

    public static void main(String[] args){
        String[] trajectory={"002-5：00-11：00","003-5：00-15：00"  //要计算的轨迹
        };
        String title="2008-12-14 5：00-16：00";

        DBSCAN d=new DBSCAN(importFile.file(title,trajectory),(double)0.00005,10);
        d.run();
    }
}
