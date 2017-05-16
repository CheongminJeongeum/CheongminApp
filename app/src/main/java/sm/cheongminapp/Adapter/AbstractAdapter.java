package sm.cheongminapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by user on 2017. 5. 14..
 */
public class AbstractAdapter<T> extends BaseAdapter {
    ArrayList<T> adapterList = new ArrayList<T>();
    LayoutInflater mInflator;

    public AbstractAdapter(Context ctx) {
        super();
        adapterList = new ArrayList<T>();
        mInflator = ((Activity)ctx).getLayoutInflater();
    }

    public void addOrderList(ArrayList<T> obj) {
        adapterList.addAll(obj);
    }

    public void clear() {
        adapterList.clear();
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int i) {
        return adapterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
