package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Meteostation {

    private String name="";
    private List<Measure> measures = new LinkedList<>();

    private Map<Integer, Measure> averagesByYear = null;

    public String getName() { return name; }

    public int getNumberOfMeasures() {
        return measures.size();
    }

    public double getSunshine() {
        return measures.stream().mapToDouble(s-> s.sunHours).sum();
    }

    public double getRainfall() {
        return measures.stream().mapToDouble(s-> s.rainMM).sum();
    }

    public Measure getWorseRainfall() {
        //double worstRain = measures.stream().mapToDouble(s-> s.rainMM).filter(d -> d!=0.0).max().getAsDouble();;
        return Collections.max(measures, Comparator.comparing( ms1 -> ms1.rainMM  ));
    }

    public Measure getBestSunshine() {
        //double bestSunshine =  measures.stream().mapToDouble(s-> s.sunHours).max().getAsDouble();
        return Collections.max(measures, Comparator.comparing( ms1 -> ms1.sunHours));
    }

    public double getAverageSunshineForMonth(int month) {
        return measures.stream().filter(s -> s.month == month).filter(s -> s.sunHours > 0.0).mapToDouble(s -> s.sunHours).average().getAsDouble();
    }

    public double getAverageRainfallForMonth(int month) {
        return measures.stream().filter(s -> s.month == month).filter(s -> s.rainMM > 0.0).mapToDouble(s -> s.rainMM).average().getAsDouble();
    }

    public int getWorstYear() {

        calculateAveragesByYear();
        List<Map.Entry<Integer, Measure>> sortedMeasures = averagesByYear.entrySet().stream().sorted(Comparator.comparing(m -> m.getValue())).collect(Collectors.toList());

        if(sortedMeasures.size()==0)
            return 0;

        return sortedMeasures.get(sortedMeasures.size()-1).getValue().year;
    }

    public int getBestYear() {

        calculateAveragesByYear();
        List<Map.Entry<Integer, Measure>> sortedMeasures = averagesByYear.entrySet().stream().sorted(Comparator.comparing(m -> m.getValue())).collect(Collectors.toList());

        if(sortedMeasures.size()==0)
            return 0;

        return sortedMeasures.get(0).getValue().year;
    }

    private void calculateAveragesByYear() {

        if(averagesByYear==null) {

            averagesByYear = new HashMap<>();
            Map<Integer, List<Measure>> map = measures.stream().collect(Collectors.groupingBy( m -> m.year));

            for ( Integer year : map.keySet() ) {
                List<Measure> meauresForThisYear = map.get(year);

                Measure ms = new Measure();
                ms.year = year;
                ms.sunHours = meauresForThisYear.stream().filter(m -> m.sunHours>0).mapToDouble(d->d.sunHours).average().orElse(-1.0);

                //cast to int here
                ms.afDays = (int)(meauresForThisYear.stream().filter(m -> m.afDays>0).mapToInt(d->d.afDays).average().orElse(-1.0));
                ms.rainMM =  meauresForThisYear.stream().filter(m -> m.rainMM>0).mapToDouble(d->d.rainMM).average().orElse(-1.0);
                ms.tmax = meauresForThisYear.stream().filter(m -> m.tmax>0).mapToDouble(d->d.tmax).average().orElse(-1.0);
                ms.tmin = meauresForThisYear.stream().filter(m -> m.tmin>0).mapToDouble(d->d.tmin).average().orElse(-1.0);

                //System.out.println("Average measure for month: "+ year + " " + ms);
                averagesByYear.put(year, ms);
            }
        }
    }

    public Meteostation(String stationRawData) {

        //remove these # * symbols
        stationRawData = stationRawData.replace("*", "");
        stationRawData = stationRawData.replace("#", "");

        String lines[] = stationRawData.split("\\r?\\n");
        if(lines.length==0)
            return;

        if(lines[0].contains(" ")){
            name= lines[0].substring(0, lines[0].indexOf(" "));
        } else {
            name = lines[0];
        }

        System.out.println("Processing : " + name );

        boolean headerDetected = false;
        for (String line : lines) {

            String tokens[] = line.split("\\s+");
            if(!headerDetected) {
                headerDetected = line.contains("degC");;
                continue;
            }

            try {
                Measure ms = new Measure(line);
                measures.add(ms);
                //System.out.println("Measure: " + ms) ;
            }
            catch(Exception e) {
                System.out.println("Exception : " + e) ;
            }
        }
    }

    public String toString() {
        return name + " measures:" + measures.size() + " sun:" + getSunshine() + " rain:" + getRainfall();
    }
}
