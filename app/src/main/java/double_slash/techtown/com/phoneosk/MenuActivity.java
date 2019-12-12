package double_slash.techtown.com.phoneosk;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import double_slash.techtown.com.Phoneosk.R;

public class MenuActivity extends AppCompatActivity {

    ImageView imgBack;

    LinearLayout linCart;

    TextView tvStore;
    TextView tvPhoneNumber;
    TextView tvTime;
    TextView tvPosition;

    ListView listMenu;

    MenuAdapter adapter;

    ArrayList<String> MenuNameParsed = new ArrayList<>();
    ArrayList<String> MenuPriceParsed = new ArrayList<>();
    ArrayList<String> MenuCountParsed = new ArrayList<>();
    ArrayList<String> Temp = new ArrayList<>();

    ArrayList<MenuItem> Menus = new ArrayList<MenuItem>();

    public String storeID;
    String name = null;
    String address = null;
    String phone = null;
    String open = null;
    String close = null;
    String time;
    ContentValues contentValues;

    int ReceiveActivity =0;
//    int MenuNameParsedSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        linCart = (LinearLayout) findViewById(R.id.linCart);

        tvStore = (TextView) findViewById(R.id.tvStore);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPosition = (TextView) findViewById(R.id.tvPosition);

        Intent intent = this.getIntent();
        storeID = intent.getStringExtra("storeID");
        contentValues = new ContentValues();

        contentValues.put("key", storeID);

        HttpAsyncTask httpAsyncTask = new HttpAsyncTask("http://hycurium.cafe24.com/phoneosk/store.jsp", contentValues, getApplicationContext());
        httpAsyncTask.execute();



        MenuNameParsed = intent.getStringArrayListExtra("MenuNameParsed");
        MenuPriceParsed = intent.getStringArrayListExtra("MenuPriceParsed");
        for (int i=0 ; i<MenuNameParsed.size() ; i++) {
            MenuCountParsed.add(i, "0");
        }

        try {
            String Check = intent.getStringExtra("Check");
            if (Check.equals("OK")) {
                Temp = intent.getStringArrayListExtra("MenuCountParsed");
                for (int i =0; i<MenuNameParsed.size() ; i++) {
                    MenuCountParsed.set(i, Temp.get(i));
                }
                ReceiveActivity = 1;
            }
        } catch (Exception e) { }


//        Toast.makeText(this, MenuNameParsed.size() + "",Toast.LENGTH_LONG).show();

//        if(MenuNameParsed != null) {
//            MenuNameParsedSize = MenuNameParsed.size();
//        }
        listMenu = (ListView) findViewById(R.id.listMenu);
        adapter = new MenuAdapter();

//        adapter.readContact();

        adapter.readContactTest(MenuNameParsed, MenuPriceParsed);

