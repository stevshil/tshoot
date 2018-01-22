package com.neueda.trade.server.monitoring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neueda.trade.server.model.Trade;
import com.neueda.trade.server.model.TradeDao;
import com.neueda.trade.server.model.TradeState;

@Component
public class TradeStats {
    private AtomicInteger totalTrades = new AtomicInteger();
    private AtomicInteger activeTrades = new AtomicInteger();
    private AtomicInteger placedTrades = new AtomicInteger();
    private AtomicInteger cancelledTrades = new AtomicInteger();
    private AtomicInteger deniedTrades = new AtomicInteger();
    private AtomicInteger rejectedTrades = new AtomicInteger();
    private AtomicInteger settledTrades = new AtomicInteger();
    
    public TradeStats(@Autowired TradeDao tradeDao) {
    	System.out.println(tradeDao);
    	List<Trade> trades = tradeDao.findAll();
    	trades.stream().forEach(t -> {
    		switch (t.getState()) {
    		case Accept: break;
    		case Cancel: cancel(); break;
    		case Deny: deny(); break;
    		case Execute: break;
    		case Modify: break;
    		case Place: place(); break;
    		case Reject: reject(); break;
    		case Settle: settle(); break;
    		default: break;
    		}
    	});
    }
    
    public interface Invoke<R>
    {
        R invoke();
    }
    

	public int getTotalTrades() {
		return totalTrades.get();
	}
	public int getActiveTrades() {
		return activeTrades.get();
	}
	
	public int getPlacedTrades() {
		return placedTrades.get();
	}
	public int getCancelledTrades() {
		return cancelledTrades.get();
	}
	public int getDeniedTrades() {
		return deniedTrades.get();
	}
	public int getRejectedTrades() {
		return rejectedTrades.get();
	}
	public int getSettledTrades() {
		return settledTrades.get();
	}

	public int place() {
		placedTrades.incrementAndGet();
		activeTrades.incrementAndGet();
		return totalTrades.incrementAndGet();
	}

	public int cancel() {
		activeTrades.decrementAndGet();
		return cancelledTrades.incrementAndGet();
	}

	public int deny() {
		activeTrades.decrementAndGet();
		return deniedTrades.incrementAndGet();
	}

	public int reject() {
		activeTrades.decrementAndGet();
		return rejectedTrades.incrementAndGet();
	}

	public int settle() {
		activeTrades.decrementAndGet();
		return settledTrades.incrementAndGet();
	}
	
	/**
	 * EOD reconciliation clears down the stats
	 * @return number of outstanding active trades which should be zero on a good day
	 */
	public int reconcile() {
		int active = activeTrades.get();
		Stream.of(totalTrades, activeTrades, placedTrades, cancelledTrades, 
				  deniedTrades, rejectedTrades, settledTrades).forEach(ai -> ai.set(0));
		return active;
	}

}
