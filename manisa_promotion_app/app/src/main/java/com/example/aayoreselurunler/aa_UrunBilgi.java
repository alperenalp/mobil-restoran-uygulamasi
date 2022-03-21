package com.example.aayoreselurunler;

import android.net.UrlQuerySanitizer;

import java.net.URL;

public class aa_UrunBilgi {
    public String aa_isim;
    public String aa_fiyat;
    public String aa_aciklama;
    public String aa_olcu;
    public String aa_odeme_yontemi;
    public String aa_promosyon;
    public String aa_url;

    public aa_UrunBilgi() {
    }

    public aa_UrunBilgi(String aa_isim, String aa_fiyat, String aa_aciklama, String aa_olcu, String aa_odeme_yontemi, String aa_promosyon, String aa_url) {
        this.aa_isim = aa_isim;
        this.aa_fiyat = aa_fiyat;
        this.aa_aciklama = aa_aciklama;
        this.aa_olcu = aa_olcu;
        this.aa_odeme_yontemi = aa_odeme_yontemi;
        this.aa_promosyon = aa_promosyon;
        this.aa_url = aa_url;
    }
}
