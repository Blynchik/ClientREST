package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Consumer {
    public static void main(String[] args) {
        final String sensorName = "Sensor RESTClient";

        sendNewSensor(sensorName);

        for (int i = 0; i < 1000; i++) {
            sendNewMeasurement(sensorName);
        }
        ArrayList response = getMeasurements();

        for (Object measurement : response) {
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

    public static ArrayList getMeasurements() {
        String url = "http://localhost:8080/measurements";
        final RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, ArrayList.class);
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
