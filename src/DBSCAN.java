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
    public Double[][] distance;                            //上半三角有数据

    DBSCAN(String s[], Double E, Integer minPts){
        try{
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            this.E=E;
            this.minPts=minPts;
            distance=new Double[p.num][p.num];
            cluster=new ArrayList<>();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Double calDistance(Points.Point a, Points.Point b){                 //翻译两点之间点欧氏距离
        return Math.pow(Math.pow(a.x()-b.x(),2)+Math.pow(a.y()-b.y(),2),0.5);
    }
    public void run(){
        Points unvisited=new Points();
        unvisited.copy(p);
        while(unvisited.num!=0){
            Points.Point center=unvisited.assemble[0];
            unvisited.deletePoint(unvisited.assemble[0]);
            Points candidate=new Points();
            ArrayList<Integer> temp=findPoint(unvisited,center);
            if(temp.size()>=minPts) {
                for (Integer x : temp) {
                    candidate.add(unvisited.assemble[x]);                          //将可达的点加入候选集
                    unvisited.deletePoint(unvisited.assemble[x]);
                }
                Points tempCluster=new Points();
                tempCluster.add(center);
                cluster.add(tempCluster);
            }else{                                                                 //是噪声点就直接略过
                continue;
            }
            while(candidate.num!=0){
                center=candidate.assemble[0];                                      //将候选集第一个点从候选集移入簇内
                cluster.get(0).add(center);                                        //并将其可达点加入候选集
                candidate.deletePoint(center);

            }
        }
    }
    public ArrayList<Integer> findPoint(Points unvisited,Points.Point center){     //找到center点直接密度可达的点并
        ArrayList<Integer> result=new ArrayList<>();
        for(int i=0;i<unvisited.num;i++){
            if(calDistance(unvisited.assemble[i],center)<=E){
                result.add(i);
            }
        }
        return result;
    }


}
