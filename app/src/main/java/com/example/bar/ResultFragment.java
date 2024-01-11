package com.example.bar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultFragment extends Fragment {
    public ResultFragment() {}

    TextView wojText;
    TextView setCarText;
    TextView dateText;
    String selectedCar;
    String selectedYear;
    String selectedModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).setActionBar("  Informacje o pojazdach", R.drawable.baseline_directions_car_24);
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        setCarText = view.findViewById(R.id.setCarText);
        wojText = view.findViewById(R.id.wojText);
        dateText = view.findViewById(R.id.dateText);
        StringBuilder stringBuilder = new StringBuilder();

        getParentFragmentManager().setFragmentResultListener("values", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                stringBuilder.setLength(0);
                String valueEdit = result.getString("klucz");
                wojText.setText(valueEdit.toUpperCase());
                String wojKey = result.getString("kluczWoj");
                String rodzPal = result.getString("rodzPaliw");
                String dateBegin = result.getString("dateBegin");
                String dateEnd = result.getString("dateEnd");
                String brandCar = result.getString("brand").toUpperCase();
                String yearCar = result.getString("year");
                String modelCar = result.getString("model").toUpperCase();
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                stringBuilder.append(dateBegin + " - " + dateEnd);
                String date1 = dateBegin.replace("/","");
                String date2 = dateEnd.replace("/","");
                dateText.setText(stringBuilder);

                if(!brandCar.isEmpty()){
                    selectedCar = "&filter[marka]="+brandCar;
                }else selectedCar ="";
                if(!yearCar.isEmpty()){
                    selectedYear = "&filter[sposob-produkcji]="+yearCar;
                }else selectedYear ="";
                if(!modelCar.isEmpty()){
                    selectedModel="&filter[model]="+modelCar;
                }else selectedModel ="";
                String urlAuta = "https://api.cepik.gov.pl/pojazdy?wojewodztwo="+ wojKey +"&data-od="+ date1 +"&data-do="+ date2 +"&typ-daty=1&tylko-zarejestrowane=true&pokaz-wszystkie-pola=true&limit=100&page=1"+rodzPal+selectedCar+selectedYear+selectedModel;
                api(requestQueue, urlAuta);

            }
        });
        return view;
    }
    void api(RequestQueue requestQueue, String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            JSONArray vehiclesArray = response.getJSONArray("data");
                            for (int i = 0; i < vehiclesArray.length(); i++) {
                                JSONObject vehicleObject = vehiclesArray.getJSONObject(i);
                                JSONObject attributes = vehicleObject.getJSONObject("attributes");
                                String marka = attributes.getString("marka");
                                String model = attributes.getString("model");
                                String sposobProdukcji = attributes.getString("sposob-produkcji");
                                String rodzPaliw = attributes.getString("rodzaj-paliwa");
                                stringBuilder.append("Marka: " + marka + " Model: " + model +" Rok produkcji: "+ sposobProdukcji + " Rodzaj paliwa: "+ rodzPaliw +"\n");
                            }
                            setCarText.setText(stringBuilder.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null) {
                            VolleyLog.e(error.getMessage());
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            ((MainActivity)getActivity()).setFragment(new VoivodeshipFragment());
                            Toast.makeText(getContext(), "Wprowadzono niepoprawne dane", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}