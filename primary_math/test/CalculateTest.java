import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculateTest {

	private static Calculate calculate =new Calculate();

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("启动");
	}

	@Test
	void testToSuffixExpression() throws IOException {
        String sMid ="2 * ( 7 / 3 - 2 ) =";

        System.out.println("中缀表达式："+sMid);
    	Queue<String> queueToSuffixResult=calculate.toSuffixExpression(sMid);
        System.out.println("后缀表达式："+queueToSuffixResult);
        String sOut="";
        while(!queueToSuffixResult.isEmpty()) {
        	sOut+=queueToSuffixResult.remove();
        }
    	assertEquals("273/2-*", sOut);

	}

	@Test
	void testCalculate() throws IOException {

		//分数形式对比失败，转用string形式对比
        String strINPUT ="2 * ( 7 / 3 - 2 ) =";//测试用例
        System.out.println("计算表达式："+strINPUT);

        String Excepted="2/3";//预计结果，string形式

        Calculate expression = new Calculate();
        Queue<String> queue = expression.toSuffixExpression(strINPUT);
        Fraction fractionCalculated = expression.calculate(queue);//未化简的分数形式结果
        System.out.println("答案"+fractionCalculated.getNumerator()+"/"+fractionCalculated.getDenominator());

        String Calculated=fractionCalculated.getNumerator()+"/"+fractionCalculated.getDenominator(); 
        assertEquals(Excepted, Calculated);
	}

}
