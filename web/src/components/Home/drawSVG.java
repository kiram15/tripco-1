import jdk.internal.util.xml.impl.Input;

import java.io.*;

public class drawSVG{
    FileInputStream fStream = new FileInputStream("COmap.svg");
    String nextLine;
    nextLine = fStream;
    while(nextLine != null){
        
    }

    File file = new File("CoMapTripCo.svg");
    file.getParentFile().mkdirs();
    PrintWriter pw = new PrintWriter(file);
    pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

}
