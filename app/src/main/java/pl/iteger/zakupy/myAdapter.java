package pl.iteger.zakupy;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jolanta.szyszkiewicz on 2017.11.23.
 */

public class myAdapter extends ArrayAdapter<Zakupy> {

    private final LayoutInflater mInflater; //bez final

    private int[] colors = new int[] { 0xFFFFFFFF, 0xFFC4DFF9 };

    public myAdapter(@NonNull Context context, int resource, @NonNull List<Zakupy> objects) {
        super(context, resource,  objects);
        mInflater = LayoutInflater.from(getContext());  //from(context)
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder vHolder;
        //jak tworzymy nowe obiekty nasz convert view jest nullem, więc musimy sprawdzić czy aby na pewno nim jest
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_wiadomosci, null); //root - parent, tu, nie ma;

            vHolder = new ViewHolder();
            vHolder.produkt =  convertView.findViewById(R.id.produkt_szczegol); // wykona się tylko gdy tworzymy nowy widok, oszczędzamy baterie, a referencja jest przechowywana w vHolder
            vHolder.ilosc =  convertView.findViewById(R.id.ilosc_szczegol);
            vHolder.id =  convertView.findViewById(R.id.id_szczegol);

            convertView.setTag(vHolder); //możemy przez setTag wrzucić wsio
        } else {
            vHolder = (ViewHolder) convertView.getTag(); //jeśli już convertView istniał, to znaczy że wcześniej zostal utworzony Holder, więc go pobieramy
        }

        Zakupy zakupy = getItem(position);
        vHolder.produkt.setText(zakupy.getProdukt());
        vHolder.ilosc.setText(zakupy.getIlosc());
        vHolder.id.setText(String.valueOf(zakupy.getId()));
        if(position %2 != 0)
        {
            convertView.setBackgroundColor(Color.rgb(251,234,209));
        }
        else
        {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private static class ViewHolder { // ta klasa będzie trzymała referencje do naszych widoków
        public TextView produkt;
        public TextView ilosc;
        public TextView id;
    }
}
