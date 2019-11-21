package double_slash.techtown.com.phoneosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import double_slash.techtown.com.Phoneosk.R;

public class MenuCartActivity extends AppCompatActivity {

    ImageView imgBack_Cart;

    ListView listSelect_Cart;
    MenuSelectedAdapter adapter;

    TextView tvAllPrice_Cart;
    TextView tvAllCount_Cart;

    EditText edtRequest_Cart;

    LinearLayout linMenuAdd_Cart;
    LinearLayout linCart_Cart;

    ArrayList<String> MenuName = new ArrayList<>();
    ArrayList<String> MenuPrice = new ArrayList<>();
    ArrayList<String> MenuCount = new ArrayList<>();
    ArrayList<String> nMenuPrice = new ArrayList<>();

    ArrayList<MenuItem> SelectedMenus = new ArrayList<MenuItem>();

    int AllPrice = 0;
    int CountSum = 0;
    String strAllPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cart);

        tvAllPrice_Cart = (TextView) findViewById(R.id.tvAllPrice_Cart);
        tvAllCount_Cart = (TextView) findViewById(R.id.tvAllCount_Cart);
        edtRequest_Cart = (EditText)findViewById(R.id.edtRequest_Cart);
        linMenuAdd_Cart = (LinearLayout) findViewById(R.id.linMenuAdd_Cart);
        linCart_Cart = (LinearLayout) findViewById(R.id.linCart_Cart);

        Intent intent = new Intent(this.getIntent());

        MenuName = intent.getStringArrayListExtra("MenuName");
        MenuPrice = intent.getStringArrayListExtra("MenuPrice");
        MenuCount = intent.getStringArrayListExtra("MenuCount");

        SumPrice();

        tvAllPrice_Cart.setText(new StringBuffer(strAllPrice).reverse().toString() + "원");

        for (int i =0 ;i<MenuCount.size() ; i++)
            CountSum += Integer.parseInt(MenuCount.get(i));
        tvAllCount_Cart.setText(String.valueOf(CountSum));

        imgBack_Cart = (ImageView) findViewById(R.id.imgBack_Cart);

        listSelect_Cart = (ListView) findViewById(R.id.listSelect_Cart);
        adapter = new MenuSelectedAdapter();
        adapter.readContact();
        listSelect_Cart.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listSelect_Cart, 0);

        imgBack_Cart.setOnClickListener(Click);
        linMenuAdd_Cart.setOnClickListener(Click);
        linCart_Cart.setOnClickListener(Click);
    }

    public ArrayList<MenuItem> getMenus(){
        return SelectedMenus;
    }

    class MenuSelectedAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return SelectedMenus.size();
        }

        @Override
        public Object getItem(int position) {
            return SelectedMenus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuSelectedView view = new MenuSelectedView(getApplicationContext());

            MenuItem item = SelectedMenus.get(position);
            view.setTvMenu(item.getMenu());
            view.setTvPrice(item.getPrice());
            view.setTvCount_Cart(item.getCount());
            return view;
        }

        public void addSelectedMenu(MenuItem view){
            SelectedMenus.add(view);
        }

        public void readContact() {
            for (int i=0; i<MenuName.size() ; i++) {
                if (Integer.parseInt(MenuCount.get(i)) != 0) {
                    addSelectedMenu(new MenuItem(MenuName.get(i), MenuPrice.get(i), MenuCount.get(i)));
                }
            }
        }
    }

    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgBack_Cart:
