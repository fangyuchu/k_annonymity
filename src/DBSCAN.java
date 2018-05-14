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
            if(!visit[neighborPts.get(k)]){
                visit[neighborPts.get(k)]=true;
                ArrayList<Integer> neighborPts2=findPoint(p.assemble[neighborPts.get(k)]);
                if(neighborPts2.size()>=k){
                    for(int j=0;j<neighborPts2.size();j++){
                        if(neighborPts.indexOf(neighborPts2.get(j))==-1){
                            neighborPts.add(neighborPts2.get(j));
                        }
                    }
                }
            }
            if(index[neighborPts.get(k)]==0){
                index[neighborPts.get(k)]=ind;
                p.assemble[neighborPts.get(k)].cluster=ind;
            }
        }
    }

    public ArrayList<Integer> findPoint(Points.Point center){
        //找到center点直接密度可达的点,返回其在assemble中的下标
        // returnall points within P's eps-neighborhood (including P)
        ArrayList<Integer> result=new ArrayList<>();
        for(int i=0;i<p.num;i++){
            if(calDistance(p.assemble[i],center)<=E){
                result.add(i);
            }
        }
        return result;
    }
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
