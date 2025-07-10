package com.junel.tugasprojekarjunelin; // Pastikan ini sesuai dengan package Anda

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout; // Import ini juga untuk til

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private AutoCompleteTextView actPelabuhanAsal;
    private AutoCompleteTextView actPelabuhanTujuan;
    private TextInputEditText etTanggalKeberangkatan;
    private AutoCompleteTextView actJenisPenyeberangan;
    private MaterialButton btnCariJadwal;

    // Tambahkan TextInputLayout jika ingin melakukan validasi error secara visual
    private TextInputLayout tilPelabuhanAsal, tilPelabuhanTujuan, tilTanggalKeberangkatan, tilJenisPenyeberangan;

    private LinearLayout passengerCountLayout;
    private ImageButton btnMinusBaby, btnPlusBaby, btnMinusAdult, btnPlusAdult;
    private TextView tvBabyCount, tvAdultCount;
    private MaterialButton btnSelesai;

    private int babyCount = 0;
    private int adultCount = 0;

    // Konstanta untuk Intent Extras - PENTING!
    // Ini harus sama dengan yang dideklarasikan di JadwalActivity
    public static final String EXTRA_ASAL = "extra_asal";
    public static final String EXTRA_TUJUAN = "extra_tujuan";
    public static final String EXTRA_TANGGAL_KEBERANGKATAN = "extra_tanggal_keberangkatan";
    public static final String EXTRA_JENIS_PENYEBERANGAN = "extra_jenis_penyeberangan";
    public static final String EXTRA_BABY_COUNT = "extra_baby_count";
    public static final String EXTRA_ADULT_COUNT = "extra_adult_count";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi Views
        actPelabuhanAsal = findViewById(R.id.actPelabuhanAsal);
        actPelabuhanTujuan = findViewById(R.id.actPelabuhanTujuan);
        etTanggalKeberangkatan = findViewById(R.id.etTanggalKeberangkatan);
        actJenisPenyeberangan = findViewById(R.id.actJenisPenyeberangan);
        btnCariJadwal = findViewById(R.id.btnCariJadwal);

        // Inisialisasi TextInputLayouts (Jika digunakan untuk error handling)
        tilPelabuhanAsal = findViewById(R.id.tilPelabuhanAsal);
        tilPelabuhanTujuan = findViewById(R.id.tilPelabuhanTujuan);
        tilTanggalKeberangkatan = findViewById(R.id.tilTanggalKeberangkatan);
        tilJenisPenyeberangan = findViewById(R.id.tilJenisPenyeberangan);


        passengerCountLayout = findViewById(R.id.passengerCountLayout);
        btnMinusBaby = findViewById(R.id.btnMinusBaby);
        btnPlusBaby = findViewById(R.id.btnPlusBaby);
        btnMinusAdult = findViewById(R.id.btnMinusAdult);
        btnPlusAdult = findViewById(R.id.btnPlusAdult);
        tvBabyCount = findViewById(R.id.tvBabyCount);
        tvAdultCount = findViewById(R.id.tvAdultCount);
        btnSelesai = findViewById(R.id.btnSelesai);


        String[] pelabuhanOptions = new String[]{"Sibolga - Pelabuhan Pelindo", "G.Sitoli - Pelabuhan Pelindo", "Padang - Teluk Bayur"};
        ArrayAdapter<String> pelabuhanAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                pelabuhanOptions
        );
        actPelabuhanAsal.setAdapter(pelabuhanAdapter);
        actPelabuhanTujuan.setAdapter(pelabuhanAdapter);


        String[] jenisPenyeberanganOptions = new String[]{
                "Penumpang",
                "Kendaraan (tanpa supir dan penumpang)",
                "Penumpang dan Kendaraan"
        };
        ArrayAdapter<String> jenisPenyeberanganAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                jenisPenyeberanganOptions
        );
        actJenisPenyeberangan.setAdapter(jenisPenyeberanganAdapter);

        // Listener untuk Jenis Penyeberangan untuk mengontrol visibilitas passengerCountLayout
        actJenisPenyeberangan.setOnItemClickListener((parent, view, position, id) -> {
            String selectedType = (String) parent.getItemAtPosition(position);
            if (selectedType.equals("Penumpang") || selectedType.equals("Penumpang dan Kendaraan")) {
                passengerCountLayout.setVisibility(View.VISIBLE);
            } else {
                passengerCountLayout.setVisibility(View.GONE);
                babyCount = 0; // Reset count
                adultCount = 0; // Reset count
                updatePassengerCounts();
            }
        });

        // Listener untuk tanggal keberangkatan (memunculkan DatePicker)
        etTanggalKeberangkatan.setOnClickListener(v -> showDatePickerDialog());

        // Listener untuk tombol minus dan plus bayi
        btnMinusBaby.setOnClickListener(v -> {
            if (babyCount > 0) {
                babyCount--;
                updatePassengerCounts();
            }
        });
        btnPlusBaby.setOnClickListener(v -> {
            babyCount++;
            updatePassengerCounts();
        });

        // Listener untuk tombol minus dan plus dewasa
        btnMinusAdult.setOnClickListener(v -> {
            if (adultCount > 0) {
                adultCount--;
                updatePassengerCounts();
            }
        });
        btnPlusAdult.setOnClickListener(v -> {
            adultCount++;
            updatePassengerCounts();
        });

        // Listener untuk tombol Selesai di layout jumlah penumpang
        btnSelesai.setOnClickListener(v -> {
            String selectedType = actJenisPenyeberangan.getText().toString();
            // Validasi jika penumpang diperlukan tapi count 0
            if ((selectedType.equals("Penumpang") || selectedType.equals("Penumpang dan Kendaraan")) && (babyCount == 0 && adultCount == 0)) {
                Toast.makeText(HomeActivity.this, "Jumlah penumpang (dewasa/bayi) tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this,
                        "Jumlah Bayi: " + babyCount + ", Dewasa: " + adultCount + " telah disimpan.",
                        Toast.LENGTH_SHORT).show();
                passengerCountLayout.setVisibility(View.GONE); // Sembunyikan setelah selesai
            }
        });

        // Listener untuk tombol "Cari Jadwal"
        btnCariJadwal.setOnClickListener(v -> {
            String asal = actPelabuhanAsal.getText().toString().trim();
            String tujuan = actPelabuhanTujuan.getText().toString().trim();
            String tanggal = etTanggalKeberangkatan.getText().toString().trim();
            String jenis = actJenisPenyeberangan.getText().toString().trim();

            // --- Validasi Input ---
            boolean isValid = true;

            if (asal.isEmpty()) {
                tilPelabuhanAsal.setError("Pelabuhan asal harus diisi!");
                isValid = false;
            } else {
                tilPelabuhanAsal.setError(null);
            }

            if (tujuan.isEmpty()) {
                tilPelabuhanTujuan.setError("Pelabuhan tujuan harus diisi!");
                isValid = false;
            } else {
                tilPelabuhanTujuan.setError(null);
            }

            if (tanggal.isEmpty()) {
                tilTanggalKeberangkatan.setError("Tanggal keberangkatan harus diisi!");
                isValid = false;
            } else {
                tilTanggalKeberangkatan.setError(null);
            }

            if (jenis.isEmpty()) {
                tilJenisPenyeberangan.setError("Jenis penyeberangan harus diisi!");
                isValid = false;
            } else {
                tilJenisPenyeberangan.setError(null);
            }

            // Validasi: Pelabuhan asal dan tujuan tidak boleh sama
            if (asal.equals(tujuan) && !asal.isEmpty()) { // Tambah !asal.isEmpty() agar tidak error saat kosong
                tilPelabuhanTujuan.setError("Pelabuhan asal dan tujuan tidak boleh sama!");
                isValid = false;
            }

            // Validasi: Jika jenis penyeberangan memerlukan penumpang, jumlahnya tidak boleh 0
            if ((jenis.equals("Penumpang") || jenis.equals("Penumpang dan Kendaraan")) && (babyCount == 0 && adultCount == 0)) {
                Toast.makeText(HomeActivity.this, "Untuk jenis penyeberangan ini, jumlah penumpang tidak boleh kosong.", Toast.LENGTH_LONG).show();
                passengerCountLayout.setVisibility(View.VISIBLE); // Pastikan layout jumlah penumpang terlihat
                isValid = false;
            }


            if (isValid) {
                // Semua validasi berhasil, kirim data ke JadwalActivity
                Intent intent = new Intent(HomeActivity.this, JadwalActivity.class);
                intent.putExtra(EXTRA_ASAL, asal);
                intent.putExtra(EXTRA_TUJUAN, tujuan);
                intent.putExtra(EXTRA_TANGGAL_KEBERANGKATAN, tanggal);
                intent.putExtra(EXTRA_JENIS_PENYEBERANGAN, jenis);
                intent.putExtra(EXTRA_BABY_COUNT, babyCount);
                intent.putExtra(EXTRA_ADULT_COUNT, adultCount);

                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, "Harap perbaiki kesalahan input Anda.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode untuk menampilkan DatePicker Dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    // Format tanggal sesuai kebutuhan, contoh: "05 Juli 2025"
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
                    etTanggalKeberangkatan.setText(sdf.format(calendar.getTime()));
                },
                year, month, day
        );
        // Opsi: Atur tanggal minimum agar tidak bisa memilih tanggal lampau
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // Metode untuk memperbarui tampilan jumlah bayi dan dewasa
    private void updatePassengerCounts() {
        tvBabyCount.setText(String.valueOf(babyCount));
        tvAdultCount.setText(String.valueOf(adultCount));
    }
}