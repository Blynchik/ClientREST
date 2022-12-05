package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Consumer {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Consumer consumer = new Consumer();

        consumer.sendNewSensor(restTemplate);
        for(int i = 0; i < 1000; i ++){
            consumer.sendNewMeasurement(restTemplate);
        }
        ArrayList response = consumer.getMeasurements(restTemplate);

        for(Object measurement:response){
            System.out.println(measurement.toString());
        }
    }

    void sendNewSensor(RestTemplate restTemplate) {
        Map<String, String> sensorToSend = new HashMap<String, String>();
        sensorToSend.put("name", "RESTClient Sensor");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(sensorToSend);

        String newSensor = "http://localhost:8080/sensors/registration";
        String newSensorResponse = restTemplate.postForObject(newSensor, request, String.class);
        System.out.println(newSensorResponse);
    }

    void sendNewMeasurement(RestTemplate restTemplate) {
        Map<String, Object> measurementToSend = new HashMap<String, Object>();
        measurementToSend.put("value", getTemp());
        measurementToSend.put("raining", getRaining());
        measurementToSend.put("sensor", Map.of("name", "RESTClient Sensor"));

        HttpEntity<Map<String,Object>> request = new HttpEntity<>(measurementToSend);
        String newMeasurement = "http://localhost:8080/measurements/add";
        String newMeasurementResponse = restTemplate.postForObject(newMeasurement, request, String.class);
        System.out.println(newMeasurementResponse);
    }

    ArrayList getMeasurements(RestTemplate restTemplate){
        String url = "http://localhost:8080/measurements";
        return restTemplate.getForObject(url, ArrayList.class);
    }

    private Boolean getRaining() {
        int a = (int) (Math.random()*2);
        return a == 1;
    }

    private Double getTemp() {
        return Math.random() * (200) - 100;
    }
}
