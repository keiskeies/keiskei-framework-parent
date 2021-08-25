package top.keiskeiframework.file.local.process;

import lombok.Data;
import top.keiskeiframework.file.local.process.image.*;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 17:11
 */
@Data
public class ImageProcess {
    private Blur blur;
    private Circle circle;
    private Indexcrop indexcrop;
    private Quality quality;
    private Resize resize;
    private Watermark watermark;
    /**
     * [value]	表示对比度的调节程度。值越大，对比度越高。	[-100, 100]
     * 0表示原图对比度；小于0表示低于原图对比度；大于0表示高于原图对比度。
     */
    private Integer contrast;
    /**
     * 进行自动旋转
     *
     * 0：表示按原图默认方向，不进行自动旋转。
     *
     * 1：先进行图片旋转，然后再进行缩略。
     *
     * 0 和 1，默认是 1
     */
    private Integer autoOrient = 1;
    /**
     * [value]	指定图片的亮度。	[-100, 100]
     * 0表示原图亮度，小于0表示低于原图亮度，大于0表示高于原图亮度。
     */
    private Integer bright = 0;
    /**
     * jpg	将原图保存成 jpg 格式，如果原图是 png、webp、bmp 存在透明通道，默认会把透明填充成白色。
     * png	将原图保存成 png 格式。
     * webp	将原图保存成 webp 格式。
     * bmp	将原图保存成 bmp 格式。
     * gif	将 gif 格式保存成 gif 格式，非 gif 格式是按原图格式保存。
     * tiff	将原图保存成 tiff 格式。
     */
    private String format;
    /**
     * 1 表示保存成渐进显示的 jpg 格式。
     * 0 表示保存成普通的 jpg 格式。
     * [0, 1]
     */
    private Integer interlace;
    /**
     * [value]	图片按顺时针旋转的角度	[0, 360]
     * 默认值：0，表示不旋转。
     */
    private Double rotate;
    /**
     * 表示锐化效果的强度。值越大，图片越清晰，但过大的值可能会导致图片失真。	[50,399]
     * 为达到较优效果，推荐取值为100。
     */
    private Integer sharpen;

    public ImageProcess(String[] processes) {
        for (String process :processes) {
            String[] actions = process.split(",");
            switch (actions[0]) {
                case "contrast":this.contrast = Integer.valueOf(actions[1]);break;
                case "auto-orient":this.autoOrient = Integer.valueOf(actions[1]);break;
                case "bright":this.bright = Integer.valueOf(actions[1]);break;
                case "format":this.format = actions[1];break;
                case "interlace":this.interlace = Integer.valueOf(actions[1]);break;
                case "rotate" :this.rotate = Double.valueOf(actions[1]);break;
                case "blur": this.blur = new Blur(actions);break;
                case "circle": this.circle = new Circle(actions);break;
                case "indexcrop": this.indexcrop = new Indexcrop(actions);break;
                case "quality": this.quality = new Quality(actions);break;
                case "resize": this.resize = new Resize(actions);break;
                case "watermark": this.watermark = new Watermark(actions);break;
                default: break;
            }

        }
    }

}
