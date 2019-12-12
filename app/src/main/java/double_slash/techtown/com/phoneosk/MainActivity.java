package double_slash.techtown.com.phoneosk;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import double_slash.techtown.com.Phoneosk.R;


public class MainActivity extends AppCompatActivity {

    public String storeID = null;
    String name = null;
    String address = null;
    String phone = null;
    String open = null;
    String close = null;
    ContentValues contentValues;

    String url="http://hycurium.cafe24.com/phoneosk/menu.jsp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentValues = new ContentValues();
        Button button = (Button) findViewById(R.id.btnstart);
        button.setOnClickListener(Click);

    }

    public void OnClickHandler (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("서비스에 등록하시고 싶은 점주님, 버그가 발생할 경우 문의 주세요!").setMessage("tnpdlzi@naver.com");
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnstart:
                    qr();

            }
        }
    };

    public void qr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(QR_Activity_Start.class);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            Log.d("in", "in");
            if (result.getContents() == null) {
                Log.d("null", "null");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Log.d("scanning", "scanning");
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                String input = result.getContents();
                storeID = input;
                contentValues.put("key", input);


                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(url, contentValues, getApplicationContext());
                httpAsyncTask.execute();

            }

        } else {
            Log.d("else", "else");
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }

    class HttpAsyncTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        private Context context;

        public HttpAsyncTask(String url, ContentValues values, Context context) {
            Log.d("data", values.toString());
            Log.d("url", url);

            this.url = url;
            this.values = values;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result; // 요청 결과를 저장할 변수.
            String stResult;
            RequestHttpUrlConnection requestHttpURLConnection = new RequestHttpUrlConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            Log.d("data", values.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("s", s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            ArrayList<String> MenuNameParsed = new ArrayList<>();
            ArrayList<String> MenuPriceParsed = new ArrayList<>();

            //  txt.setText(s);
            //Toast.makeText(context, MenuNameParsed.get(1), Toast.LENGTH_LONG).show();

            receiveArray(s, MenuNameParsed, MenuPriceParsed);

            Intent intent = new Intent(context, double_slash.techtown.com.phoneosk.MenuActivity.class);

            intent.putStringArrayListExtra("MenuNameParsed", MenuNameParsed);
            intent.putStringArrayListExtra("MenuPriceParsed", MenuPriceParsed);
            intent.putExtra("storeID", storeID);
            intent.putExtra("Check", "OK");
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("phone", phone);
            intent.putExtra("open", open);
            intent.putExtra("close", close);

           startActivity(intent);
           // context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }

        private void receiveArray(String dataObject, ArrayList MenuNameParsed, ArrayList MenuPriceParsed) {


            try {
                // String 으로 들어온 값 JSONObject 로 1차 파싱
                JSONObject wrapObject = new JSONObject(dataObject);
                // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
                JSONArray jsonArray = new JSONArray(wrapObject.getString("ITEMS"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Array 에서 하나의 JSONObject 를 추출
                    JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                    // 추출한 Object 에서 필요한 데이터를 표시할 방법을 정해서 화면에 표시
                    // 필자는 RecyclerView 로 데이터를 표시 함
                    //             mItems.add(new Item(dataJsonObject.getString("nation")+i,dataJsonObject.getString("name")+i,
                    //                     dataJsonObject.getString("address")+i,dataJsonObject.getString("age")));

                    MenuNameParsed.add(i, dataJsonObject.getString("menuName"));
                    MenuPriceParsed.add(i, dataJsonObject.getString("price"));
                }
                // Recycler Adapter 에서 데이터 변경 사항을 체크하라는 함수 호출
                //           adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
