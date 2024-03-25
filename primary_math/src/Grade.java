import org.omg.CORBA.ARG_OUT;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Grade {
    public void grade() throws IOException {
        /*
         * 从命令行接受参数，题目的数量，和运算数的范围
         */
        int j = 1;
        String str1 = "";//需计算的表达式
        String str2 = "";//计算的答案
        String str3 = "";//大体情况，写入Grade.txt
        String str4 = "";//获得控制台输入的答案
        Scanner sc = new Scanner(System.in);

        Queue<Integer> correct = new LinkedList<Integer>();//正确题目的队列
        Queue<Integer> wrong = new LinkedList<Integer>();//错误题目的队列

        BufferedReader file = null;
        FileIO writer = new FileIO();//输入流对象

        System.out.println("开始答题");

        try {
            file = new BufferedReader(new InputStreamReader(new FileInputStream("textfile/Exercises.txt")));
            //会把第 n 题输入，可能会把第 n 题当作操作数,所以写入的expression不含有这些
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while ((str1 = file.readLine()) != null) {
            System.out.println(str1);//按照题目的格式输入时，注意题号是否会和运算数一起计算
            System.out.println("请输入你的答案(输入'quit'结束答题)：");
            str4 = sc.next();
            Calculate cal = new Calculate();
            Fraction f = cal.outcome(str1);
            str2 = f.transferFraction(f);//最终结果

            if (str4.equals("quit")) {
                System.out.println("答题中止！");
                wrong.add(j);
                while ((str1 = file.readLine()) != null) {
                    ++j;
                    wrong.add(j);
                }
                break;
            } else if (str4.equals(str2)) {
                System.out.println("你真棒，答对了\n");
                correct.add(j);
            } else {
                System.out.println("真遗憾，答错了，正确答案为" + str2 + "\n");
                wrong.add(j);
            }

            j++;
        }

        /*
        输出结果，存入文档
        */
        System.out.println("答题结束");
        str3 += "Correct:" + correct.size() + correct + "\n" +
                "Wrong:" + wrong.size() + wrong + "\n";
        writer.fileWrite(str3, Paths.get("textfile/Grade.txt"));
        System.out.println("你共答对了" + correct.size() + "道题");
        System.out.println("你共错对了" + wrong.size() + "道题");
        System.out.println("正确率为" + correct.size() / (j-1.0) * 100 + "%");//queue.size方法可以获得队列元素的个数*/
    }

    public static void main(String[] args) throws IOException {
        Grade gra = new Grade();
        gra.grade();

    }
}
