package edu.csu2017fa314.T02.Model;
public class Distance{
  
  private String startID;
  private String endID;
  private int gcd;

  public Distance(String startID, String endID, int gcd){
      this.startID = startID;
      this.endID = endID;
      this.gcd = gcd;
  }

  public String getStartID(){ 
      return this.startID; 
  }
  
  public String getEndID(){ 
      return this.endID; 
  }
  
  public int getGcd(){ 
      return this.gcd; 
  }

}
