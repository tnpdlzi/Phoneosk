package double_slash.techtown.com.phoneosk;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HttpAsyncTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private Context context;

    public HttpAsyncTask(String url, ContentValues values, Context context) {

        this.url = url;
        this.values = values;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result; // 요청 결과를 저장할 변수.
        RequestHttpUrlConnection requestHttpURLConnection = new RequestHttpUrlConnection();
        result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }

}
