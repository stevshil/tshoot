package com.neueda.trade.injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Upstraem application to inject trades into the system
 * 
 * @author Neueda
 *
 */
@SpringBootApplication
@Profile({"generator", "injector-wildfly"})
@ComponentScan({"com.neueda.trade.system", "com.neueda.trade.injector", "com.neueda.trade.server.database"})
public class Generator implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	/**
	 * Generate random trades using the Injector profiles. 
	 * In development it will always use the REST API on localhost as this
	 * works for all three profiles: dev, test and prod.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		logger.info("Starting trade generator");
		new SpringApplicationBuilder(Generator.class).web(false).properties("spring.profiles.active=generator", "trade.injector.place.only=true").profiles("injector-config").run(args);
	}
	
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private InjectTrades injector;
   
    @Autowired
    Environment env;
    
    @Autowired
    @Value("${trade.injector.generator.delay:10}")
    int delay;
    
	@Override
	public void run(String... args)  {
		if (env.acceptsProfiles("docker")) {
			logger.warn("Waiting for {} seconds to avoid race condition...", delay);
			try {Thread.sleep(delay*1000);} catch (InterruptedException ex) {}
		}
		logger.info("Running generator");
		injector.run();
		logger.info("Stopping generator");	
		SpringApplication.exit(appContext, () -> 0);
	}

}
