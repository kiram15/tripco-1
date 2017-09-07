package edu.csu2017fa314.T02.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


public class Hub
{
    String[] infoArray;
    Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
    ArrayList<Location> finalLocations = new ArrayList<Location>();
    static ArrayList<Location> kLocations = new ArrayList<Location>();

    public ArrayList<Location> readFile(String fileName) {
        File file = new File(fileName);
        Scanner scnr;
        try {
            scnr = new Scanner(file);
            if (scnr.hasNextLine()) {
                String s = scnr.nextLine();
                s = s.toLowerCase();
                String [] infoArray = s.split(",");

                for (int i = 0; i < infoArray.length; i++) {
                    String infoString = infoArray[i];
                    switch (infoString.trim()) { // associating column titles with column num, putting it in map
                        case "id":
                            columns.put("id", i);
                            break;
                        case "name":
                            columns.put("name", i);
                            break;
                        case "city":
                            columns.put("city", i);
                            break;
                        case "latitude":
                            columns.put("latitude", i);
                            break;
                        case "longitude":
                            columns.put("longitude", i);
                            break;
                        case "elevation":
                            columns.put("elevation", i);
                            break;
                    }
                }
            }
            while (scnr.hasNextLine()) {
                String brewery = scnr.nextLine();
                brewery = brewery.toLowerCase();
                String[] newArray = brewery.split(","); //column names

                String objectID = newArray[columns.get("id")].trim();
                String objectName = newArray[columns.get("name")].trim();
                String objectCity = newArray[columns.get("city")].trim();
                String objectLatitude = newArray[columns.get("latitude")].trim();
                String objectLongitude = newArray[columns.get("longitude")].trim();
                int objectElevation = Integer.parseInt(newArray[columns.get("elevation")].trim());
                
                double doubleLat = latLonConvert(objectLatitude);
                double doubleLon = latLonConvert(objectLongitude);

                Location location = new Location(objectID, objectName, objectCity, doubleLat,
                        doubleLon, objectElevation);

                finalLocations.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finalLocations;
    }
    
    public double latLonConvert(String s) {
		String sCopy = s;
		int end; String symbol;
		double retVal;
		ArrayList<Double> values = new ArrayList<>();
		
		//in the loops, uses symbol as stopping point, extracts number, adds to arrayList, 
		//and cuts off remaining, then uses the formula to convert into degrees

		if (s.contains("\"") && s.contains("'")) { // case for 106°49'43.24" W										
			for (int i = 0; i < 3; i++) {
				if (i == 0)      { symbol = "°"; }
				else if (i == 1) { symbol = "'"; }
				else             { symbol = "\""; }
				end = sCopy.indexOf(symbol);
				values.add(Double.parseDouble(sCopy.substring(0, end)));
				sCopy = sCopy.substring(end + 1);
			}
			retVal = (values.get(0) + (values.get(1) / 60) + (values.get(2) / 3600));	
			if (sCopy.equals("W") || sCopy.equals("S")) {
				return (retVal*(-1)); 
			}
			else {
				return retVal;
			}
		} 
		else if (s.contains("'")) { // case for 106°49.24' W format
			for (int i = 0; i < 2; i++) {
				if (i == 0) { symbol = "°"; }
				else        { symbol = "'"; }
				end = sCopy.indexOf(symbol);
				values.add(Double.parseDouble(sCopy.substring(0, end)));
				sCopy = sCopy.substring(end + 1);
			}
			retVal = (values.get(0) + (values.get(1) / 60));
			if (sCopy.equals("W") || sCopy.equals("S")) {
				return (retVal*(-1)); 
			}
			else {
				return retVal;
			}
			
		} 
		else if (s.contains("°")) { // case for 106.24° format
			end = sCopy.indexOf("°");
			return (Double.parseDouble(sCopy.substring(0, end)));
		} 
		else { // case for -106.24 format
			return Double.parseDouble(s);
		}
	}
}
