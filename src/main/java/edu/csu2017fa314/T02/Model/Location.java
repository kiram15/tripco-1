
package edu.csu2017fa314.T02.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Location {
    private String name;
    private double lat;
    private double lon;
    private LinkedHashMap<String, String> info;

    /**creates a location object that consists of a latitude, longitude,
    *name, and map of extra info require
    */
  public Location(String name, double lat, double lon, LinkedHashMap<String, String> info) {
      this.name = name;
      this.lat = lat;
      this.lon = lon;
      if(info != null){
          info.put("Latitude", Double.toString(lat));
          info.put("Longitude", Double.toString(lon));
      }
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

  public void setLon(double lon){
      this.lon = lon;
  }

  public void setLat(double lat){
      this.lat = lat;
  }

  /** Distance object toString method
   * @return the string containing the distance information
   */
  public String toString() {
	    return "Name: '" + this.name
                + "', Latitude: '"
                + this.lat + "', Longitude: '"
                + this.lon;
  }

  @Override
  public boolean equals(Object object) {
        if(object instanceof Location
                && ((Location)object).getName().equals(this.name)
                && ((Location)object).getLatitude() == (this.lat)
                && ((Location)object).getLongitude() == (this.lon)) {
                    return true;
        } else {
            return false;
        }
    }

}
