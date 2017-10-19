package edu.csu2017fa314.T02.Model;
public class Distance implements Comparable<Distance>{

  private Location startID;
  private Location endID;
  private int gcd;

  public Distance(Location startID, Location endID){
      this.startID = startID;
      this.endID = endID;
      this.gcd = computeGCD(startID, endID);
  }

  public Location getStartID(){
      return this.startID;
  }

  public Location getEndID(){
      return this.endID;
  }

  public int getGcd(){
      return this.gcd;
  }

  public int computeGCD(Location loc1, Location loc2) {

      double lat1 = loc1.getLatitude();
      double lon1 = loc1.getLongitude();
      double lat2 = loc2.getLatitude();
      double lon2 = loc2.getLongitude();

      double r = 3958.7613; //radius of earth in miles
      double phi1 = Math.toRadians(lat1);
      double lam1 = Math.toRadians(lon1);
      double phi2 = Math.toRadians(lat2);
      double lam2 = Math.toRadians(lon2);
      double dLam = Math.abs(lam1 - lam2);
      double y = Math.sqrt(Math.pow((Math.cos(phi2) * Math.sin(dLam)), 2) + Math.pow((Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(dLam)), 2));
      double x = (Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(dLam));
      double dTheta = Math.atan2(y, x);
      double dist = dTheta * r;
      int gcd = (int) Math.round(dist);
      return gcd;
  }

  @Override
  public boolean equals(Object o){
      if(!(o instanceof Distance)){
          return false;
      }

      Distance other = (Distance) o;
      String startName = startID.getName();
      String endName = endID.getName();
      String otherStartName = other.startID.getName();
      String otherEndName = other.endID.getName();

      if(startName.equals(otherStartName) && endName.equals(otherEndName)){
          return true;
      }
      else return false;
  }

    @Override
    public int compareTo(Distance other) {
        return this.gcd - other.gcd;
    }

    public String toString(){
      return ("StartID: " + startID.toString() + ", EndID: " + endID.toString() + ", GCD: " + gcd);
    }

    public boolean equals(Distance d) {
        if (this.startID.equals(d.startID) && (this.endID.equals(d.endID)) && (this.gcd == d.gcd)) { return true; }
        else { return false; }
    }

}
