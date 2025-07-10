package com.junel.tugasprojekarjunelin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView; // Tambahkan import ini
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class KonfirmasiPemesananActivity extends AppCompatActivity {

    // UI elements untuk tampilan konfirmasi awal
    private TextView tvShipNameConfirm, tvDepartureDetailsConfirm, tvArrivalDetailsConfirm,
            tvJenisPenyeberanganConfirm, tvPassengerCountConfirm, tvUpgradeDetailsConfirm,
            tvHargaTiketConfirm, tvDetailPemesanConfirm, tvDataPenumpangConfirm, tvTotalBayarConfirm;
    private ScrollView confirmationScrollView; // Ganti LinearLayout dengan ScrollView
    private LinearLayout confirmationLayout; // Ini tetap ada di dalam ScrollView

    // UI elements untuk tampilan tiket
    private ScrollView ticketScrollView; // Tambahkan ScrollView untuk tiket
    private LinearLayout ticketLayout; // Ini adalah LinearLayout di dalam ticketScrollView
    private LinearLayout ticketContainer; // Container untuk menampung tiket yang digandakan
    private TextView tvFinalPaymentTicket;

    // UI elements umum di header/footer
    private ImageView backButtonConfirm;
    private TextView tvHeaderTitle;
    private Button btnKonfirmasiPembayaran;
    private LinearLayout totalPaymentInfo;

    // Variabel untuk menyimpan data yang diterima dari Intent
    private String shipName, departureTime, departureDate, departureLocation,
            arrivalTime, arrivalDate, arrivalLocation, jenisPenyeberangan,
            upgradeDetails, pemesanNama, pemesanTelp, pemesanEmail;
    private int adultCount, infantCount;
    private long baseTicketPrice, upgradePrice, finalTotalPrice;
    private ArrayList<String> adultNames, infantNames;
    private String noPemesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pemesanan);

        initViews();
        displayConfirmationDetails();
        setupClickListeners();
    }

    private void initViews() {
        backButtonConfirm = findViewById(R.id.backButtonConfirm);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);

        // Inisialisasi elemen-elemen dari layout konfirmasi
        confirmationScrollView = findViewById(R.id.confirmationScrollView); // Inisialisasi ScrollView
        confirmationLayout = findViewById(R.id.confirmationLayout); // Ini adalah LinearLayout di dalam ScrollView
        tvShipNameConfirm = findViewById(R.id.tvShipNameConfirm);
        tvDepartureDetailsConfirm = findViewById(R.id.tvDepartureDetailsConfirm);
        tvArrivalDetailsConfirm = findViewById(R.id.tvArrivalDetailsConfirm);
        tvJenisPenyeberanganConfirm = findViewById(R.id.tvJenisPenyeberanganConfirm);
        tvPassengerCountConfirm = findViewById(R.id.tvPassengerCountConfirm);
        tvUpgradeDetailsConfirm = findViewById(R.id.tvUpgradeDetailsConfirm);
        tvHargaTiketConfirm = findViewById(R.id.tvHargaTiketConfirm);
        tvDetailPemesanConfirm = findViewById(R.id.tvDetailPemesanConfirm);
        tvDataPenumpangConfirm = findViewById(R.id.tvDataPenumpangConfirm);

        // Inisialisasi elemen-elemen dari layout tiket (wadah utama dan wadah dinamis)
        ticketScrollView = findViewById(R.id.ticketScrollView); // Inisialisasi ScrollView baru untuk tiket
        ticketLayout = findViewById(R.id.ticketLayout); // Ini LinearLayout di dalam ticketScrollView
        ticketContainer = findViewById(R.id.ticketContainer);

        tvFinalPaymentTicket = findViewById(R.id.tvFinalPaymentTicket);

        // Inisialisasi elemen-elemen footer
        btnKonfirmasiPembayaran = findViewById(R.id.btnKonfirmasiPembayaran);
        totalPaymentInfo = findViewById(R.id.totalPaymentInfo);
        tvTotalBayarConfirm = findViewById(R.id.tvTotalBayarConfirm);
    }

    private void displayConfirmationDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            shipName = intent.getStringExtra("shipName");
            departureTime = intent.getStringExtra("departureTime");
            departureDate = intent.getStringExtra("departureDate");
            departureLocation = intent.getStringExtra("departureLocation");
            arrivalTime = intent.getStringExtra("arrivalTime");
            arrivalDate = intent.getStringExtra("arrivalDate");
            arrivalLocation = intent.getStringExtra("arrivalLocation");
            jenisPenyeberangan = intent.getStringExtra("jenisPenyeberangan");
            adultCount = intent.getIntExtra("adultCount", 0);
            infantCount = intent.getIntExtra("infantCount", 0);
            baseTicketPrice = intent.getLongExtra("baseTicketPrice", 0);
            upgradePrice = intent.getLongExtra("upgradePrice", 0);
            upgradeDetails = intent.getStringExtra("upgradeDetails");
            finalTotalPrice = intent.getLongExtra("finalTotalPrice", 0);

            pemesanNama = intent.getStringExtra("pemesanNama");
            pemesanTelp = intent.getStringExtra("pemesanTelp");
            pemesanEmail = intent.getStringExtra("pemesanEmail");

            adultNames = intent.getStringArrayListExtra("adultNames");
            infantNames = intent.getStringArrayListExtra("infantNames");
            noPemesanan = intent.getStringExtra("noPemesanan");


            tvShipNameConfirm.setText("Kapal : " + shipName);
            tvDepartureDetailsConfirm.setText(String.format(Locale.getDefault(), "%s, %s\n%s",
                    departureTime, departureDate, departureLocation));
            tvArrivalDetailsConfirm.setText(String.format(Locale.getDefault(), "%s, %s\n%s",
                    arrivalTime, arrivalDate, arrivalLocation));
            tvJenisPenyeberanganConfirm.setText("Jenis Penyeberangan: " + jenisPenyeberangan);
            tvPassengerCountConfirm.setText(String.format(Locale.getDefault(), "Jumlah Penumpang: Dewasa (x%d), Bayi (x%d)", adultCount, infantCount));

            if (upgradePrice > 0 && upgradeDetails != null && !upgradeDetails.isEmpty()) {
                tvUpgradeDetailsConfirm.setVisibility(View.VISIBLE);
                tvUpgradeDetailsConfirm.setText("Fasilitas Upgrade: " + upgradeDetails);
            } else {
                tvUpgradeDetailsConfirm.setVisibility(View.GONE);
            }
            tvHargaTiketConfirm.setText(formatRupiah(baseTicketPrice));
            tvTotalBayarConfirm.setText(formatRupiah(finalTotalPrice));

            tvDetailPemesanConfirm.setText(String.format(Locale.getDefault(), "Nama: %s\nTelepon: %s\nEmail: %s",
                    pemesanNama, pemesanTelp, pemesanEmail));

            StringBuilder passengerBuilder = new StringBuilder();
            if (adultNames != null && !adultNames.isEmpty()) {
                passengerBuilder.append("Penumpang Dewasa:\n");
                for (int i = 0; i < adultNames.size(); i++) {
                    passengerBuilder.append(String.format(Locale.getDefault(), "  Dewasa %d: %s\n", (i + 1), adultNames.get(i)));
                }
            }
            if (infantNames != null && !infantNames.isEmpty()) {
                if (passengerBuilder.length() > 0) passengerBuilder.append("\n");
                passengerBuilder.append("Penumpang Bayi:\n");
                for (int i = 0; i < infantNames.size(); i++) {
                    passengerBuilder.append(String.format(Locale.getDefault(), "  Bayi %d: %s\n", (i + 1), infantNames.get(i)));
                }
            }
            tvDataPenumpangConfirm.setText(passengerBuilder.toString().trim());

        } else {
            Toast.makeText(this, "Data pemesanan tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        backButtonConfirm.setOnClickListener(v -> onBackPressed());

        btnKonfirmasiPembayaran.setOnClickListener(v -> {
            Toast.makeText(KonfirmasiPemesananActivity.this, "Pembayaran Berhasil!", Toast.LENGTH_LONG).show();

            tvHeaderTitle.setText("Tiket Anda");
            backButtonConfirm.setVisibility(View.GONE);

            // Sembunyikan ScrollView konfirmasi, tampilkan ScrollView tiket
            confirmationScrollView.setVisibility(View.GONE);
            ticketScrollView.setVisibility(View.VISIBLE);

            // Panggil metode untuk mengisi dan menggandakan tiket
            generateAndPopulateTickets();

            totalPaymentInfo.setVisibility(View.GONE);
            btnKonfirmasiPembayaran.setText("Kembali ke Beranda");
            btnKonfirmasiPembayaran.setOnClickListener(v2 -> {
                Intent homeIntent = new Intent(KonfirmasiPemesananActivity.this, HomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            });
        });
    }

    private void generateAndPopulateTickets() {
        ticketContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        ArrayList<String> allPassengerNames = new ArrayList<>();
        if (adultNames != null) {
            allPassengerNames.addAll(adultNames);
        }
        if (infantNames != null) {
            allPassengerNames.addAll(infantNames);
        }

        int totalPassengers = allPassengerNames.size();

        for (int i = 0; i < totalPassengers; i++) {
            View ticketView = inflater.inflate(R.layout.layout_ticket_view, ticketContainer, false);

            TextView tvPassengerNamesTicket = ticketView.findViewById(R.id.tvPassengerNamesTicket);
            TextView tvClassUpgradeTicket = ticketView.findViewById(R.id.tvClassUpgradeTicket);
            TextView tvDepartureLocationTicket = ticketView.findViewById(R.id.tvDepartureLocationTicket);
            TextView tvDepartureDateTimeTicket = ticketView.findViewById(R.id.tvDepartureDateTimeTicket);
            TextView tvArrivalLocationTicket = ticketView.findViewById(R.id.tvArrivalLocationTicket);
            TextView tvArrivalDateTimeTicket = ticketView.findViewById(R.id.tvArrivalDateTimeTicket);
            TextView tvTicketNumber = ticketView.findViewById(R.id.tvTicketNumber);
            ImageView ivBarcodeTicket = ticketView.findViewById(R.id.ivBarcodeTicket);

            String currentPassengerName = allPassengerNames.get(i);
            String passengerType = "";
            if (i < (adultNames != null ? adultNames.size() : 0)) {
                passengerType = " (Dewasa)";
            } else {
                passengerType = " (Bayi)";
            }

            tvPassengerNamesTicket.setText(String.format(Locale.getDefault(), "%d. %s%s", (i + 1), currentPassengerName, passengerType));
            tvClassUpgradeTicket.setText("Kelas Ekonomi (Default)" + (upgradeDetails != null && !upgradeDetails.isEmpty() ? "\n" + upgradeDetails : ""));

            tvDepartureLocationTicket.setText(departureLocation);
            tvDepartureDateTimeTicket.setText(String.format(Locale.getDefault(), "%s, %s", departureDate, departureTime));
            tvArrivalLocationTicket.setText(arrivalLocation);
            tvArrivalDateTimeTicket.setText(String.format(Locale.getDefault(), "%s, %s", arrivalDate, arrivalTime));

            String currentTicketNumber = generateTicketNumber(i + 1);
            tvTicketNumber.setText("NO. TIKET: " + currentTicketNumber);

            ivBarcodeTicket.setImageResource(R.drawable.sample_barcode);

            ticketContainer.addView(ticketView);
        }

        tvFinalPaymentTicket.setText(String.format("Total Pembayaran: %s", formatRupiah(finalTotalPrice)));
    }


    private String generateTicketNumber(int passengerIndex) {
        String baseNumber = (noPemesanan != null && !noPemesanan.isEmpty()) ? noPemesanan : String.valueOf(System.currentTimeMillis() % 1000000);
        String shipPrefix = "";
        if (shipName != null && shipName.length() >= 3) {
            shipPrefix = shipName.substring(0, 3).toUpperCase();
        } else if (shipName != null) {
            shipPrefix = shipName.toUpperCase();
        }
        return String.format(Locale.getDefault(), "%s%s-%s-%02d", shipPrefix, baseNumber, "PSGR", passengerIndex);
    }


    private String formatRupiah(long amount) {
        return String.format(Locale.getDefault(), "Rp %,d", amount).replace(",", ".");
    }
}