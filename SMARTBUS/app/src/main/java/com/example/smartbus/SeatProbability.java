package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.AuthFailureError;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup;


public class SeatProbability extends AppCompatActivity {
    String[] stops_arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_probability);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
//        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.copyOfRange(stops_arr, position+1, stops_arr.length))
        spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeatProbability.this, android.R.layout.simple_spinner_item, Arrays.copyOfRange(stops_arr, position+1, stops_arr.length));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin2.setAdapter(adapter);
            }

             @Override
             public void onNothingSelected(AdapterView<?> parentView) {
                 // your code here
             }

        });
        Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
        spin2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
                Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
                getProbApi(spin1.getSelectedItem().toString(), spin2.getSelectedItem().toString());
            }

             @Override
             public void onNothingSelected(AdapterView<?> parentView) {
                 // your code here
             }

        });
        getStopsApi();
    }


    private void getStopsApi(){
        RequestQueue queue = Volley.newRequestQueue(SeatProbability.this);
        String url = "https://smart-bus-2020.herokuapp.com/getstops";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stops = response.getJSONArray("stops");
                    stops_arr = new String[stops.length()];
                    // looping through All Stops
                    for (int i = 0; i < stops.length(); i++) {
                        // JSONObject stop = stops.getJSONObject(i);
                        stops_arr[i] = stops.getString(i);
                    }
                    Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeatProbability.this, android.R.layout.simple_spinner_item, Arrays.copyOfRange(stops_arr, 0, stops_arr.length-1));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(adapter);



                }catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String res="Response failed";
                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
            }
        }
        );

        queue.add(stringRequest);

    }

    private void getProbApi(final String source, final String destination){
//        Toast.makeText(getApplicationContext(),source+"to"+destination,Toast.LENGTH_SHORT).show(); //run madi ee toast ge en barthide helu
        RequestQueue queue = Volley.newRequestQueue(SeatProbability.this);
        String url = "https://smart-bus-2020.herokuapp.com/getprobability";
        JSONObject obj = new JSONObject();
        try{
            obj.put("source",source);
            obj.put("destination",destination);
        }catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stops = response.getJSONArray("probs");
                    TableLayout probTable = (TableLayout) findViewById(R.id.probTable);
                    int count = probTable.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View child = probTable.getChildAt(i);
                        if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
                    }
                    TableRow header_row= new TableRow(SeatProbability.this);
                    TableRow.LayoutParams header_lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    header_row.setLayoutParams(header_lp);
                    TextView header_stop_view = new TextView(SeatProbability.this);
                    TextView header_prob_view = new TextView(SeatProbability.this);
                    header_stop_view.setText("Stop");
                    header_prob_view.setText("\tProbability of getting seat");
                    header_row.addView(header_stop_view);
                    header_row.addView(header_prob_view);
                    probTable.addView(header_row,0);
                    // looping through All Stops
                    for (int i = 0; i < stops.length(); i++) {
                        JSONObject stop = stops.getJSONObject(i);
                        String stop_name = stop.getString("stop");
                        String probability = stop.getString("probability");
                        TableRow row= new TableRow(SeatProbability.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        // add LayoutParams for row here
                        row.setLayoutParams(lp);
                        TextView stop_view = new TextView(SeatProbability.this);
                        TextView prob_view = new TextView(SeatProbability.this);
                        // add LayoutParams for TextView here
                        stop_view.setText(stop_name);
                        prob_view.setText(probability);
                        row.addView(stop_view);
                        row.addView(prob_view);
                        probTable.addView(row,i+1);

                    }

                }catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String res="Response failed";
                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                }
        }
        );
//        {
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String,String>
//                params.put("source","hindupur");
//                params.put("destination","banglore");
//                return params;
//            }
//
//            @Override
//            public  Map<String,String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String,String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//        };

        queue.add(stringRequest);

    }
}