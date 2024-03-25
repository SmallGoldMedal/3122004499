import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class Expression {

    //生成一组原始的计算式
    public static String generateExp(Integer limit) throws StringIndexOutOfBoundsException{
        //生成一组要计算的随机数
        Random rd = new Random();
        rd.nextInt(limit);
        int e1=  rd.nextInt(limit);
        int e2=  rd.nextInt(limit);
        int e3=  rd.nextInt(limit);
        int e4=  rd.nextInt(limit);
        int []num = {e1,e2,e3,e4};

        char []opt ={'+','-','*','/'};//注意：没有除号

        int []no = {1,2,3};//关键理解：控制选择的运算符(0-3)

        /*
         * 生成不带括号的算术式
         * 每一次循环生成一个运算数和一个运算符，最后再加上一个数字
         */
        StringBuilder str = new StringBuilder();//str为算术式
        for(int j = 0; j <  no[rd.nextInt(3)]; j++){//调整j的范围可以控制运算符的数目
            str.append(num[rd.nextInt(4)]).append(" ");
            str.append(opt[rd.nextInt(4)]).append(" ");
            //关键：操作数与运算符需用空格隔开，以便后续区分÷和/
        }
        str.append(num[rd.nextInt(4)]).append(" ");

        /*
         * 进行加入一个括号的操作,暂未实现加入多个括号
         */
        int index;//记录数组的下标
        int len;
        String str1 = "";
        String str2 = "";
        String str3 = "";

        int []indexs = {0,4,8};//从indexs开始加左括号
        int []lens ={5,9,13};//括号的宽度，即从从indexs+len开始加右括号
        index = indexs[rd.nextInt(3)];
        len = lens[rd.nextInt(3)];
        if( index+len< str.length()-1 ||(index+len==str.length()+1 && index!= 0) ){
            //去除括号括号“太大”以及首尾同时加括号的情况
            //同时，这样也实现“不一定加括号”的小要求
            str1 = str.substring(0,index)+"( ";  //左括号以及左括号以左的部分
            str2 = str.substring(index,index+len)+" )";//右括号以及左右括号间的部分
            str3 = str.substring(index+len);//右括号以右的部分
        }

        return str1+str2+str3;
    }

    //再次生成一组原始的计算式，并完成：合法化、查重、生成答案
    public void legalExp (Integer number,Integer limit) throws IOException {
        //number表示题目数量,limit表示运算数范围
        int j = 1;//控制题目生成的数量,从1开始

        String str1 = "";
        String str2 = "";//存放中间结果
        StringBuilder str3 = new StringBuilder();//算术表达式，写入Expression.txt
        StringBuilder str4 = new StringBuilder();//答案，写入Answer.txt

        HashMap<String, Integer> answers = new HashMap<String, Integer>();

        FileIO writer = new FileIO();//输入（输出）流对象
        Expression exp = new Expression();//表达式对象，只用于获取未合法化的表达式

        do {
            str1 = exp.generateExp(limit) + "= ";//获得未经合法化的表达式
            Calculate cal = new Calculate();
            Fraction f = cal.outcome(str1);//计算结果，未化简

            if (f.getNumerator() == 100000) {
                 //剔除不合法的算术表达式：分母为零或出现负数
                // “100000”有关的具体算法存于Calculate中
                continue;
            }

            str2 = f.transferFraction(f);//最终结果，已经化简(符合项目要求的答案要求）

            if(answers.containsKey(Save.string)){
                //如果重复，跳过
                //Sava的具体算法存于Calculate中
                continue;             //-->S:这里可以直接省略
            }else{
                //如果没有重复，存入
                answers.put(Save.string, null);
                System.out.printf("%4d.      %s%n",j,str1);//格式化输出

                str3.append(j).append(".").append("    ").append(str1).append("\n");
                str4.append(j).append(".").append("    ").append(str1).append(str2).append("\n");
                j++;
            }

        } while (j <= number);
        System.out.println("表达式生成完毕");

        writer.fileWrite(str3.toString(), Paths.get("textfile/Exercises.txt"));//所有的 不含答案的计算式
        writer.fileWrite(str4.toString(), Paths.get("textfile/Answers.txt"));//所有的 完整计算式
    }

    public static void main(String[] args) throws IOException {
        Expression exp = new Expression();
        exp.legalExp(10000,10);
    }

}
