package cn.itcast.main;

public interface BeanFactory {
	//根据Bean的name获得 Bean对象的方法
	Object getBean(String beanName);
}
