package edu.csu2017fa314.T02.Model;
public class Distance implements Comparable<Distance>{
  
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

}