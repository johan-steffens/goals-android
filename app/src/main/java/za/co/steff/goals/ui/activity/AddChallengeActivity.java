package za.co.steff.goals.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;

import org.greenrobot.essentials.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import za.co.steff.goals.R;
import za.co.steff.goals.common.type.ChallengeType;
import za.co.steff.goals.data.database.ObjectBox;
import za.co.steff.goals.data.database.entity.Aspect;
import za.co.steff.goals.data.database.entity.Challenge;
import za.co.steff.goals.ui.adapter.AspectSpinnerAdapter;
import za.co.steff.goals.ui.dialog.ConfirmationDialog;
import za.co.steff.goals.ui.view.MaterialSpinner;

public class AddChallengeActivity extends BaseActivity {

    public static final String EXTRAS_KEY_ASPECT_ID = "Goals.AddChallenge.AspectId";

    @BindView(R.id.txtAspect)
    TextView txtAspect;
    @BindView(R.id.spinType)
    MaterialSpinner spinType;
    @BindView(R.id.layoutScore)
    View layoutScore;
    @BindView(R.id.editScore)
    EditText editScore;
    @BindView(R.id.spinAspect)
    MaterialSpinner spinAspect;
    @BindView(R.id.editCompletionCondition)
    EditText editCompletionCondition;
    @BindView(R.id.editReward)
    EditText editReward;
    @BindView(R.id.switchExpiration)
    Switch switchExpiration;
    @BindView(R.id.btnExpirationDate)
    AppCompatButton btnExpirationDate;

    private long aspectId = 0L;
    private Aspect aspect;

    private AspectSpinnerAdapter aspectAdapter;
    private ArrayAdapter<String> typeAdapter;

