import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/*
Examination类并不参与程序步骤，而是我们对于查重算法的一种替换：
原本的查重算法；
   直接以计算式答案为标准进行筛除，属于“暴力筛除”，会筛除一些原本符合规则的计算式；
同时，这样的算法也会不允许出现答案一致的计算式，因此，当计算式的规模过大时(>=1000)，
生成计算式的速度会逐渐下降，以及计算式答案会逐渐趋近于零。

新的查重算法：
    计以算式的后缀表达式为标准进行筛除，理论上讲不会出现“不必要筛除”的情况，
而且，也不会对答案重复造成多余的限制。但是，由于时间问题和个人能力，这个算法仍然存在许多问题：
1.会对“/”的情况造成误判：
 如：3 / ( 3 + 4 ) + 3 = 与 3 + ( 4 + 3 ) / 3 =
    ( 8 * 7 ) / 8 * 8 = 与 8 / ( 7 * 8 ) * 8 =
2.时间复杂度和空间复杂度很低：
  1）采取的循环遍历、一一比对的方式；
  2）每次调用都会创建一个大数组；
3.与原程序的结合还没有开始

因此，只在本类中生成未经过“答案查重”的计算式，在以“后缀表达式”查重计算式，
并输出显示不合规的计算式的后缀表达式及其编号
 */
public class Examination {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要生成的题目个数：");
        int number = sc.nextInt();
        System.out.println("请输入运算数的范围：");
        int limit = sc.nextInt();
        int j = 1;

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

                //如果没有重复，存入
                answers.put(Save.string, null);
                System.out.printf("%4d.      %s%n",j,str1);//格式化输出

