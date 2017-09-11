
package edu.csu2017fa314.T02.Model;
public class Location {
    private String ID;
    private double lat;
    private double lon;

  public Location(String id, double Lat, double Lon) {
      this.ID = id;
      this.lat = Lat;
      this.lon = Lon;
  }
  
  public String getID() {
	  return this.ID;
  }
  
  public double getLatitude() {
	  return this.lat;
  }
  
  public double getLongitude() {
	  return this.lon;
  }
  
  public String toString() { 
	    return "ID: '" + this.ID + "', Latitude: '" + this.lat + "', Longitude: '" + this.lon;
	} 
}
