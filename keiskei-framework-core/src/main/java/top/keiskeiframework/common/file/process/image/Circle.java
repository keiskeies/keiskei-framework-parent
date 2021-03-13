package top.keiskeiframework.common.file.process.image;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:40
 */
@Data
public class Circle {
    /**
     * 指定裁剪图片所用的圆形区域的半径
     * 不能超过原图的最小边的一半。如果超过，则仍按照图片最大内切圆进行裁剪。
     */
    private Integer r;
    /**
     * 指定裁剪宽度。	[0~图片宽度]
     */
    private Integer w;
    /**
     * 指定裁剪高度。	[0~图片高度]
     */
    private Integer h;
    /**
     * 指定裁剪起点横坐标（默认左上角为原点）。	[0~图片边界]
     */
    private Integer x;
    /**
     * 指定裁剪起点纵坐标（默认左上角为原点）。	[0~图片边界]
     */
    private Integer y;
    /**
     * 设置裁剪的原点位置。原点按照九宫格的形式分布，一共有九个位置可以设置，为每个九宫格的左上角顶点。
     * nw、north、ne、west、center、east、sw、south、se
     */
    private String g;

    public Circle(String[] actions) {
        for (int i = 1; i < actions.length; i++) {
            String[] params = actions[i].split("_");
            switch (params[0]) {
                case "r": this.r = Integer.valueOf(params[1]);break;
                case "w":this.w = Integer.valueOf(params[1]);break;
                case "h":this.h = Integer.valueOf(params[1]);break;
                case "x":this.x = Integer.valueOf(params[1]);break;
                case "y":this.y = Integer.valueOf(params[1]);break;
                case "g":this.g = params[1];break;
                default: break;
            }
        }
    }
    
    public void circle(Thumbnails.Builder<File> thumbnails, int srcImgWidth, int srcImgHeight) {
        if (null != r) {
            int l = r * 2;
            int m = Math.min(srcImgHeight, srcImgWidth);
            l = Math.min(l, m);
            thumbnails.sourceRegion(Positions.CENTER, l, l);
        } else {
            int x = 0, y = 0;
            int width = srcImgWidth;
            int height = srcImgHeight;
            if (null != this.x && null != this.y) {
                x = this.x;
                y = this.y;
                width = null != w ? w : srcImgWidth;
                height = null != h ? h : srcImgHeight;
            }
            if (null != g) {
                width = width / 3;height = height / 3;
                switch (g) {
                    case "nw":   break;
                    case "north": x = width;break;
                    case "ne": x = width * 2;break;
                    case "west": y = height;break;
                    case "cent": x = width; y = height;break;
                    case "east": x = width * 2; y = height;break;
                    case "sw": y = height * 2;break;
                    case "south": x = width; y = height * 2;break;
                    case "se": x = width * 2; y = height * 2;break;
                    default: break;
                }
            }
            thumbnails.sourceRegion(x,y,width,height);
        }
    }
}
