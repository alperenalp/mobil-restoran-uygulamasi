package com.example.aayoreselurunler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class aa_AnaSayfa extends AppCompatActivity {
    FirebaseAuth aa_mUser;
    RecyclerView aa_anaListe;
    ArrayList<aa_UrunBilgi> aa_urunListe;
    aa_UrunAdapter aa_adapter;
    FirebaseFirestore aa_dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aa_ana_sayfa);
        aa_mUser = FirebaseAuth.getInstance();
        aa_anaListe = findViewById(R.id.aa_recyclerView);
        aa_urunListe = new ArrayList<>();

        aa_adapter = new aa_UrunAdapter(aa_urunListe);

        aa_anaListe.setHasFixedSize(true);
        aa_anaListe.setLayoutManager(new LinearLayoutManager(aa_AnaSayfa.this));
        aa_anaListe.setAdapter(aa_adapter);

        aa_dataBase = FirebaseFirestore.getInstance();
        aa_VerileriGetir();

    }

    public void aa_VerileriGetir() {
        aa_dataBase.collection("AA_ManisaUrunleri").orderBy("isim", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getUrunler", document.getId() + " => " + document.getData());
                                Log.d("getUrunler", document.get("isim").toString());
                                aa_urunListe.add(new aa_UrunBilgi(document.get("isim").toString(),
                                        document.get("fiyat").toString(),
                                        document.get("aciklama").toString(),
                                        document.get("olcu").toString(),
                                        document.get("odemesekli").toString(),
                                        document.get("promosyon").toString(),
                                        document.get("url").toString()));
                            }
                            Log.d("getUrunler", aa_urunListe.size() + "");
                            aa_adapter.notifyDataSetChanged();
                        } else {
                            Log.d("getUrunler", "Sokak Hayvanlarını sunucudan çekerken bir hata oluştu", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kullanici_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.anasayfa:
                Toast.makeText(getApplicationContext(), "Ana sayfadasınız", Toast.LENGTH_LONG).show();
                return true;
            case R.id.cikisyap:
                Toast.makeText(getApplicationContext(), "Çıkış yapılacak", Toast.LENGTH_LONG).show();
                return true;
            case R.id.yeniurun:
                Toast.makeText(getApplicationContext(), "Ürün ekleme sayfasına gidiliyor", Toast.LENGTH_LONG).show();
                startActivity(new Intent(aa_AnaSayfa.this, aa_YeniUrunEkle.class));
                return true;
            case R.id.hakkimizda:
                Toast.makeText(getApplicationContext(), "yapim asamasinda", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}