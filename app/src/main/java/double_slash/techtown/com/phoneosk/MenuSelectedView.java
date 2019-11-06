package double_slash.techtown.com.phoneosk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import double_slash.techtown.com.Phoneosk.R;

public class MenuSelectedView extends LinearLayout {

    TextView tvSelectedMenu;
    TextView tvSelectedPrice;
    TextView tvCount_Cart;

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
}
