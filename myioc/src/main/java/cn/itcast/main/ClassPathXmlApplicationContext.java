package cn.itcast.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.itcast.config.Bean;
import cn.itcast.config.Property;
import cn.itcast.config.parse.ConfigManager;
import cn.itcast.utils.BeanUtils;

public class ClassPathXmlApplicationContext implements BeanFactory {

	//ϣ����ClassPathXmlApplicationContext��һ����
	//�ͳ�ʼ��spring����(װ��Beanʵ����)
	//������Ϣ
	private Map<String, Bean> config;
	//ʹ��һ��map����spring������=> ��������spring������Ķ���
	private Map<String,Object> context = new HashMap<String, Object>();
	
	
	public ClassPathXmlApplicationContext(String path) {
		//1 ��ȡ�����ļ������Ҫ��ʼ����Bean��Ϣ
			config = ConfigManager.getConfig(path);
		//2 �������� ��ʼ��Bean
			if(config!=null){
				for(Entry<String, Bean> en : config.entrySet()){
					//��������е�Bean��Ϣ
					String beanName = en.getKey();
					Bean bean = en.getValue();
					
					Object existBean = context.get(beanName);
					//��ΪcreateBean������Ҳ����Context�з���Bean
					//�����ڳ�ʼ��֮ǰ��Ҫ�ж��������Ƿ��Ѿ����������Bean.��ȥ��ɳ�ʼ���Ĺ���
					//�������ǵ�Bean��scope����ֵΪsingleton,�Ž�Bean����������
					if(existBean==null && bean.getScope().equals("singleton") ){
					//����bean���� ����bean����
					Object beanObj = createBean(bean);
					//3 ����ʼ���õ�Bean����������
					context.put(beanName, beanObj);
					}
				}
			}
	}
	//����Bean���ô���Beanʵ��
	/*
	 * <bean name="A" class="cn.itcast.bean.A"  >
			<!-- ��A����������,spring���Զ������õ�ֵע�뵽A�� -->
			<property name="name" value="tom" ></property>
		</bean>
	 */
	private Object createBean(Bean bean) {
			//1 ���Ҫ������Bean��Class
				String className = bean.getClassName();
				Class clazz  = null;
				try {
					 clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException("�͹�!������Bean��Class�����Ƿ���ȷ!"+className);
				}
				//���class=> ��class��Ӧ�Ķ��󴴽�����
				Object beanObj  = null;
				try {
					 beanObj = clazz.newInstance();//���ÿղι���
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("�͹�!����Beanû�пղι���!"+className);
				}
				
			//2 ��Ҫ���Bean������,����ע��
				if(bean.getProperties()!=null){
					for(Property prop : bean.getProperties()){
						//ע����������
						// ���Ҫע�����������
						String name =  prop.getName();
						String value = prop.getValue();
						String ref = prop.getRef();
						//ע��ֵ�������Է�ʽ2:ʹ��BeanUtils������������Ե�ע��
						if(value!=null){// ˵����ֵ���͵�������Ҫע��
							Map<String,String[]> paramMap = new HashMap<String, String[]>();
							paramMap.put(name, new String[]{value});
							//����BeanUtils������ֵ���͵�����ע��(����ע��=>�����Զ��������ת��)
							try {
								org.apache.commons.beanutils.BeanUtils.populate(beanObj,paramMap);
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException("�͹�!��������"+name+"����!");
							} 
						}
						
						//����ע��Bean���͵�����
						if(prop.getRef()!=null){
							//2> �鷳=> ����Bean��ע��
							// �����������ƻ��ע�����Զ�Ӧ��Set����
							Method setMethod = BeanUtils.getWriteMethod(beanObj,name);
							// ��ΪҪע������bean����ǰbean��,�����ȴ������в��ҵ�ǰҪע���Bean�Ƿ��Ѿ�����,������������
							Object existBean =  context.get(prop.getRef());	
							
							if(existBean == null ){
								//˵�������л�����������Ҫע���Bean
								//��Bean����
								existBean = createBean(config.get(prop.getRef()));
								//�������õ�Bean����������
								if(config.get(prop.getRef()).getScope().equals("singleton")){
									context.put(prop.getRef(), existBean);
								}
							}
							
							// ����set����ע�뼴��
							try {
								setMethod.invoke(beanObj, existBean);
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException("�͹�!����Bean������"+name+"û�ж�Ӧ��set����,�򷽷���������ȷ"+className);
							}
						}
						
						
						
						
						
						
						
						/*// �����������ƻ��ע�����Զ�Ӧ��Set����
						Method setMethod = BeanUtils.getWriteMethod(beanObj,name);
						// ����һ����Ҫע�뵽Bean�е�����
						Object param = null;
						if(prop.getValue()!=null){
							//1> ��=> value����ע��
							//���Ҫע���ʵ��ֵ
							String value = prop.getValue();
							param = value;
						}
						if(prop.getRef()!=null){
							//2> �鷳=> ����Bean��ע��
							
							// ��ΪҪע������bean����ǰbean��,�����ȴ������в��ҵ�ǰҪע���Bean�Ƿ��Ѿ�����,������������
							Object existBean =  context.get(prop.getRef());	
							
							if(existBean == null ){
								//˵�������л�����������Ҫע���Bean
								//��Bean����
								existBean = createBean(config.get(prop.getRef()));
								//�������õ�Bean����������
								if(config.get(prop.getRef()).getScope().equals("singleton")){
									context.put(prop.getRef(), existBean);
								}
							}
							
							param = existBean;
						}
						
						
						// ����set����ע�뼴��
						try {
							setMethod.invoke(beanObj, param);
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("�͹�!����Bean������"+name+"û�ж�Ӧ��set����,�򷽷���������ȷ"+className);
						} */
					}
				}
				
				
		return beanObj;
	}



	@Override
	//����Bean�����ƻ��Beanʵ��
	public Object getBean(String beanName) {
			
		Object bean = context.get(beanName);
		
		//���bean��scope����Ϊprototype .��ô context�оͲ��������Bean����
		if(bean==null){
			//��������ڸ�Bean����,��ô�ʹ������Bean����
			bean = createBean(config.get(beanName));
		}
		
		return bean;
	}


}
