package org.example;

import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawChart {
    public static void main(String[] args) {
        List<Double> temperatures = getTemperatures();
        drawChart(temperatures);
    }

    private static List<Double> getTemperatures() {

       return Consumer.getMeasurements().stream()
               .map(m->Double.valueOf(m.get("value").toString()))
               .collect(Collectors.toList());
    }

    private static void drawChart(List<Double> temperatures) {
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x -> x).toArray();

        new SwingWrapper(QuickChart.getChart("Temperatures", "X", "Y", "temperature",
                xData, yData))
                .displayChart();
    }
}
