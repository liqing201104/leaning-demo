package cn.itcast.main;

import cn.itcast.bean.A;
import cn.itcast.bean.B;
import cn.itcast.bean.C;

public class Test {
	@org.junit.Test
	public void fun1(){
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		A a = (A) bf.getBean("A");
		/*A a2 = (A) bf.getBean("A");
		A a3 = (A) bf.getBean("A");*/
		
		System.out.println(a.getName());//tom
		
	}
	
	
	@org.junit.Test
	public void fun2(){
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		B b = (B) bf.getBean("B");
		B b2 = (B) bf.getBean("B");
		B b3 = (B) bf.getBean("B");
		B b4 = (B) bf.getBean("B");
		
		System.out.println(b.getA().getName());//jerry
		
	}
	
	@org.junit.Test
	public void fun3(){
		BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
		
		C c = (C) bf.getBean("C");
		C c2 = (C) bf.getBean("C");
		C c3 = (C) bf.getBean("C");
		C c4 = (C) bf.getBean("C");
		
		System.out.println(c.getB().getA().getName());//jerry
		
	}
}
