package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.listycitylab3.City;
import com.example.listycitylab3.R;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
    }

    private AddCityDialogListener listener;

    // ➕ Holds the city to edit (null when adding)
    private City chosenCity;

    // ➕ Constructor for edit mode
    public AddCityFragment(City city) {
        this.chosenCity = city;
    }

    // ➕ Empty constructor for add mode
    public AddCityFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    // ➕ Refresh the list when editing finishes
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (chosenCity != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateCityList(); // call the helper in MainActivity
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // ➕ Pre-fill fields if editing
        if (chosenCity != null) {
            editCityName.setText(chosenCity.getName());
            editProvinceName.setText(chosenCity.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                // ➕ Dynamic title
                .setTitle(chosenCity != null ? "Edit City" : "Add a city")
                .setNegativeButton("Cancel", null)
                // ➕ Dynamic button text and action
                .setPositiveButton(chosenCity != null ? "Edit" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (chosenCity != null) {
                        // Edit mode: update existing city
                        chosenCity.setName(cityName);
                        chosenCity.setProvince(provinceName);
                    } else {
                        // Add mode: create a new city
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
