package za.co.steff.goals.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.co.steff.goals.R;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.database.entity.Palette;
import za.co.steff.goals.ui.adapter.PaletteListAdapter;

public class PaletteDialog extends DialogFragment {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.listPalette)
    ListView listPalette;
    @BindView(R.id.btnChoose)
    AppCompatButton btnChoose;

    private Palette selection;
    private PaletteDialogEventListener listener;

    public static PaletteDialog newInstance() {
        PaletteDialog frag = new PaletteDialog();
        Bundle args = new Bundle();
        // args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate and bind the view
        View view = inflater.inflate(R.layout.dialog_palette, container);
        ButterKnife.bind(this, view);

        // Transparent dialog
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialise view
        populateListData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void populateListData() {
        // Get data from database
        List<Palette> palettes = ObjectBox.get().boxFor(Palette.class).getAll();

        // Create adapter and assign to list
        PaletteListAdapter adapter = new PaletteListAdapter(getContext(), palettes);
        adapter.setListener(paletteListEventListener);
        listPalette.setAdapter(adapter);
    }

    private PaletteListAdapter.PaletteListEventListener paletteListEventListener = new PaletteListAdapter.PaletteListEventListener() {
        @Override
        public void onPaletteSelected(Palette palette) {
            selection = palette;
            btnChoose.setEnabled(true);
        }
    };

    @OnClick(R.id.btnChoose)
    void onChooseButtonClicked() {
        // Notify listeners
        if(listener != null) {
            listener.onPaletteSelectionConfirmed(selection);
        }

        // Dismiss the dialog
        dismiss();
    }

    public void setListener(PaletteDialogEventListener listener) {
        this.listener = listener;
    }

    public interface PaletteDialogEventListener {

        void onPaletteSelectionConfirmed(Palette palette);

    }

}
