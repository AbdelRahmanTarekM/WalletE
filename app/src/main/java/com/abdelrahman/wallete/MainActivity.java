package com.abdelrahman.wallete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abdelrahman.wallete.Widgets.WalletRecyclerView;
import com.abdelrahman.wallete.adapters.AdapterExpenses;
import com.abdelrahman.wallete.adapters.ResetListener;
import com.abdelrahman.wallete.adapters.SimpleTouchCallBack;
import com.abdelrahman.wallete.beans.Expense;
import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WalletRecyclerView mRecycler;
    private LinearLayout mBottomBar;
    private TextView mTotal;
    Realm mRealm;
    AdapterExpenses mAdapter;
    RealmResults<Expense> mResults;

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            mAdapter.update(mResults);
            double total=0;
            for (Expense expense :
                    mResults) {
                total +=expense.getAmount();

            }
            mTotal.setText(Double.toString(total));
        }
    };

    private ResetListener mResetListener=new ResetListener() {
        @Override
        public void onReset() {
            Log.d("Abdo","onReset called");
            AppWalletE.save(MainActivity.this,Filter.NONE);
            loadResults(Filter.NONE);
        }
    };

    private EditListener mEditListener=new EditListener() {
        @Override
        public void onEdit(int position) {
            showDialogEdit(position);
        }
    };

    private void showDialogEdit(int position) {
        DialogEdit dialogEdit=new DialogEdit();
        Bundle bundle=new Bundle();
        Expense mExpense=mResults.get(position);
        bundle.putDouble("AMOUNT",mExpense.getAmount());
        bundle.putString("PURPOSE",mExpense.getPurpose());
        bundle.putLong("ADDED-DATE",mExpense.getAddedDate());
        bundle.putLong("WHEN",mExpense.getWhen());
        dialogEdit.setArguments(bundle);
        dialogEdit.show(getSupportFragmentManager(),"Edit");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init vars
        mRealm = Realm.getDefaultInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecycler = (WalletRecyclerView) findViewById(R.id.rv_expenses);
        mBottomBar=(LinearLayout)findViewById(R.id.bottom_bar_layout);
        mTotal=(TextView)findViewById(R.id.tv_total_amount);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        int filterResult = AppWalletE.load(this);
        loadResults(filterResult);
        mAdapter = new AdapterExpenses(this, mRealm, mResults,mResetListener,mEditListener);
        //Setting RecyclerView
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(mAdapter);

        SimpleTouchCallBack callBack = new SimpleTouchCallBack(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mRecycler);
        //UI
        setSupportActionBar(mToolbar);
        initBackgroundImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_all:
                filterOption=Filter.NONE;
                break;
            case R.id.action_saving:
                filterOption = Filter.SAVING;
                break;
            case R.id.action_expenses:
                filterOption = Filter.EXPENSE;
                break;
            default:
                handled = false;
                break;
        }
        AppWalletE.save(this,filterOption);
        loadResults(filterOption);
        return handled;
    }

    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Expense.class).findAllAsync();
                break;
            case Filter.EXPENSE:
                mResults = mRealm.where(Expense.class).lessThan("amount", 0d).findAllAsync();
                break;
            case Filter.SAVING:
                mResults = mRealm.where(Expense.class).greaterThan("amount", 0d).findAllAsync();
                break;
        }
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }



    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "ADD");
    }

}
