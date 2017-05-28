package sm.cheongminapp.data;

/**
 * Created by Raye on 2017-05-22.
 */

public class SignData {
    private String Text;
    private String VideoPath;

    public SignData(String text, String videoPath) {
        this.Text = text;
        this.VideoPath = videoPath;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }
}
