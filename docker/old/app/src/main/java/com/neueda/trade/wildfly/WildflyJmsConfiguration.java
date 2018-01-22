package com.neueda.trade.wildfly;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WildflyJmsConfiguration {
       
    @Bean(name="tradeQueue")
    public Queue tradeQueue(@Value("${trade.upstream.queue}") String name)
            throws JMSException, NamingException {
        Context ctx = new InitialContext();
        return (Queue) ctx.lookup("java:jboss/exported/tradeQueue");
    }

	@Bean(name = "tradeTopic")
	public Topic tradeTopic(@Value("${trade.downstream.topic}") String name) throws JMSException, NamingException {
        Context ctx = new InitialContext();
        return (Topic) ctx.lookup("java:jboss/exported/tradeTopic");
	}

    @Bean
    public ConnectionFactory connectionFactory() throws NamingException {
        Context ctx = new InitialContext();
        return (ConnectionFactory) ctx.lookup("java:/ConnectionFactory");
    }
     
}
