package za.co.steff.goals.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.PaletteType;
import za.co.steff.goals.data.database.entity.Palette;

public class PaletteListAdapter extends BaseAdapter {

    private Context context;
    private List<Palette> palettes;
    private int selectedIndex;

    private PaletteListEventListener listener;

    public PaletteListAdapter(Context context, List<Palette> palettes) {
        this.context = context;
        this.palettes = palettes;
    }

    @Override
    public int getCount() {
        if(palettes != null)
            return palettes.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(palettes != null)
            return palettes.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Inflate view for the first time
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_palette_list, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Palette palette = (Palette) getItem(position);
        if(palette != null) {
            holder.txtName.setText(palette.getName());
            holder.txtName.setTextColor(context.getResources().getColor(PaletteType.getDarkRes(palette.getPaletteType())));
            holder.viewDivider.setBackgroundColor(context.getResources().getColor(PaletteType.getLightRes(palette.getPaletteType())));
            holder.imgChecked.setVisibility(position == selectedIndex ? View.VISIBLE : View.GONE);
            holder.imgUnchecked.setVisibility(position != selectedIndex ? View.VISIBLE : View.GONE);

            // Set selection listener
            convertView.setOnClickListener((view) -> {
                // Notify listeners
                if(listener != null) {
                    listener.onPaletteSelected(palette);
                }

                // Update selected row
                rowSelected(position);
            });
        }

        // Set holder as tag of view and return
        convertView.setTag(holder);
        return convertView;
    }

    public void setListener(PaletteListEventListener listener) {
        this.listener = listener;
    }

    private void rowSelected(int position) {
        selectedIndex = position;
        notifyDataSetChanged();
    }

    class ViewHolder {

        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.viewDivider)
        View viewDivider;
        @BindView(R.id.imgUnchecked)
        ImageView imgUnchecked;
        @BindView(R.id.imgChecked)
        ImageView imgChecked;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public interface PaletteListEventListener {

        void onPaletteSelected(Palette palette);

    }

}
