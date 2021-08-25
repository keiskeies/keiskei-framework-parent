package top.keiskeiframework.file.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.io.Serializable;


/**
 * 文件信息接口类
 *
 * @author 陈加敏
 * @since 2019/7/24 10:58
 */
@Data
@Accessors(chain = true)
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -507987531536944960L;
    /**
     * 文件路径
     */
    private String url;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件流
     */
    private InputStream is;
    /**
     * 文件类型
     */
    private String contentType;
}
