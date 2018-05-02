import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2018/5/2.
 */
public class trajGenerator {
    Points initial;
    ArrayList<Points> traj=new ArrayList<>();
    double[] speedControl={0,0.0001};
    trajGenerator(String title,int times){
        Raster temp=new Raster(10,importFile.file(title));
        initial=new Points().copy(temp.p);
        traj.add(initial);
        for(int j=times;j>=0;j--) {
            Points t = new Points().copy(traj.get(traj.size() - 1));
            for (int i = 0; i < t.num; i++) {
                t.assemble[i].setX(t.assemble[i].x() + nextDouble());
                t.assemble[i].setY(t.assemble[i].y() + nextDouble());
            }
            t.reset();
            traj.add(t);
        }
    }
    public double nextDouble() {
        double positive=speedControl[0] + ((speedControl[1] - speedControl[0]) * new Random().nextDouble());
        double negative=0-(speedControl[0] + ((speedControl[1] - speedControl[0]) * new Random().nextDouble()));
        return new Random().nextDouble()>=0.5?positive:negative;
    }
    public static void main(String[] args){
        trajGenerator test=new trajGenerator("20081024",3);
        test.nextDouble();
    }
}
