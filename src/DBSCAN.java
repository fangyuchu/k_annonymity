import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by fangyc on 03/03/2017.
 */
public class DBSCAN {
    public Points p;
    public double E;                                       //领域半径
    public int minPts;
    public boolean visit[];
    public int[] index;                                     //聚类结果的索引，0为噪声
    public int ind=0;
    DBSCAN(Points p,double E, int minPts){
        try{
            this.p=p;
            this.E=E;
            this.minPts=minPts;
            //distance=new double[p.num][p.num];
            visit=new boolean[p.num];
            index=new int[p.num];
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    DBSCAN(String s[], double E, int minPts){
        try{
            p=new Points(ReadExcel.readCell(s[0]),0);
            for(int i=1;i<s.length;i++){
                p.add(ReadExcel.readCell(s[i]),i);
            }
            this.E=E;
            this.minPts=minPts;
            //distance=new double[p.num][p.num];
            visit=new boolean[p.num];
            index=new int[p.num];
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Double calDistance(Points.Point a, Points.Point b){                 //翻译两点之间点欧氏距离
        return Math.pow(Math.pow(a.x()-b.x(),2)+Math.pow(a.y()-b.y(),2),0.5);
    }


    public void runDB(){
        /*C = 0
        foreach point P in dataset D {
            ifP is visited
            continue next point
            mark P as visited
            NeighborPts = regionQuery(P, eps)
            ifsizeof(NeighborPts) < MinPts
            mark P as NOISE
            else {
                C = next cluster
                expandCluster(P, NeighborPts, C, eps, MinPts)
            }
        }*/
        for(int i=0;i<p.num;i++){
            if(visit[i]){
                continue;
            }
            System.out.println(i);
            visit[i]=true;
            ArrayList<Integer> neighborPts=findPoint(p.assemble[i]);
            if(neighborPts.size()<minPts){
                index[i]=0;
                p.assemble[i].cluster=0;
            }else{
                ind++;
                expandCluster(i,neighborPts);
            }
        }
    }
    public void expandCluster(int i,ArrayList<Integer> neighborPts){
        /*
        add Pto cluster C
        foreach point P' in NeighborPts {
            ifP' is not visited {
                 mark P' as visited
                 NeighborPts' = regionQuery(P', eps)
                 if sizeof(NeighborPts') >= MinPts
                    NeighborPts = NeighborPts joined with NeighborPts'
                 }
           ifP' is not yet member of any cluster
                    add P' to cluster C
            }
         */
        index[i]=ind;
        p.assemble[i].cluster=ind;
        for(int k=0;k<neighborPts.size();k++){
            System.out.printf("k:%d,neighborPts.size:%d\n",k,neighborPts.size());
            if(!visit[neighborPts.get(k)]){
                visit[neighborPts.get(k)]=true;
                ArrayList<Integer> neighborPts2=findPoint(p.assemble[neighborPts.get(k)]);
                if(neighborPts2.size()>=k){
                    for(int j=0;j<neighborPts2.size();j++){
                        if(neighborPts.indexOf(neighborPts2.get(j))==-1){
                            neighborPts.add(neighborPts2.get(j));
                        }
                    }
                    //neighborPts.addAll(neighborPts2);
                }
            }
            if(index[neighborPts.get(k)]==0){
                index[neighborPts.get(k)]=ind;
                p.assemble[neighborPts.get(k)].cluster=ind;
            }
        }
    }

    public ArrayList<Integer> findPoint(Points.Point center){     //找到center点直接密度可达的点,返回其在assemble中的下标
        // returnall points within P's eps-neighborhood (including P)
        ArrayList<Integer> result=new ArrayList<>();
        for(int i=0;i<p.num;i++){
            if(calDistance(p.assemble[i],center)<=E){
                result.add(i);
            }
        }
        return result;
    }
    /*public void calDistance(){
        for(int i=0;i<p.num;i++){
            for(int j=0;j<p.num;j++){
                //System.out.printf(" %d ",j);
                distance[i][j]=Math.pow(Math.pow(p.getX(i)-p.getX(j),2)+Math.pow(p.getY(i)-p.getY(j),2),0.5);
                //distance[i][j]=1.0;
            }
        }
    }*/
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
    /*public void run(){                                           //有问题
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
    }*/

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

        DBSCAN d=new DBSCAN(importFile.file(title,trajectory),(double)0.005,10);
        d.runDB();
        System.out.printf("finish");
        //d.run();
    }
}
