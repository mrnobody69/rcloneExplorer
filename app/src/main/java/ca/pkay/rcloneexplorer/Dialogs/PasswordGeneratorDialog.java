package ca.pkay.rcloneexplorer.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ca.pkay.rcloneexplorer.R;

public class PasswordGeneratorDialog extends DialogFragment {

    private final int MIN_PASSWORD_LENGTH = 4;
    private final int MAX_PASSWORD_LENGTH = 128;
    private final String SAVED_IS_DARK_THEME = "ca.pkay.rcexplorer.PasswordGeneratorDialog.IS_DARK_THEME";
    private final String SAVED_GENERATED_PASSWORD = "ca.pkay.rcexplorer.PasswordGeneratorDialog.GENERATED_PASSWORD";
    private final String SAVED_PASSWORD_LENGTH = "ca.pkay.rcexplorer.PasswordGeneratorDialog.PASSWORD_LENGTH";
    private final String SAVED_CHECKBOX_LOWERCASE = "ca.pkay.rcexplorer.PasswordGeneratorDialog.CHECKBOX_LOWERCASE";
    private final String SAVED_CHECKBOX_UPPERCASE = "ca.pkay.rcexplorer.PasswordGeneratorDialog.CHECKBOX_UPPERCASE";
    private final String SAVED_CHECKBOX_NUMBERS = "ca.pkay.rcexplorer.PasswordGeneratorDialog.CHECKBOX_NUMBERS";
    private final String SAVED_CHECKBOX_SPECIAL_CHARS = "ca.pkay.rcexplorer.PasswordGeneratorDialog.CHECKBOX_SPECIAL_CHARS";
    private Context context;
    private Callbacks callback;
    private String generatedPassword;
    private boolean isDarkTheme;
    private TextView passwordDisplay;
    private TextView passwordLength;
    private SeekBar seekBar;
    private CheckBox checkBoxLowerCase;
    private CheckBox checkBoxUpperCase;
    private CheckBox checkBoxNumbers;
    private CheckBox checkBoxSpecialChars;

