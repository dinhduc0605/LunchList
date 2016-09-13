package tutorial.apt.lunchlist.ui.activity;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tutorial.apt.lunchlist.R;
import tutorial.apt.lunchlist.model.Restaurant;

public class LunchList extends TabActivity {
    private List<Restaurant> mRestaurants = new ArrayList<>();
    private EditText mEdtName, mEdtAddress, mEdtNote;
    private Button mBtnSave;
    private RadioGroup mTypeRadioGroup;
    private ListView mLvRestaurants;
    private RestaurantAdapter mAdapter;
    private Restaurant mCurrent;
    private int mProgress;
    private Restaurant[] mRestaurantList = {
            new Restaurant("Hà Nội", "Thủ đô của nước Cộng hoà Xã hội chủ nghĩa Việt Nam", "sit_down", ""),
            new Restaurant("Bắc Kinh", "Thành phố lớn thứ hai của Trung Quốc", "sit_down", ""),
            new Restaurant("Washington, D.C", "Thủ đô của Hoa Kỳ", "sit_down", ""),
            new Restaurant("New York", "Nằm trong vùng Đông Bắc Hoa Kỳ", "sit_down", ""),
            new Restaurant("Hồ Chí Minh", "Thành phố lớn nhất Việt Nam", "sit_down", "")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        initView();
        getWidgetControl();
    }

    private void getWidgetControl() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(mEdtName.getText().toString());
                restaurant.setAddress(mEdtAddress.getText().toString());
                restaurant.setNotes(mEdtNote.getText().toString());
                switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_take_out:
                        restaurant.setType("take_out");
                        break;
                    case R.id.rb_sit_down:
                        restaurant.setType("sit_down");
                        break;
                    case R.id.rb_delivery:
                        restaurant.setType("delivery");
                        break;
                }
                mAdapter.add(restaurant);
                mCurrent = restaurant;
            }
        });
    }

    private void initView() {
        mBtnSave = (Button) findViewById(R.id.btn_main_save);
        mEdtName = (EditText) findViewById(R.id.edt_main_name);
        mEdtAddress = (EditText) findViewById(R.id.edt_main_address);
        mEdtNote = (EditText) findViewById(R.id.edt_main_notes);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.rg_main_types);
        mLvRestaurants = (ListView) findViewById(R.id.lv_restaurants);
        mAdapter = new RestaurantAdapter();
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

    private Runnable longTask = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                doSomeLongWork(i, 2000);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgressBarVisibility(false);
                }
            });
        }
    };

    private void doSomeLongWork(final int i, final int incr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress += incr;
                setProgress(mProgress);
                mAdapter.add(mRestaurantList[i]);
            }
        });
        SystemClock.sleep(500);
    }

    private AdapterView.OnItemClickListener onItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Restaurant r = mRestaurants.get(position);
            mEdtName.setText(r.getName());
            mEdtAddress.setText(r.getAddress());
            if (r.getType().equals("sit_down")) {
                mTypeRadioGroup.check(R.id.rb_sit_down);
            } else if (r.getType().equals("take_out")) {
                mTypeRadioGroup.check(R.id.rb_take_out);
            } else {
                mTypeRadioGroup.check(R.id.rb_delivery);
            }
            getTabHost().setCurrentTab(1);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toast) {
            String message = "No Restaurant selected";
            if (mCurrent != null) {
                message = mCurrent.getNotes();
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.run) {
            setProgressBarVisibility(true);
            new Thread(longTask).start();
        }
        return super.onOptionsItemSelected(item);
    }

    class RestaurantAdapter extends ArrayAdapter<Restaurant> {

        public RestaurantAdapter() {
            super(LunchList.this, R.layout.row, mRestaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            RestaurantHolder holder = null;
            if (row == null) {
                row = getLayoutInflater().inflate(R.layout.row, parent, false);
                holder = new RestaurantHolder(row);
                row.setTag(holder);
            } else {
                holder = (RestaurantHolder) row.getTag();
            }
            holder.populateFrom(mRestaurants.get(position));
            return row;
        }
    }

    static class RestaurantHolder {
        private ImageView mIcon;
        private TextView mName;
        private TextView mAddress;

        public RestaurantHolder(View row) {
            mIcon = (ImageView) row.findViewById(R.id.icon);
            mName = (TextView) row.findViewById(R.id.name);
            mAddress = (TextView) row.findViewById(R.id.address);
        }

        void populateFrom(Restaurant restaurant) {
            mName.setText(restaurant.getName());
            mAddress.setText(restaurant.getAddress());
            if (restaurant.getType().equals("sit_down")) {
                mIcon.setImageResource(R.drawable.ball_red);
            } else if (restaurant.getType().equals("take_out")) {
                mIcon.setImageResource(R.drawable.ball_yellow);
            } else {
                mIcon.setImageResource(R.drawable.ball_green);
            }
        }
    }

}
