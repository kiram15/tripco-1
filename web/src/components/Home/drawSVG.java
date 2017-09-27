import jdk.internal.util.xml.impl.Input;
import java.io.*;
import edu.csu2017cs314.T02;
import edu.csu2017cs314.T02.Model.Distance;
import edu.csu2017cs314.T02.Model.Location;
import edu.csu2017cs314.T02.Model.Hub;
import java.util.ArrayList;


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
        //draw borders
        pw.println("<svg width=\"1066.6073\" height=\"783.0824000000003\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <rect id=\"svg_1\" height=\"793.0824\" width=\"1076.6073\" y=\"265.9176\" x=\"235.3927\" stroke-linecap=\"null\" stroke-linejoin=\"null\" stroke-dasharray=\"null\" stroke-width=\"3\" stroke=\"#000000\" fill=\"none\"/>\n" +
                " </g>\n" +
                "</svg>");

        double originStart = 0.0;
        double finalEnd = 0.0;
        bool first = false;
        //draw lines from start to end locations
        for(d : shortestItinerary){ //needs to be adjusted to loop through and get each start --> end pair
            unitHeight = 195.7706; //COmap height/4
            unitWidth = 152.3724714; //COmap width/7
            if(!first){
                originStart = d.getStartID().getLatitude();
                first = true;
            }
            startLat = d.getStartID().getLatitude();
            startLon = d.getStartID().getLongitude();
            endLat = d.getEndID().getLatitude();
            endLon = d.getEndID().getLongitude();

            finalEnd = d.getEndID().getLongitude();
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

        //draw last line connected end point with start
        endX1 = (finalEnd + 109) * unitHeight) + 38;
        endY1 = (finalEnd - 41) * unitWidth) + 38;
        endX2 = (originStart + 109) * unitHeight) + 38;
        endY2 = (originStart - 41) * unitWidth) + 38;

        pw.println("<svg width=\"1066.6073\" height=\"783.0824000000003\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>\n" +
                " </g>\n" +
                "</svg>");


    }




}
