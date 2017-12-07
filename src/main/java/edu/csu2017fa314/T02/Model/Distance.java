package edu.csu2017fa314.T02.Model;
public class Distance
        implements Comparable<Distance>{

  private Location startId;
  private Location endId;
  private int gcd;

  /** creates a distance object that stores two locations
  * and calculates the distance between them
  */
  public Distance(Location startId, Location endId, boolean miles){
      this.startId = startId;
      this.endId = endId;
      this.gcd = computeGcd(startId, endId, miles);
  }

  public Location getStartId(){
      return this.startId;
  }

  public Location getEndId(){
      return this.endId;
  }

  public int getGcd(){
      return this.gcd;
  }

  /**
   * computes the distance between the two locations in the distance object
   */
  public int computeGcd(Location loc1, Location loc2, boolean miles) {

      double lat1 = loc1.getLatitude();
      double lon1 = loc1.getLongitude();
      double lat2 = loc2.getLatitude();
      double lon2 = loc2.getLongitude();

      double r0 = 0.0;
      if(miles){
        r0 = 3958.7613; //radius of earth in miles
      }else{ r0 = 6371.0088; } //radius of earth in km
      double phi1 = Math.toRadians(lat1);
      double lam1 = Math.toRadians(lon1);
      double phi2 = Math.toRadians(lat2);
      double lam2 = Math.toRadians(lon2);
      double dlam = Math.abs(lam1 - lam2);
      double y0 = Math.sqrt(Math.pow((Math.cos(phi2) * Math.sin(dlam)), 2)
              + Math.pow((Math.cos(phi1) * Math.sin(phi2)
              - Math.sin(phi1) * Math.cos(phi2) * Math.cos(dlam)), 2));
      double x0 =
              (Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(dlam));
      double dtheta = Math.atan2(y0, x0);
      double dist = dtheta * r0;
      int gcd = (int) Math.round(dist);
      return gcd;
  }

  @Override
  public boolean equals(Object obj){
      if(!(obj instanceof Distance)){
          return false;
      }

      Distance other = (Distance) obj;
      String startName = startId.getName();
      String endName = endId.getName();
      String otherStartName = other.startId.getName();
      String otherEndName = other.endId.getName();

      if(startName.equals(otherStartName) && endName.equals(otherEndName)){
          return true;
      }
      else {
          return false;
      }
  }

    @Override
    public int compareTo(Distance other) {
        return this.gcd - other.gcd;
    }

    /** Distance object toString method
     * @return the string containing the distance information
     */
    public String toString(){
        return "Distance{"
                + "StartId= '" + startId.toString() + '\''
                + ", EndId= '" + endId.toString() + '\''
                + ", GCD= '" + gcd + '}';
    }
}
