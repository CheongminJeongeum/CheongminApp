package sm.cheongminapp.view.viewholder;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
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
        final ChatSignData signData = (ChatSignData) object;
        videoView.setTag(signData);

        signData.setPlayIndex(0);
        // 첫번째 영상 재생
        videoView.setVideoPath(signData.getSignDataList().get(signData.getPlayIndex()).getVideoPath());

        // 영상 재생이 완료되면 다음 영상으로 넘기거나 정지
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(signData.getSignDataList().size() > signData.getPlayIndex() + 1)
                {
                    signData.setPlayIndex(signData.getPlayIndex() + 1);
                    videoView.setVideoPath(signData.getSignDataList().get(signData.getPlayIndex()).getVideoPath());
                    videoView.start();
                }
            }
        });


        videoView.start();
    }
}
