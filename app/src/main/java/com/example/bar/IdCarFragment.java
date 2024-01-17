package com.example.bar;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class IdCarFragment extends Fragment {
    EditText editID;
    Button button;
    public IdCarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_id_car, container, false);
        editID= view.findViewById(R.id.editIdCar);
        button = view.findViewById(R.id.btnAccept);
        button.setOnClickListener(view1->{
            ((MainActivity)getActivity()).setFragment(new ResultFragment());
            Bundle bundle = new Bundle();
            bundle.putString("idCar", editID.getText().toString());
            getParentFragmentManager().setFragmentResult("idCarValues", bundle);
        });
        return view;
    }
}