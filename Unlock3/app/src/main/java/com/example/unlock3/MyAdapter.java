package com.example.unlock3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends BaseAdapter {
    //utiliser ArrayList<Note> pour stocker tous les données dans la bdd
    private ArrayList<Note> list;

    //LayoutInflater est pour transforme un layout en objet de View
    //par exemple ici layout "itemlayout" en View
    private LayoutInflater layoutInflater;

    //quand on crée un objet de MyAdapter , on a besoin de données de "list"
    //context: indiquer c'est dans quelle page/activity
    public MyAdapter(Context context, ArrayList<Note> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        //on obtient un objet de Note, Note correspond une ligne dans le tableau
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //créer le "view" dans l'item
    //méthode optimisé
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = layoutInflater.inflate(R.layout.itemlayout, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //charger les contenus de bdd dans les view
        Note note = (Note)getItem(i);
        //viewHolder.t_nu.setText(String.valueOf(note.getId()));
        viewHolder.t_action.setText(note.getContent());
        viewHolder.t_date.setText(note.getNote_time());
        // obtenir indice
        int index = this.list.indexOf(note);

        // utiliser indice
        viewHolder.t_nu.setText(String.valueOf(index + 1));

        return view;
    }

    //pour charger les données pour le "view" d'item
    //cette classe est pour trouver les objets
    //cette action est dans la classe ViewHolder
    class ViewHolder{

        TextView t_nu, t_action, t_date;
        public ViewHolder(View view){
            this.t_nu = view.findViewById(R.id.item_nu);
            this.t_action = view.findViewById(R.id.item_action);
            this.t_date = view.findViewById(R.id.item_date);
        }

    }
}
