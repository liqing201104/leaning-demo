package cn.itcast.test;

import java.util.Map;

import cn.itcast.config.Bean;
import cn.itcast.config.parse.ConfigManager;

public class Test {
	//���Զ�ȡ�����ļ���ConfigManager.java �Ƿ���ȷ
	@org.junit.Test
	public void fun1(){
		Map<String, Bean> config = ConfigManager.getConfig("/applicationContext.xml");
		
		System.out.println(config);
		
	}
}
