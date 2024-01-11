package com.example.bar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class VoivodeshipFragment extends Fragment {
    Button btnAccept;
    EditText brandAuto;
    EditText yearAuto;
    EditText dateBegin;
    EditText dateEnd;
    EditText autoModel;
    String kluczSlownika;
    String wartoscSlownika;
    String[] paliwoArray;
    String[] wojArray;
    String value;
    Spinner spinner;
    Spinner wojSpinner;
    String wojSelect;
    String selectedOption;
    Button wojButton;
    StringBuilder stringBuilder = new StringBuilder();
    public VoivodeshipFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voivodeship, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btnAccept = view.findViewById(R.id.buttonAccept);
        wojButton = view.findViewById(R.id.wojButton);
        brandAuto = view.findViewById(R.id.editTextAuto);
        yearAuto = view.findViewById(R.id.editTextAutoYear);
        autoModel = view.findViewById(R.id.editTextAutoModel);
        dateBegin = view.findViewById(R.id.editTextDate);
        dateEnd = view.findViewById(R.id.editTextDate2);
        spinner = view.findViewById(R.id.spinnerText);
        wojSpinner = view.findViewById(R.id.spinnerTextWoj);
        paliwoArray = getResources().getStringArray(R.array.paliwo);
        wojArray = getResources().getStringArray(R.array.wojewodztwa);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = "https://api.cepik.gov.pl/slowniki/wojewodztwa";
        ArrayAdapter<String> adapterBen = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, paliwoArray);
        adapterBen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterBen);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: selectedOption = "&filter[rodzaj-paliwa]=BENZYNA";
                        break;
                    case 1: selectedOption = "&filter[rodzaj-paliwa]=OLEJ%20NAPĘDOWY";
                        break;
                    case 2: selectedOption = "";
                        break;
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> adapterWoj = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,wojArray);
        adapterWoj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wojSpinner.setAdapter(adapterWoj);
        wojSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wojSelect = wojArray[position].toUpperCase();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        api(requestQueue, url);
        btnAccept.setOnClickListener(view1 ->{
            api(requestQueue, url);
        });
        wojButton.setOnClickListener(view1 ->{
            dialog();
        });
        return view;
    }
    void dialog(){
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(stringBuilder);
        Button dialogbtn = dialog.findViewById(R.id.dialog_button);
        dialogbtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    void api(RequestQueue requestQueue, String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolleyLog.d(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.getMessage());
                    }
                }
        );
        requestQueue.add(stringRequest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("data").getJSONObject("attributes");
                            JSONArray objW = obj.getJSONArray("dostepne-rekordy-slownika");
                            VolleyLog.d(objW.toString());
                            stringBuilder.setLength(0);
                            for (int i = 0; i < objW.length(); i++) {
                                JSONObject object = objW.getJSONObject(i);
                                kluczSlownika = object.getString("klucz-slownika");
                                wartoscSlownika = object.getString("wartosc-slownika");
                                stringBuilder.append("Klucz: " + kluczSlownika + "\n" +"Wartosc: " + wartoscSlownika + "\n\n");
                                if (wartoscSlownika.equals(wojSelect) &&
                                        !dateBegin.getText().toString().isEmpty() &&
                                        !dateEnd.getText().toString().isEmpty()) {
                                    value = kluczSlownika;
                                    ((MainActivity)getActivity()).setFragment(new ResultFragment());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("klucz", wojSelect);
                                    bundle.putString("dateBegin",dateBegin.getText().toString());
                                    bundle.putString("dateEnd",dateEnd.getText().toString());
                                    bundle.putString("rodzPaliw",selectedOption);
                                    bundle.putString("kluczWoj", value);
                                    bundle.putString("brand", brandAuto.getText().toString());
                                    bundle.putString("year",yearAuto.getText().toString());
                                    bundle.putString("model",autoModel.getText().toString());
                                    getParentFragmentManager().setFragmentResult("values", bundle);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}