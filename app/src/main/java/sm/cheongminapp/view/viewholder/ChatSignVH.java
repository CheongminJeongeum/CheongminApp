package sm.cheongminapp.view.viewholder;

import android.view.View;
import android.widget.VideoView;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.data.ChatSignData;

/**
 * Created by Raye on 2017-05-22.
 */

public class ChatSignVH extends BaseViewHolder {

    private VideoView videoView;

    public ChatSignVH(View itemView) {
        super(itemView);

        this.videoView = (VideoView) itemView.findViewById(R.id.layout_chat_sign_video);
    }

    @Override
    public void onBindView(ChatObject object) {
        ChatSignData signData = (ChatSignData) object;

        // 0번째 비디오 경로를 설정
        videoView.setVideoPath(signData.SignDataList.get(0).VideoPath);
        videoView.start();
    }
}
