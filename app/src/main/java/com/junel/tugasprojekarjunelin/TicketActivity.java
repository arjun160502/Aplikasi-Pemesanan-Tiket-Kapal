package com.junel.tugasprojekarjunelin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.NumberFormat;
import java.util.Locale;

public class TicketActivity extends AppCompatActivity {

    // Konstanta untuk request code ketika meluncurkan Activity lain
    private static final int REQUEST_CODE_UPGRADE_CLASS = 101;

    // --- KONSTANTA KUNCI INTENT UNTUK DATA TIKET (DIKIRIM DARI JADWALACTIVITY DAN DITERUSKAN KE PEMESANANACTIVITY) ---
    // Pastikan JadwalActivity menggunakan konstanta ini saat mengirim data
    public static final String EXTRA_SHIP_NAME = "shipName"; // Mengubah nama konstanta menjadi lebih ringkas
    public static final String EXTRA_DEPARTURE_TIME = "departureTime";
    public static final String EXTRA_DEPARTURE_DATE = "departureDate";
    public static final String EXTRA_DEPARTURE_LOCATION = "departureLocation";
    public static final String EXTRA_ARRIVAL_TIME = "arrivalTime";
    public static final String EXTRA_ARRIVAL_DATE = "arrivalDate";
    public static final String EXTRA_ARRIVAL_LOCATION = "arrivalLocation";
    public static final String EXTRA_ADULT_COUNT = "adultCount"; // Untuk jumlah dewasa (int)
    public static final String EXTRA_INFANT_COUNT = "infantCount"; // Untuk jumlah bayi (int)
    public static final String EXTRA_BASE_PRICE = "baseTicketPrice"; // Konstanta untuk harga dasar tiket (long)
    public static final String EXTRA_JENIS_PENYEBERANGAN = "jenisPenyeberangan";
    // KOnstanta EXTRA_PASSENGER_COUNT dan EXTRA_PRICE yang lama Dihapus/Diganti

    // Konstanta kunci Intent untuk data UPGRADE (diterima dari UpgradeActivity dan diteruskan ke PemesananActivity)
    public static final String EXTRA_UPGRADE_DETAILS = "upgradeDetails"; // Mengubah nama konstanta
    public static final String EXTRA_UPGRADE_PRICE = "upgradePrice";     // Mengubah nama konstanta


    // Views dari layout
    private ImageView backButton;
    private TextView tvShipNameTicket, tvDepartureTimeTicket, tvDepartureDateTicket, tvDepartureLocationTicket,
            tvArrivalTimeTicket, tvArrivalDateTicket, tvArrivalLocationTicket, tvJenisPenyeberanganTicket,
            tvPassengerCountTicket, tvPriceTicket, tvTotalPriceTicket;
    private CardView cardKelasEkonomiTicket, cardUpgradeKelasTicket; // Pastikan cardKelasEkonomiTicket ada di XML Anda
    private Button btnLanjutTicket;

    // Tambahkan deklarasi TextView untuk detail upgrade
    private TextView tvUpgradeDetails;
    private TextView tvUpgradePriceOnCard;
    private TextView tvUpgradeDescription;

    // --- Data Tiket yang akan disimpan dan diteruskan ---
    private String shipName, departureTime, departureDate, departureLocation,
            arrivalTime, arrivalDate, arrivalLocation, jenisPenyeberangan;
    private int adultCount, infantCount;
    private long baseTicketPrice = 0; // Menggunakan long untuk harga agar lebih aman

    // Data upgrade yang dipilih
    private String selectedUpgradeDetails = "";
    private long selectedUpgradePrice = 0; // Menggunakan long untuk harga

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // --- Inisialisasi Views ---
        initViews();

        // --- Ambil data dari Intent Activity sebelumnya (Misal: JadwalActivity) ---
        getIntentData();

        // --- Tampilkan data tiket ke UI ---
        displayTicketDetails();

        // --- Listeners ---
        setupClickListeners();

