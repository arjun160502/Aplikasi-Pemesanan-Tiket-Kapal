// File: DaftarActivity.java

package com.junel.tugasprojekarjunelin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences; // Import SharedPreferences
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class DaftarActivity extends AppCompatActivity {

    private TextInputEditText etFullName;
    private AutoCompleteTextView actGender;
    private AutoCompleteTextView actIdType;
    private TextInputEditText etIdNumber;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private CheckBox cbTerms;
    private Button btnDaftarAkun;
    private TextView tvMasukSekarang;

    // Nama file SharedPreferences dan kunci untuk menyimpan data
    private static final String PREF_NAME = "UserAccount";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        etFullName = findViewById(R.id.etFullName);
        actGender = findViewById(R.id.actGender);
        actIdType = findViewById(R.id.actIdType);
        etIdNumber = findViewById(R.id.etIdNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnDaftarAkun = findViewById(R.id.btnDaftarAkun);
        tvMasukSekarang = findViewById(R.id.tvMasukSekarang);

        // Setup Dropdown Jenis Kelamin
        String[] genderOptions = new String[]{"Pria", "Wanita"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genderOptions
        );
        actGender.setAdapter(genderAdapter);

        // Setup Dropdown Jenis Identitas
        String[] idTypeOptions = new String[]{"KTP", "Paspor", "SIM", "Kartu Pelajar"};
        ArrayAdapter<String> idTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                idTypeOptions
        );
        actIdType.setAdapter(idTypeAdapter);

        // --- Tombol "Daftar Akun" ---
        btnDaftarAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String selectedGender = actGender.getText().toString().trim();
                String selectedIdType = actIdType.getText().toString().trim();
                String idNumber = etIdNumber.getText().toString().trim();
                boolean termsChecked = cbTerms.isChecked();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                        selectedGender.isEmpty() || selectedIdType.isEmpty() || idNumber.isEmpty()) {
                    Toast.makeText(DaftarActivity.this, "Semua kolom dengan tanda (*) harus diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    etPassword.setError("Kata sandi minimal 8 karakter");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Konfirmasi kata sandi tidak cocok");
                    return;
                }
                if (!termsChecked) {
                    Toast.makeText(DaftarActivity.this, "Anda harus menyetujui Syarat & Ketentuan", Toast.LENGTH_SHORT).show();
                    return;
                }

                // --- Simpan data ke SharedPreferences ---
                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_PASSWORD, password);
                editor.apply(); // Gunakan apply() untuk menyimpan secara asynchronous

                Toast.makeText(DaftarActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                // Langsung navigasi ke HomeActivity setelah pendaftaran berhasil
                Intent intent = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup DaftarActivity
            }
        });

        // --- Teks "Masuk Sekarang" ke MainActivity ---
        tvMasukSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi kembali ke MainActivity (login)
                Intent intent = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup DaftarActivity
            }
        });
    }
}