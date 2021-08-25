package top.keiskeiframework.file.local.process.image;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:41
 */
@Data
public class Indexcrop {
    /**
     * 指定在x轴切割出的每块区域的长度。x参数与y参数只能任选其一。	[1，图片宽度]
     */
    private Integer x;
    /**
     * 指定在y轴切割出的每块区域的长度。x参数与y参数只能任选其一。	[1，图片高度]
     */
    private Integer y;
    /**
     * 选择切割后图片区域。（0表示第一块）	[0，区域数)。
     * 如果取值超出切割出的区域数，返回原图。
     */
    private Integer i;

    public Indexcrop(String[] actions) {
        for (String action : actions) {
            String[] params = action.split(",");
            switch (params[0]) {
                case "i":this.i = Integer.valueOf(params[1]);break;
                case "x":this.x = Integer.valueOf(params[1]);break;
                case "y":this.y = Integer.valueOf(params[1]);break;
                default: break;
            }
        }
    }

    public void indexcrop(Thumbnails.Builder<File> thumbnails, int srcImgWidth, int srcImgHeight) {
        if (null == i || i < 0) {
            i = 0;
        }
        if (null != x) {
            if (x >= srcImgHeight) {
                return;
            }
            int cropNum = srcImgWidth / x;
            i = Math.min(cropNum - 1, i);
            thumbnails.sourceRegion(i * x, 0, x, srcImgHeight);

        } else if (null != y) {
            if (y >= srcImgHeight) {
                return;
            }
            int cropNum = srcImgHeight / y;
            thumbnails.sourceRegion(0, i * y, srcImgWidth, y);
        }
    }
}
