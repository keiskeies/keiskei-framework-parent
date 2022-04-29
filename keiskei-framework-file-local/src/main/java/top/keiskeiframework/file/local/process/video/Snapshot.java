package top.keiskeiframework.file.local.process.video;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.StringUtils;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 17:07
 */
@Data
public class Snapshot {

    /**
     * 指定截图时间。	[0,视频时长]
     * 单位：ms
     */
    private Long t;
    /**
     * 指定截图宽度，如果指定为0，则自动计算。	[0,视频宽度]
     * 单位：像素（px）
     */
    private Integer w;
    /**
     * 指定截图高度，如果指定为0，则自动计算；如果w和h都为0，则输出为原视频宽高。	[0,视频高度]
     * 单位：像素（px）
     */
    private Integer h;
    /**
     * 指定截图模式，不指定则为默认模式，根据时间精确截图。如果指定为fast，则截取该时间点之前的最近的一个关键帧。	枚举值：fast
     */
    private String m;
    /**
     * 指定输出图片的格式。	枚举值：jpg、png
     */
    private String f;
    /**
     * 指定是否根据视频信息自动旋转图片。如果指定为auto，则会在截图生成之后根据视频旋转信息进行自动旋转。	枚举值：auto
     */
    private String ar;

    public Snapshot(String[] actions) {
        for (String action : actions) {
            String[] params = action.split("_");
            switch (params[0]) {
                case "t":this.t = Long.valueOf(params[1]);break;
                case "w":this.w = Integer.valueOf(params[1]);break;
                case "h":this.h = Integer.valueOf(params[1]);break;
                case "m":this.m = params[1];break;
                case "f":this.f = params[1];break;
                case "ar":this.ar = params[1];break;
                default: break;
            }
        }
    }
    public BufferedImage snapshot(File video) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        ff.start();
        // 截取中间帧图片(具体依实际情况而定)
        int i = 0;
        long allTime = ff.getLengthInTime() / 1000;
        int length = ff.getLengthInFrames();
        if (t < 0 || t > allTime) {
            t = 0L;
        }
        t = (long) (t * 1D * length / allTime);
        Frame frame = null;
        while (i < length) {
            frame = ff.grabFrame();
            if ((i > t) && (frame.image != null)) {
                break;
            }
            i++;
        }


        // 截取的帧图片
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcImage = converter.getBufferedImage(frame);
        int srcImageWidth = srcImage.getWidth();
        int srcImageHeight = srcImage.getHeight();

        // 对截图进行等比例缩放(缩略图)

        int width, height;
        if (null != w && 0 != w) {
            width = w;
            height = (int) (((double) width / srcImageWidth) * srcImageHeight);
        } else if (null != h && 0 != h) {
            height = h;
            width = (int) (((double) height / srcImageHeight) * srcImageWidth);
        } else {
            width = srcImageWidth;
            height = srcImageHeight;
        }

        BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        ff.stop();
        return thumbnailImage;
    }

}
