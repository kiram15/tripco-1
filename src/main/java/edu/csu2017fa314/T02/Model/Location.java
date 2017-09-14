
package edu.csu2017fa314.T02.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Location {
    private String name;
    private double lat;
    private double lon;
    private LinkedHashMap<String, String> info;

  public Location(String name, double Lat, double Lon, LinkedHashMap<String, String> info) {
      this.name = name;
      this.lat = Lat;
      this.lon = Lon;
      this.info = info;
  }

  public String getName() {
	  return this.name;
  }

  public double getLatitude() {
	  return this.lat;
  }

  public double getLongitude() {
	  return this.lon;
  }

  public String toString() {
	    return "Name: '" + this.name + "', Latitude: '" + this.lat + "', Longitude: '" + this.lon;
	}
}
