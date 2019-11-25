package double_slash.techtown.com.phoneosk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import double_slash.techtown.com.phoneosk.MenuCartActivity;

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


        Toast.makeText(this, MenuNameParsed.size() + "",Toast.LENGTH_LONG).show();

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


}
