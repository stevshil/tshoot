package com.neueda.trade.server.monitoring;

/**
 * Created by Neueda.
 */

public interface TradeMonitor {
	public TradeStats getStats();

	public void place();
	public void update();
	public void cancel();
	public void accept();
	public void deny();
	public void execute();
	public void reject();
	public void settle();
	public int reconcile();
}
