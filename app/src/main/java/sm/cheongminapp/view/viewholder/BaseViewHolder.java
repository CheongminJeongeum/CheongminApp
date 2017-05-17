package sm.cheongminapp.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import sm.cheongminapp.data.ChatObject;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBindView(ChatObject object);
}
