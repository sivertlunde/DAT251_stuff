package myapp.cache;

import java.util.HashMap;
import java.util.Map;

import myapp.domain.Forecast;

public class ForecastCache {
	
	private Map<String, Forecast> cache;
	private ForecastFetcher fetcher;
	private TimestampService timestampService;
	
	public ForecastCache(ForecastFetcher mockedForecastFetcher, TimestampService mockedTimestampService) {
		cache = new HashMap<>();
		fetcher = mockedForecastFetcher;
		timestampService = mockedTimestampService;
	}
	
	public Forecast getForecastFor(String location) {
		Forecast forLocation = cache.get(location);
		if (forLocation == null || timestampService.hasExpired(forLocation.getTimestamp())) {
			cache.put(location, fetcher.fetchForecastFor(location));
		}
		return cache.get(location);
	}

	public void setFetcher(ForecastFetcher mockedForecastFetcher) {
		this.fetcher = mockedForecastFetcher;
	}

}
