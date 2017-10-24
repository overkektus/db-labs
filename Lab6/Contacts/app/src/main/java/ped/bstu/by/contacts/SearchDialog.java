package ped.bstu.by.contacts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Egor on 12.10.2017.
 */

public class SearchDialog extends DialogFragment {

    private Datable datable;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        datable = (Datable) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final String name = getArguments().getString("name");
        final String surname = getArguments().getString("surname");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Поиск номера телефона")
                .setView(R.layout.dialog_search)
                .setPositiveButton("Найти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        datable.Search(name, surname);
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }

}
