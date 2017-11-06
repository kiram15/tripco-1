
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
      if(info != null){
          info.put("Latitude", Double.toString(Lat));
          info.put("Longitude", Double.toString(Lon));
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

//  public LinkedHashMap<String, String> getInfoMap(){
//      return this.info;
//  }

//  //returns value associated with key in the info Map
//  public String getOtherInfo(String key){
//      return this.info.get(key);
//  }

  public void setLon(double lon){
      this.lon = lon;
  }

  public void setLat(double lat){
      this.lat = lat;
  }

  public String toString() {
	    return "Name: '" + this.name + "', Latitude: '" + this.lat + "', Longitude: '" + this.lon;
  }

    public boolean equals(Object object) {
        if(object instanceof Location &&
                ((Location)object).getName().equals(this.name) && ((Location)object).getLatitude() == (this.lat) &&
                ((Location)object).getLongitude() == (this.lon)) {
            return true;
        } else {
            return false;
        }
    }


//    public boolean equals(Location l) {
//      if (this.name.equals(l.name) && (this.lat == l.lat) && (this.lon == l.lon)) { return true; }
//      else { return false; }
//  }


}
