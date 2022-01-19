package xditya.me.ultroid;

import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class herokuDeploys {
    public static String herokuDeployer(TextView tvTemp, int apiid, String apihash, String session, String redisuri, String redispass, String herokuapi) throws IOException {
        tvTemp.setText("Connecting to heroku...");

        // this isnt working.


        // #TODO fix this


        String base_url = "https://api.heroku.com/app-setups", results = "";

        URL url = new URL(base_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", ":application/json");
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
            return results;
        }
    }
}
