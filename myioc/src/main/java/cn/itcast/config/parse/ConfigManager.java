package cn.itcast.config.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.itcast.config.Bean;
import cn.itcast.config.Property;

public class ConfigManager {
	//读取 配置文件 => 并返回读取结果
	
	public static	Map<String,Bean>  getConfig(String path){
		//创建一个用于返回的ｍａｐ对象
		Map<String,Bean> map = new HashMap<String, Bean>();
		//dom4j实现
			//1 创建解析器
				SAXReader reader = new SAXReader();
			//2 加载配置文件=>document对象
				InputStream is = 	ConfigManager.class.getResourceAsStream(path);
				Document doc = null;
				try {
					 doc = reader.read(is);
				} catch (DocumentException e) {
					e.printStackTrace();
					throw new RuntimeException("客官!请检查您的xml配置是否正确!");
				}
			//3 定义xpath表达式,取出所有Bean元素
				String xpath = "//bean";
			//4 对Bean元素进行遍历
				List<Element>  list = doc.selectNodes(xpath);
				if(list!=null){
					for(Element beanEle : list){
						Bean bean = new Bean();
						//将bean元素的name/class 属性封装到Bean对象中
						String  name = beanEle.attributeValue("name");
						String  className = beanEle.attributeValue("class");
						String  scope = beanEle.attributeValue("scope");
						
						
						bean.setName(name);
						bean.setClassName(className);
						if(scope!=null ){
							bean.setScope(scope);
						}
						
						//获得Bean元素下的所有Property子元素 ,将属性name/value/ref封装到Property对象中
						List<Element> children = beanEle.elements("property");
						
						if(children!=null){
							for(Element child : children){
								Property prop = new Property();
								
								String  pName = child.attributeValue("name");
								String  pValue = child.attributeValue("value");
								String  pRef = child.attributeValue("ref");
								
								prop.setName(pName);
								prop.setRef(pRef);
								prop.setValue(pValue);
								
								//将Property封装到Bean对象
								bean.getProperties().add(prop);
							}
						}
						//将Bean对象封装到Map中(用于返回的map)
						map.put(name, bean);
					}
				}
			//5 返回Map结果
				return map;
		
	}
}
