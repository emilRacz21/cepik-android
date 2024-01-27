package com.example.bar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ResultFragment extends Fragment {
    public ResultFragment() {}
    TextView wojText;
    TextView dateTextBegin;
    TextView dateTextEnd;
    TextView wojNums;
    String selectedCar;
    String selectedYear;
    String selectedModel;
    LinearLayout layoutProgress;
    LinearLayout layoutCurrent;
    ListView carListView;
    String[][] valueTab;

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
        carListView = view.findViewById(R.id.carListView);
        wojText = view.findViewById(R.id.wojText);
        wojNums = view.findViewById(R.id.wojNums);
        dateTextBegin = view.findViewById(R.id.dateText);
        dateTextEnd = view.findViewById(R.id.dateTextEnd);
        layoutProgress = view.findViewById(R.id.hideLayout);
        layoutCurrent = view.findViewById(R.id.layoutCurrent);
        StringBuilder stringBuilder = new StringBuilder();
        layoutCurrent.setVisibility(View.GONE);
        getParentFragmentManager().setFragmentResultListener("idCarValues", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String idCar = result.getString("idCar");
                System.out.println(idCar);
                String urlAuta = "https://api.cepik.gov.pl/pojazdy/"+idCar;
                System.out.println(urlAuta);
                if(!result.isEmpty()){
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    api(requestQueue, urlAuta, true);
                }
            }
        });

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
                String date1 = dateBegin.replace(".","");
                String date2 = dateEnd.replace(".","");
                dateTextBegin.setText(dateBegin);
                dateTextEnd.setText(dateEnd);
                selectedCar = !brandCar.isEmpty() ? "&filter[marka]=" + brandCar : "";
                selectedYear = !yearCar.isEmpty() ? "&filter[sposob-produkcji]=" + yearCar : "";
                selectedModel = !modelCar.isEmpty() ? "&filter[model]=" + modelCar : "";
                String urlAuta = "https://api.cepik.gov.pl/pojazdy?wojewodztwo="+ wojKey +"&data-od="+ date1 +"&data-do="+ date2 +"&typ-daty=1&tylko-zarejestrowane=true&pokaz-wszystkie-pola=true&limit=500&page=1"+rodzPal+selectedCar+selectedYear+selectedModel;
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                System.out.println(urlAuta);
                api(requestQueue, urlAuta,false);
            }
        });
        return view;
    }

    void api(RequestQueue requestQueue, String url, boolean isSingleVehicle) {
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    layoutProgress.setVisibility(View.GONE);
                    layoutCurrent.setVisibility(View.VISIBLE);
                    try {
                        JSONArray vehiclesArray;
                        if (isSingleVehicle) {
                            JSONObject data = response.getJSONObject("data").getJSONObject("attributes");
                            valueTab = new String[1][7];
                            wojNums.setText(getResources().getString(R.string.ilo_zarejestrowanych_pojazd_w) + " 1");

                            String marka = data.getString("marka");
                            String model = data.getString("model");
                            String sposobProdukcji = data.getString("sposob-produkcji");
                            String rodzPaliw = data.getString("rodzaj-paliwa");
                            String powiat = data.getString("rejestracja-powiat");
                            String podRodzAuta = data.getString("podrodzaj-pojazdu");
                            String rejWoj = data.getString("rejestracja-wojewodztwo");
                            int masa = data.getInt("masa-wlasna");
                            wojText.setText(rejWoj.toUpperCase());
                            valueTab[0][0] = marka;
                            valueTab[0][1] = model;
                            valueTab[0][2] = sposobProdukcji;
                            valueTab[0][3] = rodzPaliw;
                            valueTab[0][4] = powiat;
                            valueTab[0][5] = podRodzAuta;
                            valueTab[0][6] = String.valueOf(masa);
                        } else {
                            vehiclesArray = response.getJSONArray("data");
                            valueTab = new String[vehiclesArray.length()][8];
                            wojNums.setText(getResources().getString(R.string.ilo_zarejestrowanych_pojazd_w) + " " + vehiclesArray.length());

                            for (int i = 0; i < vehiclesArray.length(); i++) {
                                JSONObject vehicleObject = vehiclesArray.getJSONObject(i);
                                JSONObject attributes = vehicleObject.getJSONObject("attributes");
                                String marka = attributes.getString("marka");
                                String model = attributes.getString("model");
                                String sposobProdukcji = attributes.getString("sposob-produkcji");
                                String rodzPaliw = attributes.getString("rodzaj-paliwa");
                                String powiat = attributes.getString("rejestracja-powiat");
                                String podRodzAuta = attributes.getString("podrodzaj-pojazdu");
                                String id1 = vehicleObject.getString("id");
                                int masa = attributes.getInt("masa-wlasna");
                                valueTab[i][0] = marka;
                                valueTab[i][1] = model;
                                valueTab[i][2] = sposobProdukcji;
                                valueTab[i][3] = rodzPaliw;
                                valueTab[i][4] = powiat;
                                valueTab[i][5] = podRodzAuta;
                                valueTab[i][6] = String.valueOf(masa);
                                valueTab[i][7] = id1;
                                carListView.setOnItemClickListener((parent, view, position, id) -> copyID(position));
                            }
                        }
                        CustomListAdapter customListAdapter = new CustomListAdapter(getContext(), valueTab);
                        carListView.setAdapter(customListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error != null && error.getMessage() != null) {
                        VolleyLog.e(error.getMessage());
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        ((MainActivity) getActivity()).setFragment(new FormFragment());
                        Toast.makeText(getContext(), "Wprowadzono niepoprawne dane", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    void copyID(int position){
        ClipboardManager clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("Vehicle ID", valueTab[position][7]);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), "Skopiowano ID auta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "ID niedostępne!", Toast.LENGTH_SHORT).show();
        }
    }

}