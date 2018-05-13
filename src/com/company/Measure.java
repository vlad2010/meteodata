package com.company;

public class Measure implements Comparable<Measure> {

    public int year=0;
    public int month=0;
    public double tmax=0.0;
    public double tmin=0.0;
    public int afDays=0;
    public double rainMM=0.0;
    public double sunHours=0.0;

    private enum TokenPos {
        YEAR, MONTH, TMAX, TMIN, AFDAYS, RAINMM, SUNHOURS;
        private static TokenPos[] allValues = values();
        private static TokenPos fromInt(int n){return allValues[n];};
    };

    private static final String NODATA = "---";
    private static final String SITECLOSED = "Site";
    private final int TOKENS_MAX = 7;

    public int compareTo(Measure otherMeasure) {
        // simple comparation criteia : by number of sunHours
        if(otherMeasure.sunHours==sunHours) return 0;

        if(otherMeasure.sunHours>sunHours)
            return 1;
        return -1;
    }

    public Measure() {

    }

    public Measure(String measureString) {
        String tokens[] = measureString.trim().split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            if(i>=TOKENS_MAX) {
                continue;
            }

            if (NODATA.equals(tokens[i])) {
                continue;
            }

            // this is end of data
            if(SITECLOSED.equals(tokens[i])) {
                break;
            }

            TokenPos index = TokenPos.fromInt(i);

            switch (index) {
                case YEAR:
                    year = Integer.parseInt(tokens[index.ordinal()]);
                    break;
                case MONTH:
                    month = Integer.parseInt(tokens[index.ordinal()]);
                    break;
                case TMAX:
                    tmax = Double.parseDouble(tokens[index.ordinal()]);
                    break;
                case TMIN:
                    tmin = Double.parseDouble(tokens[index.ordinal()]);
                    break;
                case AFDAYS:
                    afDays = Integer.parseInt(tokens[index.ordinal()]);
                    break;
                case RAINMM:
                    rainMM = Double.parseDouble(tokens[index.ordinal()]);
                    break;
                case SUNHOURS:
                    // remove all nonumeric symbols
                    tokens[index.ordinal()] = tokens[index.ordinal()].replaceAll("[^\\d.]", "");
                    sunHours = Double.parseDouble(tokens[index.ordinal()]);
                    break;

            }
        }
    }

    public String toString() {
        return year + "-" + month + " tmax:" + tmax + " tmin:" + tmin + " afD:" + afDays + " rMM:" + rainMM + " sH:" + sunHours;
    }
}
