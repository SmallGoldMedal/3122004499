import java.io.IOException;
import java.util.Scanner;

public class Main {//后期可以考虑拆分合法性检验和正确率判断
        public static void main(String[] args) throws IOException {
            //从命令行接受参数，题目的数量，和运算数的范围。
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入要生成的题目个数：");
            int n = sc.nextInt();
            System.out.println("请输入运算数的范围：");
            int r = sc.nextInt();

            //生成表达式
            Expression exp = new Expression();
            exp.legalExp(n , r);


            //答题
            int choice;
            while(true) {
                System.out.println("是否进行答题(输入'1'表示答题，'0'表示不答)");
                choice = sc.nextInt();
                if (choice == 1) {
                    new Grade().grade();
                    break;
                } else if (choice == 0) {
                    System.out.println("成功结束答题，好好学习！");
                    System.exit(1);
                } else {
                    System.out.println("输入错误，请重新输入");
                }
            }
    }
}
