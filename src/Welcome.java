import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by Administrator on 2018/5/14.
 */
public class Welcome extends JFrame {
    Font fMandarin=new Font("黑体",Font.BOLD,17);
    Font fEnglish=new Font("Arial",Font.BOLD,17);
    Welcome(){
        this.setTitle("群智用户位置匿名系统");
        Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)screensize.getHeight();
        int width=height/2;

        GridBagLayout gl=new GridBagLayout();
        GridBagConstraints gbs=new GridBagConstraints();
        this.setLayout(gl);

        JPanel sf=selectFile(width);
        JPanel ip=inputParameter(width);
        JPanel db=dbscanParameter();
        JPanel ok=run(sf,ip,db);

        this.add(sf);
        this.add(ip);
        this.add(db);
        this.add(ok);

        gbs.fill=GridBagConstraints.BOTH;
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gbs.weightx=1;
        gbs.insets=new Insets(10,20,10,20);
        gl.setConstraints(sf,gbs);
        gbs.weightx=0;
        gl.setConstraints(ip,gbs);
        gl.setConstraints(db,gbs);

        setBounds((int)(screensize.getWidth()/2-width/2),height/4,width,width);             //居中显示窗口，边长大小为屏幕高度的一半
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public JPanel selectFile(int width){
        JButton jb=new JButton("选择文件");
        jb.setFont(fMandarin);
        JTextField jt=new JTextField(20);
        jt.setEditable(false);
        ActionListener a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser sf=new JFileChooser();
                sf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                sf.setCurrentDirectory(new File(importFile.route));
                sf.showDialog(new JLabel(), "选择");
                File file=sf.getSelectedFile();
                if(file!=null) {
                    jt.setText(file.getAbsolutePath());
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
        gl.setConstraints(jt,gbs);
        return jp;
    }
    public JPanel inputParameter(int width){
        GridBagLayout gl=new GridBagLayout();
        JPanel jp=new JPanel();
        jp.setLayout(gl);
        JLabel jd=new JLabel("经度范围：");
        jd.setFont(fMandarin);
        JTextField jdmin=new JTextField("0",5);
        defaultText(jdmin,"0");
        numberInput(jdmin);
        JLabel h1=new JLabel("————");
        JTextField jdmax=new JTextField("360",5);
        defaultText(jdmax,"360");
        numberInput(jdmax);
        JLabel wd=new JLabel("纬度范围：");
        wd.setFont(fMandarin);
        JTextField wdmin=new JTextField("0",5);
        defaultText(wdmin,"0");
        numberInput(wdmin);
        JLabel h2=new JLabel("————");
        JTextField wdmax=new JTextField("360",5);
        defaultText(wdmax,"360");
        numberInput(wdmax);
        JLabel kL=new JLabel("k:");
        kL.setFont(fEnglish);
        JTextField k=new JTextField(5);
        numberInput(k);
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
        GridBagConstraints gbs=new GridBagConstraints();
        gbs.insets=new Insets(5,5,5,5);
        gbs.fill=GridBagConstraints.BOTH;
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gl.setConstraints(jdmax,gbs);
        gl.setConstraints(wdmax,gbs);
        gl.setConstraints(k,gbs);
        return jp;
    }
    public JPanel dbscanParameter(){
        GridBagLayout gl=new GridBagLayout();
        JPanel jp=new JPanel();
        jp.setLayout(gl);
        //参数部分
        JLabel epsilonL=new JLabel("epsilon:");
        epsilonL.setFont(fEnglish);
        JLabel minPtsL=new JLabel("minPts:");
        minPtsL.setFont(fEnglish);
        JTextField epsilon=new JTextField(10);
        numberInput(epsilon);
        JTextField minPts=new JTextField(10);
        numberInput(minPts);
        epsilon.setEditable(false);                                 //初始默认不dbscan，不可输入
        minPts.setEditable(false);
        //单选框部分
        ButtonGroup btg=new ButtonGroup();
        JRadioButton nodb=new JRadioButton("快速栅格合并",true);
        nodb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                epsilon.setEditable(false);
                minPts.setEditable(false);
            }
        });
        JRadioButton db=new JRadioButton("基于密度聚类的栅格合并");
        db.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                epsilon.setEditable(true);
                minPts.setEditable(true);
            }
        });
        btg.add(nodb);
        btg.add(db);
        JPanel selectDB=new JPanel();
        selectDB.setLayout(new GridLayout(2,1));
        selectDB.add(nodb);
        selectDB.add(db);
        jp.add(selectDB);
        jp.add(epsilonL);
        jp.add(epsilon);
        jp.add(minPtsL);
        jp.add(minPts);
        GridBagConstraints gbs=new GridBagConstraints();
        gbs.insets=new Insets(5,50,5,5);
        gbs.fill=GridBagConstraints.BOTH;
        gbs.gridheight=2;                                   //单选框占两行
        gl.setConstraints(selectDB,gbs);
        gbs.gridheight=1;
        gbs.gridwidth=GridBagConstraints.REMAINDER;
        gl.setConstraints(epsilon,gbs);
        gl.setConstraints(minPts,gbs);
        return jp;
    }
    public JPanel run(JPanel sf,JPanel ip,JPanel db){
        JPanel jp=new JPanel();
        GridLayout gl=new GridLayout(2,1,5,5);
        jp.setLayout(gl);
        JLabel st=new JLabel("就绪",JLabel.CENTER);
       // st.setBorder(BorderFactory.createLineBorder(Color.red));
        st.setFont(fMandarin);
        JButton ok=new JButton("确认");
        ok.setFont(fMandarin);
        ActionListener a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField jf;
                jf=(JTextField)ip.getComponent(9);
                if(!checkInput(jf))return;
                int k=Integer.valueOf(jf.getText());
                jf=(JTextField)ip.getComponent(1);
                double ymin=Double.valueOf(jf.getText());
                jf=(JTextField)ip.getComponent(3);
                double ymax=Double.valueOf(jf.getText());
                jf=(JTextField)ip.getComponent(5);
                double xmin=Double.valueOf(jf.getText());
                jf=(JTextField)ip.getComponent(7);
                double xmax=Double.valueOf(jf.getText());
                JTextField route=(JTextField) sf.getComponent(1);
                if(!checkInput(route)){
                    sf.getComponent(0).setForeground(Color.red);
                    return;
                }
                double epsilon;
                int minPts;
                JPanel selection=(JPanel) db.getComponent(0);               //获取单选框面板
                JRadioButton nodb=(JRadioButton)selection.getComponent(0);  //不dbscan的选项
                if(!nodb.isSelected()) {                                    //上述选项没被选中，既需要dbscan
                    jf = (JTextField) db.getComponent(2);
                    if (!checkInput(jf)) return;
                    epsilon = Double.valueOf(jf.getText());
                    jf = (JTextField) db.getComponent(4);
                    if (!checkInput(jf)) return;
                    minPts = Integer.valueOf(jf.getText());
                }else{
                    epsilon=0;                                              //两参数置为0，说明不需要dbscan
                    minPts=0;
                }
                st.setText("运行中...");
                Runnable r=new Runnable() {
                    @Override
                    public void run() {
                        Raster t=new Raster(k,importFile.file(route.getText()));
                        t.screen(xmin,xmax,ymin,ymax);
                        if(!(epsilon==0&&minPts==0)) {
                            t.dbscan(epsilon, minPts);
                        }
                        t.partition();
                        new DrawRaster(t,route.getText());
                        st.setText("运行完成");
                    }
                };
                SwingUtilities.invokeLater(r);
            }
        };
        jp.add(st);
        ok.addActionListener(a);
        jp.add(ok);
        return jp;
    }
    public boolean checkInput(JTextField jt){
        //若有输入，返回true；否则提示后返回false
        String warning="请输入！！！";
        if(jt.getText().equals("")||jt.getText().equals(warning)){
            jt.setText(warning);
            defaultText(jt,warning);
            return false;
        }
        return true;
    }
    public void numberInput(JTextField jt){
        //使jt只能输入数字，或小数点
        KeyListener k=new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)||keyChar==KeyEvent.VK_PERIOD) {

                } else {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        jt.addKeyListener(k);
    }
    public void defaultText(JTextField jt,String defult){
        //在jf被点击时，默认字符串消失。失去焦点时，显示默认字符串
        FocusListener f=new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(jt.getText().equals(defult)){
                    jt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(jt.getText().equals("")){
                    jt.setText(defult);
                }
            }
        };
        jt.addFocusListener(f);
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
        new Welcome();
    }
}
