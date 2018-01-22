package com.neueda.trade.server.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.neueda.trade.server.TradeException;
import com.neueda.trade.server.TradeServer;
import com.neueda.trade.server.rules.Model;

@RunWith( SpringRunner.class )
@SpringBootTest(classes = {TradeServer.class})
@ActiveProfiles({"test","server"})
public class ModelEnumTest {

	@Test
	public void testTranslateLetterToBuySellCorrectly() {
		assertThat(Model.fromChar("B", BuySell.class), is(BuySell.Buy));
		assertThat(Model.fromChar("S", BuySell.class), is(BuySell.Sell));
		assertThat(Model.toChar(BuySell.Buy), is("B"));
		assertThat(Model.toChar(BuySell.Sell), is("S"));
	}
	
	@Test(expected=TradeException.class)
	public void testInvalidTranslateLetterToBuySellFails() {
		assertThat(Model.fromChar("X", BuySell.class), is(BuySell.Buy));
	}
	
	@Test(expected=TradeException.class)
	public void testNullTranslateLetterToBuySellFails() {
		assertThat(Model.fromChar(null, BuySell.class), is(BuySell.Buy));
	}

	@Test
	public void testTranslateLetterToTradeStateCorrectly() {
		assertThat(Model.fromChar("A", TradeState.class), is(TradeState.Accept));
		assertThat(Model.toChar(TradeState.Accept), is("A"));
		assertThat(Model.fromChar("C", TradeState.class), is(TradeState.Cancel));
		assertThat(Model.toChar(TradeState.Cancel), is("C"));
		assertThat(Model.fromChar("D", TradeState.class), is(TradeState.Deny));
		assertThat(Model.toChar(TradeState.Deny), is("D"));
		assertThat(Model.fromChar("E", TradeState.class), is(TradeState.Execute));
		assertThat(Model.toChar(TradeState.Execute), is("E"));
		assertThat(Model.fromChar("M", TradeState.class), is(TradeState.Modify));
		assertThat(Model.toChar(TradeState.Modify), is("M"));
		assertThat(Model.fromChar("P", TradeState.class), is(TradeState.Place));
		assertThat(Model.toChar(TradeState.Place), is("P"));
		assertThat(Model.fromChar("R", TradeState.class), is(TradeState.Reject));
		assertThat(Model.toChar(TradeState.Reject), is("R"));
		assertThat(Model.fromChar("S", TradeState.class), is(TradeState.Settle));
		assertThat(Model.toChar(TradeState.Settle), is("S"));
	}

	@Test(expected=TradeException.class)
	public void testInvalidTranslateLetterToTradeStateFails() {
		assertThat(Model.fromChar("X", BuySell.class), is(BuySell.Buy));
	}


}
