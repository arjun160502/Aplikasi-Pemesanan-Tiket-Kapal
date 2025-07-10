package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JadwalActivity extends AppCompatActivity {

    // Konstanta untuk Intent extras dari HomeActivity (tetap sama)
    public static final String EXTRA_ASAL = "extra_asal";
    public static final String EXTRA_TUJUAN = "extra_tujuan";
    public static final String EXTRA_TANGGAL_KEBERANGKATAN = "extra_tanggal_keberangkatan";
    public static final String EXTRA_JENIS_PENYEBERANGAN = "extra_jenis_penyeberangan";
    public static final String EXTRA_BABY_COUNT = "extra_baby_count"; // Baby count = infantCount
    public static final String EXTRA_ADULT_COUNT = "extra_adult_count";


    // Views dari layout (tetap sama)
    private ImageView backButton;
    private TextView tvRuteHeader;
    private TextView tvTanggalDanPenumpangHeader;
    private CheckBox cbKasurKamar;

    private CardView cardRuteUtama;
    private TextView tvNamaKapalUtama, tvWaktuAsalUtama, tvLokasiAsalUtama,
            tvWaktuTujuanUtama, tvLokasiTujuanUtama,
            tvJenisPenyeberanganCardUtama, tvJumlahPenumpangCardUtama,
            tvKasurStatusUtama, tvKamarStatusUtama, tvHargaUtama;
    private MaterialButton btnPilih;

    private CardView cardRuteAlternatif;
    private TextView tvNamaKapalAlternatif, tvWaktuAsalAlternatif, tvLokasiAsalAlternatif,
            tvWaktuTujuanAlternatif, tvLokasiTujuanAlternatif,
            tvJenisPenyeberanganCardAlternatif, tvJumlahPenumpangCardAlternatif,
            tvKasurStatusAlternatif, tvKamarStatusAlternatif, tvHargaAlternatif;
    private MaterialButton btnPilihAlt;

    // Data yang diterima dari HomeActivity
    private String asal, tujuan, tanggalKeberangkatanStr, jenisPenyeberangan;
    private int babyCount, adultCount; // Ini akan diteruskan sebagai infantCount dan adultCount

    // --- Harga dasar tiket (nilai long) ---
    private long hargaRuteUtama = 93100L;
    private long hargaRuteAlternatif = 120000L; // Ini harga yang akan diteruskan sebagai LONG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        // Inisialisasi Views
        initViews(); // Menggunakan metode initViews untuk kerapian

        // Ambil data dari Intent HomeActivity
        getIntentData(); // Menggunakan metode getIntentData

        // Setup Listeners
        setupClickListeners(); // Menggunakan metode setupClickListeners

        // Panggil updateUIWithIntentData setelah semua data diterima dan views diinisialisasi
        updateUIWithIntentData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        tvRuteHeader = findViewById(R.id.tvRuteHeader);
        tvTanggalDanPenumpangHeader = findViewById(R.id.tvTanggalDanPenumpangHeader);
        cbKasurKamar = findViewById(R.id.cbKasurKamar);

        cardRuteUtama = findViewById(R.id.cardRuteUtama);
        tvNamaKapalUtama = findViewById(R.id.tvNamaKapalUtama);
        tvWaktuAsalUtama = findViewById(R.id.tvWaktuAsalUtama);
        tvLokasiAsalUtama = findViewById(R.id.tvLokasiAsalUtama);
        tvWaktuTujuanUtama = findViewById(R.id.tvWaktuTujuanUtama);
        tvLokasiTujuanUtama = findViewById(R.id.tvLokasiTujuanUtama);
        tvJenisPenyeberanganCardUtama = findViewById(R.id.tvJenisPenyeberanganCardUtama);
        tvJumlahPenumpangCardUtama = findViewById(R.id.tvJumlahPenumpangCardUtama);
        tvKasurStatusUtama = findViewById(R.id.tvKasurStatusUtama);
        tvKamarStatusUtama = findViewById(R.id.tvKamarStatusUtama);
        tvHargaUtama = findViewById(R.id.tvHargaUtama);
        btnPilih = findViewById(R.id.btnPilih);

        cardRuteAlternatif = findViewById(R.id.cardRuteAlternatif);
        tvNamaKapalAlternatif = findViewById(R.id.tvNamaKapalAlternatif);
        tvWaktuAsalAlternatif = findViewById(R.id.tvWaktuAsalAlternatif);
        tvLokasiAsalAlternatif = findViewById(R.id.tvLokasiAsalAlternatif);
        tvWaktuTujuanAlternatif = findViewById(R.id.tvWaktuTujuanAlternatif);
        tvLokasiTujuanAlternatif = findViewById(R.id.tvLokasiTujuanAlternatif);
        tvJenisPenyeberanganCardAlternatif = findViewById(R.id.tvJenisPenyeberanganCardAlternatif);
        tvJumlahPenumpangCardAlternatif = findViewById(R.id.tvJumlahPenumpangCardAlternatif);
        tvKasurStatusAlternatif = findViewById(R.id.tvKasurStatusAlternatif);
        tvKamarStatusAlternatif = findViewById(R.id.tvKamarStatusAlternatif);
        tvHargaAlternatif = findViewById(R.id.tvHargaAlternatif);
        btnPilihAlt = findViewById(R.id.btnPilihAlt);
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            asal = extras.getString(EXTRA_ASAL);
            tujuan = extras.getString(EXTRA_TUJUAN);
            tanggalKeberangkatanStr = extras.getString(EXTRA_TANGGAL_KEBERANGKATAN);
            jenisPenyeberangan = extras.getString(EXTRA_JENIS_PENYEBERANGAN);
            babyCount = extras.getInt(EXTRA_BABY_COUNT, 0);
            adultCount = extras.getInt(EXTRA_ADULT_COUNT, 0);
        } else {
            Toast.makeText(this, "Data pencarian tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish()); // Kembali ke HomeActivity

        cbKasurKamar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(JadwalActivity.this, "Menampilkan jadwal dengan fasilitas kasur & kamar...", Toast.LENGTH_SHORT).show();
                cardRuteUtama.setVisibility(View.GONE); // Sembunyikan rute tanpa fasilitas
                cardRuteAlternatif.setVisibility(View.VISIBLE); // Pastikan rute dengan fasilitas terlihat
            } else {
                Toast.makeText(JadwalActivity.this, "Menampilkan semua jadwal.", Toast.LENGTH_SHORT).show();
                cardRuteUtama.setVisibility(View.VISIBLE); // Tampilkan kembali rute utama
                cardRuteAlternatif.setVisibility(View.VISIBLE); // Pastikan rute alternatif juga terlihat
            }
        });

        btnPilih.setOnClickListener(v -> {
            // Data untuk rute utama
            String shipName = tvNamaKapalUtama.getText().toString().replace("Kapal : ", "").trim();
            String departureTime = "22:00 WIB";
            String arrivalTime = "06:00 WIB";
            String departureDate = parseAndFormatDate(tanggalKeberangkatanStr, 0); // Tanggal keberangkatan yang sama
            String arrivalDate = parseAndFormatDate(tanggalKeberangkatanStr, 1);   // Tanggal kedatangan (+1 hari)
            String departureLocation = tvLokasiAsalUtama.getText().toString();
            String arrivalLocation = tvLokasiTujuanUtama.getText().toString();
            // Harga akan diambil dari variabel `hargaRuteUtama` (long)

            startTicketActivity(
                    shipName, departureTime, departureDate, departureLocation,
                    arrivalTime, arrivalDate, arrivalLocation,
                    jenisPenyeberangan, adultCount, babyCount, hargaRuteUtama // Mengirim adultCount, babyCount, dan harga (long)
            );
        });

        btnPilihAlt.setOnClickListener(v -> {
            // Data untuk rute alternatif
            String shipName = tvNamaKapalAlternatif.getText().toString().replace("Kapal : ", "").trim();
            String departureTime = "22:00 WIB";
            String arrivalTime = "06:00 WIB";
            String departureDate = parseAndFormatDate(tanggalKeberangkatanStr, 1); // Tanggal keberangkatan (+1 hari)
            String arrivalDate = parseAndFormatDate(tanggalKeberangkatanStr, 2);   // Tanggal kedatangan (+2 hari)
            String departureLocation = tvLokasiAsalAlternatif.getText().toString();
            String arrivalLocation = tvLokasiTujuanAlternatif.getText().toString();
            // Harga akan diambil dari variabel `hargaRuteAlternatif` (long)

            startTicketActivity(
                    shipName, departureTime, departureDate, departureLocation,
                    arrivalTime, arrivalDate, arrivalLocation,
                    jenisPenyeberangan, adultCount, babyCount, hargaRuteAlternatif // Mengirim adultCount, babyCount, dan harga (long)
            );
        });
    }

    // Metode untuk memperbarui UI dengan data dari Intent
    private void updateUIWithIntentData() {
        tvRuteHeader.setText(asal + " ➝ " + tujuan);
        String passengerSummary = formatPassengerSummary(jenisPenyeberangan, babyCount, adultCount);
        tvTanggalDanPenumpangHeader.setText(tanggalKeberangkatanStr + " • " + jenisPenyeberangan + " • " + passengerSummary);

        // Update Card Rute Utama
        tvNamaKapalUtama.setText("Kapal : Wira Glory");
        tvLokasiAsalUtama.setText(asal);
        tvLokasiTujuanUtama.setText(tujuan);
        tvJenisPenyeberanganCardUtama.setText("Jenis Penyeberangan: " + jenisPenyeberangan);
        tvJumlahPenumpangCardUtama.setText("Jumlah Penumpang: " + passengerSummary);
        tvWaktuAsalUtama.setText("22:00 WIB\nMalam\n" + parseAndFormatDate(tanggalKeberangkatanStr, 0));
        tvWaktuTujuanUtama.setText("06:00 WIB\nPagi\n" + parseAndFormatDate(tanggalKeberangkatanStr, 1));
        tvKasurStatusUtama.setText("🛏️ Kasur Tidak Tersedia");
        tvKamarStatusUtama.setText("🚪 Kamar Tidak Tersedia");
        tvHargaUtama.setText(formatRupiah(hargaRuteUtama)); // Tampilkan harga dari variabel long

        // Update Card Rute Alternatif
        tvNamaKapalAlternatif.setText("Kapal : Wira Viktoria");
        tvLokasiAsalAlternatif.setText(asal);
        tvLokasiTujuanAlternatif.setText(tujuan);
        tvJenisPenyeberanganCardAlternatif.setText("Jenis Penyeberangan: " + jenisPenyeberangan);
        tvJumlahPenumpangCardAlternatif.setText("Jumlah Penumpang: " + passengerSummary);
        tvWaktuAsalAlternatif.setText("22:00 WIB\nMalam\n" + parseAndFormatDate(tanggalKeberangkatanStr, 1));
        tvWaktuTujuanAlternatif.setText("06:00 WIB\nPagi\n" + parseAndFormatDate(tanggalKeberangkatanStr, 2));
        tvKasurStatusAlternatif.setText("🛏️ Kasur Tersedia");
        tvKamarStatusAlternatif.setText("🚪 Kamar Tersedia");
        tvHargaAlternatif.setText(formatRupiah(hargaRuteAlternatif)); // Tampilkan harga dari variabel long
    }

    // Metode bantuan untuk memulai TicketActivity dengan data yang lengkap
    private void startTicketActivity(
            String shipName, String departureTime, String departureDate, String departureLocation,
            String arrivalTime, String arrivalDate, String arrivalLocation,
            String jenisPenyeberangan, int adultCount, int infantCount, long baseTicketPrice) { // Mengubah parameter

        Intent intent = new Intent(JadwalActivity.this, TicketActivity.class);

        // --- MENGGUNAKAN KONSTANTA DARI TICKETACTIVITY UNTUK KONSISTENSI ---
        // (Pastikan konstanta ini sudah diimport atau sudah didefinisikan di TicketActivity sebagai public static final)
        intent.putExtra(TicketActivity.EXTRA_SHIP_NAME, shipName);
        intent.putExtra(TicketActivity.EXTRA_DEPARTURE_TIME, departureTime);
        intent.putExtra(TicketActivity.EXTRA_DEPARTURE_DATE, departureDate);
        intent.putExtra(TicketActivity.EXTRA_DEPARTURE_LOCATION, departureLocation);
        intent.putExtra(TicketActivity.EXTRA_ARRIVAL_TIME, arrivalTime);
        intent.putExtra(TicketActivity.EXTRA_ARRIVAL_DATE, arrivalDate);
        intent.putExtra(TicketActivity.EXTRA_ARRIVAL_LOCATION, arrivalLocation);
        intent.putExtra(TicketActivity.EXTRA_JENIS_PENYEBERANGAN, jenisPenyeberangan);
        intent.putExtra(TicketActivity.EXTRA_ADULT_COUNT, adultCount);   // Mengirim dewasa sebagai int
        intent.putExtra(TicketActivity.EXTRA_INFANT_COUNT, infantCount); // Mengirim bayi sebagai int
        intent.putExtra(TicketActivity.EXTRA_BASE_PRICE, baseTicketPrice); // MENGIRIM HARGA SEBAGAI LONG

        startActivity(intent);
    }


    // Metode bantuan untuk memformat ringkasan penumpang
    private String formatPassengerSummary(String jenisPenyeberangan, int baby, int adult) {
        if (jenisPenyeberangan.equals("Kendaraan (tanpa supir dan penumpang)")) {
            return "0 Penumpang";
        } else {
            StringBuilder sb = new StringBuilder();
            if (adult > 0) {
                sb.append(adult).append(" Dewasa");
            }
            if (baby > 0) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(baby).append(" Bayi");
            }
            return sb.toString().isEmpty() ? "0 Penumpang" : sb.toString();
        }
    }

    // Metode bantuan untuk memparsing tanggal dan menambahkan hari
    private String parseAndFormatDate(String dateString, int daysToAdd) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")); // Pastikan format input benar
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        try {
            Date date = inputFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            if (date != null) {
                calendar.setTime(date);
            }
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd); // Tambah hari
            return outputFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Kembali ke string asli jika gagal parsing
        }
    }

    // Metode bantuan untuk format angka ke Rupiah
    private String formatRupiah(long number) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatRupiah.format(number).replace("Rp", "Rp ").replace(",00", "");
    }
}