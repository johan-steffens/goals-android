package za.co.steff.goals.ui.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import za.co.steff.goals.data.database.entity.Aspect;

public class AspectSpinnerAdapter extends ArrayAdapter<String> {

    private List<Aspect> aspects;

    public AspectSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Aspect> aspects) {
        super(context, resource);
        this.aspects = aspects;
        invalidateAspects();
    }

    private void invalidateAspects() {
        List<String> items = new LinkedList<>();
        if(aspects != null) for(Aspect aspect : aspects) {
            items.add(aspect.getName());
        }
        clear();
        addAll(items);
    }

    public Aspect getAspect(int position) {
        return aspects.get(position);
    }

}
