package za.co.steff.goals.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.ChallengeType;
import za.co.steff.goals.common.util.DateUtil;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Challenge;
import za.co.steff.goals.ui.adapter.ChallengesListAdapter;
import za.co.steff.goals.ui.dialog.ConfirmationDialog;
import za.co.steff.goals.ui.dialog.PaletteDialog;
import za.co.steff.goals.ui.dialog.SingleInputDialog;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.layoutDrawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.viewNavigation)
    NavigationView navigationView;
    @BindView(R.id.listChallenges)
    ListView listChallenges;

    private View challengeActionSheet;
    private BottomSheetDialog dialog;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected int getContentView() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initialise() {
        // Set title
        toolbar.setTitle("Success Toolset");

        // Initialise drawer toggle and add to drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardActivity.this, drawerLayout, toolbar, R.string.menu_open,R.string.menu_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                toolbar.setNavigationIcon(R.drawable.ic_back_white);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                toolbar.setNavigationIcon(R.drawable.ic_menu_white);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void populateListData() {
        // Get list of aspects
        List<Aspect> aspects = ObjectBox.get().boxFor(Aspect.class).getAll();
        Collections.sort(aspects, (aspect, compare) -> {
            if(! aspect.getChallenge().isNull() && compare.getChallenge().isNull())
                return -1;
            else if(aspect.getChallenge().isNull() && ! compare.getChallenge().isNull())
                return 1;
            return 0;
        });

        // Create adapter and set to list view
        ChallengesListAdapter adapter = new ChallengesListAdapter(DashboardActivity.this, aspects);
        adapter.setListener(challengesListEventListener);
        listChallenges.setAdapter(adapter);

        // Add the footer
        if(listChallenges.getFooterViewsCount() == 0) {
            View footer = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.footer_challenges_list, null);
            FooterViewHolder holder = new FooterViewHolder(footer);
            holder.btnAspects.setOnClickListener(onAspectsButtonClickListener);
            holder.btnAddChallenge.setOnClickListener(onAddChallengeButtonClicked);
            footer.setTag(holder);
            listChallenges.addFooterView(footer);
        }
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.btnMenuManageAspects) {
                Intent i = new Intent(DashboardActivity.this, AspectActivity.class);
                startActivity(i);
                drawerLayout.closeDrawers();
                return true;
            } else if(menuItem.getItemId() == R.id.btnMenuSettings) {
                Log.e("MENU", "btnMenuSettings clicked");
                return true;
            }
            return false;
        }
    };

    private ChallengesListAdapter.ChallengesListEventListener challengesListEventListener = new ChallengesListAdapter.ChallengesListEventListener() {
        @Override
        public void onChallengeItemSelected(Aspect aspect, int position) {
            // If no challenge is set, add a challenge
            if(aspect.getChallenge().isNull()) {
                Intent i = new Intent(DashboardActivity.this, AddChallengeActivity.class);
                i.putExtra(AddChallengeActivity.EXTRAS_KEY_ASPECT_ID, aspect.getId());
                startActivity(i);
                return;
            }

            // Otherwise get the challenge
            Challenge challenge = aspect.getChallenge().getTarget();

            // Initialise sheet view
            ActionSheetViewHolder holder;
            if(challengeActionSheet == null) {
                challengeActionSheet = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.sheet_challenges_list_actions, null);
                holder = new ActionSheetViewHolder(challengeActionSheet);
            } else {
                holder = (ActionSheetViewHolder) challengeActionSheet.getTag();
            }

            // Set completed button
            if(challenge.getType() == ChallengeType.Mission || challenge.getType() == ChallengeType.Daily || challenge.getGoal() == challenge.getProgress()) {
                holder.btnSetCompleted.setVisibility(View.VISIBLE);

                if(challenge.getType() == ChallengeType.Mission || challenge.getGoal() == challenge.getProgress()) {
                    holder.txtSetCompletedTitle.setText(challenge.getGoal() != challenge.getProgress() ? "Complete challenge" : "Clear challenge");
                    holder.imgSetCompletedIcon.setImageDrawable(challenge.getGoal() != challenge.getProgress() ? getResources().getDrawable(R.drawable.ic_check_white) : getResources().getDrawable(R.drawable.ic_clear_white));
                    holder.btnSetCompleted.setOnClickListener(view -> {
                        if(challenge.getGoal() == challenge.getProgress()) {
                            aspect.getChallenge().setTarget(null);
                            ObjectBox.get().boxFor(Aspect.class).put(aspect);
                        } else {
                            challenge.setProgress(challenge.getGoal());
                            ObjectBox.get().boxFor(Challenge.class).put(challenge);
                        }
                        populateListData();
                        dialog.dismiss();
                    });
                } else {
                    holder.txtSetCompletedTitle.setText(DateUtil.isToday(challenge.getUpdatedAt()) ? "Set incomplete today" : "Set completed today");
                    holder.imgSetCompletedIcon.setImageDrawable(DateUtil.isToday(challenge.getUpdatedAt()) ? getResources().getDrawable(R.drawable.ic_clear_white) : getResources().getDrawable(R.drawable.ic_check_white));
                    holder.btnSetCompleted.setOnClickListener(view -> {
                        if(DateUtil.isToday(challenge.getUpdatedAt())) {
                            challenge.setUpdatedAt(DateTime.now().minusDays(1));
                            challenge.setProgress(challenge.getProgress() - 1);
                        } else {
                            challenge.setUpdatedAt(DateTime.now());
                            challenge.setProgress(challenge.getProgress() + 1);
                        }

                        ObjectBox.get().boxFor(Challenge.class).put(challenge);
                        populateListData();
                        dialog.dismiss();
                    });
                }
            } else {
                holder.btnSetCompleted.setVisibility(View.GONE);
            }

            // Increment by one button
            if(challenge.getType() == ChallengeType.Score || challenge.getType() == ChallengeType.Percentage) {
                holder.btnIncrementOne.setVisibility(View.VISIBLE);
                holder.btnIncrementOne.setOnClickListener(view -> {
                    int newProgress = challenge.getProgress() + 1;
                    challenge.setProgress(Math.min(challenge.getGoal(), newProgress));
                    ObjectBox.get().boxFor(Challenge.class).put(challenge);
                    populateListData();
                    dialog.dismiss();
                });
            } else {
                holder.btnIncrementOne.setVisibility(View.GONE);
            }

            // Increment by ten button
            if(challenge.getType() == ChallengeType.Score || challenge.getType() == ChallengeType.Percentage) {
                holder.btnIncrementTen.setVisibility(View.VISIBLE);
                holder.btnIncrementTen.setOnClickListener(view -> {
                    int newProgress = challenge.getProgress() + 10;
                    challenge.setProgress(Math.min(challenge.getGoal(), newProgress));
                    ObjectBox.get().boxFor(Challenge.class).put(challenge);
                    populateListData();
                    dialog.dismiss();
                });
            } else {
                holder.btnIncrementTen.setVisibility(View.GONE);
            }

            // Set score button
            if(challenge.getType() == ChallengeType.Score) {
                holder.btnSetScore.setVisibility(View.VISIBLE);
                holder.btnSetScore.setOnClickListener(view -> {
                    SingleInputDialog scoreDialog = SingleInputDialog.newInstance();
                    scoreDialog.setNumericOnly(true);
                    scoreDialog.setTitle("Score");
                    scoreDialog.setMessage("Enter the score you want to set this challenge to");
                    scoreDialog.setPlaceholder("Score");
                    scoreDialog.setListener(input -> {
                        challenge.setProgress(Math.min(challenge.getGoal(), Integer.parseInt(input)));
                        ObjectBox.get().boxFor(Challenge.class).put(challenge);
                        populateListData();
                    });
                    scoreDialog.show(getSupportFragmentManager(), "Input");
                    dialog.dismiss();
                });
            } else {
                holder.btnSetScore.setVisibility(View.GONE);
            }

            // Abandon challenge button
            holder.btnAbandon.setOnClickListener(view -> {
                ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance();
                confirmationDialog.setMessage("Are you sure you want to abandon the challenge?");
                confirmationDialog.setListener((confirmed -> {
                    if(confirmed) {
                        aspect.getChallenge().setTarget(null);
                        ObjectBox.get().boxFor(Aspect.class).put(aspect);
                        populateListData();
                    }
                }));
                confirmationDialog.show(getSupportFragmentManager(), "Confirm");
                dialog.dismiss();
            });

            challengeActionSheet.setTag(holder);

            // Show action sheet
            if(dialog == null) {
                dialog = new BottomSheetDialog(DashboardActivity.this);
                dialog.setContentView(challengeActionSheet);
            }

            dialog.show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh list
        if(listChallenges != null) {
            populateListData();
        }
    }

    private View.OnClickListener onAspectsButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(DashboardActivity.this, AspectActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener onAddChallengeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(DashboardActivity.this, AddChallengeActivity.class);
            startActivity(i);
        }
    };

    private void onSetCompletedClicked(Challenge challenge) {
        Log.e("Challenge", "onSetCompletedClicked() " + challenge.toString());
    }

    class FooterViewHolder {

        @BindView(R.id.btnAspects)
        AppCompatButton btnAspects;
        @BindView(R.id.btnAddChallenge)
        AppCompatButton btnAddChallenge;

        FooterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    class ActionSheetViewHolder {

        @BindView(R.id.btnSetCompleted)
        View btnSetCompleted;
        @BindView(R.id.imgSetCompletedIcon)
        ImageView imgSetCompletedIcon;
        @BindView(R.id.txtSetCompletedTitle)
        TextView txtSetCompletedTitle;
        @BindView(R.id.btnIncrementOne)
        View btnIncrementOne;
        @BindView(R.id.btnIncrementTen)
        View btnIncrementTen;
        @BindView(R.id.btnSetScore)
        View btnSetScore;
        @BindView(R.id.btnAbandon)
        View btnAbandon;

        ActionSheetViewHolder(View view) {
            // Bind the view
            ButterKnife.bind(this, view);

            // Display all buttons
            btnSetCompleted.setVisibility(View.VISIBLE);
            btnIncrementOne.setVisibility(View.VISIBLE);
            btnIncrementTen.setVisibility(View.VISIBLE);
            btnSetScore.setVisibility(View.VISIBLE);
            btnAbandon.setVisibility(View.VISIBLE);
        }

    }

}
