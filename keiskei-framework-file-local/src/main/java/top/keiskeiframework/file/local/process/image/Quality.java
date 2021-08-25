package top.keiskeiframework.file.local.process.image;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 17:02
 */
@Data
public class Quality {

    /**
     * 决定图片的相对质量，对原图按照q%进行质量压缩。例如，如果原图质量为100%，添加quality,q_90参数会得到质量为90％的图片。如果原图质量为80%，添加quality,q_90参数会得到质量72%的图片。
     * 说明 只有为JPG格式的原图添加该参数，才可以决定图片的相对质量。如果原图为WebP格式，添加该参数相当于指定了原图绝对质量，即与参数Q的作用相同。
     * 1~100
     */
    private Integer q;
    /**
     * 决定图片的绝对质量，将原图质量压缩至Q%，如果原图质量小于指定参数值，则按照原图质量重新进行压缩。
     * 例如，如果原图质量是95%，添加quality,Q_90参数会得到质量90％的图片。如果原图质量是80%，添加quality,Q_90只能得到质量80%的图片。
     *
     * 说明 该参数只能对保存格式为JPG、WebP的图片使用，对其他格式的图片无效果。 如果同时指定了q和Q，会按照Q的值进行处理。
     * 1~100
     */
    private Integer qq;

    public Quality(String[] actions) {
        for (int i = 1; i < actions.length; i++) {
            String[] params = actions[i].split("_");
            switch (params[0]) {
                case "q":this.q = Integer.valueOf(params[1]);break;
                case "Q":this.qq = Integer.valueOf(params[1]);break;
                default: break;
            }
        }
    }
    public void quality(Thumbnails.Builder<File> thumbnails, int srcImgWidth, int srcImgHeight) {
        if (null != q) {
            thumbnails.outputQuality(q / 100F);
        } else if (null != qq) {
            thumbnails.outputQuality(qq / 100F);
        }
    }

}
