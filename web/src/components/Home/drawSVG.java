import jdk.internal.util.xml.impl.Input;
import java.io.*;

public class drawSVG {
    public static void main(String args[]) {
        //create printWriter to CoMapTripCo svg
        File file = new File("CoMapTripCo.svg");
        file.getParentFile().mkdirs();
        PrintWriter pw = new PrintWriter(file);
        //copy COmap svg into CoMapTripCo svg
        try(BufferedReader br = new BufferedReader(new FileReader("COmap.svg"))){
            String line;
            while ((line = br.readLine()) != null){
                pw.println(line);
            }
        }
        for(l : finalLocations){ //needs to be adjusted to loop through and get each start --> end pair
            unitHeight = 195.7706; //COmap height/4
            unitWidth = 152.3724714; //COmap width/7
            startLat = l.getLat();
            startLon = l.getLon();
            endLat = l.getLat();
            endLon = l.getLon();
            x1 = (startLon + 109) * unitHeight) + 38;
            y1 = (startLat - 41) * unitWidth) + 38;
            x2 = (endLon + 109) * unitHeight) + 38;
            y2 = (endLat - 41) * unitWidth) + 38;
            pw.println("<svg width=\"1066.6073\" height=\"783.0824000000003\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                    " <g>\n" +
                    "  <title>Layer 1</title>\n" +
                    "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>\n" +
                    " </g>\n" +
                    "</svg>");
        }


    }




}
