package com.jtournie.cml.tasitrack;

import android.preference.ListPreference;
import android.content.Context;
import android.util.AttributeSet;
import android.preference.Preference;

/**
 * Created by jtournie on 22/12/14.
 */
public class ListPreferenceShowSummary extends ListPreference {


    private final static String TAG = ListPreferenceShowSummary.class.getName();
    private String sCurrentValue;

    public ListPreferenceShowSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListPreferenceShowSummary(Context context) {
        super(context);
        init();
    }

    private void init() {

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object arg1) {
                arg0.setSummary(arg1.toString()/*getEntry()*/);
                return true;
            }
        });
    }

    @Override
    public CharSequence getSummary() {
        return super.getEntry();
    }

}
