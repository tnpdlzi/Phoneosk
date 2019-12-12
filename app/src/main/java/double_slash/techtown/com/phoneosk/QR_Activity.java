package double_slash.techtown.com.phoneosk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

}
