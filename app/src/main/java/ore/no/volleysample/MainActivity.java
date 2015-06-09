package ore.no.volleysample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ore.no.volleysample.weather.Weather;
import ore.no.volleysample.weather.WeatherRequest;
import ore.no.volleysample.util.TsVolleyManager;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherRequest wr = new WeatherRequest(
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        Toast.makeText(
                                getApplicationContext(),
                                weather.name + ":" + weather.description,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getApplicationContext(),
                                error.toString(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        TsVolleyManager.getInstance(this).addToRequestQueue(wr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
