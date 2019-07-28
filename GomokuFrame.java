package gomoku;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//五子棋,继承于JFrame，使用了ActionListener监听器接口
public class GomokuFrame extends JFrame implements ActionListener {
    //自定义容器
    private GomokuPanel gomokuPanel;
    //设置一个顶层标签
    private JLabel jLabel_main = new JLabel();
    //菜单条
    private JMenuBar jMenuBar;
    //游戏菜单
    private JMenu jMenuGame;
    //保存游戏菜单选项
    private JMenuItem savaGame;
    //加载游戏菜单选项
    private JMenuItem loadGame;
    //悔棋，开始，最小化，关闭，音乐开关按钮,AI模式按钮
    private JButton button_BackChess,button_Start,button_Min,button_Close,bt_Music,button_AI;
    //黑棋标签
    private JLabel jLabel_black = new JLabel(new ImageIcon("image/blackmsg.png"));
    //白棋标签
    private JLabel jLabel_white = new JLabel(new ImageIcon("image/whitemsg.png"));
    //电脑玩家标签
    private JLabel jLabel_AIplayer = new JLabel(new ImageIcon("image/computer.png"));
    //人类玩家1标签
    private JLabel jLabel_player_one = new JLabel(new ImageIcon("image/player.png"));
    //人类玩家2标签
    private JLabel jLabel_player_two = new JLabel(new ImageIcon("image/player.png"));
    //判断黑白棋，初始化默认为玩家白棋先下
    private boolean isBlack = false;
    //判断当前棋局是否结束
    private boolean isEnd = false;
    //音乐类
    private Music music = new Music("music/b.wav");
    //音乐开关状态，初始化为开状态
    private boolean turnON_Music = true;
    //权值数组
    private int[][] weightArrays = new int[15][15];
    //权值库
    private WeightStore weightStore = new WeightStore();
    //AI模式开关状态，默认为关闭状态
    private boolean isAIplayer = false;
    //是否是起始状态,初始化为是起始状态
    private boolean isStart = true;
    // 人机对战难度等级条目
    private String[] listData = new String[]{"初出茅庐", "出神入化"};
    // 创建一个下拉列表框
    private JComboBox<String> comboBox = new JComboBox<String>(listData);
    //-------------------------------------------------------------------------
    //所有的赢法
    private boolean[][][] wins = new boolean[15][15][600];
    //赢法索引
    private int count = 0;
    //我方得分
    private int[][] myScore = new int[15][15];
    //电脑得分
    private int[][] computerScore = new int[15][15];
    //我方在某种赢法上的落子个数
    private int[] myWin = new int[600];
    //电脑在某种赢法上的落子个数
    private int[] computerWin = new int[600];
    //最高得分
    private int max = 0;
    //最高得分的坐标
    private int u,v;
    //保存
    private  int[] tempMywin = new int[600];
    private int[] tempComputer = new int[600];
    private int flag = 0;
    //--------------------------------------------------------------------------------

