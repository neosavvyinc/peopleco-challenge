import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.awt.datatransfer.Clipboard;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

public class ChallengeExample {

    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray retrieveBoards(String boardUrl) {

        String string = "";
        JSONArray boards = null;
        try {

            try {
                URL url = new URL(boardUrl);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    string += line + "\n";
                }
                System.out.println("\nREST Service Invoked Successfully.." + "\n" + string);

                boards = new JSONArray(string);

                in.close();
            } catch (Exception e) {
                System.out.println("\nError while calling REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return boards;
    }

    private static JSONObject retrieveBoard(JSONObject boardInput, String baseUrl) {

        JSONObject board = null;
        String response = "";

        try {

            try {
                URL url = new URL(baseUrl + "/" + boardInput.getString("board_id"));
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    response += line + "\n";
                }
                System.out.println("\nREST Service Invoked Successfully.." + "\n" + response);

                board = new JSONObject(response);

                in.close();
            } catch (Exception e) {
                System.out.println("\nError while calling REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return board;

    }

    private static JSONObject makeMove(JSONObject boardInput, String coordinate, String baseUrl) {

        JSONObject moveResult = null;
        String response = "";

        try {

            try {
                URL url = new URL(baseUrl + "/" + boardInput.getString("board_id") + "/" + coordinate);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    response += line + "\n";
                }
                System.out.println("\nREST Service Invoked Successfully.." + "\n" + response);

                moveResult = new JSONObject(response);

                in.close();
            } catch (Exception e) {
                System.out.println("\nError while calling REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return moveResult;
    }

    public static void main( String[] args ) {

        disableSslVerification();

        JSONArray boards = retrieveBoards("https://student.ubuntudev.people.co/api/challenge/battleship/04de00b15b33/boards");
        JSONObject board = retrieveBoard(boards.getJSONObject(0), "https://student.ubuntudev.people.co/api/challenge/battleship/04de00b15b33/boards/");

        String[] topAxis = {"A","B","C","D","E","F","G","H","I","J"};
        String[] leftAxis = {"1","2","3","4","5","6","7","8","9","10"};

        JSONObject lastMove = null;
        for (String topAxi : topAxis) {
            for (String leftAxi : leftAxis) {

                lastMove = makeMove(boards.getJSONObject(1), topAxi + leftAxi, "https://student.ubuntudev.people.co/api/challenge/battleship/04de00b15b33/boards");

                if( lastMove.getBoolean("is_finished") ) {
                    break;
                }
            }
            if( lastMove.getBoolean("is_finished") ) {
                break;
            }

        }


    }


}