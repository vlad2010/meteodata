package com.company;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {


    public static void saveToFile(String text, String file) {
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // for test scenario hardcodes list of stations is suitable
        String[] stations = {
                "aberporth", "armagh", "ballypatrick","bradford", "braemar", "camborne", "cambridge", "cardiff", "chivenor", "cwmystwyth",
                "dunstaffnage", "durham", "eastbourne", "eskdalemuir", "heathrow", "hurn", "lerwick", "leuchars", "lowestoft", "manston",
                "nairn", "newtonrigg", "oxford", "paisley", "ringway", "rossonwye", "shawbury", "sheffield", "southampton", "stornoway",
                "suttonbonington", "tiree", "valley", "waddington", "whitby",   "wickairport", "yeovilton"
        };
        System.out.println("Upload data for " + stations.length + " meteo stations. ");
        List<String> stationsRawData = new LinkedList<>();

        for(String s : stations) {
            String url = String.format("https://www.metoffice.gov.uk/pub/data/weather/uk/climate/stationdata/%sdata.txt ", s);
            System.out.println("Loading -> " + url);
            String stationMeasures = Downloader.downloadData(url);

            stationsRawData.add(stationMeasures);

            // DEBUG
            //saveToFile(stationMeasures, String.format("./measures/%s.txt", s));
        }

        // create data holder class
        Meteocluster mCluster = new Meteocluster(stationsRawData);

        //get specific info
        System.out.println("\nStations by measures");
        final List<Meteostation> stationsByMeasures = mCluster.getStationsByNumberOfMeasures();
        stationsByMeasures.forEach( st -> System.out.println(st.getName() + " measures: " + st.getNumberOfMeasures()));

        System.out.println("\nStations by sunshine");
        final List<Meteostation> bySunShine = mCluster.getStationsBySunshine();
        bySunShine.forEach( st -> System.out.println(st.getName() + " sunshine: " + st.getSunshine()));

        System.out.println("\nStations and worst rainfall");
        final Map<String, Measure> stationsAndWorstRainFall = mCluster.getStationsAndWorstRainFall();
        stationsAndWorstRainFall.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " worst rainfall: " + s.getValue().rainMM));

        System.out.println("\nStations and best sunshine");
        final Map<String, Measure> stationsAndBestSunshine = mCluster.getStationsAndBestSunshine();
        stationsAndBestSunshine.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " best sunshine: " + s.getValue().rainMM));

        System.out.println("\nStations and average rain for May");
        final Map<String,Double> averageRain = mCluster.getAverageRainfallForMonth(5);
        averageRain.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " average rain for May: " + s.getValue()));

        System.out.println("\nStations and average sun for May");
        final Map<String,Double> averageSun = mCluster.getAverageSunshineForMonth(5);
        averageSun.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " average sun for May: " + s.getValue()));

        System.out.println("\nBest year for station");
        final Map<String , Integer> bestYears = mCluster.getBestYearForStations();
        bestYears.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " best year : " + s.getValue()));

        System.out.println("\nWorst yeat for station");
        final Map<String , Integer> worstYears = mCluster.getWorstYearForStations();
        worstYears.entrySet().stream().forEach(s-> System.out.println("Station: " + s.getKey() + " worst year : " + s.getValue()));
    }
}
