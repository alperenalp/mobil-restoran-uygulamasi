package com.example.aayoreselurunler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class aa_UrunAdapter extends RecyclerView.Adapter<aa_UrunAdapter.ViewHolder> {
    private ArrayList<aa_UrunBilgi> aa_localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView aa_isim;
        private final TextView aa_fiyat;
        private final TextView aa_aciklama;
        private final TextView aa_odeme_yontemi;
        private final TextView aa_promosyon;
        private final ImageView aa_foto;

        public ViewHolder(View view) {
            super(view);
            aa_isim = (TextView) view.findViewById(R.id.aa_txt_ana_liste_isim);
            aa_fiyat = (TextView) view.findViewById(R.id.aa_txt_ana_liste_fiyat);
            aa_aciklama = (TextView) view.findViewById(R.id.aa_txt_ana_liste_aciklama);
            aa_odeme_yontemi = (TextView) view.findViewById(R.id.aa_txt_ana_liste_odeme_yontemi);
            aa_promosyon = (TextView) view.findViewById(R.id.aa_txt_ana_liste_promosyon);
            aa_foto = view.findViewById(R.id.aa_iv_ana_liste_foto);
        }
    }
    public aa_UrunAdapter(ArrayList<aa_UrunBilgi> dataSet) {
        aa_localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View aa_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aa_analiste_item, viewGroup, false);
        return new ViewHolder(aa_view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String aa_olcu = aa_localDataSet.get(position).aa_olcu;
        viewHolder.aa_isim.setText("Bir " + aa_olcu + " " + aa_localDataSet.get(position).aa_isim);
        viewHolder.aa_fiyat.setText("Fiyatı: " + aa_localDataSet.get(position).aa_fiyat + "TL");
        viewHolder.aa_aciklama.setText("Açıklama: " + aa_localDataSet.get(position).aa_aciklama);
        viewHolder.aa_odeme_yontemi.setText(aa_localDataSet.get(position).aa_odeme_yontemi);
        viewHolder.aa_promosyon.setText(aa_localDataSet.get(position).aa_promosyon);
        String aa_url = aa_localDataSet.get(position).aa_url;
        Picasso.get().load(aa_url).into(viewHolder.aa_foto);
    }

    @Override
    public int getItemCount() {
        return aa_localDataSet.size();
    }
}