        // Set initial visibility for upgrade details and update total price
        // Ini memastikan tampilan awal sesuai, terutama jika tidak ada upgrade
        if (selectedUpgradePrice > 0 && !selectedUpgradeDetails.isEmpty()) {
            tvUpgradeDetails.setVisibility(View.VISIBLE);
            tvUpgradePriceOnCard.setVisibility(View.VISIBLE);
            tvUpgradeDescription.setVisibility(View.GONE); // Sembunyikan deskripsi default jika ada upgrade
        } else {
            tvUpgradeDetails.setVisibility(View.GONE);
            tvUpgradePriceOnCard.setVisibility(View.GONE);
            tvUpgradeDescription.setVisibility(View.VISIBLE); // Tampilkan deskripsi default
        }
        updateTotalPrice();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        tvShipNameTicket = findViewById(R.id.tvShipNameTicket);
        tvDepartureTimeTicket = findViewById(R.id.tvDepartureTimeTicket);
        tvDepartureDateTicket = findViewById(R.id.tvDepartureDateTicket);
        tvDepartureLocationTicket = findViewById(R.id.tvDepartureLocationTicket);
        tvArrivalTimeTicket = findViewById(R.id.tvArrivalTimeTicket);
        tvArrivalDateTicket = findViewById(R.id.tvArrivalDateTicket);
        tvArrivalLocationTicket = findViewById(R.id.tvArrivalLocationTicket);
        tvJenisPenyeberanganTicket = findViewById(R.id.tvJenisPenyeberanganTicket);
        tvPassengerCountTicket = findViewById(R.id.tvPassengerCountTicket);
        tvPriceTicket = findViewById(R.id.tvPriceTicket);
        tvTotalPriceTicket = findViewById(R.id.tvTotalPriceTicket);

        cardKelasEkonomiTicket = findViewById(R.id.cardKelasEkonomiTicket); // Penting: Inisialisasi ini jika ada di XML
        cardUpgradeKelasTicket = findViewById(R.id.cardUpgradeKelasTicket);
        btnLanjutTicket = findViewById(R.id.btnLanjutTicket);

