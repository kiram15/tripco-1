package edu.csu2017fa314.T02.Server;

public class ServerSvgResponse {
    private String response = "svg";
    private String contents;
    private int width;
    private int height;

    public ServerSvgResponse(int width, int height, String contents) {
        this.contents = contents;
        this.width = width;
        this.height = height;
        //System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "response='" + response + '\'' +
                ", contents=" + contents +
                '}';
    }
}
