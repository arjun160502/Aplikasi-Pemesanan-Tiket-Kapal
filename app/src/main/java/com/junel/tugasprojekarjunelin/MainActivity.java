// File: MainActivity.java

package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.content.SharedPreferences; // Import SharedPreferences
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText edUser;
    private EditText edPass;
    private Button loginButton;
    private TextView tvLupaPassword;
    private TextView tvDaftarDisini;

    // Nama file SharedPreferences dan kunci untuk mengambil data
    private static final String PREF_NAME = "UserAccount";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.imageView8), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edUser = findViewById(R.id.eduser);
        edPass = findViewById(R.id.edpass);
        loginButton = findViewById(R.id.button);
        tvLupaPassword = findViewById(R.id.textView2);
        tvDaftarDisini = findViewById(R.id.textView4);

        // --- Tombol Login ---
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usernameInput = edUser.getText().toString().trim();
                    String passwordInput = edPass.getText().toString().trim();

                    if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        // --- Ambil data dari SharedPreferences ---
                        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null); // null jika tidak ada
                        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, null); // null jika tidak ada

                        // Lakukan validasi menggunakan data yang disimpan
                        if (savedEmail != null && savedPassword != null &&
                                usernameInput.equals(savedEmail) && passwordInput.equals(savedPassword)) {
                            Toast.makeText(MainActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            // Navigasi ke HomeActivity
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            finish(); // Menutup MainActivity
                        } else {
                            Toast.makeText(MainActivity.this, "Username atau Password salah!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        // === Logika untuk "Lupa Password?" ===
        if (tvLupaPassword != null) {
            tvLupaPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // **PERUBAHAN DI SINI:** Mengarahkan ke ForgotPasswordActivity
                    Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Mengarah ke halaman lupa password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // --- Teks "Daftar Disini" ke DaftarActivity ---
        if (tvDaftarDisini != null) {
            tvDaftarDisini.setTextColor(ContextCompat.getColor(this, R.color.textBlue)); // Pastikan R.color.textBlue ada
            tvDaftarDisini.setTypeface(null, Typeface.BOLD);

            tvDaftarDisini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigasi ke DaftarActivity
                    Intent intent = new Intent(MainActivity.this, DaftarActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Menuju halaman pendaftaran", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}