package top.keiskeiframework.file.local.process.image;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片水印
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 17:04
 */
@Data
public class Watermark {

    /**
     * 可选参数，指定水印的透明度。如果是图片水印，就是让图片变得透明，如果是文字水印，就是让水印变透明。
     * <p>
     * [0，100]
     * 默认值：100， 表示 100%（不透明）
     */
    private Integer t = 100;
    /**
     * 可选参数，指定水印在图片中的位置，详情参考下方区域数值对应图。
     * <p>
     * [nw,north,ne,west,center,east,sw,south,se]
     */
    private String g;
    /**
     * 可选参数，指定水印的水平边距， 即距离图片边缘的水平距离。这个参数只有当水印位置是左上、左中、左下、右上、右中、右下才有意义。
     * <p>
     * [0，4096]
     * 默认值：10
     * <p>
     * 单位：像素（px）
     */
    private Integer x;
    /**
     * 可选参数，指定水印的垂直边距，即距离图片边缘的垂直距离， 这个参数只有当水印位置是左上、中上、右上、左下、中下、右下才有意义。	[0，4096]
     * 默认值：10
     * <p>
     * 单位：像素（px）
     */
    private Integer y;
    /**
     * 可选参数，指定水印的中线垂直偏移。当水印位置在左中、中部、右中时，可以指定水印位置根据中线往上或者往下偏移。	[-1000， 1000]
     * 默认值：0
     * <p>
     * 单位：像素（px）
     */
    private Integer voffset;
    /**
     * 必选参数，用于指定作为水印图片的Object名称，参数值为Object名称进行Base64编码后的字符串。
     * <p>
     * 例如，作为水印图片的Object为Bucket根目录下的panda.png，则编码过后的字符串为cGFuZGEucG5n。
     * <p>
     * 说明
     * 水印图只能使用当前存储空间内的Object。
     * 用于编码的必须为包含前缀的完整Object名称。
     * 例如，作为水印图片的Object为Bucket内image文件夹下的panda.png，则用于编码的Object名称为image/panda.png，编码过后的字符串为aW1hZ2UvcGFuZGEucG5n。
     * <p>
     * Base64编码后的
     */
    private String image;
    /**
     * text
     * 必选参数，指定文字水印的文字内容。参数值为文字内容进行Base64编码后的字符串。
     * <p>
     * 说明
     * 文字水印内容必须是通过以下方法进行Base64编码后的字符串： encodeText = url_safe_base64_encode(fontText)
     * 最大长度为64个字符（即支持汉字最多20个左右）。
     * Base64编码后的字符串
     */
    private String text;
    /**
     * 可选参数，指定文字水印的文字类型。
     * <p>
     * 说明 文字水印的文字类型必须是通过以下方法进行Base64编码后的字符串：encodeText = url_safe_base64_encode(fontType)
     * Base64编码后的字符串，详情请参见文字类型编码对应表。
     * 默认值：wqy-zenhei （ 编码后的值：d3F5LXplbmhlaQ）
     * wqy-zenhei	文泉驿正黑	d3F5LXplbmhlaQ==	根据RFC，可省略填充符=变为d3F5LXplbmhlaQ
     * wqy-microhei	文泉微米黑	d3F5LW1pY3JvaGVp	无
     * fangzhengshusong	方正书宋	ZmFuZ3poZW5nc2h1c29uZw==	根据RFC，可省略填充符=变为ZmFuZ3poZW5nc2h1c29uZw
     * fangzhengkaiti	方正楷体	ZmFuZ3poZW5na2FpdGk=	根据RFC，可省略填充符=变为ZmFuZ3poZW5na2FpdGk
     * fangzhengheiti	方正黑体	ZmFuZ3poZW5naGVpdGk=	根据RFC，可省略填充符=变为ZmFuZ3poZW5naGVpdGk
     * fangzhengfangsong	方正仿宋	ZmFuZ3poZW5nZmFuZ3Nvbmc=	根据RFC，可省略填充符=变为ZmFuZ3poZW5nZmFuZ3Nvbmc
     * droidsansfallback	DroidSansFallback	ZHJvaWRzYW5zZmFsbGJhY2s=	根据RFC，可省略填充符=变为ZHJvaWRzYW5zZmFsbGJhY2s
     */
    private String type = "wqy-zenhei";
    /**
     * 可选参数，指定文字水印的文字颜色。
     * <p>
     * 六位十六进制数，每两位构成RGB颜色。如：000000表示黑色，FFFFFF表示白色。
     * 默认值：000000（黑色）
     */
    private String color = "000000";
    /**
     * 可选参数，指定文字水印的文字大小（单位为 px）。
     *
     * (0，1000]
     * 默认值：40
     *
     * 单位：像素（px）
     */
    private Integer size = 40;
    /**
     * 可选参数，指定文字水印的阴影透明度。
     *
     * [0，100]
     */
    private Integer shadow;
    /**
     * 可选参数，指定文字顺时针旋转角度。
     *
     * [0，360]
     */
    private Double rotate;
    /**
     * 可选参数，指定是否将水印铺满原图。
     *
     * [0，1]
     * 1：表示将水印铺满原图。
     * 0：表示不将水印铺满全图。
     */
    private Integer fill;
    /**
     * 可选参数，指定文字和图片水印的前后顺序。
     *
     * [0，1]
     * 0（默认值）：表示图片水印在前。
     * 1：表示文字水印在前。
     */
    private Integer order;
    /**
     * 可选参数，指定文字水印和图片水印的对齐方式。
     *
     * [0，1，2]
     * 0（默认值）：表示文字水印和图片水印上对齐。
     * 1：表示文字水印和图片水印中对齐。
     * 2：表示文字水印和图片水印下对齐。
     */
    private Integer align;
    /**
     * 可选参数，指定文字水印和图片水印间的间距。
     *
     * [0，1000]
     */
    private Integer interval;
    /**
     * 指定水印图片按照主图的比例进行缩放，取值为缩放的百分比。如设置参数值为10，如果主图为100x100， 水印图片大小就为10x10。当主图变成了200x200，水印图片大小就为20x20。	[1，100]
     */
    private Integer p = 100;
    public Watermark(String[] actions) {
        for (int i = 1; i < actions.length; i++) {
            String[] params = actions[i].split("_");
            switch (params[0]) {
                case "t":this.t = Integer.valueOf(params[1]);break;
                case "g":this.g = params[1];break;
                case "x":this.x = Integer.valueOf(params[1]);break;
                case "y":this.y = Integer.valueOf(params[1]);break;
                case "voffset":this.voffset = Integer.valueOf(params[1]);break;
                case "image":this.image = params[1];break;
                case "text":this.text = params[1];break;
                case "type":this.type = params[1];break;
                case "color":this.color = params[1];break;
                case "size":this.size = Integer.valueOf(params[1]);break;
                case "shadow":this.shadow = Integer.valueOf(params[1]);break;
                case "rotate":this.rotate = Double.valueOf(params[1]);break;
                case "fill":this.fill = Integer.valueOf(params[1]);break;
                case "order":this.order = Integer.valueOf(params[1]);break;
                case "align":this.align = Integer.valueOf(params[1]);break;
                case "interval":this.interval = Integer.valueOf(params[1]);break;
                case "p":this.p = Integer.valueOf(params[1]);break;
                default: break;
            }
        }
    }

