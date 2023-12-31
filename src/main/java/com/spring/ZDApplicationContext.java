package com.spring;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZDApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String, Object> singletonMap = new ConcurrentHashMap<>(); //存放单例Bean
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(); //存放Bean定义
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ZDApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
        // ComponentScan注解->获得扫描路径->扫描->BeanDefinitionMap
        scan(configClass);

        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.getScope().equals("singleton")){   // 单例bean在Spring启动时初始化
                Object bean = createBean(beanName, beanDefinition);
                singletonMap.put(beanName, bean);
            }
        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance(); // 通过反射实例化bean

            // 依赖注入
            for(Field declaredField : clazz.getDeclaredFields()){
                if(declaredField.isAnnotationPresent(AutoWeired.class)){
                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }

            // Aware回调扩展点
            if(instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // BeanPostProcessor: postProcess
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessInitialization(instance, beanName);
            }

            // 初始化
            if(instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // BeanPostProcessor: afterProcess
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.afterProcessInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scan(Class configClass) {
        // 获得扫描路径
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value();
        path = path.replace(".", "/");

        // 扫描
        ClassLoader classLoader = ZDApplicationContext.class.getClassLoader(); //获得app类加载器
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile()); // 获得扫描路径的File对象
        if (file.isDirectory()) { // 逐个检查

            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", "."); // 转换为classloader可加载的路径

                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            // 对含有Component注解的类进行处理，当前类是一个Bean
                            // 解析类->生成该Bean的BeanDefinition

                            if(BeanPostProcessor.class.isAssignableFrom(clazz)){
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                            }

                            Component component = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();
                            Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if (scopeAnnotation == null) {
                                beanDefinition.setScope("singleton");
                            } else {
                                beanDefinition.setScope(scopeAnnotation.value());
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);

                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
    }

    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                return singletonMap.get(beanName);
            } else {
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }
}
