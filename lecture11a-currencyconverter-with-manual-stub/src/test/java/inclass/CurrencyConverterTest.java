package inclass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Currency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CurrencyConverterTest {

	private static final Currency USD = Currency.getInstance("USD");
	private static final Currency NOK = Currency.getInstance("NOK");
	
	private MyCurrencyConverter currencyConverter;

	@BeforeEach
	public void createNewCurrencyConverter() {
		currencyConverter = new MyCurrencyConverter();
	}

	@Test
	public void testBasicCurrencyConversion() {
		currencyConverter.setExchangeRateServide(new ExchangeRateServiceStub());
		double amountInUsd = currencyConverter.convertAmount(100.0, USD, NOK);
		assertEquals(840.0, amountInUsd, Double.MIN_VALUE);
	}

	@Test
	public void testZeroIsReturnedWhenIOException() {
		currencyConverter.setExchangeRateServide(new ExchangeRateServiceStub2());
		double amountInUsd = currencyConverter.convertAmount(100.0, USD, NOK);
		assertEquals(0.0, amountInUsd, Double.MIN_VALUE);
	}
}










