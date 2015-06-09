package ore.no.volleysample.weather;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ore.no.volleysample.util.TsVolleyRequest;

public class WeatherRequest extends TsVolleyRequest<Weather> {
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather";

    public WeatherRequest(Response.Listener<Weather> success, Response.ErrorListener failure) {
        super(URL, Method.GET, success, failure);
        addGETParam("q", "Tokyo");
    }

    @Override
    protected boolean exportLogVerbose() {
        return true;
    }

    @Override
    protected Weather asyncParse(String response) {
        Weather w = new Weather();
        try {
            JSONObject json = new JSONObject(response);

            w.name = json.getString("name");

            if (json.has("weather")) {
                JSONArray weathers = json.getJSONArray("weather");
                JSONObject weather = weathers.getJSONObject(0);
                if (weather.has("description")) {
                    w.description = weather.getString("description");
                }
            }
        } catch (JSONException e) {
            w.name = "";
            w.description = "";
        }
        return w;
    }
}
