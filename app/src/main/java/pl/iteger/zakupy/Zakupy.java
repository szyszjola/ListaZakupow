package pl.iteger.zakupy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jolanta.szyszkiewicz on 2017.11.22.
 */

public class Zakupy implements Parcelable {


    String Produkt;
    String Ilosc;
    int id;

    protected Zakupy(Parcel in) {
        Produkt = in.readString();
        Ilosc = in.readString();
        id = in.readInt();
    }

    public static final Creator<Zakupy> CREATOR = new Creator<Zakupy>() {
        @Override
        public Zakupy createFromParcel(Parcel in) {
            return new Zakupy(in);
        }

        @Override
        public Zakupy[] newArray(int size) {
            return new Zakupy[size];
        }
    };

    public String getProdukt() {
        return Produkt;
    }

    public void setProdukt(String produkt) {
        Produkt = produkt;
    }

    public String getIlosc() {
        return Ilosc;
    }

    public void setIlosc(String ilosc) {
        Ilosc = ilosc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Zakupy(int id, String produkt, String ilosc) {
        this.id = id;
        this.Ilosc = ilosc;
        this.Produkt = produkt;
    }

    public Zakupy()
    {}


    @Override
    public String toString() {
        return "[" + id + "] " + Produkt + ", x" + Ilosc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Produkt);
        parcel.writeString(Ilosc);
        parcel.writeInt(id);
    }
}

