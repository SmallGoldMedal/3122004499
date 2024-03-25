import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class FractionTest {

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("启动测试");
	}

	@Test
	void testAdd() {
		Fraction f1= new Fraction(2,5);//分数形式2/5
		Fraction f2= new Fraction(7,4);//分数形式7/4
		Fraction outf=f1.add(f2);//两者相加，分数形式

		String exceptedOut="43/20";//预计结果43/20
		String Out=outf.getNumerator()+"/"+outf.getDenominator();
		assertEquals(exceptedOut,Out);
	}

	@Test
	void testSub() {
		Fraction f1= new Fraction(11,9);//分数形式11/9
		Fraction f2= new Fraction(1,3);//分数形式1/3
		Fraction outf=f1.sub(f2);//两者相减，分数形式

		String exceptedOut="24/27";//预计结果24/27,不是8/9
		String Out=outf.getNumerator()+"/"+outf.getDenominator();
		assertEquals(exceptedOut,Out);
	}

	@Test
	void testMuti() {
		Fraction f1= new Fraction(3,8);//分数形式3/8
		Fraction f2= new Fraction(5,24);//分数形式5/24
		Fraction outf=f1.muti(f2);//两者相乘，分数形式

		String exceptedOut="15/192";//预计结果15/192
		String Out=outf.getNumerator()+"/"+outf.getDenominator();
		assertEquals(exceptedOut,Out);
	}

	@Test
	void testDiv() {
		Fraction f1= new Fraction(7,16);//分数形式7/16
		Fraction f2= new Fraction(8,2);//分数形式8/2
		Fraction outf=f1.div(f2);//两者相除，分数形式

		String exceptedOut="14/128";//预计结果14/128（未约分的）
		String Out=outf.getNumerator()+"/"+outf.getDenominator();
		assertEquals(exceptedOut,Out);
	}

	@Test
	void testAppointment() {
		Fraction f= new Fraction(15,192);//分数形式15/192
		f.Appointment();//简单约分
		String exceptedAppointed="5/64";//预计结果5/64
		String Out=f.getNumerator()+"/"+f.getDenominator();
		assertEquals(exceptedAppointed,Out);
	}

	@Test
	void testTransferFraction() {
		Fraction f= new Fraction(500,64);//分数形式500/64
		String exceptedTransfer="7'13/16";//7'13/16
		String Out=f.transferFraction(f);
		assertEquals(exceptedTransfer,Out);
	}

}
