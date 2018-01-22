package com.neueda.trade.injector;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.stereotype.Component;

@Component
@Profile("injector-wildfly")
@ComponentScan({"com.neueda.trade.server.jms", "com.neueda.trade.activemq"})
public class WildflyClient {
	private static final Logger logger = LoggerFactory.getLogger(WildflyClient.class);

	private InitialContext context;
	
	public WildflyClient(@Value("${trade.injector.wildfly.user}") String user,
			@Value("${trade.injector.wildfly.password}") String password,
			@Value("${trade.injector.wildfly.url}") String url) throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        props.put(Context.PROVIDER_URL, url); 
        props.put(Context.SECURITY_PRINCIPAL, user);
        props.put(Context.SECURITY_CREDENTIALS, password);
	    context = new InitialContext(props); 
	    logger.info("Wildfly initial context: {}", context);   
	}
	
	@Bean
	ConnectionFactory wildflyConnectionFactory(@Value("${trade.injector.wildfly.factory}") String name,
			@Value("${trade.injector.wildfly.user}") String user,
			@Value("${trade.injector.wildfly.password}") String password) throws NamingException {
 	    ConnectionFactory factory = (ConnectionFactory) context.lookup(name);
	    UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
	    adapter.setTargetConnectionFactory(factory);
	    adapter.setUsername(user);
	    adapter.setPassword(password);
	    return adapter;
	}
	
    @Bean(name="tradeQueue")
    public Queue tradeQueue(@Value("${trade.upstream.queue}") String name)
            throws JMSException, NamingException {
	    return (Queue) context.lookup(name);
    }
    
}
