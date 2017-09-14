package edu.csu2017fa314.T02.Model;
public class Distance{

  private String startName;
  private String endName;
  private int gcd;

  public Distance(String startName, String endName, int gcd){
      this.startName = startName;
      this.endName = endName;
      this.gcd = gcd;
  }

  public String getStartName(){
      return this.startName;
  }

  public String getEndName(){
      return this.endName;
  }

  public int getGcd(){
      return this.gcd;
  }

}
