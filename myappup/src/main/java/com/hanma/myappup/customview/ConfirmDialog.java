package com.hanma.myappup.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hanma.myappup.R;
import com.hanma.myappup.feature.Callback;


/**
 * Updata by Litianlue on 2020/05/15.
 */
public class ConfirmDialog extends Dialog {

    Callback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    public ConfirmDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        sureBtn = (TextView)mView.findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView)mView.findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);


        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(1);
                ConfirmDialog.this.cancel();
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(0);
                ConfirmDialog.this.cancel();
            }
        });
        super.setContentView(mView);
    }


    public ConfirmDialog setContent(String s){
        content.setText(s);
        return this;
    }


}
