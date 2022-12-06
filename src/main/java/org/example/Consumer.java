package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class Consumer {
    public static void main(String[] args) {
        final String sensorName = "Sensor RESTClient";

        sendNewSensor(sensorName);

        for (int i = 0; i < 10; i++) {
            sendNewMeasurement(sensorName);
        }
        List<Map<String, Object>> response = getMeasurements();

        for (Map<String, Object> measurement : response) {
            System.out.println(measurement.toString());
        }
    }

    public static void sendNewSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> sensorToSend = new HashMap<>();
        sensorToSend.put("name", sensorName);

        makeRequest(url, sensorToSend);
    }

    public static void sendNewMeasurement(String sensorName) {
        final String url = "http://localhost:8080/measurements/add";

        Map<String, Object> measurementToSend = new HashMap<>();
        measurementToSend.put("value", getTemp());
        measurementToSend.put("raining", getRaining());
        measurementToSend.put("sensor", Map.of("name", sensorName));

        makeRequest(url, measurementToSend);
    }

    public static List<Map<String, Object>> getMeasurements() {
        String url = "http://localhost:8080/measurements";
        final RestTemplate restTemplate = new RestTemplate();

       ArrayList response =  restTemplate.getForObject(url, ArrayList.class);

        if (response == null || response.isEmpty()){
            return Collections.emptyList();
        }

        return response;
    }

    private static Boolean getRaining() {
        int a = (int) (Math.random() * 2);
        return a == 1;
    }

    private static Double getTemp() {
        return Math.random() * (200) - 100;
    }

    private static void makeRequest(String url, Map<String, Object> json) {
        final RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(json);

        try {
            String newSensorResponse = restTemplate.postForObject(url, request, String.class);
            System.out.println("Успешно отправлено");
        } catch (HttpClientErrorException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
