package org.railsschool.tiramisu.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import org.railsschool.tiramisu.R;
import org.railsschool.tiramisu.views.events.ProgressDoneEvent;
import org.railsschool.tiramisu.views.events.ProgressForkEvent;

import de.greenrobot.event.EventBus;

/**
 * @class ProgressActivity
 * @brief
 */
public abstract class ProgressActivity extends Activity {
    //private final static Object _spinnerLock = new Object();

    private ProgressDialog _spinner;
    private int            _backgroundThreads;
    private Handler        _backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _spinner = new ProgressDialog(this);
        _spinner.setIndeterminate(true);
        _spinner.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        _backgroundThreads = 0;

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        if (_spinner != null && _spinner.isShowing()) {
            _spinner.dismiss();
        }
    }

    public void onEventMainThread(ProgressForkEvent event) {
        _backgroundThreads++;

        if (_backgroundThreads == 1) {
            _backgroundHandler = new Handler();
            _backgroundHandler.postDelayed(
                () -> {
                    _backgroundHandler = null;
                    _spinner.show();
                    _spinner.setContentView(R.layout.spinner); // Must be called after show()
                },
                500
            );
        }
    }

    public void onEventMainThread(ProgressDoneEvent event) {
        _backgroundThreads--;

        if (_backgroundThreads == 0) {
            if (_backgroundHandler == null) {
                _spinner.dismiss();
            } else {
                _backgroundHandler.removeCallbacksAndMessages(null);
            }
        }
    }
}
