package edu.csu2017fa314.T02.Model;
public class Distance{
  
  private Location startID;
  private Location endID;
  private int gcd;

  public Distance(Location startID, Location endID, int gcd){
      this.startID = startID;
      this.endID = endID;
      this.gcd = gcd;
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

}