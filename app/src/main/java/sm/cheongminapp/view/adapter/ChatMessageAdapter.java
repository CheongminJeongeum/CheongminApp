package sm.cheongminapp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sm.cheongminapp.view.viewholder.BaseViewHolder;
import sm.cheongminapp.data.ChatInput;
import sm.cheongminapp.view.viewholder.ChatInputVH;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.view.viewholder.ChatResponseVH;
import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatResponse;

public class ChatMessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<ChatObject> chatObjects;

    public ChatMessageAdapter(ArrayList<ChatObject> arrayList)
    {
        chatObjects = arrayList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Create the ViewHolder based on the viewType
        View itemView;
        switch (viewType) {
            case ChatObject.INPUT_OBJECT:
                itemView = inflater.inflate(R.layout.layout_chat_input, parent, false);
                return new ChatInputVH(itemView);
            case ChatObject.RESPONSE_OBJECT:
                itemView = inflater.inflate(R.layout.layout_chat_response, parent, false);
                return new ChatResponseVH(itemView);
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

    public void addChatInput(String text) {
        ChatInput input = new ChatInput();
        input.setText(text);

        chatObjects.add(input);
    }

    public void addResponseInput(String text) {
        ChatResponse input = new ChatResponse();
        input.setText(text);

        chatObjects.add(input);
    }

}
