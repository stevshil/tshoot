package com.neueda.trade.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("wildfly")
@ComponentScan("com.neueda.trade.wildfly")
public class ConfigWildfly {

}
