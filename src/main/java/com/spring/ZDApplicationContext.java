package com.spring;


import java.io.File;
import java.net.URL;

public class ZDApplicationContext {
    private Class configClass;

    public ZDApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
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
                        if (clazz.isAnnotationPresent(Component.class)) {  // 对含有Component注解的类进行处理

                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            }

        }


    }

    public Object getBean(String beanName) {
        return null;
    }
}
