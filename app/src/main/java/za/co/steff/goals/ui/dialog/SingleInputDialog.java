package za.co.steff.goals.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.co.steff.goals.R;

public class SingleInputDialog extends DialogFragment {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.editInput)
    EditText editInput;

    private boolean numericOnly = false;

    private String title = "Input";
    private String message = "Please input";
    private String placeholder = "Input";

    private SingleInputDialogEventListener listener;

    public static SingleInputDialog newInstance() {
        SingleInputDialog dialog = new SingleInputDialog();
        Bundle args = new Bundle();
        // args.putInt("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate and bind the view
        View view = inflater.inflate(R.layout.dialog_single_input, container);
        ButterKnife.bind(this, view);

        // Set title, message, placeholder
        txtTitle.setText(title);
        txtMessage.setText(message);
        editInput.setHint(placeholder);

        // Numeric-only inputs
        if(numericOnly) {
            editInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }

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

    public void setNumericOnly(boolean numericOnly) {
        this.numericOnly = numericOnly;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setListener(SingleInputDialogEventListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btnSubmit)
    void onSubmitButtonClicked() {
        if(validate() && listener != null) {
            listener.onSingleInputSubmitted(editInput.getText().toString());
            dismiss();
        }
    }

    @OnClick(R.id.btnCancel)
    void onCancelButtonClicked() {
        dismiss();
    }

    private boolean validate() {
        // Validate single input
        if(Strings.isNullOrEmpty(editInput.getText().toString())) {
            editInput.setError("This field cannot be empty");
            return false;
        } else {
            editInput.setError(null);
            return true;
        }
    }

    public interface SingleInputDialogEventListener {

        void onSingleInputSubmitted(String input);

    }

}
