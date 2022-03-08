package top.keiskeiframework.cpreading.reader.dto.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/10/30 16:59
 */
@Data
public class WechatUserJudgeResponse {

    /**
     * access_token : 38_wt30U7Hc9IHpmcU28juF_iUvv6raKHMM26Ime4EqQK5u8HhkQEjruRO-XVFuGS-PrkqXrSlAYHN1kqPWPjwBpQ
     * expires_in : 7200
     * refresh_token : 38_P0wCO6Y29AgEg6ucvip8msBBGvck4mMEu8pGiVyShC5ekTIstjN4u3jPTwrLREibSgD43hwBavHPJwAwCxo1Cg
     * openid : o2Q001jEDTZSrmYaK8ltDlJK2hsA
     * scope : snsapi_base
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String session_key;

    /**
     * SUCCESS/FAIL
     * <p>
     * SUCCESS表示商户接收通知成功并校验成功
     **/
    @ApiModelProperty(value = "返回状态码", dataType = "String")
    private String return_code;

    /**
     * 返回信息，如非空，为错误原因：
     * <p>
     * 签名失败
     * <p>
     * 参数格式校验错误
     **/
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String return_msg;
}
