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
//REDIRECT URL
public static URL getFinalURL(URL url) {
    try {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        con.addRequestProperty("Referer", "https://www.google.com/");
        con.connect();
        //con.getInputStream();
        int resCode = con.getResponseCode();
        if (resCode == HttpURLConnection.HTTP_SEE_OTHER
                || resCode == HttpURLConnection.HTTP_MOVED_PERM
                || resCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String Location = con.getHeaderField("Location");
            if (Location.startsWith("/")) {
                Location = url.getProtocol() + "://" + url.getHost() + Location;
            }
            return getFinalURL(new URL(Location));
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return url;
}
}
