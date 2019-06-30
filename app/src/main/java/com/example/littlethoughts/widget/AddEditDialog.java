package com.example.littlethoughts.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.littlethoughts.R;

public class AddEditDialog extends AlertDialog {

    public AddEditDialog(Context context, int titleResId, String editResTitle, int sureResId, CancelListener cancelListener, SureListener listener) {
        this(context, context.getText(titleResId).toString(), editResTitle, context.getText(sureResId).toString(), cancelListener, listener);
    }

    public AddEditDialog(Context context, String dialogTitle, String editText, String sureBtnText, CancelListener cancelListener, SureListener sureListener) {
        super(context, true, null);
        View view = LayoutInflater.from(context).inflate(R.layout.add_edit_dialog, null, false);
        TextView title = view.findViewById(R.id.ae_dialog_title);
        EditText edit = view.findViewById(R.id.ae_dialog_edit_text);
        Button cancel = view.findViewById(R.id.ae_cancel);
        Button sure = view.findViewById(R.id.ae_sure);
        title.setText(dialogTitle);
        edit.setText(editText);
        cancel.setOnClickListener(l -> {
            if (cancelListener != null) {
                cancelListener.cancel();
            }
            dismiss();
        });
        sure.setText(sureBtnText);
        sure.setOnClickListener(l -> {
            sureListener.sure(edit.getText().toString());
            dismiss();
        });
        setView(view);
        show();
    }

    public interface CancelListener {
        void cancel();
    }

    public interface SureListener {
        void sure(String editText);
    }
}
