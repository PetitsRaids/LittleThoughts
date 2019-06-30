package com.example.littlethoughts.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.littlethoughts.R;

public class EnsureDialog extends AlertDialog {

    public EnsureDialog(Context context, int titleResId, int messageResId, int sureResId, ensureListener listener) {
        this(context, context.getText(titleResId).toString(), context.getText(messageResId).toString(), context.getText(sureResId).toString(), listener);
    }

    public EnsureDialog(Context context, String title, String message, String sureBtnMsg, ensureListener listener) {
        super(context, true, null);
        View view = LayoutInflater.from(context).inflate(R.layout.ensure_dialog, null, false);
        TextView ensureTitle = view.findViewById(R.id.ensure_title);
        TextView ensureMessage = view.findViewById(R.id.ensure_message);
        Button sure = view.findViewById(R.id.ensure_sure);
        Button cancel = view.findViewById(R.id.ensure_cancel);
        ensureTitle.setText(title);
        ensureMessage.setText(message);
        sure.setText(sureBtnMsg);
        sure.setOnClickListener(l -> {
            listener.ensure();
            dismiss();
        });
        cancel.setOnClickListener(l -> dismiss());
        setView(view);
        show();
    }

    public interface ensureListener {
        void ensure();
    }
}