        listMenu.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listMenu);

        imgBack.setOnClickListener(Click);
        linCart.setOnClickListener(Click);
    }

    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Menus.size();
        }

        @Override
        public Object getItem(int position) {
            return Menus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuView view = new MenuView(getApplicationContext());

            MenuItem item = Menus.get(position);
            view.setTvMenu(item.getMenu());
            view.setTvPrice(item.getPrice());
            view.setTvCount(item.getCount());
            return view;
        }

        public void addMenu(MenuItem view){
            Menus.add(view);
        }

        public void readContactTest(ArrayList MenuNameParsed, ArrayList MenuPriceParsed){
            for(int i = 0; i < MenuNameParsed.size(); i++){
                addMenu(new MenuItem(MenuNameParsed.get(i).toString(), MenuPriceParsed.get(i).toString(), MenuCountParsed.get(i).toString()));
            }
        }

    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgBack:
                    onBackPressed();
                    break;

                case R.id.linCart:
                    ArrayList<String> MenuName = new ArrayList<>();
                    ArrayList<String> MenuPrice = new ArrayList<>();
                    ArrayList<String> MenuCount = new ArrayList<>();

                    for (int i =0; i<adapter.getCount() ; i++) {
                        MenuName.add(Menus.get(i).getMenu());
                        MenuPrice.add(Menus.get(i).getPrice());
                        MenuCount.add(Menus.get(i).getCount());

                    }

                    Intent intent = new Intent(getApplicationContext(), MenuCartActivity.class);

                    intent.putStringArrayListExtra("MenuName", MenuName);
                    intent.putStringArrayListExtra("MenuPrice", MenuPrice);
                    intent.putStringArrayListExtra("MenuCount", MenuCount);
                    intent.putExtra("storeID", storeID);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);


                    startActivity(intent);
                    break;
            }
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount() ; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += 275;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    class MenuView extends LinearLayout {

        TextView tvMenu;
        TextView tvPrice;
        TextView tvCount;

        ImageView imgAdd;
        ImageView imgSub;

        int Count;
        String ViewMenuName;
        String ViewMenuPrice;

        public MenuView(Context context) {
            super(context);

            init(context);
        }

        public MenuView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.menuitem,this,true);

            Count = 0;

            imgAdd = (ImageView) findViewById(R.id.imgAdd);
            imgSub = (ImageView) findViewById(R.id.imgSub);
            tvCount = (TextView) findViewById(R.id.tvCount);

            Log.d("Receive", String.valueOf(ReceiveActivity));

            if (ReceiveActivity == 1) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    MenuItem item = (MenuItem) adapter.getItem(i);
                    int CartCount = Integer.parseInt(item.getCount());
                    if (CartCount != 0) {
                        imgAdd.setImageResource(R.drawable.plus);
                        imgSub.setImageResource(R.drawable.minus);
                        tvCount.setTextColor(Color.parseColor("#FF4B00"));
                        Count = CartCount;
                    }
                }
                ReceiveActivity = 0;
            }

            imgAdd.setOnClickListener(Click);
            imgSub.setOnClickListener(Click);

            tvMenu = findViewById(R.id.tvMenu);
            tvPrice = findViewById(R.id.tvPrice);
        }

        public void setTvMenu(String menu) {
            tvMenu.setText(menu);
        }

        public void setTvPrice(String price) {
            tvPrice.setText(price);
        }

        public void setTvCount(String count) {
            tvCount.setText(count);
        }

        OnClickListener Click = new OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.imgAdd:
                        Count++;
                        ViewMenuName = tvMenu.getText().toString();
                        ViewMenuPrice = tvPrice.getText().toString();
                        tvCount.setText(String.valueOf(Count));
                        tvCount.setTextColor(Color.parseColor("#FF4B00"));
                        imgAdd.setImageResource(R.drawable.plus);
                        imgSub.setImageResource(R.drawable.minus);
                        break;

                    case R.id.imgSub:
                        Count--;
                        ViewMenuName = tvMenu.getText().toString();
                        ViewMenuPrice = tvPrice.getText().toString();
                        tvCount.setText(String.valueOf(Count));
                        if (Count == 0) {
                            tvCount.setTextColor(Color.parseColor("#E3E3E3"));
                            imgSub.setImageResource(R.drawable.minus_g);
                            imgAdd.setImageResource(R.drawable.plus_g);
                        }
                        else if (Count <0) {
                            tvCount.setText("0");
                            Count =0;
                        }
                        break;
                }
                getCount();
            }
        };

        public void getCount() {
            for (int i =0; i<adapter.getCount() ; i++) {
                if (Menus.get(i).getMenu() == ViewMenuName) {
                    Menus.set(i, new MenuItem(ViewMenuName, ViewMenuPrice, String.valueOf(Count)));
                }
            }
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
            RequestHttpUrlConnection requestHttpURLConnection = new RequestHttpUrlConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            Log.d("STdata", values.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("STs", s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();


            //  txt.setText(s);
            //Toast.makeText(context, MenuNameParsed.get(1), Toast.LENGTH_LONG).show();

            receiveStArray(s);
            tvStore.setText(name);
            Log.d("name", name);

            tvPosition.setText(address);
            Log.d("address", address);

            tvPhoneNumber.setText(phone);
            Log.d("phone", phone);


            time = open +" ~ " + close;
            tvTime.setText(time);



            // context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }



        private void receiveStArray(String dataObject) {


            try {
                // String 으로 들어온 값 JSONObject 로 1차 파싱
                JSONObject wrapObject = new JSONObject(dataObject);
                // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
                name = wrapObject.getString("name");
                address = wrapObject.getString("address");
                phone = wrapObject.getString("phone");
                open = wrapObject.getString("open");
                close = wrapObject.getString("close");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
