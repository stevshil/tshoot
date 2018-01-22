package com.neueda.trade.injector;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InjectorUtils {

	
	private ThreadLocalRandom random = ThreadLocalRandom.current();

	/**
	 * Uses trade.injector.error.rate to determine if caller should define an error scenario
	 * @return
	 */
	public boolean injectFault() {
		return injectorErrorRate > 0 ? random.nextDouble(100.0)<injectorErrorRate: false;
	}

	/**
	 * Uses trade.injector.error.rate to determine if caller should define an error scenario
	 * @return
	 */
	public boolean isFailure() {
		return injectorFailRate > 0 ? random.nextDouble(100.0)<injectorFailRate: false;
	}


	/**
	 * Pause current thread to simulate processing delays
	 */
	public void pause() {
		double delay = getInjectorFreq()>0 ? 3600.0/getInjectorFreq() : 0;
		if (delay > 0) {
			try {
				Thread.sleep((long)(delay*1000*random.nextDouble(1-DELAY_VAR, 1+DELAY_VAR)));	
			}
			catch (InterruptedException ex) {}
		}
	}

	/**
	 * Variance as a fraction above/below sleep delay
	 */
	private static final double DELAY_VAR = 0.5;	

	/**
	 * number of trades to generate
	 */
	@Autowired
	@Value(value="${trade.injector.limit:10}")
	private int injectorLimit;

	/**
	 * number per minute
	 */
	@Autowired
	@Value("${trade.injector.freq:3600}")
	private int injectorFreq;

	/**
	 * fraction of trades that may cause an error
	 */
	@Autowired
	@Value("${trade.injector.error.rate:0}")
	private double injectorErrorRate;

	/**
	 * fraction of messages to show failure (deny/reject)
	 */
	@Autowired
	@Value("${trade.injector.fail.rate:0}")
	private double injectorFailRate;

	public int getInjectorLimit() {
		return injectorLimit;
	}

	public int getInjectorFreq() {
		return injectorFreq;
	}

	public double getInjectorErrorRate() {
		return injectorErrorRate;
	}

	public double getInjectorFailRate() {
		return injectorFailRate;
	}
	
}
