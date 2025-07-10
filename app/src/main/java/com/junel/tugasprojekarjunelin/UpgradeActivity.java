package com.junel.tugasprojekarjunelin; // Pastikan ini package yang benar

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// import androidx.cardview.widget.CardView; // Tidak digunakan di sini, bisa dihapus
// import com.google.android.material.button.MaterialButton; // Tidak digunakan di sini, bisa dihapus

import java.text.NumberFormat;
import java.util.Locale;

public class UpgradeActivity extends AppCompatActivity {

    // --- Ubah nama konstanta agar konsisten dengan TicketActivity ---
    // Import konstanta dari TicketActivity atau gunakan nama yang sama
    // Sebaiknya import agar lebih aman dan tidak perlu mendefinisikan ulang
    // import static com.junel.tugasprojekarjunelin.TicketActivity.EXTRA_UPGRADE_DETAILS;
    // import static com.junel.tugasprojekarjunelin.TicketActivity.EXTRA_UPGRADE_PRICE;
    // Atau, definisikan ulang dengan nama yang SAMA PERSIS:
    public static final String EXTRA_UPGRADE_DETAILS = "upgradeDetails"; // SAMA DENGAN TICKETACTIVITY
    public static final String EXTRA_UPGRADE_PRICE = "upgradePrice";     // SAMA DENGAN TICKETACTIVITY


    // Views (tetap sama)
    private ImageView backButtonUpgrade;
    private TextView tvQuantityRanjang, tvQuantityBusiness, tvQuantityKamar;
    private ImageView btnMinusRanjang, btnPlusRanjang, btnMinusBusiness, btnPlusBusiness, btnMinusKamar, btnPlusKamar;
    private TextView tvSubtotalUpgrade;
    private Button btnSubmitUpgrade;

    // Harga per unit (tetap sama)
    private static final int PRICE_KASUR_RANJANG = 25000;
    private static final int PRICE_KASUR_BUSINESS = 80000;
    private static final int PRICE_KAMAR_4BED = 350000;

