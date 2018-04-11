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
	//��ȡ �����ļ� => �����ض�ȡ���
	
	public static	Map<String,Bean>  getConfig(String path){
		//����һ�����ڷ��صģ������
		Map<String,Bean> map = new HashMap<String, Bean>();
		//dom4jʵ��
			//1 ����������
				SAXReader reader = new SAXReader();
			//2 ���������ļ�=>document����
				InputStream is = 	ConfigManager.class.getResourceAsStream(path);
				Document doc = null;
				try {
					 doc = reader.read(is);
				} catch (DocumentException e) {
					e.printStackTrace();
					throw new RuntimeException("�͹�!��������xml�����Ƿ���ȷ!");
				}
			//3 ����xpath���ʽ,ȡ������BeanԪ��
				String xpath = "//bean";
			//4 ��BeanԪ�ؽ��б���
				List<Element>  list = doc.selectNodes(xpath);
				if(list!=null){
					for(Element beanEle : list){
						Bean bean = new Bean();
						//��beanԪ�ص�name/class ���Է�װ��Bean������
						String  name = beanEle.attributeValue("name");
						String  className = beanEle.attributeValue("class");
						String  scope = beanEle.attributeValue("scope");
						
						
						bean.setName(name);
						bean.setClassName(className);
						if(scope!=null ){
							bean.setScope(scope);
						}
						
						//���BeanԪ���µ�����Property��Ԫ�� ,������name/value/ref��װ��Property������
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
								
								//��Property��װ��Bean����
								bean.getProperties().add(prop);
							}
						}
						//��Bean�����װ��Map��(���ڷ��ص�map)
						map.put(name, bean);
					}
				}
			//5 ����Map���
				return map;
		
	}
}
