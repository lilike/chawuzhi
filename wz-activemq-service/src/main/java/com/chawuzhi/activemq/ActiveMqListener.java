package com.chawuzhi.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqListener {

    @JmsListener(destination = "hello") 
    @SendTo("out.queue")  
    public String receiveQueue(String text) {  
        System.out.println("Consumer get message:"+text);  
        return "return MEssage I have got :" + text;
    }  
    
    
    
}
