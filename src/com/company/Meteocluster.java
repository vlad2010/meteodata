package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Meteocluster {

    private List<Meteostation> meteoStations = new LinkedList<>();

    public Meteocluster( List<String> rawData) {
        for(String stationRawData : rawData) {
            meteoStations.add(new Meteostation(stationRawData));
        }
    }

    public List<Meteostation> getStationsByNumberOfMeasures() {
        return meteoStations.stream().sorted( (o1,o2) -> o2.getNumberOfMeasures() - o1.getNumberOfMeasures()  ).collect(Collectors.toList());
    }

    public List<Meteostation> getStationsBySunshine() {
        return meteoStations.stream().sorted( (o1,o2) -> Double.compare(o2.getSunshine(), o1.getSunshine()) ).collect(Collectors.toList());
    }

    public List<Meteostation> getStationsByRainfall() {
         return meteoStations.stream().sorted( (o1,o2) -> Double.compare(o1.getRainfall(), o2.getRainfall()) ).collect(Collectors.toList());
    }

    public Map<String,Measure> getStationsAndWorstRainFall() {
        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getWorseRainfall() ));
    }

    public Map<String,Measure> getStationsAndBestSunshine() {
        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getBestSunshine() ));
    }

    public Map<String, Double> getAverageRainfallForMonth(int month) {
        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getAverageRainfallForMonth(month) ));
    }

    public Map<String, Double> getAverageSunshineForMonth(int month) {

        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getAverageSunshineForMonth(month) ));
    }

    // best-worst criterias : more sun is the better, the second criteria is rainfall
    public Map<String, Integer> getBestYearForStations() {
        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getBestYear() ));
    }

    public Map<String, Integer> getWorstYearForStations() {
        return meteoStations.stream().collect(
                Collectors.toMap(mts1 -> mts1.getName(), mts2 -> mts2.getWorstYear() ));
    }

}
