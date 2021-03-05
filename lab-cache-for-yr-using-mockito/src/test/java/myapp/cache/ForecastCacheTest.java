package myapp.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import myapp.domain.Forecast;
import myapp.domain.Timestamp;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) //To avoid errors when stubbing
												  //with different parameters
public class ForecastCacheTest {

	// --- Data values used in the tests -------------------------------------
	
		// ... Maybe you can set up some test data to be reused in the tests?


	// --- Instance variables ------------------------------------------------

	@Mock private ForecastFetcher mockedForecastFetcher;
	@Mock private TimestampService mockedTimestampService;

	private ForecastCache forecastCache;
	private Forecast firstBergenForecast;
	private Forecast secondBergenForecast;
	private Forecast firstOsloForecast;
	private Timestamp timestamp;
	

	// --- Test setup --------------------------------------------------------

	@BeforeEach
	public void setup() {
		
		// ... General setup ...
		forecastCache = new ForecastCache(mockedForecastFetcher, mockedTimestampService);
		firstBergenForecast = new Forecast("Bergen");
		secondBergenForecast = new Forecast("Bergen");
		firstOsloForecast = new Forecast("Oslo");
		timestamp = new Timestamp("a date", "a time");
		
		// ... Maybe you eventually can refactor out default behavior for the 
		//     mocks, which can be overridden in test methods if needed?
	}

	// --- The tests ---------------------------------------------------------

	@Test
	public void firstRequestShouldFetchAndReturnForcastFromYr() {
		when(mockedForecastFetcher.fetchForecastFor("Bergen")).thenReturn(firstBergenForecast);
		Forecast forecastFromCache = forecastCache.getForecastFor("Bergen");
		assertEquals(firstBergenForecast, forecastFromCache);
		verify(mockedForecastFetcher, times(1)).fetchForecastFor("Bergen");
	}
	
	@Test
	public void secondRequestShouldFetchAndReturnForcastFromCache() {
		when(mockedForecastFetcher.fetchForecastFor("Bergen")).thenReturn(firstBergenForecast);
		Forecast forecast1FromCache = forecastCache.getForecastFor("Bergen");
		Forecast forecast2FromCache = forecastCache.getForecastFor("Bergen");
		assertEquals(forecast1FromCache, forecast2FromCache);
		verify(mockedForecastFetcher, times(1)).fetchForecastFor("Bergen");
	}
	
	@Test
	public void requestShouldFetchAndReturnForcastFromYrIfCacheIsExpired() {
		firstBergenForecast.setTimestamp(timestamp);
		when(mockedForecastFetcher.fetchForecastFor("Bergen")).thenReturn(firstBergenForecast, secondBergenForecast);
		when(mockedTimestampService.hasExpired(any(Timestamp.class))).thenReturn(false, true);
		Forecast forecast1FromCache = forecastCache.getForecastFor("Bergen");
		Forecast forecast2FromCache = forecastCache.getForecastFor("Bergen");
		Forecast forecast3FromCache = forecastCache.getForecastFor("Bergen");
		assertEquals(forecast1FromCache, firstBergenForecast);
		assertEquals(forecast2FromCache, firstBergenForecast);
		assertEquals(forecast3FromCache, secondBergenForecast);
		verify(mockedForecastFetcher, times(2)).fetchForecastFor("Bergen");
	}
	
	@Test
	public void requestToNewLocationShouldFetchAndReturnForcastFromYr() {
		when(mockedForecastFetcher.fetchForecastFor("Bergen")).thenReturn(firstBergenForecast);
		when(mockedForecastFetcher.fetchForecastFor("Oslo")).thenReturn(firstOsloForecast);
		Forecast forecastForBergen = forecastCache.getForecastFor("Bergen");
		Forecast forecastForOslo = forecastCache.getForecastFor("Oslo");
		assertEquals(forecastForBergen, firstBergenForecast);
		assertEquals(forecastForOslo, firstOsloForecast);
		verify(mockedForecastFetcher, times(2)).fetchForecastFor(any(String.class));
	}
	
	@Test
	public void requestToDifferentPlacesShouldFetchAndReturnForcastFromYrAndThenFromCache() {
		firstBergenForecast.setTimestamp(timestamp);
		firstOsloForecast.setTimestamp(timestamp);
		when(mockedForecastFetcher.fetchForecastFor("Bergen")).thenReturn(firstBergenForecast, secondBergenForecast);
		when(mockedForecastFetcher.fetchForecastFor("Oslo")).thenReturn(firstOsloForecast, secondBergenForecast);
		when(mockedTimestampService.hasExpired(any(Timestamp.class))).thenReturn(false);
		Forecast bergenForecastFromYr = forecastCache.getForecastFor("Bergen");
		Forecast osloForecastFromYr = forecastCache.getForecastFor("Oslo");
		Forecast bergenForecastFromCache = forecastCache.getForecastFor("Bergen");
		Forecast osloForecastFromCache = forecastCache.getForecastFor("Oslo");
		assertEquals(bergenForecastFromCache, bergenForecastFromYr);
		assertEquals(osloForecastFromCache, osloForecastFromYr);
		verify(mockedForecastFetcher, times(2)).fetchForecastFor(any(String.class));
	}
	
	// ... more tests ...

}