                str3.append(j).append(".").append("    ").append(str1).append("\n");
                str4.append(j).append(".").append("    ").append(str1).append(str2).append("\n");
                j++;


        } while (j <= number);
        System.out.println("表达式生成完毕");

        writer.fileWrite(str3.toString(), Paths.get("textfile/Exercises.txt"));//所有的 不含答案的计算式
        writer.fileWrite(str4.toString(), Paths.get("textfile/Answers.txt"));//所有的 完整计算式

        Examination examination=new Examination();
        examination.Examination1();

    }

    public void Examination1() {
        char[][] anotherChar = new char[10000][7];      //用于记录所有后缀式表达
        int temp = 0;       //相当于limit

        try (BufferedReader br = new BufferedReader(new FileReader("textfile/Exercises.txt"))) {
            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {
                char[] charArray = line.toCharArray(); //将当前行的字符转换为字符数组
                for(int i = 0 ;  ; i++) {
                    if(charArray[i] == '=')
                        break;
                    if(i <= 5 && ((charArray[i] >= '0' && charArray[i] <= '9') || charArray[i] == '.'))
                        charArray[i] = ' ';
                }

                //去除干扰字符
                for(int i = 0 ; ; i++) {
                    if(charArray[i] == '=')
                        break;
                    if(charArray[i] == ' ') {
                        for(int j = i ; ; j++) {
                            if(charArray[j] != ' ') {
                                charArray[i] = charArray[j];
                                charArray[j] = ' ';
                                i--;
                                break;
                            }
                        }
                    }
                }

                //将字符变成后缀表达式放入anotherChar中
                boolean[] forBoolean = new boolean[10];     //用于判断某位置字符是否已经输入到anotherChar中
                for(int i = 0 ; i < 10 ; i++)
                    forBoolean[i] = false;
                int t = 0;      //用于转换时anotherChar的位置标记

                for(int i = 0 ; ; i++) {    //先检测括号的存在
                    if(charArray[i] == '=')
                        break;
                    if(charArray[i] == '(') {
                        forBoolean[i] = true;
                        for(int j = i + 2 ; ; j++) {        //先输入乘除
                            if(charArray[j] == ')')
                                break;
                            if(charArray[j] == '*' || charArray[j] == '/') {
                                if(!forBoolean[j - 1]) {
                                    anotherChar[temp][t] = charArray[j - 1];
                                    forBoolean[j - 1] = true;
                                    t++;
                                }
                                if(!forBoolean[j + 1]) {
                                    anotherChar[temp][t] = charArray[j + 1];
                                    forBoolean[j + 1] = true;
                                    t++;
                                }
                                anotherChar[temp][t] = charArray[j];
                                forBoolean[j] = true;
                                t++;
                            }
                        }
                        for(int j = i + 2 ; ;j++) {     //后输入加减
                            if(charArray[j] == ')') {
                                forBoolean[j] = true;
                                break;
                            }
                            if(charArray[j] == '+' || charArray[j] == '-') {
                                if(!forBoolean[j - 1]) {
                                    anotherChar[temp][t] = charArray[j - 1];
                                    forBoolean[j - 1] = true;
                                    t++;
                                }
                                if(!forBoolean[j + 1]) {
                                    anotherChar[temp][t] = charArray[j + 1];
                                    forBoolean[j + 1] = true;
                                    t++;
                                }
                                anotherChar[temp][t] = charArray[j];
                                forBoolean[j] = true;
                                t++;
                            }
                        }
                        break;
                    }
                }

                for(int i = 0 ; ; i++) {        //再检测括号之外,先检测乘除
                    if(charArray[i] == '=')
                        break;
                    if(!forBoolean[i]) {
                        if(charArray[i] == '*' || charArray[i] == '/') {
                            if(!forBoolean[i - 1]) {
                                anotherChar[temp][t] = charArray[i - 1];
                                forBoolean[i - 1] = true;
                                t++;
                            }
                            if(!forBoolean[i + 1]) {
                                anotherChar[temp][t] = charArray[i + 1];
                                forBoolean[i + 1] = true;
                                t++;
                            }
                            anotherChar[temp][t] = charArray[i];
                            forBoolean[i] = true;
                            t++;
                        }
                    }
                }

                for(int i = 0 ; ; i++) {        //后检测乘除
                    if(charArray[i] == '=')
                        break;
                    if(!forBoolean[i]) {
                        if(charArray[i] == '+' || charArray[i] == '-') {
                            if(!forBoolean[i - 1]) {
                                anotherChar[temp][t] = charArray[i - 1];
                                forBoolean[i - 1] = true;
                                t++;
                            }
                            if(!forBoolean[i + 1]) {
                                anotherChar[temp][t] = charArray[i + 1];
                                forBoolean[i + 1] = true;
                                t++;
                            }
                            anotherChar[temp][t] = charArray[i];
                            forBoolean[i] = true;
                            t++;
                        }
                    }
                }

                index++;
                temp ++;

                if (index >= 10000) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //开始检测逻辑相同表达式
        double[] s = new double[temp];      //用于记录计算结果，没意义
        double t = 0;       //用于计算中间结果，没意义
        for(int i = 0 ; i < temp ; i ++) {
            if(anotherChar[i][2] == '+')
                t = anotherChar[i][0] + anotherChar[i][1] - 96;
            if(anotherChar[i][2] == '-')
                t = anotherChar[i][0] - anotherChar[i][1];
            if(anotherChar[i][2] == '*')
                t = (anotherChar[i][0] - 48) * (anotherChar[i][1] - 48);
            if(anotherChar[i][2] == '/')
                t = (anotherChar[i][0] - 48) / (anotherChar[i][1] - 48);

            if(anotherChar[i][4] == '+')
                t += (anotherChar[i][3] - 48);
            if(anotherChar[i][4] == '-')
                t -= (anotherChar[i][3] - 48);
            if(anotherChar[i][4] == '*')
                t *= (anotherChar[i][3] - 48);
            if(anotherChar[i][4] == '/')
                t /= (anotherChar[i][3] - 48);

            if(anotherChar[i][6] == '+')
                t += (anotherChar[i][5] - 48);
            if(anotherChar[i][6] == '-')
                t -= (anotherChar[i][5] - 48);
            if(anotherChar[i][6] == '*')
                t *= (anotherChar[i][5] - 48);
            if(anotherChar[i][6] == '/')
                t /= (anotherChar[i][5] - 48);

            s[i] = t;
        }

        int[][] Temp = new int[temp][2];    //Temp记录互相重复表达式的编号
        for(int i = 0 ; i < temp ; i++) {
            Temp[i][0] = 0;
            Temp[i][1] = 0;
        }
        int w = 0;      //w用于记录编号位置，最终为重复组个数

        for(int i = 0 ; i < temp ; i++)
            for (int j = i + 1; j < temp; j++)
                if(s[i] == s[j])
                    if(anotherChar[i][2] == anotherChar[j][2] && (anotherChar[i][2] == '+' || anotherChar[i][2] == '*'))
                        if( ( (anotherChar[i][0] == anotherChar[j][0]) && (anotherChar[i][1] == anotherChar[j][1]) ) || ( (anotherChar[i][0] == anotherChar[j][1]) && (anotherChar[i][1] == anotherChar[j][0]) ) )
                            if((anotherChar[i][4] == anotherChar[j][4]) && (anotherChar[i][6] == anotherChar[j][6]) && (anotherChar[i][6] == '+' || anotherChar[i][6] == '*'))
                                if(anotherChar[i][3] == anotherChar[j][3] && anotherChar[i][5] == anotherChar[j][5])
                                    if(w < temp) {
                                        Temp[w][0] = i;
                                        Temp[w][1] = j;
                                        w++;
                                    }

        /*测试用，随时可删*/
        for(int i = 0 ;i < w ; i++) {
            System.out.print(Temp[i][0] + 1 + ".");
            for(int j = 0 ; j < 7 ; j++) {
                System.out.print(anotherChar[Temp[i][0]][j]);
            }
            System.out.println(" ");
            System.out.print(Temp[i][1] + 1 + ".");
            for(int j = 0 ; j < 7 ; j++) {
                System.out.print(anotherChar[Temp[i][1]][j]);
            }
            System.out.println(" ");System.out.println(" ");
        }
    }
}