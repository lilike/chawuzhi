package com.chawuzhi.admin;



import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

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
        container.setPort(8090);  
        return container;  
    }  
    
    /**
     * 点对点
     * @return
     */
    @Bean
    public Queue queue(){
        return new ActiveMQQueue("test-queue");
    }

    /**
     * 发布/订阅
     * @return
     */
    @Bean
    public Topic topic(){
        return new ActiveMQTopic("test-topic");
    }





}
