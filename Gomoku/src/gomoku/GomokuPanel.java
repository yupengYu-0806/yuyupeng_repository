package gomoku;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//封装容器
public class GomokuPanel extends JPanel {
    //常量行间距、列间距、左边距和上边距
    private final int rowSpace = 39;
    private final int colSpace = 39;
    private final int leftSpace = 12;
    private final int topSpace = 12;
    //存放所有棋子的数组
    private int[][] gomokuArray = new int[15][15];
    private int boardX = -1;
    private int boardY = -1;
    //存放棋子的集合,注意：用的是java.util里面的List，不是java.awt里面的
    private List<String> gomokuStrList = new ArrayList<String>();
    //棋子半径
    private int chessRadius = 13;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        //使用2D画笔
        Graphics2D graphics2D = (Graphics2D)g;
        //设置线条宽度为2.0f
        Stroke stroke = new BasicStroke(2.0f);
        //把线条宽度应用到2D画笔上
        graphics2D.setStroke(stroke);
        graphics2D.setColor(Color.black);
        //用2D画笔开始画线
        //画横线
        for(int i = 0;i < 15;i++){
            graphics2D.drawLine(leftSpace,rowSpace * i + leftSpace,rowSpace * 14 + leftSpace,rowSpace * i + leftSpace);
        }
        //画竖线
        for(int i = 0;i < 15;i++){
            graphics2D.drawLine(topSpace + colSpace * i,topSpace,topSpace + colSpace * i,colSpace * 14 + topSpace);
        }
        //画棋子
        for(int i = 0;i < gomokuArray.length;i++){
            for(int j = 0;j < gomokuArray[i].length;j++){
                if(gomokuArray[i][j] == 0){
                    continue;
                }
                else{
                    //规定1为黑棋
                    if(gomokuArray[i][j] == 1){
                        g.drawImage(new ImageIcon("image/black.png").getImage(),j * colSpace + leftSpace - chessRadius,i * rowSpace + topSpace - chessRadius,this);
                    }
                    //规定2为白棋
                    else if(gomokuArray[i][j] == 2){
                        g.drawImage(new ImageIcon("image/white.png").getImage(),j * colSpace + leftSpace - chessRadius,i * rowSpace + topSpace - chessRadius,this);
                    }
                    //在最后一次有效落点位置画一个红点，用以标识
                    if(boardX != -1){
                        graphics2D.setColor(Color.red);
                        //画红点
                        g.fillOval(boardY * rowSpace + leftSpace - 5,boardX * colSpace + topSpace - 5,10,10);
                    }
                }
            }
        }
    }

    //保存数据的方法
    public void saveFile(File file) {
        System.out.println("gomokuPanel保存数据");
        //往外写出数据
        PrintWriter out;
        try {
            //new OutputStreamReader是加上一层IO流，new FileOutputStream是在IO流上加上文件流
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for(int i = 0;i < gomokuStrList.size();i++){
                out.println(gomokuStrList.get(i));
                //输出缓冲区里的数据，刷新缓冲区
                out.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //加载数据的方法
    public void loadFile(File file){
        //加载之前全部清空gomokuArray和gomokuStrList
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                gomokuArray[i][j] = 0;
            }
        }
        gomokuStrList.clear();
        System.out.println("gomokuPanel加载数据");
        //从文件里读数据
        BufferedReader in;
        try {
            //new InputStreamReader是加上一层IO流，new FileInputStream是在IO流上加上文件流
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            while((line = in.readLine()) != null){
                gomokuStrList.add(line);
                //加载到的文件字符串的读取
                readDate(line);
            }
        }
        //捕获所有的IO异常,这样就只要catch一次了，要不然要有好多的catch
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载到的文件字符串的读取
    public void readDate(String str){
        //以":"分割字符串
        String[] strs = str.split(":");
        int i = Integer.parseInt(strs[0]);
        int j = Integer.parseInt(strs[1]);
        boardX = i;
        boardY = j;
        int chessColor = Integer.parseInt(strs[2]);
        gomokuArray[i][j] = chessColor;
        //重画gomokuPanel
        repaint();
    }

    public int getRowSpace() {
        return rowSpace;
    }

    public int getColSpace() {
        return colSpace;
    }

    public int getLeftSpace() {
        return leftSpace;
    }

    public int getTopSpace() {
        return topSpace;
    }

    public int[][] getGomokuArray() {
        return gomokuArray;
    }

    public void setGomokuArray(int[][] gomokuArray) {
        this.gomokuArray = gomokuArray;
    }

    public int getBoardX() {
        return boardX;
    }

    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    public List<String> getGomokuStrList() {
        return gomokuStrList;
    }

    public void setGomokuStrList(List<String> gomokuStrList) {
        this.gomokuStrList = gomokuStrList;
    }

    public int getChessRadius() {
        return chessRadius;
    }

    public void setChessRadius(int chessRadius) {
        this.chessRadius = chessRadius;
    }
}
