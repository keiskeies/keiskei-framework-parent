package top.keiskeiframework.file.local.process.image;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:36
 */
@Data
public class Resize {
    /**
     * 倍数百分比。小于100为缩小，大于100为放大。	1~1000
     */
    private Double p;
    /**
     * 指定缩放的模式：
     * lfit：等比缩放，限制在指定w与h的矩形内的最大图片。
     * mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
     * fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
     * pad：固定宽高，缩放填充。
     * fixed：固定宽高，强制缩放。
     * lfit、mfit、fill、pad、fixed，默认为lfit。
     */
    private String m = "lfit";
    /**
     * 指定目标缩略图的宽度。	1~4096
     */
    private Integer w;
    /**
     * 指定目标缩略图的高度。	1~4096
     */
    private Integer h;
    /**
     * 指定目标缩略图的最长边。	1~4096
     */
    private Integer l;
    /**
     * 指定目标缩略图的最短边。	1~4096
     */
    private Integer s;
    /**
     * 指定当目标缩略图大于原图时是否处理。值是1表示不处理；值是0表示处理。	0、1，默认是1
     */
    private Integer limit = 1;
    /**
     * 当缩放模式选择为pad（缩放填充）时，可以选择填充的颜色，默认是白色。参数的填写方式：采用16进制颜色码表示，例如00FF00（绿色）。	[000000~FFFFFF]
     */
    private String color;

    public Resize(String[] actions) {
        for (int i = 1; i < actions.length; i++) {
            String[] params = actions[i].split("_");
            switch (params[0]) {
                case "p":this.p = Double.valueOf(params[1]);break;
                case "m":this.m = params[1];break;
                case "w":this.w = Integer.valueOf(params[1]);break;
                case "h":this.h = Integer.valueOf(params[1]);break;
                case "l":this.l = Integer.valueOf(params[1]);break;
                case "s":this.s = Integer.valueOf(params[1]);break;
                case "limit":this.limit = Integer.valueOf(params[1]);break;
                case "color":this.color = params[1];break;
                default: break;
            }
        }
    }

    public void resize(Thumbnails.Builder<File> thumbnails,int srcImgWidth,int srcImgHeight) {
        // 图片尺寸压缩
        if (null != p) {
            thumbnails.scale(p);
        } else {

            int width, height;
            if (null != l) {
                width = height = l;
            } else if (null != s) {
                width = null != w ? w : srcImgWidth;
                height = null != h ? h : srcImgHeight;
                if (width < height) {
                    height = (int) (s * 1D * height / width);
                    width = s;
                } else {
                    width = (int) (s * 1D * width / height);
                    height = s;
                }
            } else {
                width = null != w ? w : srcImgWidth;
                height = null != h ? h : srcImgHeight;
            }
            if ("mfit".equals(m)) {
                width = Math.min(width, srcImgWidth);
                height = Math.min(height, srcImgHeight);
            } else if ("fixed".equals(m)) {
                thumbnails.keepAspectRatio(false);
            } else if ("fill".equals(m) || "pad".equals(m)) {
                thumbnails.sourceRegion(Positions.CENTER, width, height);
            }
            thumbnails.size(width, height);
        }
    }
}
