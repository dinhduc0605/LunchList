package tutorial.apt.lunchlist.ui.activity;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tutorial.apt.lunchlist.R;
import tutorial.apt.lunchlist.model.Restaurant;
import tutorial.apt.lunchlist.util.RestaurantHelper;

public class LunchList extends TabActivity {
    private List<Restaurant> mRestaurants = new ArrayList<>();
    private EditText mEdtName, mEdtAddress, mEdtNote;
    private Button mBtnSave;
    private RadioGroup mTypeRadioGroup;
    private ListView mLvRestaurants;
    private RestaurantAdapter mAdapter;
    private Restaurant mCurrent;
    private RestaurantHelper mHelper;
    private Cursor model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getWidgetControl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
    }

    private void getWidgetControl() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = null;
                switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_take_out:
                        type = "take_out";
                        break;
                    case R.id.rb_sit_down:
                        type = "sit_down";
                        break;
                    case R.id.rb_delivery:
                        type = "delivery";
                        break;
                }
                mHelper.insert(
                        mEdtName.getText().toString(),
                        mEdtAddress.getText().toString(),
                        type,
                        mEdtNote.getText().toString()
                );
                model.requery();
            }
        });
    }

    private void initView() {
        mHelper = new RestaurantHelper(this);

        mBtnSave = (Button) findViewById(R.id.btn_main_save);
        mEdtName = (EditText) findViewById(R.id.edt_main_name);
        mEdtAddress = (EditText) findViewById(R.id.edt_main_address);
        mEdtNote = (EditText) findViewById(R.id.edt_main_notes);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.rg_main_types);
        mLvRestaurants = (ListView) findViewById(R.id.lv_restaurants);

        model = mHelper.getAll();
        startManagingCursor(model);
        mAdapter = new RestaurantAdapter(model);
        mLvRestaurants.setAdapter(mAdapter);
        mLvRestaurants.setOnItemClickListener(onItemClicked);

        TabHost.TabSpec spec = getTabHost().newTabSpec("tab1");
        spec.setContent(R.id.lv_restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);

        spec = getTabHost().newTabSpec("tab2");
        spec.setContent(R.id.tbl_add);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);

        getTabHost().setCurrentTab(0);
    }

    private AdapterView.OnItemClickListener onItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            model.moveToPosition(position);
            mEdtName.setText(mHelper.getName(model));
            mEdtAddress.setText(mHelper.getAddress(model));
            if (mHelper.getType(model).equals("sit_down")) {
                mTypeRadioGroup.check(R.id.rb_sit_down);
            } else if (mHelper.getType(model).equals("take_out")) {
                mTypeRadioGroup.check(R.id.rb_take_out);
            } else {
                mTypeRadioGroup.check(R.id.rb_delivery);
            }
            getTabHost().setCurrentTab(1);
        }
    };

    class RestaurantAdapter extends CursorAdapter {
        RestaurantAdapter(Cursor c) {
            super(LunchList.this, c);
        }

        @Override
        public void bindView(View row, Context context, Cursor c) {
            RestaurantHolder holder = (RestaurantHolder) row.getTag();
            holder.populateFrom(c, mHelper);
        }

        @Override
        public View newView(Context context, Cursor c, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            RestaurantHolder holder = new RestaurantHolder(row);
            row.setTag(holder);
            return (row);
        }
    }

    static class RestaurantHolder {
        private TextView name = null;
        private TextView address = null;
        private ImageView icon = null;

        RestaurantHolder(View row) {
            name = (TextView) row.findViewById(R.id.name);
            address = (TextView) row.findViewById(R.id.address);
            icon = (ImageView) row.findViewById(R.id.icon);
        }

        void populateFrom(Cursor c, RestaurantHelper helper) {
            name.setText(helper.getName(c));
            address.setText(helper.getAddress(c));
            if (helper.getType(c).equals("sit_down")) {
                icon.setImageResource(R.drawable.ball_red);
            } else if (helper.getType(c).equals("take_out")) {
                icon.setImageResource(R.drawable.ball_yellow);
            } else {
                icon.setImageResource(R.drawable.ball_green);
            }
        }
    }

}
