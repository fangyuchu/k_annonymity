import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Administrator on 2018/5/14.
 */
public class WelcomePage extends JFrame {
    WelcomePage(){
        this.setTitle("群智用户位置匿名系统");
        Container p=getContentPane();

        Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)screensize.getHeight();
        int width=height/2;
        GridBagLayout gl=new GridBagLayout();
        this.setLayout(gl);
        setBounds(100,100,width,width);
        JPanel sf=selectFile(width);
        JPanel ip=inputParameter(width);
        this.add(sf);
        this.add(ip);
        GridBagConstraints gbs=new GridBagConstraints();
        gbs.fill=GridBagConstraints.BOTH;
        gbs.gridwidth=3;
        gbs.weightx=1;
        gbs.insets=new Insets(10,20,10,20);
        //gbs.anchor=GridBagConstraints.NORTH;
        gl.setConstraints(sf,gbs);
        gbs.gridx=0;
        gbs.gridy=3;
        gbs.weightx=0;
        gbs.gridwidth=30;
        gl.setConstraints(ip,gbs);
        //setBounds(100, 50, 900, 900);


        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public JPanel selectFile(int width){
        JButton jb=new JButton("选择文件");
        JTextField jt=new JTextField();
        ActionListener a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser sf=new JFileChooser();
                sf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                sf.showDialog(new JLabel(), "选择");
                File file=sf.getSelectedFile();
                jt.setText(file.getAbsolutePath());
                if(file.isDirectory()){
                    String[] s=importFile.file(file.getName());
                }else{
                    String s=file.getAbsolutePath();
                }

            }
        };
        jb.addActionListener(a);
        JPanel jp=new JPanel();
        GridBagLayout gl=new GridBagLayout();
        GridBagConstraints gbs=new GridBagConstraints();
        jp.setLayout(gl);
        jp.add(jb);
        jp.add(jt);
        gbs.fill=GridBagConstraints.BOTH;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        gbs.gridwidth=1;                    //宽度占用一个格子
        gbs.insets=new Insets(5, 5, 5, 5);
        gl.setConstraints(jb,gbs);
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gbs.weightx=1;                      //横向可拉伸
        gl.setConstraints(jt,gbs);
        jp.setBorder(BorderFactory.createLineBorder(Color.red, 3));
        jp.setBounds(width/9,width/20,(int)(width/1.4),width/9);
        return jp;
    }
    public JPanel inputParameter(int width){
        GridBagLayout gl=new GridBagLayout();
        JPanel jp=new JPanel();
        jp.setLayout(gl);
        JLabel text=new JLabel("选择范围：");
        //text.setVerticalTextPosition(JLabel.CENTER);
        JLabel jd=new JLabel("经度范围：");
        JTextField jdmin=new JTextField(5);
        JLabel h1=new JLabel("————");
        JTextField jdmax=new JTextField(5);
        JLabel wd=new JLabel("纬度范围：");
        JTextField wdmin=new JTextField(5);
        JLabel h2=new JLabel("————");
        JTextField wdmax=new JTextField(5);
        JLabel kL=new JLabel("k:");
        JLabel epsilonL=new JLabel("epsilon:");
        JLabel minPtsL=new JLabel("minPts:");
        JTextField k=new JTextField(5);
        JTextField epsilon=new JTextField(5);
        JTextField minPts=new JTextField(5);
        jp.add(text);
        jp.add(jd);
        jp.add(jdmin);
        jp.add(h1);
        jp.add(jdmax);
        jp.add(wd);
        jp.add(wdmin);
        jp.add(h2);
        jp.add(wdmax);
        jp.add(kL);
        jp.add(k);
        jp.add(epsilonL);
        jp.add(epsilon);
        jp.add(minPtsL);
        jp.add(minPts);
        GridBagConstraints gbs=new GridBagConstraints();
        gbs.insets=new Insets(5,5,5,5);
        gbs.fill=GridBagConstraints.BOTH;
        gbs.gridx=2;
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gl.setConstraints(text,gbs);
        gbs.gridx=GridBagConstraints.RELATIVE;
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gl.setConstraints(jdmax,gbs);
        gl.setConstraints(wdmax,gbs);
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gl.setConstraints(k,gbs);
        gl.setConstraints(epsilon,gbs);
        gl.setConstraints(minPts,gbs);
        jp.setBorder(BorderFactory.createLineBorder(Color.red, 3));
        jp.setBounds(width/9,width/5,(int)(width/1.4),width/5);
        return jp;
    }
    public JPanel headPhoto(){
        int width=800;
        int height=300;
        JPanel jp=new JPanel();
        JLabel jl=new JLabel();
        ImageIcon img = new ImageIcon("D:/java/jar/k_annonimity/map.png");// 创建图片对象
        img.setImage(img.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT ));
        jl.setIcon(img);
        jp.add(jl);
        jp.setPreferredSize(new Dimension(width,height));
        return jp;
    }
    public static void main(String[] args) {
        new WelcomePage();
    }
}
