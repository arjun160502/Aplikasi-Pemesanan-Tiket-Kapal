package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordChangedSuccessActivity extends AppCompatActivity {

    private Button btnLainKali, btnMasukSekarang;
    private ImageView closeButtonSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed_success);

        btnLainKali = findViewById(R.id.btnLainKali);
        btnMasukSekarang = findViewById(R.id.btnMasukSekarang);
        closeButtonSuccess = findViewById(R.id.closeButtonSuccess);

        btnLainKali.setOnClickListener(v -> {
            // Kembali ke MainActivity atau HomeActivity, tergantung alur yang diinginkan
            // Misalnya, kembali ke layar login
            Intent loginIntent = new Intent(PasswordChangedSuccessActivity.this, MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Bersihkan back stack
            startActivity(loginIntent);
            finish();
        });

        btnMasukSekarang.setOnClickListener(v -> {
            // Langsung ke MainActivity (layar login) agar user bisa langsung login dengan password baru
            Intent loginIntent = new Intent(PasswordChangedSuccessActivity.this, MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Bersihkan back stack
            startActivity(loginIntent);
            finish();
        });

        closeButtonSuccess.setOnClickListener(v -> {
            // Sama dengan "Lain Kali", kembali ke layar login
            Intent loginIntent = new Intent(PasswordChangedSuccessActivity.this, MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Nonaktifkan tombol back untuk mencegah user kembali ke halaman sebelumnya
        // jika sudah di halaman sukses. Dorong mereka untuk memilih "Masuk Sekarang" atau "Lain Kali".
        // super.onBackPressed(); // Hapus atau komen baris ini
        Intent loginIntent = new Intent(PasswordChangedSuccessActivity.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }
}