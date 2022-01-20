package xditya.me.ultroid;

import android.os.AsyncTask;s
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class herokuDeploys {

    public static class Requests {
        static String getRequest(String base_url) throws IOException {
            String results = "";
            URL url = new URL(base_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         null   conn.setRequestMethod("GET");
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

        final String[] response = new String[1];

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = Requests.getRequest("https://apis.xditya.me/morse/encode?text=hello");
                } catch (Exception e) {
                    e.printStackTrace();
                    response[0] = "ERROR:\n" + e.toString();
                }
                tvTemp.setText(response[0]);

                // this f*king thing works atlast
            }
        });

    }
}
