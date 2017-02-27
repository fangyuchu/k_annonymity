/**
 * Created by Administrator on 2016/12/26.
 */
public class Roads {
    public Line[] roadRow;
    public Line[] roadColumn;
    public Integer numCuttingLine;          //分割线数量
    public int numRow;                      //数量-1
    public int numColumn;
    public int rowSize;                     //容量
    public int columnSize;


    public Roads(){
        numCuttingLine=0;
        roadRow=new Line[10];
        numRow=0;
        rowSize=10;
        roadColumn=new Line[10];
        numColumn=0;
        columnSize=10;
    }

    public void expand(){
        if(numRow==rowSize) {
            rowSize *= 2;
            Line[] temp = new Line[rowSize];
            for (int i = 0; i < numRow; i++) {
                temp[i] = roadRow[i];
            }
            roadRow = temp;
        }else if(numColumn==columnSize){
            columnSize *= 2;
            Line[] temp = new Line[columnSize];
            for (int i = 0; i < numColumn; i++) {
                temp[i] = roadColumn[i];
            }
            roadColumn = temp;
        }
    }

    public void getCuttingLine(Points p,int start,int last,char c,int numCut){  //在start和last中切割，c为r表示按行切，c为c表示按列切。
        if(c=='r'){
            roadRow[numRow++]=new Line().setLine(p.xmin,(p.getY(start)+p.getY(last))/2,p.xmax,(p.getY(start)+p.getY(last))/2,numCut);
            numCuttingLine++;
            expand();
        }else if(c=='c'){
            roadColumn[numColumn++]=new Line().setLine((p.getX(start)+p.getX(last))/2,p.ymin,(p.getX(start)+p.getX(last))/2,p.ymax,numCut);
            numCuttingLine++;
            expand();
        }else System.out.println("error input in getCuttingLine");
    }
    public class Line{
        public Double[] line;               //分割线，一行为一条线共四个元素，分别为起始点坐标和结束点坐标
        public int numCut;                  //这条线在属于第几次分割线
        Line(){
            line=new Double[4];
        }
        public Line setLine(double x1,double y1,double x2,double y2,int num){
            line[0]=x1;
            line[1]=y1;
            line[2]=x2;
            line[3]=y2;
            numCut=num;
            return this;
        }

    }


    public static void main(String[] args)
    {
        Roads test=new Roads();
        test.expand();
    }
}
