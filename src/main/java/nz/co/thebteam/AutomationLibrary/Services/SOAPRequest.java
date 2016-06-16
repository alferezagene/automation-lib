package nz.co.thebteam.AutomationLibrary.Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class SOAPRequest {

    private String URL;
    private String[] properties;
    private String requestType;
    private String XMLRequest;
    private int responseCode;
    private String authentication = "";
    private String username;
    private String password;
    private String cookie = "";
    private Map<String, List<String>> headers;
    private HttpURLConnection conn;

    public SOAPRequest(String URL, String JSONRequest, String[] properties, String requestType) {
        this.URL = URL;
        this.XMLRequest = JSONRequest;
        this.requestType = requestType;
        this.properties = properties;
    }

    public SOAPRequest(String URL, String[] properties, String requestType) {
        this.URL = URL;
        this.requestType = requestType;
        this.properties = properties;
    }



    public void setAuthentication(String type, String username, String password) {
        this.authentication = type;
        this.username = username;
        this.password = password;
    }


    public String sendRequest() {
        String outputXML = "";
        try {

            java.net.URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestType);

            for (String property : properties) {
                String[] splitProperties = property.split(":");
                conn.setRequestProperty(splitProperties[0], splitProperties[1]);
            }

            //checks for authentication settings
            if (authentication.equals("Basic")) {

                String userpass = username + ":" + password;
                String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

                conn.setRequestProperty("Authorization", basicAuth);
            } else if (authentication.equals("Bearer")) {
                conn.setRequestProperty("Authorization", "Bearer " + username);
            }

            //we only do an outputstream if we are posting otherwise we get 404
            if ((requestType.equals("POST")) || (requestType.equals("PUT"))) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();

                if (XMLRequest != null) {
                    os.write(XMLRequest.getBytes());
                } else {
                    os.write(0);
                }
                os.flush();
            } else {
                conn.setDoOutput(false);
            }

            //this does the actual request
            this.responseCode = conn.getResponseCode();

            //get cookies in response
            this.cookie = conn.getHeaderField("Set-Cookie");

            this.headers = conn.getHeaderFields();

            BufferedReader in = null;

            if ((conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) && (conn.getResponseCode() != HttpURLConnection.HTTP_OK) && (conn.getResponseCode() != 302)) {

                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode() + " " + conn.getResponseMessage() + " " + conn.getContent());
            } else if (conn.getHeaderField("Content-Encoding") != null && conn.getHeaderField("Content-Encoding").contains("gzip")) {
                in = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            String output;
            System.out.println("Output from Server ....");
            while ((output = in.readLine()) != null) {
                System.out.println(output);
                outputXML += output;
            }
            System.out.println("\r\n\r\n");
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("\r\nResponse code: " + responseCode);
            System.out.println(e.getMessage());


        }

        return outputXML;
    }


    public int getResponseCode() {
        return responseCode;
    }

    public String getCookie() {
        return this.cookie;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public String getHeader(String header) {
        return conn.getHeaderField(header);
    }


}
