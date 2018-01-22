package com.neueda.trade.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * This starts the standalone Spring Boot web server or
 * initialises the web application
 * 
 * @author Neueda
 *
 */
@SpringBootApplication
public class TradeServer extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(TradeServer.class);

    public static void main(String[] args) throws Exception {
    	logger.info("Trading application starting");
    	// SpringApplication.run(TradeServer.class, args);
    	new SpringApplicationBuilder().sources(TradeServer.class).profiles("server").run(args);
    } 
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("Initialising trade web application");
		return application.sources(TradeServer.class).profiles("wildfly").bannerMode(Banner.Mode.OFF);
	}

}


