package cn.itcast.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {
	//����1 bean����
	//����2 Ҫ��õ�Bean�����Ӧ����������
	public static Method getWriteMethod(Object beanObj, String name) {
		Method method = null;
		//ʹ����ʡ������ʵ�ָ÷���
			try {
				//1. ����Bean����=> BeanInfo
				BeanInfo info = Introspector.getBeanInfo(beanObj.getClass());
				//2. ����BeanInfo����������Ե�������
				PropertyDescriptor[] pds = info.getPropertyDescriptors();
				//3. ������Щ����������
				if(pds!=null){
					for(PropertyDescriptor pd : pds){
						//�жϵ�ǰ�����������������������Ƿ�������Ҫ�ҵ�����
						//��õ�ǰ��������������������
						String pName = pd.getName();
						//ʹ��Ҫ�ҵ����������뵱ǰ�������������������Ʊȶ�
						if(pName.equals(name)){
							//�ȶ�һ��=>�ҵ���,���д�����Ե�set����
							method = pd.getWriteMethod();
						}
					}
				}
				
				//4. �����ҵ���set����
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
			//���û���ҵ�=>�׳��쳣��ʾ�û� ����Ƿ񴴽����Զ�Ӧ��set����
			if(method==null){
				throw new RuntimeException("�͹�!����"+name+"���Ե�set�����Ƿ񴴽�!");
			}
			
			
		return method;
	}

}
