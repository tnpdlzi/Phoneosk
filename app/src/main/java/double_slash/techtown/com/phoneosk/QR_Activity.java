package double_slash.techtown.com.phoneosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import double_slash.techtown.com.Phoneosk.R;

public class QR_Activity extends AppCompatActivity {

    ImageView btnBack_QR;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference fbRef = database.getReference("message");
    public Bundle savedBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_);
        savedBundle = savedInstanceState;
        btnBack_QR = (ImageView)findViewById(R.id.btnBack_QR);

        btnBack_QR.setOnClickListener(Click);

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.scanner_QR);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnBack_QR:
                    onBackPressed();
                    break;
            }
        }
    };

    public void onResume(){
        super.onResume();
        capture.onResume();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==IntentIntegrator.REQUEST_CODE) { // scan from ZXing
//            String storeID=null;
//            String vin = null;
//            boolean success=false;
//
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if(result!=null)
//            {
//                String content = result.getContents();
//                if(content!=null)
//                {
//                    storeID=content;
//                    vin=storeID;
//                    success=true;
//                    fbRef.setValue(vin);
//                    Log.d("Ref", vin);
//                }
//            }
//        }
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if(result != null) {
//            Log.d("in","in");
//            if(result.getContents() == null) {
//                Log.d("null", "null");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//
//            } else {
//                Log.d("scanning", "scanning");
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                Intent intent1 = new Intent(this, FinalActivity.class);
//                startActivity(intent1, savedBundle);
//            }
//        } else {
//            Log.d("else", "else");
//            super.onActivityResult(requestCode, resultCode, intent);
//        }
//
//
//    }

}
