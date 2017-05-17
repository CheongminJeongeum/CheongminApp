package sm.cheongminapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatObject;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatResponseVH extends BaseViewHolder {

    private TextView tvResponseText;

    public ChatResponseVH(View itemView) {
        super(itemView);
        this.tvResponseText = (TextView) itemView.findViewById(R.id.tv_response_text);
    }

    @Override
    public void onBindView(ChatObject object) {
        this.tvResponseText.setText(object.getText());
    }
}
