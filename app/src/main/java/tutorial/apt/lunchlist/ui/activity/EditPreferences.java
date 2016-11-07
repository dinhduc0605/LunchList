package tutorial.apt.lunchlist.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import tutorial.apt.lunchlist.R;

/**
 * Created by dinhduc on 07/11/2016.
 */

public class EditPreferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
