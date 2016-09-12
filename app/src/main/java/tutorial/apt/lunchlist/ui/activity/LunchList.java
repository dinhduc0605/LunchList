package tutorial.apt.lunchlist.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import tutorial.apt.lunchlist.R;
import tutorial.apt.lunchlist.model.Restaurant;

public class LunchList extends AppCompatActivity {
    private List<Restaurant> mRestaurants = new ArrayList<>();
    private EditText mEdtName, mEdtAddress;
    private Button mBtnSave;
    private RadioGroup mTypeRadioGroup;
    private ListView mLvRestaurants;
    private ArrayAdapter<Restaurant> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_take_out:
                        restaurant.setType("take-out");
                        break;
                    case R.id.rb_sit_down:
                        restaurant.setType("sit-down");
                        break;
                    case R.id.rb_delivery:
                        restaurant.setType("delivery");
                        break;
                }
                mAdapter.add(restaurant);
            }
        });
    }

    private void initView() {
        mBtnSave = (Button) findViewById(R.id.btn_main_save);
        mEdtName = (EditText) findViewById(R.id.edt_main_name);
        mEdtAddress = (EditText) findViewById(R.id.edt_main_address);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.rg_main_types);
        mLvRestaurants = (ListView) findViewById(R.id.lv_restaurants);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRestaurants);
        mLvRestaurants.setAdapter(mAdapter);
    }

}
