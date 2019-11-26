package double_slash.techtown.com.phoneosk;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;



import double_slash.techtown.com.Phoneosk.R;

public class QR_Activity_Start extends AppCompatActivity {

    ImageView btnBack_QR;
//        public String storeID;
//        ContentValues contentValues;
//        String url = "http://hycurium.cafe24.com/minimenuProto/jsontest.jsp";


    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    //   public Bundle savedBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr___start);

        btnBack_QR = (ImageView) findViewById(R.id.btnBack1_QR);

        btnBack_QR.setOnClickListener(Click);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.scanner1_QR);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();


    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack_QR:
                    onBackPressed();
                    break;
            }
        }
    };

    public void onResume() {
        super.onResume();
        capture.onResume();

    }



}
