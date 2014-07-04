package com.mindpin.android.shakehandler;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.squareup.seismic.ShakeDetector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dd on 14-7-3.
 */
public class ShakeHandler implements ShakeDetector.Listener {
    private static final String TAG = "ShakeHandler";
    String url = null;
    String cookie = "";
    Context context;
    CallbackListener callbackListener = null;
    SensorManager sensorManager;
    ShakeDetector sd;
    Map<String, String> params, postParams;
    AsyncTask task = null;

    public interface CallbackListener {
        public void callback(String json);
    }

    public String get_location_params() {
        Location location = get_location();
        if (location != null)
            return get_location_params_json(location);
        else
            return get_location_blank_json();
    }

    private String get_location_blank_json() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("accuracy", "");
        hashMap.put("altitude", "");
        hashMap.put("bearing", "");
        hashMap.put("elapsed_realtime_nanos", "");
        hashMap.put("extras", "");
        hashMap.put("latitude", "");
        hashMap.put("longitude", "");
        hashMap.put("provider", "");
        hashMap.put("speed", "");
        hashMap.put("time", "");
        return new Gson().toJson(hashMap);
    }

    private String get_location_params_json(Location location) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("accuracy", String.valueOf(location.getAccuracy()));
        hashMap.put("altitude", String.valueOf(location.getAltitude()));
        hashMap.put("bearing", String.valueOf(location.getBearing()));
//        if (android.os.Build.VERSION.SDK_INT >= 17)
//            hashMap.put("elapsed_realtime_nanos", String.valueOf(location.getElapsedRealtimeNanos()));
//        else
        hashMap.put("elapsed_realtime_nanos", "");
        hashMap.put("extras", String.valueOf(location.getExtras()));
        hashMap.put("latitude", String.valueOf(location.getLatitude()));
        hashMap.put("longitude", String.valueOf(location.getLongitude()));
        hashMap.put("provider", String.valueOf(location.getProvider()));
        hashMap.put("speed", String.valueOf(location.getSpeed()));
        hashMap.put("time", String.valueOf(location.getTime()));
        return new Gson().toJson(hashMap);
    }

    public ShakeHandler(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        sd = new ShakeDetector(this);
        params = new HashMap<String, String>();
    }

    @Override
    public void hearShake() {
        post();
    }


    public void start_preview() {
        sd.start(sensorManager);
    }

    public void stop_preview() {
        sd.stop();
    }

    public void set_url(String url) {
        this.url = url;
    }

    public void add_http_param(String param_name, String param) {
        params.put(param_name, param);
    }

    public void add_http_param(String param_name, ParamGetter pg) {
        params.put(param_name, pg.get_param_value(pg.t));
    }

    public void set_cookie(String cookie) {
        this.cookie = cookie;
    }

    public void set_callback_listener(CallbackListener callback_listener) {
        callbackListener = callback_listener;
    }

    private void post() {
        if (task != null) {
            Log.d(TAG, "task no null");
            return;
        }
        if (url == null)
            throw new Error("no url");
        HttpRequest request = HttpRequest.post(url);
        if (!cookie.equals(""))
            request.header("Cookie", cookie);
        postParams = new HashMap<String, String>(params);
        postParams.put("location", get_location_params());
        Log.d(TAG, "postParams:" + new Gson().toJson(postParams));
        task = new RequestTask().execute(request);
    }

    public Location get_location() {
        LocationManager locMan = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locMan
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locMan
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    private class RequestTask extends AsyncTask<HttpRequest, Long, Boolean> {
        HttpRequest request;
        String result;

        @Override
        protected Boolean doInBackground(HttpRequest... requests) {
            if (requests.length > 0) {
                request = requests[0];
                request.form(postParams);
                Boolean ok = request.ok();
                if (ok)
                    result = request.body();
                return ok;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.d(TAG, "ok()");
                if (callbackListener != null)
                    callbackListener.callback(result);
            } else {
                Log.d(TAG, "not ok");
                // error
            }
            task = null;
        }
    }
}
