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
import android.widget.TextView;
import android.widget.Toast;

import com.abdelrahman.wallete.Widgets.ExpenseDatePicker;
import com.abdelrahman.wallete.beans.Expense;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by AbdelRahman on 9/18/2016.
 */
public class DialogEdit extends DialogFragment {

    private TextView mTitle;
    private ImageButton mClosebtn;
    private EditText mAmount;
    private EditText mPurpose;
    private ExpenseDatePicker mDatePicker;
    private Button mEditbtn;
    long addedDate;

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.btn_dialog_add:
                    EditInRealm();
                    break;
            }
            dismiss();
        }
    };

    private void EditInRealm() {
        if(!mAmount.getText().toString().equals("") && !mPurpose.getText().toString().equals("")) {
            double amount = Double.parseDouble(mAmount.getText().toString());
            if (amount==0){
                Toast.makeText(getActivity(),"amount can't be ZERO!",Toast.LENGTH_LONG).show();
                return;
            }
            String purpose = mPurpose.getText().toString();
            Realm realm = Realm.getDefaultInstance();
            Expense expense=new Expense(amount,purpose,addedDate,mDatePicker.getTime());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(expense);
            realm.commitTransaction();
            realm.close();
        }
        else{
            Toast.makeText(getActivity(),"please enter amount & purpose!",Toast.LENGTH_LONG).show();
        }
    }

    public DialogEdit(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle =(TextView)view.findViewById(R.id.tv_title);
        mClosebtn=(ImageButton)view.findViewById(R.id.btn_close);
        mAmount=(EditText)view.findViewById(R.id.et_amount);
        mPurpose=(EditText)view.findViewById(R.id.et_purpose);
        mDatePicker=(ExpenseDatePicker)view.findViewById(R.id.custom_date_picker);
        mEditbtn=(Button)view.findViewById(R.id.btn_dialog_add);
        fill();
    }

    private void fill() {
        mTitle.setText("Edit");
        mEditbtn.setText("Edit");

        Bundle args=getArguments();
        if (args!=null){
            mAmount.setText(Double.toString(args.getDouble("AMOUNT")));
            mPurpose.setText(args.getString("PURPOSE"));
            addedDate=args.getLong("ADDED-DATE");
            long when=args.getLong("WHEN");
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(when);
            mDatePicker.update(cal.get(Calendar.DATE),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),0,0,0);

        }

        mClosebtn.setOnClickListener(mOnClickListener);
        mEditbtn.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }
}
