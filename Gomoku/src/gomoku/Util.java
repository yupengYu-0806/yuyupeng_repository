package gomoku;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

//封装一个工具类 让窗体跟随鼠标移动
public class Util {
    private static JFrame frame;
    //判断是否点击
    private static boolean ifClick;
    //点击位置的坐标x,y
    private static int x,y;

    //构造方法，用private修饰，私有化，不让外界创建Util类的对象
    private Util(){

    }

    //窗体跟随鼠标移动方法
    public static void Mobile(JFrame jFrame){
        frame = jFrame;//这里不能用this.frame = jFrame;因为frame由static修饰,frame是属于类的
        //监听鼠标点击事件，点击鼠标会打印出点击处的坐标
        frame.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                super.mousePressed(e);
                ifClick = true;
                x = e.getX();
                y = e.getY();
                System.out.println("==" + x + ":" + y);
            }
        });
        //监听鼠标点击事件，点击鼠标会触发窗口跟随鼠标移动
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(ifClick){
                    int left = frame.getLocation().x;
                    int top = frame.getLocation().y;
                    frame.setLocation(left + e.getX() - x,top + e.getY() - y);
                }
            }
        });
    }
}
