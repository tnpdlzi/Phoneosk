package double_slash.techtown.com.phoneosk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import double_slash.techtown.com.Phoneosk.R;

public class FinalActivity extends AppCompatActivity {

    ListView list_receipt;
    MenuOrderedAdapter adapter;

    ArrayList<String> MenuName = new ArrayList<>();
    ArrayList<String> MenuPrice = new ArrayList<>();
    ArrayList<String> MenuCount = new ArrayList<>();

    ArrayList <MenuItem> OrderedMenus = new ArrayList<>();

    String name = null;
    String address = null;
    String table;

    TextView txtStoreName;
    TextView txtTableNum;
    TextView txtRequestX;
    TextView txtRequestExist;
    TextView txtPrice_receipt;
    TextView txtAddress_receipt;

    RelativeLayout rel_request;
    LinearLayout linFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        list_receipt = (ListView)findViewById(R.id.list_receipt);

        LinearLayout linFinal = (LinearLayout) findViewById(R.id.linFinal);
        txtStoreName = (TextView)findViewById(R.id.txtStoreName);
        txtTableNum = (TextView)findViewById(R.id.txtTableNum);
        txtRequestX = (TextView)findViewById(R.id.txtRequestX);
        txtRequestExist = (TextView)findViewById(R.id.txtRequestExist);
        txtPrice_receipt = (TextView)findViewById(R.id.txtPrice_receipt);
        txtAddress_receipt = (TextView)findViewById(R.id.txtAddress_receipt);
        rel_request = (RelativeLayout)findViewById(R.id.rel_request);
        ImageView btnBack_receipt = (ImageView)findViewById(R.id.btnBack_receipt);

        Intent intent = new Intent(this.getIntent());


        btnBack_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        });
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        table = intent.getStringExtra("table");
        MenuName = intent.getStringArrayListExtra("MenuName");
        MenuPrice = intent.getStringArrayListExtra("MenuPrice");
        MenuCount = intent.getStringArrayListExtra("MenuCount");
        String price = intent.getExtras().getString("AllPrice");
        String request = intent.getExtras().getString("request");

        txtPrice_receipt.setText(price);

        if(request.equals("")){

        } else{
            txtRequestX.setText("");
        }
        txtRequestExist.setText(request);

        txtAddress_receipt.setText(address);
        txtStoreName.setText(name);
        String tableNum = "Table No. " + table;
        txtTableNum.setText(tableNum);


        adapter = new MenuOrderedAdapter();
        adapter.readContact();
        list_receipt.setAdapter(adapter);

        setListViewHeightBasedOnChildren(list_receipt, 0);


    }

    class MenuOrderedAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return OrderedMenus.size();
        }

        @Override
        public Object getItem(int position) {
            return OrderedMenus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuOrderedView view = new MenuOrderedView(getApplicationContext());

            MenuItem item = OrderedMenus.get(position);
            view.setTvMenu(item.getMenu());
            view.setTvPrice(item.getPrice());
            view.setTvCount_Cart(item.getCount());
            return view;
        }

        public void addSelectedMenu(MenuItem view){
            OrderedMenus.add(view);
        }

        public void readContact() {
            for (int i=0; i<MenuName.size() ; i++) {
                if (Integer.parseInt(MenuCount.get(i)) != 0) {
                    addSelectedMenu(new MenuItem(MenuName.get(i), MenuPrice.get(i), MenuCount.get(i)));
                }
            }
        }

    }

    class MenuOrderedView extends LinearLayout {

        TextView tvOrderedMenu;
        TextView tvOrderedPrice;
        TextView tvOrderedCount;

        public MenuOrderedView(Context context) {
            super(context);

            init(context);
        }

        public MenuOrderedView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.orderedmenus_receipt,this,true);

            tvOrderedMenu = (TextView) findViewById(R.id.tvOrderedMenu);
            tvOrderedPrice = (TextView) findViewById(R.id.tvOrderedPrice);
            tvOrderedCount = (TextView) findViewById(R.id.tvOrderedCount);


        }

        public void setTvMenu(String menu) {
            tvOrderedMenu.setText(menu);
        }

        public void setTvPrice(String price) {
            tvOrderedPrice.setText(price);
        }

        public void setTvCount_Cart(String count) {
            tvOrderedCount.setText(count);
        }
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
                totalHeight += 255;
            else
                totalHeight += 12;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