    // Kuantitas yang dipilih (tetap sama)
    private int quantityRanjang = 0;
    private int quantityBusiness = 0;
    private int quantityKamar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade1); // Pastikan ini sesuai dengan nama file XML Anda

        // --- Inisialisasi Views ---
        initViews(); // Panggil metode inisialisasi untuk kerapian

        // --- Setup Listeners untuk tombol Plus/Minus ---
        setupQuantityListeners(btnMinusRanjang, btnPlusRanjang, tvQuantityRanjang, "ranjang");
        setupQuantityListeners(btnMinusBusiness, btnPlusBusiness, tvQuantityBusiness, "business");
        setupQuantityListeners(btnMinusKamar, btnPlusKamar, tvQuantityKamar, "kamar");

        // --- Listeners lainnya ---
        backButtonUpgrade.setOnClickListener(v -> {
            // Jika user menekan tombol close, kirim hasil CANCELLED
            setResult(RESULT_CANCELED);
            finish();
        });

        btnSubmitUpgrade.setOnClickListener(v -> {
            sendResultToTicketActivity();
        });

        // Perbarui subtotal awal
        updateSubtotal();

        // --- PENTING: Inisialisasi quantity dari intent jika ada (untuk edit upgrade) ---
        getIntentData();
    }

    private void initViews() {
        backButtonUpgrade = findViewById(R.id.backButtonUpgrade);
        tvQuantityRanjang = findViewById(R.id.tvQuantityRanjang);
        tvQuantityBusiness = findViewById(R.id.tvQuantityBusiness);
        tvQuantityKamar = findViewById(R.id.tvQuantityKamar);
        btnMinusRanjang = findViewById(R.id.btnMinusRanjang);
        btnPlusRanjang = findViewById(R.id.btnPlusRanjang);
        btnMinusBusiness = findViewById(R.id.btnMinusBusiness);
        btnPlusBusiness = findViewById(R.id.btnPlusBusiness);
        btnMinusKamar = findViewById(R.id.btnMinusKamar);
        btnPlusKamar = findViewById(R.id.btnPlusKamar);
        tvSubtotalUpgrade = findViewById(R.id.tvSubtotalUpgrade);
        btnSubmitUpgrade = findViewById(R.id.btnSubmitUpgrade);
    }

    // Metode untuk menerima data yang sudah ada dari TicketActivity (jika user ingin mengedit upgrade)
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Ambil data upgrade yang mungkin sudah dipilih sebelumnya
            // Ini membantu jika user kembali ke UpgradeActivity untuk mengedit pilihannya
            String existingUpgradeDetails = intent.getStringExtra(EXTRA_UPGRADE_DETAILS);
            long existingUpgradePrice = intent.getLongExtra(EXTRA_UPGRADE_PRICE, 0);

            // Jika ada detail upgrade yang sudah ada, coba parsing dan set kuantitas
            // Logika parsing ini bisa jadi kompleks tergantung format string detail
            // Untuk sementara, kita hanya akan membiarkan kuantitas default 0
            // Jika Anda ingin ini berfungsi, Anda perlu membuat format string details yang mudah diparse
            // Contoh sederhana:
            if (existingUpgradeDetails != null && !existingUpgradeDetails.isEmpty()) {
                // Ini adalah contoh sangat sederhana, Anda mungkin perlu logika parsing yang lebih kuat
                // tergantung bagaimana 'details' dibentuk di TicketActivity
                if (existingUpgradeDetails.contains("Kasur Ranjang")) {
                    // Anda perlu mengekstrak angka dari string ini, contohnya:
                    // quantityRanjang = parseQuantityFromString(existingUpgradeDetails, "Kasur Ranjang");
                    // Untuk saat ini, kita bisa saja set ke 1 atau default jika ada detail
                    // Atau yang lebih baik, TicketActivity juga kirimkan quantity masing-masing jenis upgrade
                    // bukan hanya total detail string.
                }
                // Jika ingin mempertahankan pilihan sebelumnya, Anda harus mengirim kuantitas dari TicketActivity
                // Misalnya:
                // quantityRanjang = intent.getIntExtra("quantityRanjang", 0);
                // quantityBusiness = intent.getIntExtra("quantityBusiness", 0);
                // quantityKamar = intent.getIntExtra("quantityKamar", 0);
                // dan perbarui UI:
                // tvQuantityRanjang.setText(String.valueOf(quantityRanjang));
                // tvQuantityBusiness.setText(String.valueOf(quantityBusiness));
                // tvQuantityKamar.setText(String.valueOf(quantityKamar));
            }
        }
    }


    // Metode bantuan untuk mengatur listener tombol plus/minus
    private void setupQuantityListeners(ImageView btnMinus, ImageView btnPlus, TextView tvQuantity, String type) {
        btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(tvQuantity.getText().toString());
            if (currentQuantity > 0) {
                currentQuantity--;
                tvQuantity.setText(String.valueOf(currentQuantity));
                updateQuantity(type, currentQuantity);
            }
        });

        btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(tvQuantity.getText().toString());
            // Batasan maksimal bisa ditambahkan di sini, misal: if (currentQuantity < 10)
            currentQuantity++;
            tvQuantity.setText(String.valueOf(currentQuantity));
            updateQuantity(type, currentQuantity);
        });
    }

    // Metode untuk memperbarui kuantitas dan subtotal
    private void updateQuantity(String type, int newQuantity) {
        switch (type) {
            case "ranjang":
                quantityRanjang = newQuantity;
                break;
            case "business":
                quantityBusiness = newQuantity;
                break;
            case "kamar":
                quantityKamar = newQuantity;
                break;
        }
        updateSubtotal();
    }

    // Metode untuk menghitung dan memperbarui subtotal
    private void updateSubtotal() {
        long subtotal = (long) quantityRanjang * PRICE_KASUR_RANJANG +
                (long) quantityBusiness * PRICE_KASUR_BUSINESS +
                (long) quantityKamar * PRICE_KAMAR_4BED;
        tvSubtotalUpgrade.setText(formatRupiah(subtotal));
    }

    // Metode untuk format angka ke Rupiah
    private String formatRupiah(long number) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatRupiah.format(number).replace("Rp", "Rp ").replace(",00", "");
    }

    // Metode untuk mengirim hasil kembali ke TicketActivity
    private void sendResultToTicketActivity() {
        Intent resultIntent = new Intent();

        // Bangun string detail upgrade
        StringBuilder details = new StringBuilder();
        if (quantityRanjang > 0) {
            details.append("Kasur Ranjang (x").append(quantityRanjang).append(")");
        }
        if (quantityBusiness > 0) {
            if (details.length() > 0) details.append(", ");
            details.append("Kasur Business (x").append(quantityBusiness).append(")");
        }
        if (quantityKamar > 0) {
            if (details.length() > 0) details.append(", ");
            details.append("Kamar 4 Bed (x").append(quantityKamar).append(")");
        }

        long totalUpgradePrice = (long) quantityRanjang * PRICE_KASUR_RANJANG +
                (long) quantityBusiness * PRICE_KASUR_BUSINESS +
                (long) quantityKamar * PRICE_KAMAR_4BED;

        // --- PENTING: Gunakan konstanta yang SAMA PERSIS dengan TicketActivity ---
        resultIntent.putExtra(EXTRA_UPGRADE_DETAILS, details.toString());
        resultIntent.putExtra(EXTRA_UPGRADE_PRICE, totalUpgradePrice); // Perbaikan di sini, menggunakan EXTRA_UPGRADE_PRICE

        setResult(RESULT_OK, resultIntent); // Set hasil OK dan kirim Intent
        finish(); // Tutup UpgradeActivity
    }

    @Override
    public void onBackPressed() {
        // Ketika tombol back ditekan, juga kirim hasil CANCELLED
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}