    private DateTime expirationDate;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_challenge;
    }

    @Override
    protected void preInitialize(Bundle savedInstanceState) {
        if(getIntent().getExtras() != null) {
            aspectId = getIntent().getExtras().getLong(EXTRAS_KEY_ASPECT_ID, 0L);
        }
    }

    @Override
    protected void initialise() {
        // Set title
        toolbar.setTitle("Add a Challenge");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Initialize screen
        initialiseAspectSpinner();
        initialiseTypeAdapter();
    }

    private void initialiseAspectSpinner() {
        // If adding from Extras
        if(aspectId != 0L) {
            aspect = ObjectBox.get().boxFor(Aspect.class).get(aspectId);
            txtAspect.setText(aspect.getName());
            txtAspect.setVisibility(View.VISIBLE);
            spinAspect.setVisibility(View.GONE);
            return;
        }

        // Configure the array adapter
        aspectAdapter = new AspectSpinnerAdapter(AddChallengeActivity.this, android.R.layout.simple_spinner_dropdown_item, ObjectBox.get().boxFor(Aspect.class).getAll());

        // Assign adapter to spinner
        spinAspect.setAdapter(aspectAdapter);
    }

    private void initialiseTypeAdapter() {
        // Configure the array adapter
        typeAdapter = new ArrayAdapter<>(AddChallengeActivity.this, android.R.layout.simple_spinner_dropdown_item, ChallengeType.names());

        // Assign adapter to spinner
        spinType.setAdapter(typeAdapter);
        spinType.setListener(typeSpinnerEventListener);
    }

    private MaterialSpinner.MaterialSpinnerEventListener typeSpinnerEventListener = new MaterialSpinner.MaterialSpinnerEventListener() {
        @Override
        public void onItemSelected(int position) {
            if(position == ChallengeType.Score) {
                layoutScore.setVisibility(View.VISIBLE);
            } else if(position == ChallengeType.Daily) {
                layoutScore.setVisibility(View.GONE);
                switchExpiration.setChecked(true);
                switchExpiration.setEnabled(false);
            } else {
                layoutScore.setVisibility(View.GONE);
                switchExpiration.setEnabled(true);
            }
        }
    };

    @OnCheckedChanged(R.id.switchExpiration)
    void onExpirationSwitchCheckedChanged() {
        if(switchExpiration.isChecked()) {
            switchExpiration.setText("Will expire");
            btnExpirationDate.setEnabled(true);
            if(expirationDate != null)
                btnExpirationDate.setText(expirationDate.toLocalDate().toString());
        } else {
            switchExpiration.setText("Will not expire");
            btnExpirationDate.setEnabled(false);
            btnExpirationDate.setText("Select date");
        }
    }

    @OnClick(R.id.btnExpirationDate)
    void onExpirationDateButtonClicked() {
        DateTime potentialExpirationDate = expirationDate == null ? DateTime.now().plusWeeks(1) : expirationDate;
        DatePickerDialog datePicker = new DatePickerDialog(AddChallengeActivity.this, onDatePickedListener, potentialExpirationDate.getYear(), potentialExpirationDate.getMonthOfYear() - 1, potentialExpirationDate.getDayOfMonth());
        datePicker.show();
    }

    private DatePickerDialog.OnDateSetListener onDatePickedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            // Display Selected date in textbox
            expirationDate = DateTime.now();
            expirationDate = expirationDate.withDate(year, monthOfYear + 1, dayOfMonth);
            btnExpirationDate.setText(expirationDate.toLocalDate().toString());
        }
    };

    @OnClick(R.id.btnAdd)
    void onAddChallengeButtonClicked() {
        if(valid()) {
            // Create the challenge
            Challenge challenge = new Challenge();
            challenge.setType(spinType.getPosition());
            challenge.setCompletionCondition(editCompletionCondition.getText().toString());
            challenge.setGoal(100);
            challenge.setProgress(0);

            // Set aspect
            Aspect aspect;
            if(aspectId != 0L) {
                aspect = ObjectBox.get().boxFor(Aspect.class).get(aspectId);
            } else {
                aspect = aspectAdapter.getAspect(spinAspect.getPosition());
            }
            challenge.getAspect().setTarget(aspect);

            // Set goal based on challenge type
            if(spinType.getPosition() == ChallengeType.Score) {
                challenge.setGoal(Integer.parseInt(editScore.getText().toString()));
            } else if(spinType.getPosition() == ChallengeType.Daily) {
                challenge.setGoal(Days.daysBetween(DateTime.now().toLocalDate(), expirationDate.toLocalDate()).getDays() + 1);
                challenge.setUpdatedAt(DateTime.now().minusDays(1));
            } else if(spinType.getPosition() == ChallengeType.Mission) {
                challenge.setGoal(1);
            }

            if(! editReward.getText().toString().trim().equals("")) {
                challenge.setReward(editReward.getText().toString());
            }

            if(switchExpiration.isChecked()) {
                challenge.setExpiresAt(expirationDate);
            }

            // Save the challenge
            ObjectBox.get().boxFor(Challenge.class).put(challenge);

            // Set as primary challenge for aspect
            aspect.getChallenge().setTarget(challenge);
            ObjectBox.get().boxFor(Aspect.class).put(aspect);

            // Show success and go back to dashboard
            Snackbar errorHelp = Snackbar.make(findViewById(android.R.id.content), "Challenge successfully added.", Snackbar.LENGTH_LONG);
            errorHelp.getView().setBackgroundColor(getResources().getColor(R.color.lime_light));
            errorHelp.show();
            finish();
        }
    }

    @OnClick(R.id.btnCancel)
    void onCancelButtonClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ConfirmationDialog dialog = ConfirmationDialog.newInstance();
        dialog.setMessage("Are you sure you want to cancel?\n\nNo changes will be saved.");
        dialog.setListener((confirmed) -> {
            if(confirmed) {
                super.onBackPressed();
            }
        });
        dialog.show(getSupportFragmentManager(), "Confirm");
    }

    private boolean valid() {
        boolean valid = true;

        // Validate expiration date
        if(switchExpiration.isChecked() && expirationDate == null) {
            btnExpirationDate.setError("Please select an expiration date or disable expiration date");
            valid = false;
        } else {
            btnExpirationDate.setError(null);
        }

        // Validate challenge type
        if(editCompletionCondition.getText().toString().trim().equals("")) {
            editCompletionCondition.setError("Please enter a completion condition");
            valid = false;
        } else {
            editCompletionCondition.setError(null);
        }

        // Validate challenge type
        if(spinType.getPosition() == ListView.INVALID_POSITION) {
            spinType.setError("Please select a challenge type");
            valid = false;
        } else {
            spinType.setError(null);
        }

        // Validate score challenge type
        if(spinType.getPosition() == ChallengeType.Score && Strings.isNullOrEmpty(editScore.getText().toString())) {
            editScore.setError("Please enter a target score");
            valid = false;
        } else {
            editScore.setError(null);
        }

        // Validate aspect
        if(aspectId == 0L) {
            if (spinAspect.getPosition() == ListView.INVALID_POSITION) {
                spinAspect.setError("Please select an aspect");
                valid = false;
            } else {
                spinAspect.setError(null);
            }
        }

        if(! valid) {
            Snackbar errorHelp = Snackbar.make(findViewById(android.R.id.content), "Some required fields are missing. Please correct errors and try adding again.", Snackbar.LENGTH_LONG);
            errorHelp.getView().setBackgroundColor(getResources().getColor(R.color.crimson_light));
            errorHelp.show();
        }

        return valid;
    }

}
