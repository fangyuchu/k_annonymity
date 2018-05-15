

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;

/**
 * 1:按钮在Swing中是较为常见的组件，用于触发特定动作
 * Swing中提供了多种按钮，包括提交按钮，复选框，单选按钮等
 * 这些按钮都是从AbstractButton类中继承而来的
 *
 * 2:Swing中的提交按钮组件(JButton)由JButton对象表示
 * JButton含有4种主要的构造方法
 * 参数text,icon分别代表显示文字标签和图标
 *
 * 3:本实例使用了两种方式创建按钮，第一种是在初始化按钮时赋予按钮图标与文字
 * 这种初始化必须先获得图片路径，然后将路径实例化到Icon，然后在Button中加载出来
 * 第二种方式是首先创建一个没有定义图标和文字的按钮对象，然后使用
 * setIcon()方法为这个按钮定制一个图标。
 * setToolTipText()方法是为按钮设置提示文字，鼠标停留在按钮上面即可
 * setBorderPainted()方法设置边界是否显示
 * setMaximumSize()方法设置按钮的大小与图标的大小一致，该类方法需要的参数类型是
 *           Dimension类对象，这样看上去此图片就如同按钮一样摆放在窗体中，
 *           同时也可以使用setEnabled()方法设置按钮是否可用
 *
 * @author biexiansheng
 *
 */
public class JButtonTest extends JFrame{
    public JPanel j;

    public JButtonTest(){//定义一个构造方法
        //获取图片所在的URL    以下2行代码需要额外注意
        URL url=JButtonTest.class.getResource("imageButtoo.jpg");
        //Icon icon=new ImageIcon(url);//实例化Icon对象

        //设置网格布局管理器   3行2列  水平5垂直5
        //setLayout(new GridLayout(3,2,100,100));
        j=new JPanel();
        add(j);
        //创建容器
        Container container=getContentPane();
        //创建按钮，同时设置按钮文字和图标
        JButton jb=new JButton("button");
        j.add(jb);//将按钮添加到容器中}//上下位两种按钮的实例化


       /* JButton jb2=new JButton();//实例化一个没有文字与图片的按钮
        jb2.setMaximumSize(new Dimension(90,30));//设置按钮和图片的大小相同
       // jb2.setIcon(icon);//为按钮设置图标
        jb2.setHideActionText(true);
        jb2.setToolTipText("图片按钮");//设置按钮提示为文字
        jb2.setBorderPainted(false);//设置按钮边界不显示
        jb2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //弹出对话框
                JOptionPane.showMessageDialog(null, "弹出对话框");
            }
        });
        container.add(jb2);//将按钮添加到容器中*/

        setTitle("提交按钮组件按钮小试牛刀");//设置窗口标题
        setVisible(true);//设置窗口可视化
        setSize(500,550);//设置窗口的大小
        //设置窗口的关闭方式
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JButtonTest jb=new JButtonTest();
    }

}