//                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                    startActivity(intent);
                    onBackPressed();
                    break;

                case R.id.linMenuAdd_Cart:
                    Intent intent1 = new Intent(getApplicationContext(), MenuActivity.class);
                    intent1.putStringArrayListExtra("MenuNameParsed", MenuName);
                    intent1.putStringArrayListExtra("MenuPriceParsed", MenuPrice);
                    intent1.putStringArrayListExtra("MenuCountParsed", MenuCount);
                    intent1.putExtra("Check", "OK");
                    startActivity(intent1);
                    break;

                case R.id.linCart_Cart:
                    qr();

            }
        }
    };

    public void qr(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(QR_Activity.class);
        intentIntegrator.initiateScan();
    }

    public void SumPrice() {
        int Number = 0;
        int count = -1;
        strAllPrice = "";
        for (int i =0; i<MenuPrice.size(); i++) {
            Number = Integer.parseInt(MenuCount.get(i));
            char Temp[] = MenuPrice.get(i).toCharArray();
            String a = "";
            for (int j=0 ; j<Temp.length ; j++) {
                if (Temp[j] != ',') {
                    a += Temp[j];
                }
            }
            nMenuPrice.add(a);
            AllPrice += Integer.parseInt(nMenuPrice.get(i)) * Number;
        }

        String a = String.valueOf(AllPrice);
        char Temp[] = a.toCharArray();

        for (int i=Temp.length-1; i>=0 ; i--) {
            count++;
            if (count == 3) {
                strAllPrice += ",";
                count=-1;
                i += 1;
            }
            else {
                strAllPrice += Temp[i];
            }
        }
        AllPrice = 0;
    }

    public void setListViewHeightBasedOnChildren(ListView listView, int c) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            if (c == 0)
                totalHeight += 290;
            else
                totalHeight += 12;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    class MenuSelectedView extends LinearLayout {

        TextView tvSelectedMenu;
        TextView tvSelectedPrice;
        TextView tvCount_Cart;
        TextView tvDelete;

        public MenuSelectedView(Context context) {
            super(context);

            init(context);
        }

        public MenuSelectedView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.menuselect,this,true);

            tvSelectedMenu = (TextView) findViewById(R.id.tvSelectedMenu);
            tvSelectedPrice = (TextView) findViewById(R.id.tvSelectedPrice);
            tvCount_Cart = (TextView) findViewById(R.id.tvCount_Cart);
            tvDelete = (TextView) findViewById(R.id.tvDelete);

            tvDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Count =0;
                    for (int i =0; i<MenuName.size() ; i++) {
                        if (MenuName.get(i) == tvSelectedMenu.getText().toString()) {
                            Count = Integer.parseInt(MenuCount.get(i));
                            Count--;
                            CountSum--;
                            MenuCount.set(i, String.valueOf(Count));
                            tvCount_Cart.setText(String.valueOf(Count));
                            tvAllCount_Cart.setText(String.valueOf(CountSum));
                            SumPrice();
                            tvAllPrice_Cart.setText(new StringBuffer(strAllPrice).reverse().toString() + "원");
                            SelectedMenus.set(i, new MenuItem(tvSelectedMenu.getText().toString(), tvSelectedPrice.getText().toString(), tvCount_Cart.getText().toString()));

                            if (Count == 0) {
                                MenuName.remove(i);
                                MenuPrice.remove(i);
                                MenuCount.remove(i);
                                SelectedMenus.remove(i);
                                setListViewHeightBasedOnChildren(listSelect_Cart, 0);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

        public void setTvMenu(String menu) {
            tvSelectedMenu.setText(menu);
        }

        public void setTvPrice(String price) {
            tvSelectedPrice.setText(price);
        }

        public void setTvCount_Cart(String count) {
            tvCount_Cart.setText(count);
        }

//        public String getTvAllPrice_Cart(){
//            String s = "";
//            s += tvAllPrice_Cart.getText();
//            return s;
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            Log.d("in", "in");
            if (result.getContents() == null) {
                Log.d("null", "null");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                getit();

            } else {
                Log.d("scanning", "scanning");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            }
        } else {
            Log.d("else", "else");
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void getit(){
        Intent intent1 = new Intent(getApplicationContext(), FinalActivity.class);
        intent1.putStringArrayListExtra("MenuName", MenuName);
        intent1.putStringArrayListExtra("MenuPrice", MenuPrice);
        intent1.putStringArrayListExtra("MenuCount", MenuCount);
        intent1.putExtra("AllPrice", tvAllPrice_Cart.getText());
        intent1.putExtra("request", edtRequest_Cart.getText().toString());
        //getText의 반환값은 charsequence이므로 toString() 필요!!
        intent1.putExtra("Check", "OK");
        startActivity(intent1);
    }

}
