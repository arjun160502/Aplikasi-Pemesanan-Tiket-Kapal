package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class VerificationActivity extends AppCompatActivity {

    private EditText etOTP;
    private TextView tvOTPSentToEmail, tvKirimUlangOTP;
    private Button btnVerifikasiOTP, btnKembaliVerification;
    private ImageView backButtonVerification;

    private String receivedEmail;
    private String correctOTP; // OTP yang benar, akan diambil dari Intent

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 60 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        etOTP = findViewById(R.id.etOTP);
        tvOTPSentToEmail = findViewById(R.id.tvOTPSentToEmail);
        tvKirimUlangOTP = findViewById(R.id.tvKirimUlangOTP);
        btnVerifikasiOTP = findViewById(R.id.btnVerifikasiOTP);
        btnKembaliVerification = findViewById(R.id.btnKembaliVerification);
        backButtonVerification = findViewById(R.id.backButtonVerification);

        // Ambil data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            receivedEmail = intent.getStringExtra("email");
            correctOTP = intent.getStringExtra("otp"); // Ambil OTP langsung dari Intent

            if (receivedEmail != null) {
                tvOTPSentToEmail.setText("Masukkan kode OTP yang telah kami kirim ke " + receivedEmail + " untuk mengatur ulang kata sandi");
            }
        }

        startTimer();

        btnVerifikasiOTP.setOnClickListener(v -> {
            String enteredOTP = etOTP.getText().toString().trim();

            if (enteredOTP.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan kode OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- SIMULASI: Verifikasi OTP ---
            if (enteredOTP.equals(correctOTP)) { // Verifikasi dengan OTP yang diterima dari Intent
                Toast.makeText(this, "OTP Berhasil Diverifikasi!", Toast.LENGTH_SHORT).show();
                Intent resetIntent = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                resetIntent.putExtra("email", receivedEmail); // Kirim email ke ResetPasswordActivity
                startActivity(resetIntent);
                finish(); // Tutup VerificationActivity
            } else {
                Toast.makeText(this, "Kode OTP salah!", Toast.LENGTH_SHORT).show();
            }
        });

        tvKirimUlangOTP.setOnClickListener(v -> {
            if (timeLeftInMillis <= 0) { // Hanya jika timer sudah habis
                // SIMULASI: Generate OTP baru saat kirim ulang
                correctOTP = generateOTP();
                Toast.makeText(this, "Kode OTP baru telah dikirim ke " + receivedEmail + " (Simulasi: " + correctOTP + ")", Toast.LENGTH_LONG).show();
                timeLeftInMillis = 60000; // Reset timer
                startTimer();
                tvKirimUlangOTP.setEnabled(false); // Nonaktifkan tombol selama timer berjalan
            } else {
                Toast.makeText(this, "Tunggu hingga timer habis untuk mengirim ulang.", Toast.LENGTH_SHORT).show();
            }
        });

        btnKembaliVerification.setOnClickListener(v -> {
            finish(); // Kembali ke ForgotPasswordActivity
        });

        backButtonVerification.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000);
                tvKirimUlangOTP.setText("Kirim Ulang " + seconds + " detik");
                tvKirimUlangOTP.setEnabled(false); // Nonaktifkan selama menghitung
            }

            @Override
            public void onFinish() {
                tvKirimUlangOTP.setText("Kirim Ulang Kode");
                tvKirimUlangOTP.setEnabled(true); // Aktifkan lagi
                timeLeftInMillis = 0;
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Metode simulasi untuk generate OTP (juga digunakan di ForgotPasswordActivity)
    private String generateOTP() {
        return String.format(Locale.getDefault(), "%06d", new Random().nextInt(999999));
    }
}