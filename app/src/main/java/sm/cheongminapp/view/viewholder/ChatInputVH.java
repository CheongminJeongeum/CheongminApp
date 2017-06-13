package sm.cheongminapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatObject;

public class ChatInputVH extends BaseViewHolder {

    private TextView tvInputText;
    private TextView tvInputTime;

    public ChatInputVH(View itemView) {
        super(itemView);
        this.tvInputText = (TextView) itemView.findViewById(R.id.layout_chat_input_text);
        this.tvInputTime = (TextView) itemView.findViewById(R.id.layout_chat_input_time_text);
    }

    @Override
    public void onBindView(ChatObject object) {
        this.tvInputText.setText(object.getText());
        this.tvInputTime.setText(object.getTime());
    }
}
