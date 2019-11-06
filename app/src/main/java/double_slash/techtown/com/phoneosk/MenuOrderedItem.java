package double_slash.techtown.com.phoneosk;

public class MenuOrderedItem {

    String Menu;
    String Price;
    String Count;

    public MenuOrderedItem(String menu, String price, String count) {
        Menu = menu;
        Price = price;
        Count = count;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "MenuSelectedItem{" +
                "Menu='" + Menu + '\'' +
                ", Price='" + Price + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }

}
