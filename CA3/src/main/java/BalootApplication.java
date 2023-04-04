import Presentation.Server.InterfaceServer;
public class BalootApplication {
    public static void main(String args[]) throws Exception {
        final String USERS_URI = "http://5.253.25.110:5000/api/users";
        final String COMMODITIES_URI = "http://5.253.25.110:5000/api/commodities";
        final String PROVIDERS_URI = "http://5.253.25.110:5000/api/providers";
        final String COMMENTS_URI = "http://5.253.25.110:5000/api/comments";

        final int PORT = 5000;
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.start(USERS_URI, COMMODITIES_URI, PROVIDERS_URI, COMMENTS_URI, PORT);
    }
}
