import jdk.internal.util.xml.impl.Input;
import java.io.*;
import edu.csu2017cs314.T02;
import edu.csu2017cs314.T02.Model.Distance;
import edu.csu2017cs314.T02.Model.Location;
import edu.csu2017cs314.T02.Model.Hub;
import java.util.ArrayList;
import java.util.LinkedList


public class drawSVG {
    public static void main(String args[]) {
        //create printWriter to CoMapTripCo svg
        File file = new File("CoMapTripCo.svg");
        file.getParentFile().mkdirs();
        PrintWriter pw = new PrintWriter(file);

        //copy COmap svg into CoMapTripCo svg (dont read last two line [</g> </svg>])
        int stripLines = 2;
        LinkedList<String> ll = new LinkedList<String> ();
        try(BufferedReader br = new BufferedReader(new FileReader(COmap))){
            String line;
            while ((line = br.readLine()) != null){
                ll.addLast(line);
                if (ll.size () > stripLines){
                    pw.println(lli.removeFirst());
                }
            }
        }

        //draw borders
        pw.println("  <rect fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x=\"-5\" y=\"-5\" width=\"1076.6073\" height=\"793.0824\" id=\"svg_2\"/>");

        //draw lines from start to end locations
        double originStartLat = 0.0;
        double originStartLon = 0.0;
        double finalEndLat = 0.0;
        double finalEndLon = 0.0;
        boolean first = false;
        for(Distance d : shortestItinerary){
            double unitHeight = 195.7706; //COmap height/4
            double unitWidth = 152.3724714; //COmap width/7
            if(!first){
                originStartLat = d.getStartID().getLatitude();
                originStartLon = d.getStartID().getLongitude();
                first = true;
            }
            double startLat = d.getStartID().getLatitude();
            double startLon = d.getStartID().getLongitude();
            double endLat = d.getEndID().getLatitude();
            double endLon = d.getEndID().getLongitude();

            finalEndLat = d.getEndID().getLatitude();
            finalEndLon = d.getEndID().getLongitude();

            double x1 = ((startLon + 109) * unitHeight) + 38;
            double y1 = ((startLat - 41) * unitWidth) + 38;
            double x2 = ((endLon + 109) * unitHeight) + 38;
            double y2 = ((endLat - 41) * unitWidth) + 38;
            pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" id=\"svg_1\"/>");
        }

        //draw last line connected end point with start
        double endX1 = ((finalEndLon + 109) * unitHeight) + 38;
        double endY1 = ((finalEndLat - 41) * unitWidth) + 38;
        double endX2 = ((originStartLon + 109) * unitHeight) + 38;
        double endY2 = ((originStartLat - 41) * unitWidth) + 38;

        pw.println("  <line fill=\"none\" stroke=\"#0000ff\" stroke-width=\"3\" stroke-dasharray=\"null\" stroke-linejoin=\"null\" stroke-linecap=\"null\" x1=\"" + endX1 + "\" y1=\"" + endY1 + "\" x2=\"" + endX2 + "\" y2=\"" + endY2 + "\" id=\"svg_1\"/>");

        pw.println(" </g>\n" + "</svg>");

        pw.close();
    }




}
