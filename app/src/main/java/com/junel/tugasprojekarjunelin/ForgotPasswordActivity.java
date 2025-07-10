package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmailForgetPassword;
    private Button btnKirimKodeVerifikasi, btnKembaliForgetPassword;
    private ImageView backButtonForgetPassword;

    // Untuk simulasi OTP
    private String generatedOTP;
    private String userEmail;

    // Nama file SharedPreferences (sama dengan MainActivity)
    private static final String PREF_NAME = "UserAccount";
    private static final String KEY_EMAIL = "email"; // Pastikan ini konsisten

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmailForgetPassword = findViewById(R.id.etEmailForgetPassword);
        btnKirimKodeVerifikasi = findViewById(R.id.btnKirimKodeVerifikasi);
        btnKembaliForgetPassword = findViewById(R.id.btnKembaliForgetPassword);
        backButtonForgetPassword = findViewById(R.id.backButtonForgetPassword);

        btnKirimKodeVerifikasi.setOnClickListener(v -> {
            userEmail = etEmailForgetPassword.getText().toString().trim();

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- SIMULASI: Cek apakah email terdaftar di SharedPreferences ---
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);

            if (userEmail.equals(savedEmail)) {
                // SIMULASI: Generate OTP secara acak di sini
                generatedOTP = generateOTP();

                // Tampilkan Toast dengan OTP (ini hanya untuk debugging/simulasi agar Anda tahu OTP-nya)
                Toast.makeText(this, "Kode OTP telah dibuat: " + generatedOTP + " (Masukkan ini di halaman berikutnya)", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ForgotPasswordActivity.this, VerificationActivity.class);
                intent.putExtra("email", userEmail); // Kirim email ke activity berikutnya
                intent.putExtra("otp", generatedOTP); // KIRIM OTP LANGSUNG MELALUI INTENT
                startActivity(intent);
            } else {
                Toast.makeText(this, "Email tidak terdaftar!", Toast.LENGTH_SHORT).show();
            }
        });

        btnKembaliForgetPassword.setOnClickListener(v -> {
            finish(); // Kembali ke MainActivity
        });

        backButtonForgetPassword.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    // Metode simulasi untuk generate OTP
    private String generateOTP() {
        return String.format(Locale.getDefault(), "%06d", new Random().nextInt(999999));
    }
}