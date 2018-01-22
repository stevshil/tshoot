package com.neueda.trade.server.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.neueda.trade.server.TradeServer;
import com.neueda.trade.server.rest.TradeController;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest(classes = {TradeServer.class})
@ActiveProfiles({"test","server"})
public class TradeControllerTest {

	@Autowired
	private TradeController rest;
	
	@Value("${trade.server.version}") 
	private String version;

	@Test
	public void checkStatusResponseContainsRunning() {
		String status = rest.status();
		// JUnit
		assertTrue(status.contains("running"));
		// Hamcrest
		assertThat(status, containsString("running"));
	}
	
	@Test
	public void checkJsonVersionMatchesConfiguredValue() {
		Map<String, String> map = rest.version(version);
		assertThat(map.get("version"), equalTo(version));
	}

}
