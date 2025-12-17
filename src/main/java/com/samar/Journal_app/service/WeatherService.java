package com.samar.Journal_app.service;

import com.samar.Journal_app.cache.AppCache;
import com.samar.Journal_app.constants.Placeholders;
import com.samar.Journal_app.dto.WeatherApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weatherApiKey}")
    private String apikey;
    private final static String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public  WeatherApiResponse getWeather( String city){
        String finalApi = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.apiKey, apikey).replace(Placeholders.city, city);
        ResponseEntity<WeatherApiResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherApiResponse.class);
        return response.getBody();

    }
    public int getFeelsLike(String city){
        WeatherApiResponse currentWeather = getWeather(city);
        return currentWeather.getCurrent().getFeelslike();
    }

}
