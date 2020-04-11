package com.abdelrahman.wallete;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.abdelrahman.wallete.Widgets.ExpenseDatePicker;
import com.abdelrahman.wallete.beans.Expense;

import io.realm.Realm;

/**
 * Created by AbdelRahman on 8/23/2016.
 */
public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputAmount;
    private EditText mInputPurpose;
    private Button mBtnADD;
    private ExpenseDatePicker mDate;

    private View.OnClickListener mBtnOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.btn_dialog_add:
                    addAction();
                    break;
            }
            dismiss();
        }
    };

    private void addAction() {
        if(!mInputAmount.getText().toString().equals("") && !mInputPurpose.getText().toString().equals("")) {
            double amount = Double.parseDouble(mInputAmount.getText().toString());
            if (amount==0){
                Toast.makeText(getActivity(),"amount can't be ZERO!",Toast.LENGTH_LONG).show();
                return;
            }
            String purpose = mInputPurpose.getText().toString();
            long now = System.currentTimeMillis();

            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            Expense expense = realm.createObject(Expense.class);
            expense.setAddedDate(now);
            expense.setAmount(amount);
            expense.setPurpose(purpose);
            expense.setWhen(mDate.getTime());
            realm.commitTransaction();
            realm.close();
        }
        else{
            Toast.makeText(getActivity(),"please enter amount & purpose!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add,container,false);
    }

    public DialogAdd() {
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose=(ImageButton) view.findViewById(R.id.btn_close);
        mInputAmount=(EditText) view.findViewById(R.id.et_amount);
        mInputPurpose=(EditText) view.findViewById(R.id.et_purpose);
        mBtnADD=(Button) view.findViewById(R.id.btn_dialog_add);
        mDate=(ExpenseDatePicker) view.findViewById(R.id.custom_date_picker);

        mBtnClose.setOnClickListener(mBtnOnClickListener);
        mBtnADD.setOnClickListener(mBtnOnClickListener);

    }
}
