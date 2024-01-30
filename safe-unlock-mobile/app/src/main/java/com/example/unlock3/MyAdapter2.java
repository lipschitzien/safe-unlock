package com.example.unlock3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter2 extends BaseAdapter {
    //utiliser ArrayList<Note> pour stocker tous les données dans la bdd
    private ArrayList<PersonRasp> list;

    //LayoutInflater est pour transforme un layout en objet de View
    //par exemple ici layout "itemlayout" en View
    private LayoutInflater layoutInflater;

    //quand on crée un objet de MyAdapter , on a besoin de données de "list"
    //context: indiquer c'est dans quelle page/activity
    public MyAdapter2(Context context, ArrayList<PersonRasp> list) {
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
        ViewHolder2 viewHolder;
        if(view == null){
            view = layoutInflater.inflate(R.layout.rasp_itemlayout, null, false);
            viewHolder = new ViewHolder2(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder2) view.getTag();
        }
        //charger les contenus de bdd dans les view
        PersonRasp personRasp = (PersonRasp) getItem(i);
        //viewHolder.t_nu.setText(String.valueOf(note.getId()));
        viewHolder.t_nu.setText(String.valueOf(personRasp.getId()));
        viewHolder.t_person.setText(personRasp.getPersonname());
        viewHolder.t_action.setText(personRasp.getResultat());
        viewHolder.t_date.setText(personRasp.getDate());
        /*
        // obtenir indice
        int index = this.list.indexOf(note);

        // utiliser indice
        viewHolder.t_nu.setText(String.valueOf(index + 1));

         */

        return view;
    }

    //pour charger les données pour le "view" d'item
    //cette classe est pour trouver les objets
    //cette action est dans la classe ViewHolder
    class ViewHolder2{

        TextView t_nu, t_person, t_action, t_date;
        public ViewHolder2(View view){
            this.t_nu = view.findViewById(R.id.item_nu);
            this.t_person = view.findViewById(R.id.person);
            this.t_action = view.findViewById(R.id.item_action);
            this.t_date = view.findViewById(R.id.item_date);
        }

    }
}