    //构造方法
    public GomokuFrame(){
        allWinsAlgorithm();//保存所有的赢法
        //加个背景图
        jLabel_main.setIcon(new ImageIcon("image/back01.png"));
        this.add(jLabel_main);

        //设定位置和宽高
        this.setBounds(200,100,900,700);
        //设置窗口无边框
        this.setUndecorated(true);
        //程序运行时任务栏的java图标换成图片
        this.setIconImage(new ImageIcon("image/exe_label.png").getImage());

        //实现窗口随鼠标移动
        Util.Mobile(this);
        //设置自定义容器
        gomokuPanel = new GomokuPanel();
        gomokuPanel.setBounds(160,80,592,592);
        //设置gomokuPanel为透明
        gomokuPanel.setOpaque(false);
        //把jPanel加到jLabel_main顶层标签上，不能写this.add(jPanel)，层次应搞清楚
        jLabel_main.add(gomokuPanel);

        //设置菜单条
        jMenuBar = new JMenuBar();
        jMenuBar.setBounds(0,10,100,35);
        jMenuBar.setOpaque(false);//设置菜单条为透明
        jMenuBar.setBorderPainted(false);
        //菜单条加到顶层标签上
        jLabel_main.add(jMenuBar);

        //设置游戏菜单
        jMenuGame = new JMenu("游戏");
        //游戏菜单加个背景图片
        jMenuGame.setIcon(new ImageIcon("image/bar.png"));
        jMenuGame.setContentAreaFilled(false);
        jMenuGame.setBorderPainted(false);
        //游戏菜单加到菜单条上
        jMenuBar.add(jMenuGame);

        jMenuGame.add(savaGame = new JMenuItem("保存游戏"));
        jMenuGame.add(loadGame = new JMenuItem("加载游戏"));

        //把黑棋标签加到顶层标签
        jLabel_black.setBounds(30,90,110,110);
        jLabel_black.setOpaque(false);//设置黑棋标签为透明
        jLabel_main.add(jLabel_black);

        //把白棋标签加到顶层标签
        jLabel_white.setBounds(770,480,110,110);
        jLabel_white.setOpaque(false);//设置白棋标签为透明
        jLabel_main.add(jLabel_white);

        //把人类玩家标签2加到顶层标签,它在黑棋下面
        jLabel_player_two.setBounds(35,210,100,42);
        jLabel_player_two.setOpaque(false);
        jLabel_main.add(jLabel_player_two);

        //把人类玩家标签1加到顶层标签,它在白棋下面
        jLabel_player_one.setBounds(775,600,100,42);
        jLabel_player_one.setOpaque(false);
        jLabel_main.add(jLabel_player_one);

        //悔棋按钮加到顶层标签
        button_BackChess = new JButton(new ImageIcon("image/huiqi.png"));
        button_BackChess.setBounds(30,500,100,42);
        button_BackChess.setBorderPainted((false));
        button_BackChess.setContentAreaFilled(false);
        jLabel_main.add(button_BackChess);

        //开始按钮加到顶层标签
        button_Start = new JButton(new ImageIcon("image/refresh.png"));
        button_Start.setBounds(400,6,100,42);
        button_Start.setBorderPainted(false);
        button_Start.setContentAreaFilled(false);
        jLabel_main.add(button_Start);

        //最小化按钮加到顶层标签
        button_Min = new JButton(new ImageIcon("image/min.png"));
        button_Min.setBounds(800,20,14,14);
        button_Min.setBorderPainted(false);
        button_Min.setContentAreaFilled(false);
        jLabel_main.add(button_Min);

        //关闭按钮加到顶层标签
        button_Close = new JButton(new ImageIcon("image/close.png"));
        button_Close.setBounds(835,20,14,14);
        button_Close.setBorderPainted(false);
        button_Close.setContentAreaFilled(false);
        jLabel_main.add(button_Close);

        //音乐开关按钮加到顶层标签，默认是有声音的
        //如果当前声音是开着的，图标换成对应图片
        bt_Music = new JButton(new ImageIcon("image/musicNote_ON.png"));
        bt_Music.setBounds(450,0,150,52);
        bt_Music.setBorderPainted(false);
        bt_Music.setContentAreaFilled(false);
        jLabel_main.add(bt_Music);

        //AI模式开关加到顶层标签，默认为关闭模式
        button_AI = new JButton(new ImageIcon("image/humanPlayer.png"));
        button_AI.setBounds(770,250,120,42);
        button_AI.setBorderPainted((false));
        button_AI.setContentAreaFilled(false);
        jLabel_main.add(button_AI);

        savaGame.addActionListener(this);
        loadGame.addActionListener(this);
        button_BackChess.addActionListener(this);
        button_Start.addActionListener(this);
        bt_Music.addActionListener(this);
        button_Min.addActionListener(this);
        button_Close.addActionListener(this);
        button_AI.addActionListener(this);

        //下拉框加到顶层标签
        comboBox.setBounds(770,300,120,42);
        jLabel_main.add(comboBox);
        // 设置默认选中的条目，为第一项“初出茅庐”
        comboBox.setSelectedIndex(0);
        comboBox.setEnabled(false);

        // 添加条目选中状态改变的监听器
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("选中: " + comboBox.getSelectedIndex() + " = " + comboBox.getSelectedItem());
                }
            }
        });

        //添加gomokuPanel监听事件
        gomokuPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //只有还未分出胜负才继续监听鼠标事件
                if(!isEnd){
                    super.mouseClicked(e);
                    int x = e.getX();
                    int y = e.getY();
                    int i = Math.round((float)(y - gomokuPanel.getLeftSpace()) / (gomokuPanel.getRowSpace()));
                    int j = Math.round((float)(x - gomokuPanel.getTopSpace()) / (gomokuPanel.getColSpace()));
                    System.out.println(i + ":" + j);
                    gomokuPanel.setBoardX(i);
                    gomokuPanel.setBoardY(j);

                    //如果该地方已经有棋子了
                    if(gomokuPanel.getGomokuArray()[i][j] != 0){
                        //弹出提示窗口
                        JOptionPane.showMessageDialog(GomokuFrame.this,"此地已有棋子了");
                        return;
                    }
                    //规定黑棋为1，白棋为2
                    String str = i + ":" + j + ":" + (isBlack ? 1:2);
                    gomokuPanel.getGomokuStrList().add(str);
                    gomokuPanel.getGomokuArray()[i][j] = isBlack ? 1:2;
                    //重画棋盘
                    gomokuPanel.repaint();
                    if(ifEndGame(gomokuPanel.getGomokuArray(),i,j,isBlack ? 1:2)){
                        isEnd = true;
                        isBlack = !isBlack;//选手换边
                        if(turnON_Music){
                            music.setName("music/game_win.wav");
                            music.playMusic();
                        }
                        if(!isBlack){
                            //弹出提示窗口
                            JOptionPane.showMessageDialog(GomokuFrame.this,"黑方棋手获胜，本局结束！");
                            return;
                        }
                        else{
                            //弹出提示窗口
                            JOptionPane.showMessageDialog(GomokuFrame.this,"白方棋手获胜，本局结束！");
                            return;
                        }
                    }
                    else{
                        //只要有落子，就不是起始状态
                        isStart = false;
                        //如果音乐开关状态为开，那么落子就有声音
                        if(turnON_Music){
                            music.playMusic();
                        }
                        isBlack = !isBlack;//选手换边
                        //只有AI模式打开了才会由AIplayer上
                        if(isAIplayer){
                            if(comboBox.getSelectedItem().equals("初出茅庐")){
                                AIplayer();//我的算法上
                            }
                            else if(comboBox.getSelectedItem().equals("出神入化")){
                                for(int k = 0;k<count;k++) {
                                    //如果在i，j位置上第k种算法可以实现
                                    if(wins[i][j][k]) {
                                        //使我方在第k种赢法上的落子数加1
                                        myWin[k]++;
                                        //使人机在第k种算法上的落子数赋值一个大于5的数
                                        //表示人机在第k种赢法上将不可能实现
                                        tempComputer[k] = computerWin[k];
                                        computerWin[k] = 7;
                                    }
                                }
                                computerAI();//赖雅崧的算法上
                            }
                        }
                    }
                }
            }
        });

        //程序运行显示JFrame
        this.setVisible(true);
        //关闭窗口结束程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //重写ActionListener监听器接口的方法
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == savaGame){
            //只有棋局还没结束的时候才能保存游戏
            System.out.println("保存游戏");
            //弹出保存文件的对话框
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(this);
            //获得用户选择的文件
            File file = jFileChooser.getSelectedFile();
            //保存文件
            gomokuPanel.saveFile(file);
            //返回
            return;
        }
        if(e.getSource() == loadGame){
            System.out.println("加载游戏");
            //弹出打开文件的对话框
            JFileChooser jFileChooser = new JFileChooser();
            //加载
            jFileChooser.showOpenDialog(this);
            //获得用户选择的文件
            File file = jFileChooser.getSelectedFile();
            //如果文件不为空则加载文件
            if(file != null){
                gomokuPanel.loadFile(file);
                //每次加载完要换边,具体换什么颜色要看上次最后是谁下的棋
                if(gomokuPanel.getGomokuArray()[gomokuPanel.getBoardX()][gomokuPanel.getBoardY()] == 1){
                    isBlack = false;
                }
                else{
                    isBlack = true;
                }
                //对加载出来的棋局判断是否已分胜负
                if(ifEndGame(gomokuPanel.getGomokuArray(),gomokuPanel.getBoardX(),gomokuPanel.getBoardY(),(gomokuPanel.getGomokuArray()[gomokuPanel.getBoardX()][gomokuPanel.getBoardY()] == 1) ? 1:2)){
                    isEnd = true;
                    if(turnON_Music){
                        music.setName("music/game_win.wav");
                        music.playMusic();
                    }
                    if(!isBlack){
                        //弹出提示窗口
                        JOptionPane.showMessageDialog(GomokuFrame.this,"黑方棋手获胜，本局结束！");
                        return;
                    }
                    else{
                        //弹出提示窗口
                        JOptionPane.showMessageDialog(GomokuFrame.this,"白方棋手获胜，本局结束！");
                        return;
                    }
                }
                //返回
                return;
            }
        }
        if(e.getSource() == button_BackChess){
            if(isEnd){//悔棋以后就算是分出了胜负也要重新设为未分出胜负
                isEnd = !isEnd;
                music.setName("music/b.wav");
            }
            System.out.println("悔棋");
            List<String> step = gomokuPanel.getGomokuStrList();
            if(step.size() == 0){
                return;
            }
            String[] strs = step.get(step.size() - 1).split(":");
            int i = Integer.parseInt(strs[0]);
            int j = Integer.parseInt(strs[1]);
            gomokuPanel.getGomokuArray()[i][j] = 0;
            if(isAIplayer){
                flag++;
                if(flag % 2 == 1){
                    for(int k = 0;k<count;k++){
                        if(wins[i][j][k]){
                            myWin[k] = tempMywin[k];
                            computerWin[k]--;
                        }
                    }
                }
                if(flag % 2 == 0){
                    for (int k = 0;k<count;k++){
                        if(wins[i][j][k]){
                            computerWin[k] = tempComputer[k];
                            myWin[k]--;
                        }
                    }
                }
            }

            //如果就下了一步棋就悔棋，就相当于重新开始
            if(step.size() == 1){
                gomokuPanel.setBoardX(-1);
                gomokuPanel.setBoardY(-1);
                gomokuPanel.repaint();
                isBlack = !isBlack;
                return;
            }
            //一下三句话是为了方便在悔棋后小红点重新定位
            String[] strings = step.get(step.size() - 2).split(":");
            gomokuPanel.setBoardX(Integer.parseInt(strings[0]));
            gomokuPanel.setBoardY(Integer.parseInt(strings[1]));

            gomokuPanel.repaint();
            gomokuPanel.getGomokuStrList().remove(step.size() - 1);
            isBlack = !isBlack;
        }
        if(e.getSource() == button_Start){
            System.out.println("重新开始游戏");
            isEnd = false;
            isBlack = false;
            isStart = true;
            flag = 0;
            music.setName("music/b.wav");
            gomokuPanel.getGomokuStrList().clear();
            for(int i = 0;i < 15;i++){
                for(int j = 0;j < 15;j++){
                    gomokuPanel.getGomokuArray()[i][j] = 0;

                }
            }
            for(int k = 0;k < count;k++){
                myWin[k] = 0;
                computerWin[k] = 0;
            }
            u = 0;
            v = 0;
            gomokuPanel.repaint();
        }
        if(e.getSource() == bt_Music){
            //音乐开关状态取反
            turnON_Music = !turnON_Music;
            if(turnON_Music){
                bt_Music.setIcon(new ImageIcon("image/musicNote_ON.png"));
            }
            else{
                bt_Music.setIcon(new ImageIcon("image/musicNote_OFF.png"));
            }
        }
        if(e.getSource() == button_Min){
            System.out.println("最小化窗口");
            GomokuFrame.this.setState(JFrame.ICONIFIED);
        }
        if(e.getSource() == button_Close){
            System.out.println("关闭窗口，退出游戏");
            System.exit(0);
        }
        if(e.getSource() == button_AI){
            //只有处于游戏起始状态才能更换对战模式
            if(isStart){
                isAIplayer = !isAIplayer;
                if(isAIplayer){
                    comboBox.setEnabled(true);
                    /*int i = (int)(Math.random() * 3 + 5);
                    int j = (int)(Math.random() * 3 + 5);
                    System.out.println(i + ":" + j);
                    gomokuPanel.setBoardX(i);
                    gomokuPanel.setBoardY(j);
                    String str = i + ":" + j + ":" + 1;
                    gomokuPanel.getGomokuStrList().add(str);
                    gomokuPanel.getGomokuArray()[i][j] = 1;
                    //重画棋盘
                    gomokuPanel.repaint();*/
                    button_AI.setIcon(new ImageIcon("image/AIplayer.png"));
                    //二号选手下的标签换成电脑选手
                    jLabel_player_two.setIcon(new ImageIcon("image/computer.png"));
                }
                else{
                    comboBox.setEnabled(false);
                    button_AI.setIcon(new ImageIcon("image/humanPlayer.png"));
                    //二号选手下的标签换成人类选手
                    jLabel_player_two.setIcon(new ImageIcon("image/player.png"));
                }
            }
            else{
                System.out.println("必须要结束本局才能更换对战模式");
                //弹出提示窗口
                JOptionPane.showMessageDialog(GomokuFrame.this,"必须要结束本局才能更换对战模式");
            }
        }
    }

    //判断是否可以结束游戏
    public Boolean ifEndGame(int[][] arrays,int i,int j,int flag){
        int level = ifLevelEndGame(arrays,i,j,flag);
        int vertical = ifVerticalEndGame(arrays,i,j,flag);
        int pie = ifPieEndGame(arrays,i,j,flag);
        int na = ifNaEndGame(arrays,i,j,flag);
        if(level >= 5 || vertical >= 5 || pie >= 5 || na >= 5){
            if(flag == 1){
                System.out.println("黑方获胜");
            }
            else{
                System.out.println("白方获胜");
            }
            return true;
        }
        return false;
    }

    //判断是否水平成龙
    public int ifLevelEndGame(int[][] arrays,int i,int j,int flag){
        int col = j - 1;
        int number = 1;
        //往左找
        for(;col >= 0 && arrays[i][col] == flag;col--){
            number++;
        }
        //往右找
        for(col = j + 1;col < 15 && arrays[i][col] == flag;col++){
            number++;
        }
        return number;
    }

    //判断是否垂直成龙
    public int ifVerticalEndGame(int[][] arrays,int i,int j,int flag){
        int row = i - 1;
        int number = 1;
        //往上找
        for(;row >= 0 && arrays[row][j] == flag;row--){
            number++;
        }
        //往下找
        for(row = i + 1;row < 15 && arrays[row][j] == flag;row++){
            number++;
        }
        return number;
    }

    //判断是否自左下至右上成龙
    public int ifPieEndGame(int[][] arrays,int i,int j,int flag){
        int row = i + 1;
        int col = j - 1;
        int number = 1;
        //往左下找
        for(;row < 15 && col >= 0 && arrays[row][col] == flag;row++,col--){
            number++;
        }
        //往右上找
        for(row = i - 1,col = j + 1;row >= 0 && col < 15 && arrays[row][col] == flag;row--,col++){
            number++;
        }
        return number;
    }

    //判断是否自右下至左上成龙
    public int ifNaEndGame(int[][] arrays,int i,int j,int flag){
        int row = i + 1;
        int col = j + 1;
        int number = 1;
        //往右下找
        for(;row < 15 && col < 15 && arrays[row][col] == flag;row++,col++){
            number++;
        }
        //往左上找
        for(row = i - 1,col = j - 1;row >= 0 && col >= 0 && arrays[row][col] == flag;row--,col--){
            number++;
        }
        return number;
    }

    //初始化权值数组
    public void initWeightArrays(){
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                weightArrays[i][j] = 0;
            }
        }
    }

    //打印权值数组
    public void printWeithtArrays(){
        System.out.println("对应的权值数组:");
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                System.out.print(weightArrays[i][j] + " ");
            }
            System.out.println();
        }
    }

    //整体棋局横向获取权值
    public void levelGetWeight(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "";
        String str_right = "";
        int weight_left = 0,weight_right = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int k = j - 1;k >= 0 && k >= j - 4;k--){
                        str_left += arrays[i][k];
                    }
                    weight_left = weightStore.getWeight(str_left);
                    str_left = "";
                    for(int k = j + 1;k <= 14 && k <= j + 4;k++){
                        str_right += arrays[i][k];
                    }
                    weight_right = weightStore.getWeight(str_right);
                    str_right = "";
                    //左右权值取大者
                    if(weight_left > weight_right){
                        weightArrays[i][j] += weight_left;
                    }
                    else{
                        weightArrays[i][j] += weight_right;
                    }
                }
            }
        }
    }

    //整体棋局纵向获取权值
    public void verticalGetWeight(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_up = "";
        String str_down = "";
        int weight_up = 0,weight_down = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int x = i - 1;x >= 0 && x >= i - 4;x--){
                        str_up += arrays[x][j];
                    }
                    weight_up = weightStore.getWeight(str_up);
                    str_up = "";
                    for(int x = i + 1;x <= 14 && x <= i + 4;x++){
                        str_down += arrays[x][j];
                    }
                    weight_down = weightStore.getWeight(str_down);
                    str_down = "";
                    //上下权值取大者
                    if(weight_up > weight_down){
                        weightArrays[i][j] += weight_up;
                    }
                    else{
                        weightArrays[i][j] += weight_down;
                    }
                }
            }
        }
    }

    //整体棋局自左下至右上获取权值
    public void pieGetWeight(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "";
        String str_right = "";
        int weight_left = 0,weight_right = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左下
                    for(int x = i + 1,k = j - 1;x <= 14 && x <= i + 4 && k >= 0 && k >= j - 4;x++,k--){
                        str_left += arrays[x][k];
                    }
                    weight_left = weightStore.getWeight(str_left);
                    str_left = "";
                    //右上
                    for(int x = i - 1,k = j + 1;x >= 0 && x >= i - 4 && k <= 14 && k <= j + 4;x--,k++){
                        str_right += arrays[x][k];
                    }
                    weight_right = weightStore.getWeight(str_right);
                    str_right = "";
                    //左下右上权值取大者
                    if(weight_left > weight_right){
                        weightArrays[i][j] += weight_left;
                    }
                    else{
                        weightArrays[i][j] += weight_right;
                    }
                }
            }
        }
    }

    //整体棋局自右下左上获取权值
    public void naGetWeight(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "";
        String str_right = "";
        int weight_left = 0,weight_right = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左上
                    for(int x = i - 1,k = j - 1;x >= 0 && x >= i - 4 && k >= 0 && k >= j - 4;x--,k--){
                        str_left += arrays[x][k];
                    }
                    weight_left = weightStore.getWeight(str_left);
                    str_left = "";
                    //右下
                    for(int x = i + 1,k = j + 1;x <= 14 && x <= i + 4 && k <= 14 && k <= j + 4;x++,k++){
                        str_right += arrays[x][k];
                    }
                    weight_right = weightStore.getWeight(str_right);
                    str_right = "";
                    //右下左上权值取大者
                    if(weight_left > weight_right){
                        weightArrays[i][j] += weight_left;
                    }
                    else{
                        weightArrays[i][j] += weight_right;
                    }
                }
            }
        }
    }

    //整体棋局获取权值
    public void getWeight(){
        initWeightArrays();//初始化权值数组
        levelGetWeight();//横向获取权值
        verticalGetWeight();//纵向获取权值
        pieGetWeight();//左下右上获取权值
        naGetWeight();//右下左上获取权值
    }

    //危险点检测
    public void dangerousCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //模拟下白棋
                    String str = i + ":" + j + ":" + 2;
                    gomokuPanel.getGomokuStrList().add(str);
                    gomokuPanel.getGomokuArray()[i][j] = 2;
                    if(ifEndGame(gomokuPanel.getGomokuArray(),i,j,2)){
                        weightArrays[i][j] += 15000;
                    }
                    gomokuPanel.getGomokuArray()[i][j] = 0;
                    gomokuPanel.getGomokuStrList().remove(gomokuPanel.getGomokuStrList().size() - 1);
                }
            }
        }
    }

    //胜利点检测
    public void victoryCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //模拟下黑棋
                    String str = i + ":" + j + ":" + 1;
                    gomokuPanel.getGomokuStrList().add(str);
                    gomokuPanel.getGomokuArray()[i][j] = 1;
                    if(ifEndGame(gomokuPanel.getGomokuArray(),i,j,1)){
                        weightArrays[i][j] += 20000;
                    }
                    gomokuPanel.getGomokuArray()[i][j] = 0;
                    gomokuPanel.getGomokuStrList().remove(gomokuPanel.getGomokuStrList().size() - 1);
                }
            }
        }
    }

    //横向活四检测
    public void levelHuosiCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "",str = "";
        char[] chars = new char[5];
        int weight;
        int number = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int k = j - 1;k >= 0 && k >= j - 5;k--){
                        str_left += arrays[i][k];
                        number++;
                    }
                    chars = str_left.toCharArray();
                    str_left = "";
                    for(int n = number - 1;n >= 0;n--){
                        str_left += chars[n];
                    }
                    str += str_left;
                    str += 0;
                    for(int k = j + 1;k <= 14 && k <= j + 5;k++){
                        str_right += arrays[i][k];
                        number++;
                    }
                    str += str_right;
                    weight = weightStore.huosiCompare(str,number);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    str_right = "";
                    str = "";
                    number = 0;
                }
            }
        }
    }

    //纵向活四检测
    public void verticalHuosiCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_up = "",str_down = "",str = "";
        char[] chars = new char[5];
        int weight;
        int number = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int x = i - 1;x > 0 && x >= i - 5;x--){
                        str_up += arrays[x][j];
                        number++;
                    }
                    chars = str_up.toCharArray();
                    str_up = "";
                    for(int n = number - 1;n >= 0;n--){
                        str_up += chars[n];
                    }
                    str += str_up;
                    str += 0;
                    for(int x = i + 1;x <= 14 && x <= i + 5;x++){
                        str_down += arrays[x][j];
                        number++;
                    }
                    str += str_down;
                    weight = weightStore.huosiCompare(str,number);
                    weightArrays[i][j] += weight;
                    str_up = "";
                    str_down = "";
                    str = "";
                    number = 0;
                }
            }
        }
    }

    //自左下至右上活四检测
    public void pieHuosiCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "",str = "";
        char[] chars = new char[5];
        int weight;
        int number = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左下
                    for(int x = i + 1,k = j - 1;x <= 14 && x <= i + 5 && k >= 0 && k >= j - 5;x++,k--){
                        str_left += arrays[x][k];
                        number++;
                    }
                    chars = str_left.toCharArray();
                    str_left = "";
                    for(int n = number - 1;n >= 0;n--){
                        str_left += chars[n];
                    }
                    str += str_left;
                    str += 0;
                    //右上
                    for(int x = i - 1,k = j + 1;x >= 0 && x >= i - 5 && k <= 14 && k <= j + 5;x--,k++){
                        str_right += arrays[x][k];
                        number++;
                    }
                    str += str_right;
                    weight = weightStore.huosiCompare(str,number);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    str_right = "";
                    str = "";
                    number = 0;
                }
            }
        }
    }

    //自右下至左上活四检测
    public void naHuosiCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "",str = "";
        char[] chars = new char[5];
        int weight;
        int number = 0;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左上
                    for(int x = i - 1,k = j - 1;x >= 0 && x >= i - 5 && k >= 0 && k >= j - 5;x--,k--){
                        str_left += arrays[x][k];
                        number++;
                    }
                    chars = str_left.toCharArray();
                    str_left = "";
                    for(int n = number - 1;n >= 0;n--){
                        str_left += chars[n];
                    }
                    str += str_left;
                    str += 0;
                    //右下
                    for(int x = i + 1,k = j + 1;x <= 14 && x <= i + 5 && k <= 14 && k <= j + 5;x++,k++){
                        str_right += arrays[x][k];
                        number++;
                    }
                    str += str_right;
                    weight = weightStore.huosiCompare(str,number);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    str_right = "";
                    str = "";
                    number = 0;
                }
            }
        }
    }

    //活四检测
    public void huosiCheck(){
        levelHuosiCheck();//横向活四检测
        verticalHuosiCheck();//纵向活四检测
        pieHuosiCheck();//自左下至右上活四检测
        naHuosiCheck();//自右下至左上活四检测
    }

    //横向活三检测
    public void levelHuosanCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "";
        int weight;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int k = j - 1;k >= 0 && k >= j - 4;k--){
                        str_left += arrays[i][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_left);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    for(int k = j + 1;k <= 14 && k <= j + 4;k++){
                        str_right += arrays[i][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_right);
                    weightArrays[i][j] += weight;
                    str_right = "";
                }
            }
        }
    }

    //纵向活三检测
    public void verticalHuosanCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_up = "",str_down = "";
        int weight;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    for(int x = i - 1;x > 0 && x >= i - 4;x--){
                        str_up += arrays[x][j];
                    }
                    weight = weightStore.huosanCompare("0" + str_up);
                    weightArrays[i][j] += weight;
                    str_up = "";
                    for(int x = i + 1;x <= 14 && x <= i + 4;x++){
                        str_down += arrays[x][j];
                    }
                    weight = weightStore.huosanCompare("0" + str_down);
                    weightArrays[i][j] += weight;
                    str_down = "";
                }
            }
        }
    }

    //自左下至右上活三检测
    public void pieHuosanCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "";
        int weight;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左下
                    for(int x = i + 1,k = j - 1;x <= 14 && x <= i + 4 && k >= 0 && k >= j - 4;x++,k--){
                        str_left += arrays[x][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_left);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    //右上
                    for(int x = i - 1,k = j + 1;x >= 0 && x >= i - 4 && k <= 14 && k <= j + 4;x--,k++){
                        str_right += arrays[x][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_right);
                    weightArrays[i][j] += weight;
                    str_right = "";
                }
            }
        }
    }

    //自右下至左上活三检测
    public void naHuosanCheck(){
        int[][] arrays = gomokuPanel.getGomokuArray();
        String str_left = "",str_right = "";
        int weight;
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(arrays[i][j] == 0){
                    //左上
                    for(int x = i - 1,k = j - 1;x >= 0 && x >= i - 4 && k >= 0 && k >= j - 4;x--,k--){
                        str_left += arrays[x][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_left);
                    weightArrays[i][j] += weight;
                    str_left = "";
                    //右下
                    for(int x = i + 1,k = j + 1;x <= 14 && x <= i + 4 && k <= 14 && k <= j + 4;x++,k++){
                        str_right += arrays[x][k];
                    }
                    weight = weightStore.huosanCompare("0" + str_right);
                    weightArrays[i][j] += weight;
                    str_right = "";
                }
            }
        }
    }

    //活三检测
    public void huosanCheck(){
        levelHuosanCheck();//横向活三检测
        verticalHuosanCheck();//纵向活三检测
        pieHuosanCheck();//自左下至右上活三检测
        naHuosanCheck();//自右下至左上活三检测
    }

    //AI棋手，初出茅庐
    public void AIplayer(){
        int maxWeight = weightArrays[0][0];
        getWeight();//整体棋局获取权值
        huosiCheck();//活四检测
        huosanCheck();//活三检测
        dangerousCheck();//危险点检测
        victoryCheck();//胜利点检测
        printWeithtArrays();//打印权值数组
        String local_str;
        //最大权值可能有多个，所以用List来存
        ArrayList<String> arrayList_MaxWeight = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(weightArrays[i][j] >= maxWeight){
                    maxWeight = weightArrays[i][j];
                }
            }
        }
        for(int i = 0;i < 15;i++){
            for(int j = 0;j < 15;j++){
                if(weightArrays[i][j] == maxWeight){
                    local_str = i + "," + j;
                    arrayList_MaxWeight.add(local_str);
                }
            }
        }
        String[] local_strTemp = new String[2];
        //在所有最大权值的点里面随机选一个
        local_strTemp = arrayList_MaxWeight.get((int)(Math.random() * (arrayList_MaxWeight.size()))).split(",");
        int i = Integer.parseInt(local_strTemp[0]);
        int j = Integer.parseInt(local_strTemp[1]);
        System.out.println(i + ":" + j);
        gomokuPanel.setBoardX(i);
        gomokuPanel.setBoardY(j);
        //规定电脑为黑棋，用1表示
        String str = i + ":" + j + ":" + (isBlack ? 1:2);
        gomokuPanel.getGomokuStrList().add(str);
        gomokuPanel.getGomokuArray()[i][j] = (isBlack ? 1:2);
        //重画棋盘
        gomokuPanel.repaint();
        if(ifEndGame(gomokuPanel.getGomokuArray(),i,j,isBlack ? 1:2)){
            isEnd = true;
            isBlack = !isBlack;//选手换边
            if(turnON_Music){
                music.setName("music/game_win.wav");
                music.playMusic();
            }
            if(!isBlack){
                //弹出提示窗口
                JOptionPane.showMessageDialog(GomokuFrame.this,"黑方棋手获胜，本局结束！");
                return;
            }
            else{
                //弹出提示窗口
                JOptionPane.showMessageDialog(GomokuFrame.this,"白方棋手获胜，本局结束！");
                return;
            }
        }
        else{
            isBlack = !isBlack;//选手换边
            //如果音乐开关状态为开，那么落子就有声音
            if(turnON_Music){
                music.playMusic();
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------

    //所有的赢法统计
    public void allWinsAlgorithm() {
        //将所有的赢法保存在三维数组里面
        //横向赢的算法
        for(int i = 0;i<15;i++) {
            for(int j = 0;j<11;j++) {
                for(int k = 0;k<5;k++) {
                    wins[i][j + k][count] = true;
                    //例如这表示第一种赢法
                    //win[0][0][0];
                    //win[0][1][0];
                    //win[0][2][0];
                    //win[0][3][0];
                    //win[0][4][0];

                }//end for(K)
                count++;
            }//end for(j)
        }//end for(i)

        //竖向赢的算法
        for(int i = 0;i<11;i++) {
            for(int j = 0;j<15;j++) {
                for(int k = 0;k<5;k++) {
                    wins[i + k][j][count] = true;
                }//end for(K)
                count++;
            }//end for(j)
        }//end for(i)

        //斜向赢的算法
        for(int i = 0;i<11;i++) {
            for(int j = 0;j<11;j++) {
                for(int k = 0;k<5;k++) {
                    wins[i+k][j+k][count] = true;
                }//end for(K)
                count++;
            }//end for(j)
        }//end for(i)

        //反斜向赢的算法
        for(int i = 0;i<11;i++) {
            for(int j = 14;j>3;j--) {
                for(int k = 0;k<5;k++) {
                    wins[i+k][j-k][count] = true;
                }//end for(K)
                count++;
            }//end for(j)
        }//end for(i)
        //System.out.println(count);
    }

    //AI棋手，出神入化
    public void computerAI() {
        //当我方下完一颗棋子时，对其盘上的所有点的得分和最高得分进行初始化
        //重新找出最高得分的点
        for(int i = 0;i<15;i++) {
            for(int j = 0;j<15;j++) {
                myScore[i][j] = 0;
                computerScore[i][j] = 0;
            }
        }
        max = 0;
        //遍历整个棋盘，计算出每一个没有落下棋子的点的得分
        for(int i = 0;i<15;i++) {
            for(int j = 0;j<15;j++) {
                //如果该点没有落下棋子，则进行下面得分计算
                if( gomokuPanel.getGomokuArray()[i][j] == 0) {

                    for(int k = 0;k<count;k++) {
                        //如果第k种赢法在该点可以实现，则进行该种赢法上落子数的多少进行分数统计
                        if(wins[i][j][k]) {
                            //在第k种赢法上我方已经落下1颗棋子
                            if(myWin[k]==1) {
                                myScore[i][j] += 200;
                            }
                            //在第k种赢法上我方已经落下2颗棋子
                            else if(myWin[k]==2) {
                                myScore[i][j] += 550;
                            }
                            //在第k种赢法上我方已经落下3颗棋子
                            else if(myWin[k]==3) {
                                myScore[i][j] += 3000;
                            }
                            //在第k种赢法上我方已经落下4颗棋子
                            else if(myWin[k]==4) {
                                myScore[i][j] += 16000;
                            }
                            //在第k种赢法上人机落下了1颗棋子
                            if(computerWin[k]==1) {
                                computerScore[i][j] += 240;
                            }
                            //在第k种赢法上人机落下了2颗棋子
                            else if(computerWin[k]==2) {
                                computerScore[i][j] += 600;
                            }
                            //在第k种赢法上人机落下3颗棋子
                            else if(computerWin[k]==3) {
                                computerScore[i][j] += 3700;
                            }
                            //在第k种赢法上我方落下了4颗棋子
                            else if(computerWin[k]==4) {
                                computerScore[i][j] += 20000;
                            }

                        }//end wins[i][j][k]

                    }

                    if(myScore[i][j]>max) {
                        max = myScore[i][j];
                        u = i;
                        v = j;

                    }
                    /*if(myScore[i][j]==max) {
                        if(computerScore[i][j] > computerScore[u][v]) {
                            u = i;
                            v = j;

                        }
                    }*/

                    if(computerScore[i][j]>max) {
                        max = computerScore[i][j];
                        u = i;
                        v = j;

                    }
                    /*if(computerScore[i][j]==max) {
                        if(myScore[i][j]>myScore[u][v]) {
                            u = i;
                            v = j;
                        }
                    }*/
                }
            }
        }//end if(i)
        System.out.println(u + ":" + v);
        gomokuPanel.setBoardX(u);
        gomokuPanel.setBoardY(v);
        //规定电脑为黑棋，用1表示
        String str = u + ":" + v + ":" + (isBlack ? 1:2);
        gomokuPanel.getGomokuStrList().add(str);
        //人机在u，v点上落子
        gomokuPanel.getGomokuArray()[u][v] = (isBlack ? 1:2);
        for(int k = 0;k<count;k++) {
            if(wins[u][v][k]) {
                computerWin[k]++;
                tempMywin[k] = myWin[k];
                myWin[k] = 7;
            }
        }
        //重画棋盘
        gomokuPanel.repaint();

        if(ifEndGame(gomokuPanel.getGomokuArray(),u,v,isBlack ? 1:2)){
            isEnd = true;
            isBlack = !isBlack;//选手换边
            if(turnON_Music){
                music.setName("music/game_win.wav");
                music.playMusic();
            }
            if(!isBlack){
                //弹出提示窗口
                JOptionPane.showMessageDialog(GomokuFrame.this,"黑方棋手获胜，本局结束！");
                return;
            }
            else{
                //弹出提示窗口
                JOptionPane.showMessageDialog(GomokuFrame.this,"白方棋手获胜，本局结束！");
                return;
            }
        }
        else{
            isBlack = !isBlack;//选手换边
            //如果音乐开关状态为开，那么落子就有声音
            if(turnON_Music){
                music.playMusic();
            }
        }
    }//end compterAI

    public static void main(String[] args){
        GomokuFrame gomokuFrame = new GomokuFrame();
    }
}
