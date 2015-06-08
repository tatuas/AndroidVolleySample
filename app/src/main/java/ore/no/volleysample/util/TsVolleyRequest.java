package ore.no.volleysample.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TsVolleyRequest<T> extends Request<T> {
    private final static String TAG = "TsVolleyRequest";
    private Map<String, String> mHeaders;
    private Response.Listener<T> mListener;
    private Map<String, String> mPostParams;
    private Map<String, String> mGetParams;
    private String mRequestUrl;

    public TsVolleyRequest(String url,
                           int method,
                           Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        super(method, "", errorListener);

        this.mListener = listener;
        this.mRequestUrl = url;
        this.mHeaders = new HashMap<>();
        this.mPostParams = new HashMap<>();
        this.mGetParams = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public String getUrl() {
        if (mGetParams.size() > 0) {
            Set<String> keys = mGetParams.keySet();
            List<String> paramsList = new ArrayList<>();
            for (String key : keys) {
                try {
                    String value = URLEncoder.encode(
                            mGetParams.get(key),
                            getGETParamEncodeType()
                    );
                    paramsList.add(key + "=" + value);
                } catch (Exception e) {
                    log(e.toString());
                }
            }

            return mRequestUrl + "?" + implode(paramsList, "&");
        } else {
            return mRequestUrl;
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mPostParams;
    }

    /**
     * Add POST parameter to this request instance.
     * You have to use before request calling.
     *
     * @return
     */
    protected void addPOSTParam(String key, String value) {
        if (getMethod() != Method.POST) {
            throw new UnsupportedOperationException("Reqeust method is not POST.");
        }

        mPostParams.put(key, value);
    }

    /**
     * Add GET parameter to this request instance.
     * You have to use before request calling.
     *
     * @return
     */
    protected void addGETParam(String key, String value) {
        mGetParams.put(key, value);
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseStr = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers)
            );

            log("API REQUEST URL:" + getUrl());
            log("API RESPONSE STRING:" + responseStr);
            log("API RESPONSE CODE:" + response.statusCode);

            return Response.success(
                    asyncParse(responseStr),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    private String implode(List<String> values, String glue) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int size = values.size();
        for (String value : values) {
            sb.append(value);
            if (index <= size - 2) {
                sb.append(glue);
            }
            index++;
        }
        return sb.toString();
    }

    private void log(String msg) {
        if (exportLogVerbose()) {
            Log.v(TAG, msg);
        }
    }

    protected boolean exportLogVerbose() {
        return true;
    }

    protected String getGETParamEncodeType() {
        return "UTF-8";
    }

    protected abstract T asyncParse(String response);
}
