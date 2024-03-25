import java.util.Random;
/*
 * 构分数类，处理分数的相关内容
 */
public class Fraction {
    private int denominator;// 分母
    private int numerator;// 分子

    public int getDenominator() {//获取分母
        return denominator;
    }
    public int getNumerator() {//获取分子
        return numerator;
    }


    // 构建一个分数
    public Fraction( int numerator,int denominator) {
        super();
        this.denominator = denominator;
        this.numerator = numerator;
    }
    // 构建一个分母为1的分数
    public Fraction(int numerator) {
        this.denominator = 1;
        this.numerator = numerator;
    }
    //一般构造方法
    public Fraction() {
        super();
    }

    // 加法运算
    public Fraction add(Fraction r) {
        int a = r.getNumerator();// 获得分子
        int b = r.getDenominator();// 获得分母
        int newNumerator = numerator * b + denominator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator,newDenominator);
    }

    // 减法运算
    public Fraction sub(Fraction r) {
        int a = r.getNumerator();// 获得分子
        int b = r.getDenominator();// 获得分母
        int newNumerator = numerator * b - denominator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator,newDenominator);
    }

    // 分数的乘法运算
    public Fraction muti(Fraction r) { // 乘法运算
        int a = r.getNumerator();// 获得分子
        int b = r.getDenominator();// 获得分母
        int newNumerator = numerator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator,newDenominator);
    }

    // 分数除法运算
    public Fraction div(Fraction r) {
        int a = r.getNumerator();// 获得分子
        int b = r.getDenominator();// 获得分母
        int newNumerator = numerator * b;
        int newDenominator = denominator * a;
        return new Fraction(newNumerator,newDenominator);
    }

    // 关键：用辗转相除法求最大公约数
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    // 对分数进行约分
    public void Appointment() {
        if (numerator == 0 || denominator == 1)
            return;
        // 如果分子是0或分母是1就不用约分了
        long gcd = gcd(numerator, denominator);
        this.numerator /= gcd;
        this.denominator /= gcd;
    }
    //对分数进行化简，使之符合项目所规定的计算式的要求
    public  String transferFraction(Fraction fraction){
        int old_numerator   = fraction.numerator;
        int old_denominator = fraction.denominator;
        int new_numerator   = old_numerator/gcd(old_numerator,old_denominator);
        int new_denominator = old_denominator/gcd(old_numerator,old_denominator);
        int new_integer     = new_numerator/new_denominator;
        int nnew_numerator  = new_numerator%new_denominator;
        String str = "";
        if(nnew_numerator==0){         //整数
            str += new_integer;
        }else if(new_integer!=0){      //带分数
            str = new_integer+"'"+nnew_numerator+"/"+new_denominator;
        }else{                         //真分数
            str +=nnew_numerator+"/"+new_denominator ;
        }
        return str;
    }
}
