package edu.csu2017fa314.T02.Model;
public class Location {
    private String ID;
    private String name;
    private String city;
    private String lat;
    private String lon;
    private int    altitude;

  public Location(String id, String Name, String City, String Lat, String Lon, int Elevation) {
      ID = id;
      name = Name;
      city = City;
      lat = Lat;
      lon = Lon;
      altitude = Elevation;
  }
  
  public String toString() { 
	    return "ID: '" + this.ID + "', Name: '" + this.name + "', City: '" + this.city + "'" +
	    		"Lat: '" + this.lat + "', Lon: '" + this.lon + "', Altitude: '" + this.altitude + "'";
	} 
}
