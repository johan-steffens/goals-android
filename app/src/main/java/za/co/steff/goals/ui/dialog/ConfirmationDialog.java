package za.co.steff.goals.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.co.steff.goals.R;

public class ConfirmationDialog extends DialogFragment {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtMessage)
    TextView txtMessage;

    private String title = "Confirm";
    private String message = "Are you sure you want to do this?";

    private ConfirmationDialogEventListener listener;

    public static ConfirmationDialog newInstance() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        Bundle args = new Bundle();
        // args.putInt("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate and bind the view
        View view = inflater.inflate(R.layout.dialog_confirm, container);
        ButterKnife.bind(this, view);

        // Set title and message
        txtTitle.setText(title);
        txtMessage.setText(message);

        // Transparent dialog
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @OnClick(R.id.btnYes)
    void onYesButtonClicked() {
        if(listener != null) {
            listener.onConfirmation(true);
        }
        dismiss();
    }

    @OnClick(R.id.btnNo)
    void onNoButtonClicked() {
        if(listener != null) {
            listener.onConfirmation(false);
        }
        dismiss();
    }

    public void setListener(ConfirmationDialogEventListener listener) {
        this.listener = listener;
    }

    public interface ConfirmationDialogEventListener {

        void onConfirmation(boolean confirmed);

    }

}
