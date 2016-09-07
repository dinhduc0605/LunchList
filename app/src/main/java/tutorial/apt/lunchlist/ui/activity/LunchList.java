package tutorial.apt.lunchlist.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import tutorial.apt.lunchlist.R;
import tutorial.apt.lunchlist.model.Restaurant;

public class LunchList extends AppCompatActivity {
    private Restaurant mRestaurant = new Restaurant();
    private EditText mEdtName, mEdtAddress;
    private Button mBtnSave;
    private RadioGroup mTypeRadioGroup;

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
                mRestaurant.setName(mEdtName.getText().toString());
                mRestaurant.setAddress(mEdtAddress.getText().toString());
            }
        });
        switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_take_out:
                mRestaurant.setType("take-out");
                break;
            case R.id.rb_sit_down:
                mRestaurant.setType("sit-down");
                break;
            case R.id.rb_delivery:
                mRestaurant.setType("delivery");
                break;
        }
    }

    private void initView() {
        mBtnSave = (Button) findViewById(R.id.btn_main_save);
        mEdtName = (EditText) findViewById(R.id.edt_main_name);
        mEdtAddress = (EditText) findViewById(R.id.edt_main_address);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.rg_main_types);
    }

}
