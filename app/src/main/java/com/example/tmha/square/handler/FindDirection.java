package com.example.tmha.square.handler;

import android.os.AsyncTask;

import com.example.tmha.square.listener.FindDirectionListener;
import com.example.tmha.square.model.Distance;
import com.example.tmha.square.model.Duration;
import com.example.tmha.square.model.Route;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aka on 6/27/2017.
 */

public class FindDirection {

    private static final
        String DIRECTION_URL_API
            = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final
        String GOOGLE_API_KEY
            = "AIzaSyAVCEfNlwneZUxd24Og3HS-SJtc2xdV5Qw";
    private FindDirectionListener mFindDirectionListener;
    private String mOrigin;
    private String mDestination;

    public FindDirection(FindDirectionListener mFindDirectionListener,
                         String mOrigin, String mDestination) {
        this.mFindDirectionListener = mFindDirectionListener;
        this.mOrigin = mOrigin;
        this.mDestination = mDestination;
    }

    public void execute() throws UnsupportedEncodingException {
        mFindDirectionListener.onDirectionFinderStart();
        new GetData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(mOrigin, "utf-8");
        String urlDestination = URLEncoder.encode(mDestination, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" +
                urlDestination + "&key=" + GOOGLE_API_KEY;
    }

    private class GetData extends AsyncTask<String, Void, String>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);

            Request request = builder.build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()){
                try {
                    parseJson(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseJson(String data) throws JSONException {
        List<Route> routes = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonArrayRoute = jsonData.getJSONArray("routes");
        for (int i =0; i< jsonArrayRoute.length(); i++){
            JSONObject jsonRoute = jsonArrayRoute.getJSONObject(i);
            Route route = new Route();
            JSONObject overview_polylineJson
                    = jsonRoute.getJSONObject("overview_polyline");

            JSONArray jsonArrayleg = jsonRoute.getJSONArray("legs");
            JSONObject jsonleg = jsonArrayleg.getJSONObject(0);
            JSONObject jsonDistance = jsonleg.getJSONObject("distance");
            JSONObject jsonDuration = jsonleg.getJSONObject("duration");

            JSONObject jsonStartLocation = jsonleg.getJSONObject("start_location");
            JSONObject jsonEndLocation = jsonleg.getJSONObject("end_location");

            route.setmDistance(new Distance(jsonDistance.getString("text")
                    , jsonDistance.getString("value")));
            route.setmDuration(new Duration(jsonDuration.getString("text")
                    , jsonDuration.getString("value")));
            route.setmStartAddress(jsonleg.getString("start_address"));
            route.setmEndAddress(jsonleg.getString("end_address"));
            route.setmStartLocation(
                    new LatLng(jsonStartLocation.getDouble("lat"),
                                jsonStartLocation.getDouble("lng")));
            route.setmEndLocation(
                    new LatLng(jsonEndLocation.getDouble("lat"),
                            jsonEndLocation.getDouble("lng")));
            route.setmLatLngs(
                    decodePolyLine(overview_polylineJson
                            .getString("points")));

            routes.add(route);
        }

        mFindDirectionListener.onDirectionFinderSuccess(routes);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
