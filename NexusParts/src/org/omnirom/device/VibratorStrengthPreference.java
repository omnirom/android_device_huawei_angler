/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.omnirom.device;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.database.ContentObserver;
import android.preference.SeekBarDialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.os.Vibrator;

public class VibratorStrengthPreference extends SeekBarDialogPreference implements
        SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private int mOldStrength;
    private int mMinValue;
    private int mMaxValue;
    private int mDeltaValue;
    private Vibrator mVibrator;
    private Button mTestButton;

    private static String LEVEL_PATH_LIGHT  = "/sys/devices/virtual/timed_output/vibrator/vmax_mv_light";
    private static String LEVEL_PATH_STRONG = "/sys/devices/virtual/timed_output/vibrator/vmax_mv_strong";
    private static final long testVibrationPattern[] = {0,250};

    public VibratorStrengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinValue = 600;
        mMaxValue = 3596;
        mDeltaValue = 300;

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        setDialogLayoutResource(R.layout.preference_dialog_vibrator_strength);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mOldStrength = Integer.parseInt(getValue(getContext()));
        mSeekBar = getSeekBar(view);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mOldStrength - mMinValue);

        mTestButton = (Button) view.findViewById(R.id.vib_test);
        if (!mVibrator.hasVibrator()){
            mTestButton.setEnabled(false);
        } else {
            mTestButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mVibrator.vibrate(testVibrationPattern, -1);
                }
            });
        }
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public static boolean isSupported() {
        return Utils.fileWritable(LEVEL_PATH_STRONG);
    }

	public static String getValue(Context context) {
		return Utils.getFileValue(LEVEL_PATH_STRONG, "1800");
	}

	private void setValueStrong(String newValue) {
	    Utils.writeValue(LEVEL_PATH_STRONG, newValue);
	}

        private void setValueLight(String newValue) {
            Utils.writeValue(LEVEL_PATH_LIGHT, newValue);
        }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        String storedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_VIBSTRENGTH, "1800"); 
        Utils.writeValue(LEVEL_PATH_STRONG, storedValue);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        setValueStrong(String.valueOf(progress + mMinValue));
        setValueLight(String.valueOf(progress - mDeltaValue));
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // NA
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            final int valueStrong = mSeekBar.getProgress() + mMinValue;
            final int valueLight = mSeekBar.getProgress() - mDeltaValue;
            setValueStrong(String.valueOf(valueStrong));
            setValueLight(String.valueOf(valueLight));
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.putString(DeviceSettings.KEY_VIBSTRENGTH, String.valueOf(valueStrong));
            editor.commit();
        } else {
            restoreOldState();
        }
        mVibrator.cancel();
    }

    private void restoreOldState() {
        setValueStrong(String.valueOf(mOldStrength));
    }
}

