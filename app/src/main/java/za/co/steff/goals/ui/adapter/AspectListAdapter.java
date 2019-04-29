package za.co.steff.goals.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.PaletteType;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Palette;
import za.co.steff.goals.ui.dialog.PaletteDialog;

public class AspectListAdapter extends BaseAdapter {

    private Context context;
    private List<Aspect> aspects;

    private boolean adding = false;
    private Palette selectedPalette;
    private View addingView;

    private AspectListEventListener listener;

    public AspectListAdapter(Context context, List<Aspect> aspects) {
        this.context = context;
        this.aspects = aspects;
    }

    @Override
    public int getCount() {
        if(aspects != null)
            return aspects.size() + (adding ? 1 : 0);

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(aspects != null)
            return aspects.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Render the add block at the end of the list
        if(position == aspects.size() && adding) {
            return renderAddField(position, convertView, parent);
        }

        // Don't reuse add view
        if(convertView != null && convertView.getTag() instanceof AddViewHolder) {
            convertView = null;
        }

        // Inflate view for the first time
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_aspects_list, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Aspect aspect = (Aspect) getItem(position);
        if(aspect != null) {
            holder.txtAspect.setText(aspect.getName());
            holder.txtAspect.setTextColor(context.getResources().getColor(PaletteType.getDarkRes(aspect.getPalette().getTarget().getPaletteType())));
            holder.viewDivider.setBackgroundColor(context.getResources().getColor(PaletteType.getDarkRes(aspect.getPalette().getTarget().getPaletteType())));

            if(! Strings.isNullOrEmpty(aspect.getGoal())) {
                holder.txtGoal.setText(aspect.getGoal());
                holder.txtGoal.setVisibility(View.VISIBLE);
            } else {
                holder.txtGoal.setVisibility(View.GONE);
            }

            holder.btnRemove.setOnClickListener((view) -> {
                if(listener != null) {
                    listener.onRemoveAspectClicked(aspect);
                }
                ((ViewGroup) view.getParent()).performClick();
            });
        }

        // Set holder as tag of view and return
        convertView.setTag(holder);
        return convertView;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    private View renderAddField(int position, View convertView, ViewGroup parent) {
        AddViewHolder holder;
        if(addingView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            addingView = inflater.inflate(R.layout.item_aspects_list_add, null);
            holder = new AddViewHolder(addingView);
        } else {
            holder = (AddViewHolder) addingView.getTag();
        }

        // Populate view controls
        holder.btnPalette.setOnClickListener((view -> {
            if(listener != null)
                listener.onPaletteSelectionRequested();
        }));
        holder.btnAdd.setOnClickListener((view) -> {
            // Validate aspect
            if(Strings.isNullOrEmpty(holder.editAspect.getText().toString())) {
                holder.editAspect.setError("Please enter an aspect");
                return;
            }

            // Otherwise add it
            addAspect(holder.editAspect.getText().toString(), holder.editGoal.getText().toString());
        });

        // Update selected palette
        if(selectedPalette != null) {
            holder.btnPalette.setBackgroundColor(context.getResources().getColor(PaletteType.getDarkRes(selectedPalette.getPaletteType())));
        }

        addingView.setTag(holder);
        convertView = addingView;
        return convertView;
    }

    private void addAspect(String name, String goal) {
        // Build and add aspect
        Aspect aspect = new Aspect();
        aspect.setName(name);
        aspect.getPalette().setTarget(selectedPalette != null ? selectedPalette : Palette.getForPaletteType(PaletteType.Pink, ObjectBox.get().boxFor(Palette.class)));
        aspect.setGoal(goal);
        ObjectBox.get().boxFor(Aspect.class).put(aspect);
        selectedPalette = null;

        // Notify view to refresh
        if(listener != null)
            listener.onAspectAdded();
        adding = false;
    }

    public void setSelectedPalette(Palette selectedPalette) {
        this.selectedPalette = selectedPalette;
        notifyDataSetInvalidated();
    }

    public void setListener(AspectListEventListener listener) {
        this.listener = listener;
    }

    class ViewHolder {

        @BindView(R.id.txtAspect)
        TextView txtAspect;
        @BindView(R.id.txtGoal)
        TextView txtGoal;
        @BindView(R.id.viewDivider)
        View viewDivider;
        @BindView(R.id.btnRemove)
        View btnRemove;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    class AddViewHolder {

        @BindView(R.id.editAspect)
        EditText editAspect;
        @BindView(R.id.editGoal)
        EditText editGoal;
        @BindView(R.id.btnPalette)
        View btnPalette;
        @BindView(R.id.btnAdd)
        View btnAdd;

        AddViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface AspectListEventListener {

        void onRemoveAspectClicked(Aspect aspect);
        void onPaletteSelectionRequested();
        void onAspectAdded();

    }
}
