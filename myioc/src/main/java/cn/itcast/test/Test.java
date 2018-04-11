package cn.itcast.test;

import java.util.Map;

import cn.itcast.config.Bean;
import cn.itcast.config.parse.ConfigManager;

public class Test {
	//测试读取配置文件的ConfigManager.java 是否正确
	@org.junit.Test
	public void fun1(){
		Map<String, Bean> config = ConfigManager.getConfig("/applicationContext.xml");
		
		System.out.println(config);
		
	}
}
