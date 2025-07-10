package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmNewPassword;
    private Button btnKirimResetPassword;
    private ImageView backButtonResetPassword;

    private String userEmail;

    // Nama file SharedPreferences dan kunci untuk mengambil data
    private static final String PREF_NAME = "UserAccount";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnKirimResetPassword = findViewById(R.id.btnKirimResetPassword);
        backButtonResetPassword = findViewById(R.id.backButtonResetPassword);

        // Ambil email dari Intent sebelumnya
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("email");
        }

        // Contoh: Validasi password minimal 8 karakter secara real-time
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 8) {
                    etNewPassword.setError("Minimal 8 karakter");
                } else {
                    etNewPassword.setError(null); // Clear error
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnKirimResetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmNewPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Kata sandi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 8) {
                Toast.makeText(this, "Kata sandi baru minimal 8 karakter!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Konfirmasi kata sandi tidak cocok!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- SIMULASI: Simpan password baru ke SharedPreferences ---
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_PASSWORD, newPassword); // Simpan password baru
            editor.apply();

            Toast.makeText(this, "Kata sandi berhasil diubah!", Toast.LENGTH_SHORT).show();
            Intent successIntent = new Intent(ResetPasswordActivity.this, PasswordChangedSuccessActivity.class);
            startActivity(successIntent);
            // Anda bisa finish() activity ini atau biarkan di back stack
            finish();
        });

        backButtonResetPassword.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}