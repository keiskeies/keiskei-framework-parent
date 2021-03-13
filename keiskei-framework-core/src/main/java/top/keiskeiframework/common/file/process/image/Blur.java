package top.keiskeiframework.common.file.process.image;

import lombok.Data;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/6/6 16:48
 */
@Data
public class Blur {

    /**
     * 模糊半径	[1,50]
     * 该值越大，图片越模糊。
     */
    private Integer r;
    /**
     * 正态分布的标准差	[1,50]
     * 该值越大，图片越模糊。
     */
    private Integer s;

    public Blur(String[] actions) {
        for (int i = 1; i < actions.length; i++) {
            String[] params = actions[i].split("_");
            switch (params[0]) {
                case "r": this.r = Integer.valueOf(params[1]);break;
                case "s":this.s = Integer.valueOf(params[1]);break;
                default: break;
            }
        }
    }
}
