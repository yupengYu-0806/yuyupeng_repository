package gomoku;

//权值库
public class WeightStore {
    private int weight;

    //获取整体棋局权值
    public int getWeight(String string){
        switch(string){
            case "1111":weight = 15000;break;
            case "1110":weight = 1500;break;
            case "1100":weight = 150;break;
            case "1000":weight = 15;break;
            case "2222":weight = 10000;break;
            case "2220":weight = 1000;break;
            case "2200":weight = 100;break;
            case "2000":weight = 10;break;
            default:weight = 0;break;
        }
        return weight;
    }

    //活四序列对比函数
    public int huosiCompare(String string,int length){
        char[] chars = string.toCharArray();
        String str_temp = "";
        for(int i = 0;i < length - 5;i++){
            for(int j = i;j < i + 6;j++){
                str_temp += chars[j];
            }
            switch (str_temp)
            {
                case "011100":weight = 15000;return weight;//为1的话表示是我(电脑)方可以成活四，优先级更高
                case "001110":weight = 15000;return weight;
                case "010110":weight = 15000;return weight;
                case "011010":weight = 15000;return weight;
                case "022200":weight = 10000;return weight;
                case "002220":weight = 10000;return weight;
                case "020220":weight = 10000;return weight;
                case "022020":weight = 10000;return weight;
                default:weight = 0;break;
            }
            str_temp = "";
        }
        return weight;
    }

    //活三序列对比函数
    public int huosanCompare(String string){
        switch (string)
        {
            case "01100":weight = 1500;return weight;
            case "01010":weight = 1500;return weight;
            case "02200":weight = 1000;return weight;
            case "02020":weight = 1000;return weight;
            default:weight = 0;break;
        }
        return weight;
    }
}