    private String[] lowerCase = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private String[] upperCase = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "{", "[", "}", "]", ":", ";", ",", ".", "?", "<", ">"};

    public interface Callbacks {
        void onPasswordSelected(String tag, String password);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isDarkTheme = savedInstanceState.getBoolean(SAVED_IS_DARK_THEME);
        }

        callback = (Callbacks) getParentFragment();

        AlertDialog.Builder builder;
        if (isDarkTheme) {
            builder = new AlertDialog.Builder(context, R.style.DarkDialogTheme);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setPositiveButton(R.string.set_password_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onPasswordSelected(getTag(), generatedPassword);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);

        LayoutInflater inflater = ((FragmentActivity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_password_generator, null);

        passwordDisplay = view.findViewById(R.id.password);
        seekBar = view.findViewById(R.id.password_length_seekbar);
        passwordLength = view.findViewById(R.id.password_length);
        checkBoxLowerCase = view.findViewById(R.id.checkbox_lower_case);
        checkBoxUpperCase = view.findViewById(R.id.checkbox_upper_case);
        checkBoxNumbers = view.findViewById(R.id.checkbox_numbers);
        checkBoxSpecialChars = view.findViewById(R.id.checkbox_special_chars);

        passwordLength.setText(String.valueOf(MIN_PASSWORD_LENGTH));

        seekBar.setMax(MAX_PASSWORD_LENGTH);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < MIN_PASSWORD_LENGTH) {
                    seekBar.setProgress(MIN_PASSWORD_LENGTH);
                    passwordLength.setText(String.valueOf(MIN_PASSWORD_LENGTH));
                } else {
                    passwordLength.setText(String.valueOf(progress));
                }
                generatePassword();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        checkBoxLowerCase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                generatePassword();
            }
        });

        checkBoxUpperCase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                generatePassword();
            }
        });

        checkBoxNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                generatePassword();
            }
        });

        checkBoxSpecialChars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                generatePassword();
            }
        });

        view.findViewById(R.id.lower_case).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxLowerCase.setChecked(!checkBoxLowerCase.isChecked());
            }
        });

        view.findViewById(R.id.upper_case).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxUpperCase.setChecked(!checkBoxUpperCase.isChecked());
            }
        });

        view.findViewById(R.id.numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxNumbers.setChecked(!checkBoxNumbers.isChecked());
            }
        });

        view.findViewById(R.id.special_chars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxSpecialChars.setChecked(!checkBoxSpecialChars.isChecked());
            }
        });

        view.findViewById(R.id.refresh_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });

        if (savedInstanceState != null) {
            checkBoxLowerCase.setChecked(savedInstanceState.getBoolean(SAVED_CHECKBOX_LOWERCASE));
            checkBoxUpperCase.setChecked(savedInstanceState.getBoolean(SAVED_CHECKBOX_UPPERCASE));
            checkBoxNumbers.setChecked(savedInstanceState.getBoolean(SAVED_CHECKBOX_NUMBERS));
            checkBoxSpecialChars.setChecked(savedInstanceState.getBoolean(SAVED_CHECKBOX_SPECIAL_CHARS));
            seekBar.setProgress(savedInstanceState.getInt(SAVED_PASSWORD_LENGTH));
            passwordDisplay.setText(savedInstanceState.getString(SAVED_GENERATED_PASSWORD));
            generatedPassword = savedInstanceState.getString(SAVED_GENERATED_PASSWORD);
        } else {
            generatePassword();
        }

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_IS_DARK_THEME, isDarkTheme);
        outState.putString(SAVED_GENERATED_PASSWORD, generatedPassword);
        outState.putBoolean(SAVED_CHECKBOX_LOWERCASE, checkBoxLowerCase.isChecked());
        outState.putBoolean(SAVED_CHECKBOX_UPPERCASE, checkBoxUpperCase.isChecked());
        outState.putBoolean(SAVED_CHECKBOX_NUMBERS, checkBoxNumbers.isChecked());
        outState.putBoolean(SAVED_CHECKBOX_SPECIAL_CHARS, checkBoxSpecialChars.isChecked());
        outState.putInt(SAVED_PASSWORD_LENGTH, seekBar.getProgress());
    }

    public PasswordGeneratorDialog setDarkTheme(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;
        return this;
    }

    private void generatePassword() {
        ArrayList<String> password = new ArrayList<>();
        int arraySize;
        int randomIndex;
        Random random = new SecureRandom();
        int length = Integer.parseInt(passwordLength.getText().toString());

        if (!checkBoxLowerCase.isChecked() && !checkBoxUpperCase.isChecked() && !checkBoxNumbers.isChecked() && !checkBoxSpecialChars.isChecked()) {
            generatedPassword = "";
            passwordDisplay.setText("");
            return;
        }

        while (password.size() < length) {
            if (checkBoxLowerCase.isChecked()) {
                arraySize = lowerCase.length;
                randomIndex = random.nextInt(arraySize);
                password.add(lowerCase[randomIndex]);
            }

            if (checkBoxUpperCase.isChecked()) {
                arraySize = upperCase.length;
                randomIndex = random.nextInt(arraySize);
                password.add(upperCase[randomIndex]);
            }

            if (checkBoxNumbers.isChecked()) {
                arraySize = numbers.length;
                randomIndex = random.nextInt(arraySize);
                password.add(numbers[randomIndex]);
            }

            if (checkBoxSpecialChars.isChecked()) {
                arraySize = specialChars.length;
                randomIndex = random.nextInt(arraySize);
                password.add(specialChars[randomIndex]);
            }
        }

        Collections.shuffle(password);

        while (password.size() > length) {
            password.remove(0);
        }

        generatedPassword = TextUtils.join("", password);
        passwordDisplay.setText(generatedPassword);
    }
}