        tvUpgradeDetails = findViewById(R.id.tvUpgradeDetails);
        tvUpgradePriceOnCard = findViewById(R.id.tvUpgradePriceOnCard);
        tvUpgradeDescription = findViewById(R.id.tvUpgradeDescription);
    }

    // Mengambil data dari Intent yang meluncurkan TicketActivity (dari JadwalActivity misalnya)
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Menggunakan konstanta yang sudah diperbarui
            shipName = intent.getStringExtra(EXTRA_SHIP_NAME);
            departureTime = intent.getStringExtra(EXTRA_DEPARTURE_TIME);
            departureDate = intent.getStringExtra(EXTRA_DEPARTURE_DATE);
            departureLocation = intent.getStringExtra(EXTRA_DEPARTURE_LOCATION);
            arrivalTime = intent.getStringExtra(EXTRA_ARRIVAL_TIME);
            arrivalDate = intent.getStringExtra(EXTRA_ARRIVAL_DATE);
            arrivalLocation = intent.getStringExtra(EXTRA_ARRIVAL_LOCATION);
            jenisPenyeberangan = intent.getStringExtra(EXTRA_JENIS_PENYEBERANGAN);

            // MENGAMBIL JUMLAH PENUMPANG SEBAGAI INT
            adultCount = intent.getIntExtra(EXTRA_ADULT_COUNT, 1);
            infantCount = intent.getIntExtra(EXTRA_INFANT_COUNT, 0);

            // Perbarui tampilan jumlah penumpang di UI
            tvPassengerCountTicket.setText(String.format(Locale.getDefault(), "Jumlah Penumpang: Dewasa (x%d), Bayi (x%d)", adultCount, infantCount));


            // --- PERBAIKAN DI SINI: Mengambil harga dasar sebagai LONG ---
            // Asumsi: Activity sebelumnya (JadwalActivity) mengirim harga dasar sebagai 'long'
            // dengan kunci EXTRA_BASE_PRICE.
            baseTicketPrice = intent.getLongExtra(EXTRA_BASE_PRICE, 0);

            // Jika JadwalActivity masih mengirim harga sebagai String "Rp 93.100",
            // Anda harus mengubah JadwalActivity untuk mengirimnya sebagai Long.
            // Atau, jika tidak bisa, tambahkan kembali logika parsing STRING di sini
            // dan gunakan kunci intent yang sesuai dengan pengiriman JadwalActivity.
            // Contoh (jika terpaksa tetap menerima String):
            /*
            String priceStringFromJadwal = intent.getStringExtra("nama_konstanta_harga_dari_jadwal"); // Sesuaikan nama konstanta
            if (priceStringFromJadwal != null && !priceStringFromJadwal.isEmpty()) {
                try {
                    String cleanPriceString = priceStringFromJadwal.replace("Rp ", "").replace(".", "");
                    baseTicketPrice = Long.parseLong(cleanPriceString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    baseTicketPrice = 0;
                    Toast.makeText(this, "Gagal memproses harga tiket dari string Jadwal.", Toast.LENGTH_SHORT).show();
                }
            } else {
                baseTicketPrice = 0;
            }
            */

        } else {
            Toast.makeText(this, "Kesalahan: Data tiket tidak ditemukan.", Toast.LENGTH_LONG).show();
            finish(); // Tutup Activity jika data esensial tidak ada
        }
    }

    // Menampilkan detail tiket ke UI
    private void displayTicketDetails() {
        tvShipNameTicket.setText("Kapal : " + shipName);
        tvDepartureTimeTicket.setText(departureTime);
        tvDepartureDateTicket.setText(departureDate);
        tvDepartureLocationTicket.setText(departureLocation);
        tvArrivalTimeTicket.setText(arrivalTime);
        tvArrivalDateTicket.setText(arrivalDate);
        tvArrivalLocationTicket.setText(arrivalLocation);
        tvJenisPenyeberanganTicket.setText("Jenis Penyeberangan: " + jenisPenyeberangan);
        // Baris ini sudah dipindahkan ke getIntentData untuk memperbarui UI setelah data diambil.
        // tvPassengerCountTicket.setText(String.format(Locale.getDefault(), "Jumlah Penumpang: Dewasa (x%d), Bayi (x%d)", adultCount, infantCount));
        tvPriceTicket.setText(formatRupiah(baseTicketPrice));

        // Update tampilan upgrade berdasarkan selectedUpgradeDetails/Price
        if (selectedUpgradePrice > 0 && selectedUpgradeDetails != null && !selectedUpgradeDetails.isEmpty()) {
            tvUpgradeDescription.setVisibility(View.GONE);
            tvUpgradeDetails.setVisibility(View.VISIBLE);
            tvUpgradePriceOnCard.setVisibility(View.VISIBLE);
            tvUpgradeDetails.setText(selectedUpgradeDetails);
            tvUpgradePriceOnCard.setText(formatRupiah(selectedUpgradePrice));
        } else {
            tvUpgradeDescription.setVisibility(View.VISIBLE); // Tampilkan deskripsi default
            tvUpgradeDetails.setVisibility(View.GONE);
            tvUpgradePriceOnCard.setVisibility(View.GONE);
            tvUpgradeDescription.setText("Upgrade kelas anda dan dapatkan fasilitas seperti ranjang dan kamar selama pelayaran anda"); // Pastikan teks defaultnya
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> onBackPressed());

        // Listener untuk card Kelas Ekonomi (jika ada di XML Anda)
        // Ini untuk memungkinkan user kembali memilih kelas ekonomi setelah sebelumnya memilih upgrade
        if (cardKelasEkonomiTicket != null) { // Pemeriksaan null untuk safety
            cardKelasEkonomiTicket.setOnClickListener(v -> {
                selectedUpgradePrice = 0;
                selectedUpgradeDetails = "";
                displayTicketDetails(); // Memperbarui UI
                updateTotalPrice();
                Toast.makeText(this, "Kelas Ekonomi dipilih (tanpa fasilitas tambahan).", Toast.LENGTH_SHORT).show();
                // Anda bisa menambahkan logika visual di sini, misal mengubah border/warna card
            });
        }


        cardUpgradeKelasTicket.setOnClickListener(v -> {
            Intent upgradeIntent = new Intent(TicketActivity.this, UpgradeActivity.class);
            upgradeIntent.putExtra(EXTRA_ADULT_COUNT, adultCount);
            upgradeIntent.putExtra(EXTRA_INFANT_COUNT, infantCount);
            upgradeIntent.putExtra(EXTRA_UPGRADE_PRICE, selectedUpgradePrice);
            upgradeIntent.putExtra(EXTRA_UPGRADE_DETAILS, selectedUpgradeDetails);

            startActivityForResult(upgradeIntent, REQUEST_CODE_UPGRADE_CLASS);
        });

        btnLanjutTicket.setOnClickListener(v -> {
            // Membuat Intent baru untuk memulai PemesananActivity
            Intent intentPemesanan = new Intent(TicketActivity.this, PemesananActivity.class);

            // Meneruskan semua data tiket dasar ke PemesananActivity
            intentPemesanan.putExtra(EXTRA_SHIP_NAME, shipName);
            intentPemesanan.putExtra(EXTRA_DEPARTURE_TIME, departureTime);
            intentPemesanan.putExtra(EXTRA_DEPARTURE_DATE, departureDate);
            intentPemesanan.putExtra(EXTRA_DEPARTURE_LOCATION, departureLocation);
            intentPemesanan.putExtra(EXTRA_ARRIVAL_TIME, arrivalTime);
            intentPemesanan.putExtra(EXTRA_ARRIVAL_DATE, arrivalDate);
            intentPemesanan.putExtra(EXTRA_ARRIVAL_LOCATION, arrivalLocation);
            intentPemesanan.putExtra(EXTRA_JENIS_PENYEBERANGAN, jenisPenyeberangan);
            intentPemesanan.putExtra(EXTRA_ADULT_COUNT, adultCount);
            intentPemesanan.putExtra(EXTRA_INFANT_COUNT, infantCount);
            intentPemesanan.putExtra(EXTRA_BASE_PRICE, baseTicketPrice); // Meneruskan harga dasar yang sudah berupa long

            // Meneruskan detail upgrade yang dipilih (jika ada)
            intentPemesanan.putExtra(EXTRA_UPGRADE_PRICE, selectedUpgradePrice);
            intentPemesanan.putExtra(EXTRA_UPGRADE_DETAILS, selectedUpgradeDetails);

            // Memulai PemesananActivity
            startActivity(intentPemesanan);

            // Opsional: Jika Anda tidak ingin pengguna dapat kembali ke TicketActivity
            // setelah mereka menekan "Lanjut", Anda bisa memanggil finish() di sini.
            // finish();
        });
    }

    // Metode untuk menerima hasil dari Activity lain (UpgradeActivity)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPGRADE_CLASS) {
            if (resultCode == RESULT_OK && data != null) {
                // Ambil data upgrade dari UpgradeActivity
                selectedUpgradeDetails = data.getStringExtra(EXTRA_UPGRADE_DETAILS);
                selectedUpgradePrice = data.getLongExtra(EXTRA_UPGRADE_PRICE, 0);

                // Perbarui tampilan UI dan total harga
                displayTicketDetails();
                updateTotalPrice();
                if (selectedUpgradePrice > 0) {
                    Toast.makeText(this, "Upgrade dipilih: " + selectedUpgradeDetails, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Upgrade dibatalkan/tidak dipilih.", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Pemilihan upgrade dibatalkan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Metode untuk memperbarui total harga di bagian bawah layar
    private void updateTotalPrice() {
        long finalPrice = baseTicketPrice + selectedUpgradePrice;
        tvTotalPriceTicket.setText(formatRupiah(finalPrice));
    }

    // Metode untuk format angka ke Rupiah
    private String formatRupiah(long number) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatRupiah.format(number).replace("Rp", "Rp ").replace(",00", "");
    }
}