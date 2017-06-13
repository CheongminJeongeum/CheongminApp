package sm.cheongminapp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sm.cheongminapp.data.ChatSignData;
import sm.cheongminapp.view.viewholder.BaseViewHolder;
import sm.cheongminapp.data.ChatInput;
import sm.cheongminapp.view.viewholder.ChatInputVH;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.view.viewholder.ChatResponseVH;
import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatResponse;
import sm.cheongminapp.view.viewholder.ChatSignVH;

public class ChatMessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<ChatObject> chatObjects = new ArrayList<>();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView;

        switch (viewType) {
            // Input Item
            case ChatObject.INPUT_OBJECT:
                itemView = inflater.inflate(R.layout.layout_chat_input, parent, false);

                return new ChatInputVH(itemView);

            case ChatObject.SIGN_IMAGE_OBJECT:
                itemView = inflater.inflate(R.layout.layout_chat_sign, parent, false);

                return new ChatSignVH(itemView);

            // Response Item
            default:
                itemView = inflater.inflate(R.layout.layout_chat_response, parent, false);

                return new ChatResponseVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindView(chatObjects.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return chatObjects.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return chatObjects.size();
    }

    public void addChatInput(String text, String time) {
        ChatInput input = new ChatInput();
        input.setText(text);
        input.setTime(time);

        chatObjects.add(input);
    }

    public void addResponseInput(String text, String time) {
        ChatResponse input = new ChatResponse();
        input.setText(text);
        input.setTime(time);

        chatObjects.add(input);
    }

    public void addSign(ChatSignData data) {
        chatObjects.add(data);
    }

}
