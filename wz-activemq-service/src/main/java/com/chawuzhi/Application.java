package com.chawuzhi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	}
	
	
    /** 
     * 修改tomcat默认配置 
     * @return 
     */  
    @Bean  
    public TomcatEmbeddedServletContainerFactory servletContainer(){  
        TomcatEmbeddedServletContainerFactory container = new TomcatEmbeddedServletContainerFactory();  
        container.setPort(8091);  
        return container;  
    }  
    
}
