import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class JadwalActivity {
}
package com.junel.tugasprojek(arjun&elin);

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.junel.myapplication.R;

public class MainActivity extends AppCompatActivity {

    Button btnPilihUtama, btnPilihAlternatif;
    CheckBox cbKasurKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sesuaikan nama layout jika berbeda

        // Hubungkan tombol dan checkbox dari XML
        btnPilihUtama = findViewById(R.id.btnPilih);
        btnPilihAlternatif = findViewById(R.id.btnPilihAlt);
        cbKasurKamar = findViewById(R.id.cbKasurKamar);

        // Aksi tombol rute utama
        btnPilihUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Rute utama dipilih", Toast.LENGTH_SHORT).show();
                // Tambahkan logika lanjut, seperti: startActivity(new Intent(...))
            }
        });

        // Aksi tombol rute alternatif
        btnPilihAlternatif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Rute alternatif dipilih", Toast.LENGTH_SHORT).show();
                // Tambahkan logika lanjut
            }
        });

        // Aksi checkbox filter
        cbKasurKamar.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this, "Menampilkan hanya rute dengan kasur & kamar", Toast.LENGTH_SHORT).show();
                // Bisa sembunyikan/filtrasi CardView lain di sini
            } else {
                Toast.makeText(MainActivity.this, "Menampilkan semua rute", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
