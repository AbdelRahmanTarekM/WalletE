package com.abdelrahman.wallete.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdelrahman.wallete.AppWalletE;
import com.abdelrahman.wallete.EditListener;
import com.abdelrahman.wallete.Filter;
import com.abdelrahman.wallete.R;
import com.abdelrahman.wallete.beans.Expense;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by AbdelRahman on 8/28/2016.
 */
public class AdapterExpenses extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int COUNT_NO_ITEM = 1;

    public static final int SAVING = 0;
    public static final int EXPENSE = 1;
    public static final int NO_ITEM = 2;


    private LayoutInflater mInflater;
    private RealmResults<Expense> mResults;
    private Realm mRealm;
    private int mFilterOption;
    private Context mContext;
    private ResetListener mResetListener;
    private EditListener mEditListener;

    public AdapterExpenses(Context context, Realm realm, RealmResults<Expense> results,
                           ResetListener resetListener, EditListener editListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResetListener = resetListener;
        mRealm = realm;
        mEditListener = editListener;
        update(results);
    }

    public void update(RealmResults<Expense> results) {
        mResults = results;
        mFilterOption = AppWalletE.load(mContext);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == SAVING) {
            View view = mInflater.inflate(R.layout.row_saving, parent, false);
            return new ExpenseHolder(view, mEditListener, true);
        } else if (viewType == EXPENSE) {
            View view = mInflater.inflate(R.layout.row_expense, parent, false);
            return new ExpenseHolder(view, mEditListener, false);
        } else {
            View view = mInflater.inflate(R.layout.no_items, parent, false);
            return new NoItemsHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExpenseHolder) {
            ExpenseHolder expenseHolder = (ExpenseHolder) holder;
            expenseHolder.mPurpose.setText(mResults.get(position).getPurpose());
            double amount = mResults.get(position).getAmount();
            if (!((ExpenseHolder) holder).isPositive) {
                amount = -amount;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
            String formattedAmount = decimalFormat.format(amount);
            expenseHolder.mAmount.setText(formattedAmount);
            expenseHolder.setWhen(mResults.get(position).getWhen());
        }
    }

    @Override
    public int getItemCount() {
        if (!mResults.isEmpty()) {
            return mResults.size();
        } else {
            return COUNT_NO_ITEM;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (!mResults.isEmpty()) {
            if (mResults.get(position).getAmount() > 0) {
                return SAVING;
            } else {
                return EXPENSE;
            }
        } else {
            return NO_ITEM;
        }
    }


    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.deleteFromRealm(position);
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
        resetFilterIfEmpty();
    }

    private void resetFilterIfEmpty() {
        if (mResults.isEmpty() && (mFilterOption == Filter.SAVING || mFilterOption == Filter.EXPENSE)) {
            mResetListener.onReset();
        }
    }

    public static class ExpenseHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView mPurpose;
        TextView mAmount;
        TextView mDate;
        EditListener mEditListener;
        boolean isPositive = false;

        public ExpenseHolder(View itemView, EditListener editListener, boolean bool) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mPurpose = (TextView) itemView.findViewById(R.id.tv_purpose);
            mAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            mDate = (TextView) itemView.findViewById(R.id.tv_date);
            mEditListener = editListener;
            isPositive = bool;
        }

        public void setWhen(long when) {
            mDate.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }


        @Override
        public boolean onLongClick(View v) {
            mEditListener.onEdit(getAdapterPosition());
            return true;
        }
    }

    public static class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }
}
