package edu.csu2017fa314.T02.Model;
public class Location {
    private String ID;
    private String name;
    private String city;
    private double lat;
    private double lon;
    private int    elevation;

  public Location(String id, String Name, String City, double Lat, double Lon, int Elevation) {
      this.ID = id;
      this.name = Name;
      this.city = City;
      this.lat = Lat;
      this.lon = Lon;
      this.elevation = Elevation;
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
	    return "ID: '" + this.ID + "', Name: '" + this.name + "', City: '" + this.city + "'" +
	    		"Latitude: '" + this.lat + "', Longitude: '" + this.lon + "', Elevation: '" + this.elevation + "'";
	} 
}
