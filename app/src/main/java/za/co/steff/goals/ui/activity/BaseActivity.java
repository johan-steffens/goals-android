package za.co.steff.goals.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import za.co.steff.goals.R;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * Abstract method that returns the layout content view to assign in onCreate.
     *
     * @return This activity's content view.
     */
    protected abstract int getContentView();

    /**
     * Any logic to run after setting the content view, before binded views become available.
     *
     * Perfect place to start Services, register BroadcastReceivers, etc.
     */
    protected void preInitialize(Bundle savedInstanceState) {}

    /**
     * Logic after binded views become available.
     */
    protected abstract void initialise();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        // Set the content view
        setContentView(getContentView());

        // Pre-initialization tasks
        preInitialize(savedInstanceState);

        // Bind the view
        ButterKnife.bind(this);

        // Initialize the view
        initialise();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

}
