package top.keiskeiframework.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码生成图片工具类
 *
 * @author cjm
 */
@Component
public class VerificationCodeUtil {


    /**
     * 生成验证码的范围：a-zA-Z0-9
     * 去掉0(数字)和O(拼音)容易混淆的(小写的1和L也可以去掉,大写不用了)
     */
    private static final char[] CODE_SEQUENCE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        VerificationCodeUtil.stringRedisTemplate = stringRedisTemplate;
    }

    private static StringRedisTemplate stringRedisTemplate;

    /**
     * 验证码缓存键
     */
    private final static String VERIFY_CODE_CACHE_KEY_FORMAT = "VERIFY_CODE_%s";

    /**
     * 验证验证码
     *
     * @param uuid uuid
     * @param code code
     */
    public static void judgeCode(String uuid, String code) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BizException(BizExceptionEnum.VERIFY_CODE_EXPIRED);
        }
        if (StringUtils.isEmpty(code)) {
            throw new BizException(BizExceptionEnum.VERIFY_CODE_ERROR);
        }
        String key = String.format(VERIFY_CODE_CACHE_KEY_FORMAT, uuid);
        String cacheCode = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(cacheCode)) {
            throw new BizException(BizExceptionEnum.VERIFY_CODE_EXPIRED);
        } else {
            if (!cacheCode.equalsIgnoreCase(code)) {
                throw new BizException(BizExceptionEnum.VERIFY_CODE_ERROR);
            }
            stringRedisTemplate.delete(key);
        }
    }

    /**
     * 生成验证码图片
     *
     * @param response response
     * @param uuid     uuid
     * @throws IOException .
     */
    public static void generateVerificationCode(HttpServletResponse response, String uuid) throws IOException {
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");
        String key = String.format(VERIFY_CODE_CACHE_KEY_FORMAT, uuid);
        String code = generateVerificationCode(200, 50, 5, response.getOutputStream(), "jpeg");
        stringRedisTemplate.opsForValue().set(key, code, 120, TimeUnit.SECONDS);

    }

    /**
     * 随机生成n个验证码的字符串和其图片方法
     * 1、根据宽高创建 BufferedImage图片对象
     * 2、获取图片对象的画笔对象Graphics
     * 3、画笔画入数据（背景色，边框，字体，字体位置，颜色等）
     * 4、最后通过ImageIO.write()方法将图片对象写入OutputStream
     *
     * @param width        - 图片宽度
     * @param height       - 图片高度
     * @param codeCount    - 验证码个数
     * @param outputStream - 保存验证码图片的文件的输出流
     * @param imgFormat    - 图片格式（JPG,PNG等）
     * @return String - 将随机生成的codeCount个验证码以字符串返回
     * @throws IOException IOException
     */
    public static String generateVerificationCode(int width, int height, int codeCount,
                                                  OutputStream outputStream, String imgFormat) throws IOException {
        // 1、根据宽高创建 BufferedImage图片对象
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 2、获取图片对象的画笔对象Graphics
        Graphics2D graphics = bufferedImage.createGraphics();
        // 3、画笔画入数据（背景色，边框，字体，字体位置，颜色等）
        // 背景色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        //边框颜色
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, width - 1, height - 1);
        // 字体
        Font font = new Font("Fixedsys", Font.PLAIN, height - 2);
        // Font font = new Font("微软雅黑", Font.ROMAN_BASELINE, height - 2);
        graphics.setFont(font);

        // 添加干扰线：坐标/颜色随机
        Random random = new Random();
        for (int i = 0; i < (codeCount * 2); i++) {
            graphics.setColor(getRandomColor());
            graphics.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }
        // 添加噪点:
        for (int i = 0; i < (codeCount * 3); i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x, y, 2, 2);
        }

        // 画随机数：颜色随机，宽高自定义
        StringBuilder randomCode = new StringBuilder();
        int charWidth = width / (codeCount + 2);
        int charHeight = height - 5;
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            int x = (i + 1) * charWidth;
            String strRandom = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
            randomCode.append(strRandom);
            graphics.setColor(getRandomColor());
            //设置字体旋转角度
            //角度小于30度
            int degree = random.nextInt() % 30;
            //正向旋转
            graphics.rotate(degree * Math.PI / 180, x, 45);
            graphics.drawString(strRandom, x, charHeight);
            //反向旋转
            graphics.rotate(-degree * Math.PI / 180, x, 45);
        }

        // 4、最后通过ImageIO.write()方法将图片对象写入OutputStream
        ImageIO.write(bufferedImage, imgFormat, outputStream);
        return randomCode.toString();
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    }
}


