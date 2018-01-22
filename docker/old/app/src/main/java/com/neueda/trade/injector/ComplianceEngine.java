package com.neueda.trade.injector;

import java.util.concurrent.CountDownLatch;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.neueda.trade.server.model.Trade;
import com.neueda.trade.server.model.TradeState;

/**
 * Example upstream client to place a trade and wait for any topic message
* Requires an external ActiveMQ queue so uses the "prod" configuration 
 * profile and run the tradeserver in prod mode.
 * 
 * @author Neueda
 *
 */
@SpringBootApplication
@Profile({"compliance", "injector-wildfly"})
@ComponentScan({"com.neueda.trade.system", "com.neueda.trade.injector", "com.neueda.trade.server.database"})
public class ComplianceEngine implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ComplianceEngine.class);

	public static void main(String[] args) throws Exception {
		logger.info("Starting trade compliance engine");
		new SpringApplicationBuilder(ComplianceEngine.class).web(false).properties("spring.profiles.active=compliance").profiles("injector-config").run(args);
	}

   
    @Bean (name="topicContainerFactory")
    public DefaultJmsListenerContainerFactory topicContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }

    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    private CountDownLatch latch;
    public ComplianceEngine(InjectorUtils utils) {
    	latch = new CountDownLatch(utils.getInjectorLimit());
    }

	@Autowired
	private InjectorUtils utils;

	@Autowired
	private InjectorClient client;
	
	private void sendTrade(Trade trade, TradeState ok, TradeState fail) {
		utils.pause();
		client.updateState(trade.getTransid(), utils.isFailure() ? fail : ok);
		if (utils.injectFault()) {
			// simulate duplicate or conflicting message
			utils.pause();
			client.updateState(trade.getTransid(), utils.isFailure() ? fail : ok);			
		}	
	}

    @JmsListener(containerFactory = "topicContainerFactory",
		     destination = "${trade.downstream.topic}",
		     selector = "Operation = 'Place'")
	public void placeTrade(Trade trade) throws JMSException {
		logger.info("Compliance place trade: {}", trade);
		sendTrade(trade, TradeState.Accept, TradeState.Deny);
		if (utils.injectFault()) {
			client.updateState(trade.getTransid(), TradeState.Cancel);			
		}
		if (utils.getInjectorLimit() > 0) {
			latch.countDown();
		}
	}

    @Autowired
    private ApplicationContext appContext;

	@Override
	public void run(String... args) {
		logger.info("Compliance waiting for placed trades: limit {}", utils.getInjectorLimit());
		if (utils.getInjectorLimit() > 0) {
			try {latch.await();} catch (InterruptedException ex) {}
			logger.info("Stopping compliance engine");	
			SpringApplication.exit(appContext, () -> 0);
		}
	}

}
