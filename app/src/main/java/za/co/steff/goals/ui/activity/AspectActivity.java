package za.co.steff.goals.ui.activity;

import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import za.co.steff.goals.R;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Palette;
import za.co.steff.goals.ui.adapter.AspectListAdapter;
import za.co.steff.goals.ui.dialog.ConfirmationDialog;
import za.co.steff.goals.ui.dialog.PaletteDialog;

public class AspectActivity extends BaseActivity {

    @BindView(R.id.listAspects)
    ListView listAspects;
    @BindView(R.id.fabAddAspect)
    FloatingActionButton fabAddAspect;

    private AspectListAdapter adapter;

    private boolean fabHidden = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_aspect;
    }

    @Override
    protected void initialise() {
        // Set title
        toolbar.setTitle("Add a Challenge");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Initialise screen
        populateListData();
    }

    private void populateListData() {
        List<Aspect> aspects = ObjectBox.get().boxFor(Aspect.class).getAll();

        // Create list adapter and assign to list view
        adapter = new AspectListAdapter(AspectActivity.this, aspects);
        adapter.setListener(aspectListEventListener);
        listAspects.setAdapter(adapter);
        listAspects.setOnScrollListener(listScrollListener);
    }

    @OnClick(R.id.fabAddAspect)
    void onAspectFabClicked() {
        // Notify adapter that we're adding
        adapter.setAdding(true);
        adapter.notifyDataSetInvalidated();

        // Hide fab and scroll to bottom of list
        listAspects.smoothScrollToPosition(adapter.getCount() - 1);
    }

    private void removeAspect(Aspect aspect) {
        ObjectBox.get().boxFor(Aspect.class).remove(aspect);
        populateListData();
    }

    private AbsListView.OnScrollListener listScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(! listAspects.canScrollVertically(1)){
                fabAddAspect.setVisibility(View.GONE);
                fabHidden = true;
            } else if(listAspects.canScrollVertically(1) && fabHidden) {
                fabAddAspect.setVisibility(View.VISIBLE);
                fabHidden = false;
            }
        }
    };

    private AspectListAdapter.AspectListEventListener aspectListEventListener = new AspectListAdapter.AspectListEventListener() {
        @Override
        public void onRemoveAspectClicked(Aspect aspect) {
            ConfirmationDialog dialog = ConfirmationDialog.newInstance();
            dialog.setMessage("Are you sure you want to delete this aspect?");
            dialog.setListener((confirmed -> {
                if(confirmed) {
                    removeAspect(aspect);
                }
            }));
            dialog.show(getSupportFragmentManager(), "Confirm");
        }

        @Override
        public void onPaletteSelectionRequested() {
            PaletteDialog dialog = PaletteDialog.newInstance();
            dialog.setListener(paletteDialogEventListener);
            dialog.show(getSupportFragmentManager(), "Select Palette");
        }

        @Override
        public void onAspectAdded() {
            populateListData();
            listAspects.setSelection(adapter.getCount() - 1);
        }
    };

    private PaletteDialog.PaletteDialogEventListener paletteDialogEventListener = new PaletteDialog.PaletteDialogEventListener() {
        @Override
        public void onPaletteSelectionConfirmed(Palette palette) {
            adapter.setSelectedPalette(palette);
            listAspects.setSelection(adapter.getCount() - 1);
        }
    };
}
