package edu.csu2017fa314.T02.Model;
public class Location {
    private String ID;
    private String name;
    private String city;
    private String lat;
    private String lon;
    private int    elevation;

  public Location(String id, String Name, String City, String Lat, String Lon, int Elevation) {
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
  
  public String getLatitude() {
	  return this.lat;
  }
  
  public String getLongitude() {
	  return this.lon;
  }
  
  public String toString() { 
	    return "ID: '" + this.ID + "', Name: '" + this.name + "', City: '" + this.city + "'" +
	    		"Lat: '" + this.lat + "', Lon: '" + this.lon + "', Elevation: '" + this.elevation + "'";
	} 
}
