package xditya.me.ultroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class mainApp extends AppCompatActivity {
    EditText edtAPIID, edtAPIHASH, edtSESSION, edtREDISURI, edtREDISPASSWORD, edtHEROKUAPI;
    Button btnProceed;
    TextView tvTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        edtAPIID = findViewById(R.id.edtAPIID);
        edtAPIHASH = findViewById(R.id.edtAPIHASH);
        edtSESSION = findViewById(R.id.edtSESSION);
        edtREDISURI = findViewById(R.id.edtREDISURI);
        edtREDISPASSWORD = findViewById(R.id.edtREDISPASSWORD);
        edtHEROKUAPI = findViewById(R.id.edtHEROKUAPI);
        tvTemp = findViewById(R.id.tvTemp);
        btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_apiid, apihash, session, redisuri, redispass, herokuapi;
                str_apiid = edtAPIID.getText().toString();
                apihash = edtAPIHASH.getText().toString();
                session = edtSESSION.getText().toString();
                redisuri = edtREDISURI.getText().toString();
                redispass = edtREDISPASSWORD.getText().toString();
                herokuapi = edtHEROKUAPI.getText().toString();

                System.out.println(edtAPIID);
                //check if all exist
                if (str_apiid.isEmpty() || apihash.isEmpty() || session.isEmpty() || redisuri.isEmpty() || redispass.isEmpty() || herokuapi.isEmpty()) {
                    Toast.makeText(mainApp.this, "Fill up all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if int
                int apiid;
                try {
                    apiid = Integer.parseInt(str_apiid);
                }
                catch (NumberFormatException ex) {
                    Toast.makeText(mainApp.this, "Invalid API ID provided!", Toast.LENGTH_SHORT).show();
                    return;
                }

                btnProceed.setText("Deploying...");
                String hkDep = null, error = null;
                try {
                    hkDep = herokuDeploys.herokuDeployer(tvTemp, apiid, apihash, session,redisuri, redispass, herokuapi);
                } catch (IOException e) {
                    error = e.toString();
                }
                if (hkDep == null) {
                    tvTemp.setText("ERROR\n"+error);
                }
                tvTemp.setText(hkDep);
                System.out.println(hkDep);
            }
        });

    }
}