    public void watermark(Thumbnails.Builder<File> thumbnails, int srcImgWidth, int srcImgHeight) throws IOException {

        Positions positions = Positions.TOP_LEFT;

        if (!StringUtils.isEmpty(g)) {
            switch (g) {
                case "north": positions = Positions.TOP_CENTER;break;
                case "ne": positions = Positions.TOP_RIGHT;break;
                case "west": positions = Positions.CENTER_LEFT;break;
                case "center": positions = Positions.CENTER; break;
                case "east": positions = Positions.CENTER_RIGHT; break;
                case "sx": positions = Positions.BOTTOM_LEFT; break;
                case "south": positions = Positions.BOTTOM_CENTER; break;
                case "se": positions = Positions.BOTTOM_RIGHT; break;
                default:break;
            }
        }
        BufferedImage bi = null;
        int width,height;
        if (null != image) {
            width = srcImgWidth * p / 100;
            height = srcImgHeight * p / 100;
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] src = decoder.decodeBuffer(image);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(src);
            Thumbnails.Builder<? extends InputStream> thumbnailsWatermark = Thumbnails.of(inputStream);
            thumbnailsWatermark.size(width,height);
            bi = thumbnailsWatermark.asBufferedImage();
        } else if (null != text){
            text = URLDecoder.decode(text, "utf-8");
            BASE64Decoder decoder = new BASE64Decoder();

            Font font = new Font(new String(decoder.decodeBuffer(type)), Font.PLAIN, size);

            height = size;

            String regEx = "[\\u4e00-\\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(text);
            int count = 0;
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count++;
                }
            }
            width = size / 2 * (text.length() + count);

            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            bi = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g = bi.createGraphics();

            g.setColor(new Color(Integer.parseInt(color,16)));
            g.setFont(font);

            g.drawString(text, size / 10, height - size / 10);
            g.dispose();

            if (null != rotate) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bi, "png", os);
                InputStream input = new ByteArrayInputStream(os.toByteArray());
                Thumbnails.Builder<? extends InputStream> thumbnailsWatermark = Thumbnails.of(input);
                thumbnailsWatermark.scale(1D).rotate(rotate);
                bi = thumbnailsWatermark.asBufferedImage();

            }

        }
        assert bi != null;
        thumbnails.watermark(positions,bi,t / 100F);
    }

}
