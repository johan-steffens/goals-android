package za.co.steff.goals;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import io.objectbox.android.AndroidObjectBrowser;
import za.co.steff.goals.data.database.DatabaseSeeder;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.preferences.Preference;
import za.co.steff.goals.data.preferences.PreferencesUtil;

public class GoalsApplication extends Application {

    private static GoalsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        // Initialise JodaTime
        JodaTimeAndroid.init(this);

        // Initialise and seed the database
        ObjectBox.init(this);
        if(! PreferencesUtil.getBooleanPreference(this, Preference.KEY_DATABASE_INITIAL_MIGRATION, false)) {
            DatabaseSeeder seeder = new DatabaseSeeder(this, ObjectBox.get());
            seeder.seedInitialDataset();
            PreferencesUtil.setBooleanPreference(this, Preference.KEY_DATABASE_INITIAL_MIGRATION, true);
        }

        // Keep track of application instance
        instance = this;
    }

    public static GoalsApplication getInstance() {
        return instance;
    }
}
