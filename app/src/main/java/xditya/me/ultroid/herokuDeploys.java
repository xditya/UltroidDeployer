package xditya.me.ultroid;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class herokuDeploys {

    public static class DeployLink  {
        static String getDeployLink() throws IOException {
            String base_url = "https://deploy.ultroid.tech";
            URL url = new URL(base_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println( "orignal url: " + conn.getURL() );
            conn.connect();
            System.out.println( "connected url: " + conn.getURL() );
            InputStream is = conn.getInputStream();
            System.out.println( "redirected url: " + conn.getURL() );
            URL redirect = conn.getURL();
            is.close();
            return redirect.toString();
        }
    }

    public static class Requests {
        static String getRequest(String base_url, String herokuapi, int apiid, String apihash, String session, String redisuri, String redispass) throws IOException {
            String results = "";

            byte[] json = ("{" +
                    "\"source_blob\": {\"url\": \"" + base_url + "\"}" +
                    "\"overrides\": {\"env\": {" +
                    "\"API_ID\": \"" + apiid + "\"" +
                    "\"API_HASH\": \"" + apihash + "\"" +
                    "\"SESSION\": \"" + session + "\"" +
                    "\"REDIS_URI\": \"" + redisuri + "\"" +
                    "\"REDIS_PASSWORD\": \"" + redispass + "\"" +
                    "} } }").getBytes(StandardCharsets.UTF_8);

            URL url = new URL(base_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(json.length);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/vnd.heroku+json; version=3");
            conn.setRequestProperty("Authorization", "Bearer " + herokuapi);

            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                return "API is down or unreachable at the moment!";
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    results += sc.nextLine();
                }
                sc.close();
            }
            return results;
        }
    }

    public static String herokuDeployer(TextView tvTemp, int apiid, String apihash, String session, String redisuri, String redispass, String herokuapi) throws IOException {
        tvTemp.setText("Connecting to heroku...");

        // api call to deploy app
        // #TODO

        String herokuDeployableRepo = "https://github.com/tornals/enframe-alkine/tarball/master";      // #TODO fetch from deploy.ultroid.tech
        final String[] response = new String[1];
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = Requests.getRequest(herokuDeployableRepo, herokuapi, apiid, apihash, session, redisuri, redispass);
                    System.out.println(response[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    response[0] = "ERROR";
                }
                System.out.println("response[0] in fn: " + response[0]);
            }
        });
        System.out.println("response[0] out of fn: " + response[0]);
        return response[0];
    }
}
