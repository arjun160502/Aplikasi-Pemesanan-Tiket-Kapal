package com.junel.tugasprojekarjunelin; // Pastikan ini sesuai dengan nama package Anda

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import this

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PemesananActivity extends AppCompatActivity {

    // View elements from XML
    private ImageView backButton;
    private TextView tvShipNamePemesanan, tvDepartureTimePemesanan, tvDepartureDatePemesanan,
            tvDepartureLocationPemesanan, tvArrivalTimePemesanan, tvArrivalDatePemesanan,
            tvArrivalLocationPemesanan, tvJenisPenyeberanganPemesanan, tvPassengerCountPemesanan,
            tvUpgradeDetailsPemesanan, tvPricePemesanan, tvTotalPricePemesanan;
    private EditText etNamaLengkapPemesanan, etNomorTeleponPemesanan, etEmailPemesanan;
    private LinearLayout llPassengerInputContainer;
    private Button btnPesanTiket;

    // Data passed from previous activity
    private String shipName, departureTime, departureDate, departureLocation,
            arrivalTime, arrivalDate, arrivalLocation, jenisPenyeberangan;
    private int adultCount, infantCount;
    private long baseTicketPrice; // Base price for the ticket (economy class)
    private long upgradePrice; // Price for upgrade facilities, 0 if no upgrade
    private String upgradeDetails; // Details of upgrade facilities
    private long finalTotalPrice; // Total price to pay

    // To store dynamically created EditTexts for passenger names
    private List<EditText> adultNameEditTexts = new ArrayList<>();
    private List<EditText> infantNameEditTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // Initialize views
        initViews();

        // Get data from Intent
        getIntentData();

        // Set ticket details to TextViews
        displayTicketDetails();

        // Dynamically add passenger input fields
        addPassengerInputFields();

        // Calculate and display total price
        calculateAndDisplayTotalPrice();

        // Set up click listeners
        setupClickListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);

        tvShipNamePemesanan = findViewById(R.id.tvShipNamePemesanan);
        tvDepartureTimePemesanan = findViewById(R.id.tvDepartureTimePemesanan);
        tvDepartureDatePemesanan = findViewById(R.id.tvDepartureDatePemesanan);
        tvDepartureLocationPemesanan = findViewById(R.id.tvDepartureLocationPemesanan);
        tvArrivalTimePemesanan = findViewById(R.id.tvArrivalTimePemesanan);
        tvArrivalDatePemesanan = findViewById(R.id.tvArrivalDatePemesanan);
        tvArrivalLocationPemesanan = findViewById(R.id.tvArrivalLocationPemesanan);
        tvJenisPenyeberanganPemesanan = findViewById(R.id.tvJenisPenyeberanganPemesanan);
        tvPassengerCountPemesanan = findViewById(R.id.tvPassengerCountPemesanan);
        tvUpgradeDetailsPemesanan = findViewById(R.id.tvUpgradeDetailsPemesanan);
        tvPricePemesanan = findViewById(R.id.tvPricePemesanan);
        tvTotalPricePemesanan = findViewById(R.id.tvTotalPricePemesanan);

        etNamaLengkapPemesanan = findViewById(R.id.etNamaLengkapPemesanan);
        etNomorTeleponPemesanan = findViewById(R.id.etNomorTeleponPemesanan);
        etEmailPemesanan = findViewById(R.id.etEmailPemesanan);

        llPassengerInputContainer = findViewById(R.id.llPassengerInputContainer);
        btnPesanTiket = findViewById(R.id.btnPesanTiket);
    }

    private void getIntentData() {
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
            adultCount = intent.getIntExtra("adultCount", 1); // Default to 1 adult
            infantCount = intent.getIntExtra("infantCount", 0); // Default to 0 infant
            baseTicketPrice = intent.getLongExtra("baseTicketPrice", 0);
            upgradePrice = intent.getLongExtra("upgradePrice", 0);
            upgradeDetails = intent.getStringExtra("upgradeDetails");
        }
    }

    private void displayTicketDetails() {
        tvShipNamePemesanan.setText("Kapal : " + shipName);
        tvDepartureTimePemesanan.setText(departureTime);
        tvDepartureDatePemesanan.setText(departureDate);
        tvDepartureLocationPemesanan.setText(departureLocation);
        tvArrivalTimePemesanan.setText(arrivalTime);
        tvArrivalDatePemesanan.setText(arrivalDate);
        tvArrivalLocationPemesanan.setText(arrivalLocation);
        tvJenisPenyeberanganPemesanan.setText("Jenis Penyeberangan: " + jenisPenyeberangan);
        tvPassengerCountPemesanan.setText(String.format(Locale.getDefault(), "Jumlah Penumpang: Dewasa (x%d), Bayi (x%d)", adultCount, infantCount));
        tvPricePemesanan.setText(formatRupiah(baseTicketPrice)); // Display base ticket price here

        if (upgradePrice > 0 && upgradeDetails != null && !upgradeDetails.isEmpty()) {
            tvUpgradeDetailsPemesanan.setVisibility(View.VISIBLE);
            tvUpgradeDetailsPemesanan.setText("Fasilitas Upgrade: " + upgradeDetails);
        } else {
            tvUpgradeDetailsPemesanan.setVisibility(View.GONE);
        }
    }

    private void addPassengerInputFields() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < adultCount; i++) {
            // Add "Dewasa X" label
            TextView adultLabel = new TextView(this);
            adultLabel.setText(String.format(Locale.getDefault(), "Dewasa %d", (i + 1)));
            adultLabel.setTextSize(16f); // Use 'f' for float literal
            adultLabel.setTypeface(null, android.graphics.Typeface.BOLD);
            // Use ContextCompat for getColor for better compatibility
            adultLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) { // Add top margin for subsequent adults
                labelParams.topMargin = (int) getResources().getDimension(R.dimen.margin_top_passenger_label);
            }
            llPassengerInputContainer.addView(adultLabel, labelParams);

            // Add "Nama Lengkap" label for EditText
            TextView nameLabel = new TextView(this);
            nameLabel.setText(R.string.nama_lengkap_label);
            nameLabel.setTextSize(14f);
            nameLabel.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
            llPassengerInputContainer.addView(nameLabel);

            // Add EditText for adult name
            EditText adultNameEt = new EditText(this);
            adultNameEt.setHint(getString(R.string.nama_penumpang_hint_format, "dewasa " + (i + 1)));
            adultNameEt.setInputType(android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS | android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            adultNameEt.setTextSize(16f);
            adultNameEt.setPadding(
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding)
            );
            adultNameEt.setBackgroundResource(R.drawable.rounded_edittext_background);
            LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            etParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin_bottom_edittext);
            llPassengerInputContainer.addView(adultNameEt, etParams);
            adultNameEditTexts.add(adultNameEt);
        }

        for (int i = 0; i < infantCount; i++) {
            // Add "Bayi X" label
            TextView infantLabel = new TextView(this);
            infantLabel.setText(String.format(Locale.getDefault(), "Bayi %d", (i + 1)));
            infantLabel.setTextSize(16f);
            infantLabel.setTypeface(null, android.graphics.Typeface.BOLD);
            infantLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // Add top margin if it's the first infant and there were adults, or if it's a subsequent infant
            if (i == 0 && adultCount > 0) {
                labelParams.topMargin = (int) getResources().getDimension(R.dimen.margin_top_section);
            } else if (i > 0) {
                labelParams.topMargin = (int) getResources().getDimension(R.dimen.margin_top_passenger_label);
            }
            llPassengerInputContainer.addView(infantLabel, labelParams);

            // Add "Nama Lengkap" label for EditText
            TextView nameLabel = new TextView(this);
            nameLabel.setText(R.string.nama_lengkap_label);
            nameLabel.setTextSize(14f);
            nameLabel.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
            llPassengerInputContainer.addView(nameLabel);

            // Add EditText for infant name
            EditText infantNameEt = new EditText(this);
            infantNameEt.setHint(getString(R.string.nama_penumpang_hint_format, "bayi " + (i + 1)));
            infantNameEt.setInputType(android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS | android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            infantNameEt.setTextSize(16f);
            infantNameEt.setPadding(
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding),
                    (int) getResources().getDimension(R.dimen.edittext_padding)
            );
            infantNameEt.setBackgroundResource(R.drawable.rounded_edittext_background);
            LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            etParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin_bottom_edittext);
            llPassengerInputContainer.addView(infantNameEt, etParams);
            infantNameEditTexts.add(infantNameEt);
        }
    }

    private void calculateAndDisplayTotalPrice() {
        finalTotalPrice = baseTicketPrice + upgradePrice;
        tvTotalPricePemesanan.setText(formatRupiah(finalTotalPrice));
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> onBackPressed());

        btnPesanTiket.setOnClickListener(v -> {
            if (validateInputs()) {
                collectAndPassDataToConfirmation();
            } else {
                Toast.makeText(PemesananActivity.this, "Harap lengkapi semua data yang diperlukan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        // Validate Pemesan details
        if (etNamaLengkapPemesanan.getText().toString().trim().isEmpty()) {
            etNamaLengkapPemesanan.setError("Nama lengkap tidak boleh kosong");
            return false;
        }
        if (etNomorTeleponPemesanan.getText().toString().trim().isEmpty()) {
            etNomorTeleponPemesanan.setError("Nomor telepon tidak boleh kosong");
            return false;
        }
        if (etEmailPemesanan.getText().toString().trim().isEmpty()) {
            etEmailPemesanan.setError("Email tidak boleh kosong");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailPemesanan.getText().toString().trim()).matches()) {
            etEmailPemesanan.setError("Format email tidak valid");
            return false;
        }

        // Validate Passenger names
        for (EditText et : adultNameEditTexts) {
            if (et.getText().toString().trim().isEmpty()) {
                et.setError("Nama dewasa tidak boleh kosong");
                return false;
            }
        }
        for (EditText et : infantNameEditTexts) {
            if (et.getText().toString().trim().isEmpty()) {
                et.setError("Nama bayi tidak boleh kosong");
                return false;
            }
        }
        return true;
    }

    private void collectAndPassDataToConfirmation() {
        String pemesanNama = etNamaLengkapPemesanan.getText().toString().trim();
        String pemesanTelp = etNomorTeleponPemesanan.getText().toString().trim();
        String pemesanEmail = etEmailPemesanan.getText().toString().trim();

        ArrayList<String> adultNames = new ArrayList<>();
        for (EditText et : adultNameEditTexts) {
            adultNames.add(et.getText().toString().trim());
        }

        ArrayList<String> infantNames = new ArrayList<>();
        for (EditText et : infantNameEditTexts) {
            infantNames.add(et.getText().toString().trim());
        }

        // Pastikan KonfirmasiPemesananActivity ada di package yang sama atau diimpor
        Intent confirmIntent = new Intent(PemesananActivity.this, KonfirmasiPemesananActivity.class);

        // Pass original ticket details
        confirmIntent.putExtra("shipName", shipName);
        confirmIntent.putExtra("departureTime", departureTime);
        confirmIntent.putExtra("departureDate", departureDate);
        confirmIntent.putExtra("departureLocation", departureLocation);
        confirmIntent.putExtra("arrivalTime", arrivalTime);
        confirmIntent.putExtra("arrivalDate", arrivalDate);
        confirmIntent.putExtra("arrivalLocation", arrivalLocation);
        confirmIntent.putExtra("jenisPenyeberangan", jenisPenyeberangan);
        confirmIntent.putExtra("adultCount", adultCount);
        confirmIntent.putExtra("infantCount", infantCount);
        confirmIntent.putExtra("baseTicketPrice", baseTicketPrice);
        confirmIntent.putExtra("upgradePrice", upgradePrice);
        confirmIntent.putExtra("upgradeDetails", upgradeDetails);
        confirmIntent.putExtra("finalTotalPrice", finalTotalPrice);

        // Pass Pemesan details
        confirmIntent.putExtra("pemesanNama", pemesanNama);
        confirmIntent.putExtra("pemesanTelp", pemesanTelp);
        confirmIntent.putExtra("pemesanEmail", pemesanEmail);

        // Pass Passenger names
        confirmIntent.putStringArrayListExtra("adultNames", adultNames);
        confirmIntent.putStringArrayListExtra("infantNames", infantNames);

        startActivity(confirmIntent);
    }

    // Helper method to format long to Rupiah currency
    private String formatRupiah(long amount) {
        return String.format(Locale.getDefault(), "Rp %,d", amount).replace(",", ".");
    }
}