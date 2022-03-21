package com.example.aayoreselurunler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class aa_YeniUrunEkle extends AppCompatActivity {
    String aa_myisim, aa_myaciklama, aa_myOlcu, aa_myOdeme_yontemi, aa_myPromosyon, aa_myfiyat;
    EditText aa_isim, aa_aciklama, aa_fiyat;
    Spinner aa_olcu;
    RadioButton aa_icecek, aa_sos, aa_promosyon_yok;
    CheckBox aa_taksit, aa_pesin;
    Button aa_urun_ekle, aa_urun_goster;
    ImageView aa_urun_resim;

    FirebaseStorage aa_fstorage;
    FirebaseFirestore aa_veriTabani;
    FirebaseAuth aa_mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aa_yeni_urun_ekle);

        aa_veriTabani = FirebaseFirestore.getInstance();
        aa_fstorage = FirebaseStorage.getInstance();
        aa_mUser = FirebaseAuth.getInstance();

        aa_isim = findViewById(R.id.aa_etxt_urun_isim);
        aa_aciklama = findViewById(R.id.aa_etxt_aciklama);
        aa_fiyat = findViewById(R.id.aa_etxt_urun_fiyat);
        aa_olcu = findViewById(R.id.aa_spinner_olcu);
        aa_icecek = findViewById(R.id.aa_rb_icecek);
        aa_sos = findViewById(R.id.aa_rb_sos);
        aa_promosyon_yok = findViewById(R.id.aa_rb_promosyon_yok);
        aa_taksit = findViewById(R.id.aa_cb_taksit);
        aa_pesin = findViewById(R.id.aa_cb_pesin);
        aa_urun_resim = findViewById(R.id.aa_iv_fotoyeri);
        aa_urun_ekle = findViewById(R.id.aa_btn_ekle);
        aa_urun_goster = findViewById(R.id.aa_btn_urun_goster);

        List<String> aa_spinnerElemanlari = new ArrayList<>();
        aa_spinnerElemanlari.add("Adet");
        aa_spinnerElemanlari.add("Kilo");
        aa_spinnerElemanlari.add("Litre");
        aa_spinnerElemanlari.add("Porsiyon");
        aa_spinnerElemanlari.add("Paket");

        ArrayAdapter<String> aa_adapter = new ArrayAdapter<>(aa_YeniUrunEkle.this,
                android.R.layout.simple_spinner_dropdown_item, aa_spinnerElemanlari);
        aa_olcu.setAdapter(aa_adapter);

        aa_urun_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aa_urun_ekle_click(view);
            }
        });

        aa_urun_goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aa_YeniUrunEkle.this, aa_AnaSayfa.class));
                finish();
            }
        });

        aa_urun_resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aa_fotoSec();
            }
        });
    }


    public void aa_fotoSec() {
        Toast.makeText(aa_YeniUrunEkle.this, "foto seç çağrıldı",
                Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(aa_YeniUrunEkle.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(aa_YeniUrunEkle.this, new String[]{Manifest.permission.
                    READ_EXTERNAL_STORAGE}, 045);
        } else {
            Intent aa_intentfoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(aa_intentfoto, 45);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 045) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentfoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentfoto, 45);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    Uri aa_fotoData;
    Bitmap aa_secilenResim;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 45 && resultCode == RESULT_OK && data != null) {
            aa_fotoData = data.getData();
            try {
                aa_secilenResim = MediaStore.Images.Media.getBitmap(this.getContentResolver(), aa_fotoData);
                aa_urun_resim.setImageBitmap(aa_secilenResim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    String aa_fotoURL;
    private void aa_urun_ekle_click(View view) {
        StorageReference storageRef = aa_fstorage.getReference();
        long aa_zaman = System.nanoTime();
        StorageReference mountainImagesRef = storageRef.child("ManisaYoresi/img" +
                String.valueOf(aa_zaman) + ".jpg");
        Bitmap bitmap = ((BitmapDrawable) aa_urun_resim.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] aa_data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(aa_data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(aa_YeniUrunEkle.this, "Foto yüklenirken sorun oluştu", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            aa_fotoURL = uri.toString();
                            Toast.makeText(aa_YeniUrunEkle.this, "Foto başarılı şekilde yüklendi", Toast.LENGTH_SHORT).show();
                            aa_Urun_Kaydet();
                        }
                    });
                }
            }
        });
    }

    public void aa_Urun_Kaydet() {
        if (aa_pesin.isChecked() && aa_taksit.isChecked())
            aa_myOdeme_yontemi = "Ödeme Yöntemi: Nakit ve Taksit";
        else if (aa_taksit.isChecked())
            aa_myOdeme_yontemi = "Ödeme Yöntemi: Taksit";
        else if (aa_pesin.isChecked())
            aa_myOdeme_yontemi = "Ödeme Yöntemi: Nakit ";
        else
            aa_myOdeme_yontemi = "";

        if (aa_icecek.isChecked() && aa_sos.isChecked())
            aa_myPromosyon = "Promosyon: İçecek ve sos";
        else if (aa_icecek.isChecked())
            aa_myPromosyon = "Promosyon: İçecek";
        else if (aa_sos.isChecked())
            aa_myPromosyon = "Promosyon: Sos";
        else if (aa_promosyon_yok.isChecked())
            aa_myPromosyon = "Promosyon: Yok";
        else
            aa_myPromosyon = "";

        aa_myisim = aa_isim.getText().toString();
        aa_myaciklama = aa_aciklama.getText().toString();
        aa_myfiyat = aa_fiyat.getText().toString();
        aa_myOlcu = aa_olcu.getSelectedItem().toString();

        Log.d("URL", aa_fotoURL);
        Log.d("URL", aa_myisim);
        Log.d("URL", aa_myaciklama);
        Log.d("URL", aa_myfiyat);
        Log.d("URL", aa_myOlcu);
        Log.d("URL", aa_myOdeme_yontemi);
        Log.d("URL", aa_myPromosyon);

        if (!TextUtils.isEmpty(aa_myisim) && !TextUtils.isEmpty(aa_myfiyat)) {
            if (Double.parseDouble(aa_myfiyat) >= 0) {

                Map<String, Object> manisaUrun = new HashMap<>();
                manisaUrun.put("isim", aa_myisim);
                manisaUrun.put("fiyat", aa_myfiyat);
                manisaUrun.put("aciklama", aa_myaciklama);
                manisaUrun.put("odemesekli", aa_myOdeme_yontemi);
                manisaUrun.put("olcu", aa_myOlcu);
                manisaUrun.put("promosyon", aa_myPromosyon);
                manisaUrun.put("url", aa_fotoURL);

                aa_veriTabani.collection("AA_ManisaUrunleri")
                        .add(manisaUrun)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("yeni veri", "yeni veri eklenmiştir " + documentReference.getId());
                                Toast.makeText(aa_YeniUrunEkle.this, "veriler kayıt edildi",
                                        Toast.LENGTH_SHORT).show();
                                aa_myisim = "";
                                aa_myfiyat = "";
                                aa_myaciklama = "";
                                aa_myOdeme_yontemi = "";
                                aa_myOlcu = "";
                                aa_myPromosyon = "";
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("yeni veri", "Beklenmeyen bir hata oluştu", e);
                                Toast.makeText(aa_YeniUrunEkle.this,
                                        "veriler kayıt edilirken beklenmeyen bir hata oluştu", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Alanlar Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(getApplicationContext(), "Ana sayfaya gidiliyor", Toast.LENGTH_LONG).show();
                startActivity(new Intent(aa_YeniUrunEkle.this, aa_AnaSayfa.class));
                finish();
                return true;
            case R.id.cikisyap:
                Toast.makeText(getApplicationContext(), "Çıkış yapılacak", Toast.LENGTH_LONG).show();
                return true;
            case R.id.yeniurun:
                Toast.makeText(getApplicationContext(), "Ürün ekleme sayfasındasınız", Toast.LENGTH_LONG).show();
                return true;
            case R.id.hakkimizda:
                Toast.makeText(getApplicationContext(), "yapim asamasinda", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}