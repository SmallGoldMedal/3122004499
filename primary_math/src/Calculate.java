import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TransferQueue;

public class Calculate {
    /*
     * 计算表达式的值，由计算式得出结果
     * 是整个Calculate的“主”方法
     */
   public Fraction outcome(String s) {
        Queue<String> q = toSuffixExpression(s);
        return calculate(q);
    }

    // 将中缀表达式转化成后缀表达式
    // 采取的是用栈的非递归算法
    public Queue<String> toSuffixExpression(String s) {//生成后缀表达式
        Stack<Character> stack = new Stack<Character>();
        Queue<String> queue = new LinkedList<String>();

        int index = 0;//字符串标签
        while (index < s.length()) {//str.length()表示元素的个数

            char c = s.charAt(index);
            // 如果是数字，就入队列
            if (isDigital(c)) {
                // 入队的时候要判断后面是否还有剩余的数字，要把整个数字入队列，而不是一个数字字符
                // 在多位数的时候有用
                int p = index;
                while (p < s.length() && isDigital(s.charAt(p))) {
                    p++;
                }
                queue.add(s.substring(index, p));//截取字符串，从指定位置（start）开始截到指定的位置（end）
                index = p;
                continue;
                // 如果是左括号，就入栈
            } else if (c == '(') {
                stack.push(c);
                // 如果是右括号，就弹出栈中的元素，直到遇到左括号为止。左右括号均不入队列
            } else if (c == ')') {
                while ('(' != stack.peek()) {
                    queue.add(stack.pop() + "");
                }
                // 弹出左括号
                stack.pop();
                // 如果是运算符，分下面的情况讨论
            } else if (isOperator(c)) {
                // 如果符号栈为空，就直接压入栈
                if (stack.isEmpty()) {
                    stack.push(c);
                    // 如果符号栈的栈顶是左括号，则压入栈中
                } else if ('(' == stack.peek()) {
                    stack.push(c);
                    // 如果当前元素的优先级比符号栈的栈顶元素优先级高，则压入栈中
                } else if (priority(c) > priority(stack.peek())) {//debug将大于出栈
                    stack.push(c);
                    // 如果此时遍历的运算符的优先级小于等于此时符号栈栈顶的运算符的优先级，
                    // 则将符号栈的栈顶元素弹出并且放到队列中，并且将正在遍历的符号压入符号栈
                } else if (priority(c) <= priority(stack.peek())) {
                    queue.add(stack.pop() + "");
                   while(  !stack.isEmpty() && priority(c) == priority(stack.peek()) ) {
                        queue.add(stack.pop() + "");
                    }
                    stack.push(c);
                }
            }
            index++;
        }

        // 遍历完后，将栈中元素全部弹出到队列中
        while (!stack.isEmpty()) {
            queue.add(stack.pop() + "");
        }

        return queue;
    }

    //后缀表达式求值
    public Fraction calculate(Queue<String> queue) {
       //考虑分数的计算，直接把数字转化为fraction对象，再压入栈fracStack
        Stack<Fraction> fracStack = new Stack<Fraction>();

        String str = "";//中间结果字符串
        Save.string = "";//每一次操作开始就把string置为空

        while (!queue.isEmpty() ) {

            // 从队列中出队
            String s = queue.remove();
            // Save.save(s);//保存结果

            // 如果是数字，就压入栈中
            if (isDigital(s.charAt(0))) {
                //parseInt(String s): 返回用十进制参数表示的整数值。
                Fraction f = new Fraction(Integer.parseInt(s));
                fracStack.push(f);

            } // 如果是运算符，就从栈中弹出两个元素
            else if (isOperator(s.charAt(0))) {
                char c = s.charAt(0);

                Fraction f = fracStack.pop();//操作数
                Fraction f1 = new Fraction(f.getNumerator(),f.getDenominator());
                f = fracStack.pop();//被操作数
                Fraction f2 = new Fraction(f.getNumerator(),f.getDenominator());


                //表达式合法性检验
                if ( c == '-' ) {
                    f = f2.sub(f1);//若结果为负，break；返回-1，用以标记表达式不合法
                    f.Appointment();//简单约分
                    if (f.getDenominator() < 0 || f.getNumerator() < 0  ) {//分母小于零出现负数
                        fracStack.push(new Fraction(100000));
                        break;
                    }
                }
                if ( c == '/') {
                    f2.Appointment();
                    if(f1.getDenominator()==0 || f1.getNumerator() ==0){//如果分数为0
                        fracStack.push(new Fraction(100000));
                        break;
                    }
                }

                //栈顶元素的四则运算
                switch (c) {
                    case '+': {
                        fracStack.push(f2.add(f1));

                        break;
                    }
                    case '-': {

                        fracStack.push(f2.sub(f1));
                        break;
                    }
                    case '*': {

                        fracStack.push(f2.muti(f1));
                        break;
                    }
                    case '/': {

                        fracStack.push(f2.div(f1));

                        break;

                    }
                }//switch case 结束的地方

                //计算中间结果，用于查重
                str += fracStack.peek().getNumerator()
                        +"/"+fracStack.peek().getDenominator()+" ";
            }

        }

        Save.save(str);//保存中间结果，用于查重

        if(fracStack.isEmpty() || fracStack.peek().getNumerator() == 100000){
            //所有的后缀表达式已经都处理，或者注意分子的值
            Fraction f = new Fraction(100000);
            return f;
        }else
            return  fracStack.pop();
    }//calculate方法结束的地方


    /*
    辅助计算后缀表达式的一些方法
     */
    //判断是符号
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    //判断是数字
    private boolean isDigital(char c) {
        return c >= '0' && c <= '9';
    }
    //运算符的优先级
    private int priority(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '(':
                return 0;//左括号的优先级最低
            default:
                throw new RuntimeException("Illegal operator:" + c);
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("测试开始");
        String str = "2+6*(5-1+6/2-1+2)+1";
        System.out.println("待测试的式子"+str);
        Calculate cal = new Calculate();
        Queue<String> queue = cal.toSuffixExpression(str);
        Fraction f = cal.outcome(str);
        f.transferFraction(f);
        System.out.println("后缀表达式为"+queue);

        System.out.println("计算结果"+f.transferFraction(f));

        System.out.println("测试结束");
    }
}
