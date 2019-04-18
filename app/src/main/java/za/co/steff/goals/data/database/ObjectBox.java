package za.co.steff.goals.data.database;

import android.content.Context;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import za.co.steff.goals.data.database.entity.MyObjectBox;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        new AndroidObjectBrowser(boxStore).start(context);
    }

    public static BoxStore get() { return boxStore; }

}
