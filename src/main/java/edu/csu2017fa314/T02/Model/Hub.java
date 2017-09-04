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
    ArrayList<Location> finalLocations = new ArrayList<>();
    static ArrayList<Location> kLocations = new ArrayList<>();

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
                        case "altitude":
                            columns.put("altitude", i);
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
                int objectAltitude = Integer.parseInt(newArray[columns.get("altitude")].trim());

                Location location = new Location(objectID, objectName, objectCity, objectLatitude,
                        objectLongitude, objectAltitude);

                finalLocations.add(location);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finalLocations;
    }
}
