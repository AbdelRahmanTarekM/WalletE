package com.abdelrahman.wallete.Widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by AbdelRahman on 8/29/2016.
 */
public class WalletRecyclerView extends RecyclerView {

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {


        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);


        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {


        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {

        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {

        }
    };

    public WalletRecyclerView(Context context) {
        super(context);
    }

    public WalletRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WalletRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }
}
