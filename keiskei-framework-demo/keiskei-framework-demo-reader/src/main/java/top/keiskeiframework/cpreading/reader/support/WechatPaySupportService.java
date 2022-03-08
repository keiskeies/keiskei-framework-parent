package top.keiskeiframework.cpreading.reader.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.exception.GlobalExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.HttpClientUtils;
import top.keiskeiframework.cpreading.reader.config.MiniAppProperties;
import top.keiskeiframework.cpreading.reader.config.WechatPayProperties;
import top.keiskeiframework.cpreading.dto.wechat.*;
import top.keiskeiframework.cpreading.feeling.dto.wechat.*;
import top.keiskeiframework.cpreading.reader.dto.wechat.*;
import top.keiskeiframework.cpreading.reader.enums.AppTypeEnum;
import top.keiskeiframework.cpreading.reader.enums.PaymentWayEnum;
import top.keiskeiframework.cpreading.reader.sdk.WXPay;
import top.keiskeiframework.cpreading.reader.sdk.WXPaySdkConfig;
import top.keiskeiframework.cpreading.reader.sdk.WXPayUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;
import java.util.Map;


/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/1 15:15
 */
@Component
@Slf4j
public class WechatPaySupportService {

    @Autowired
    private WechatPayProperties wechatPayProperties;

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 获取用户OPENID
     *
     * @param code        .
     * @param appTypeEnum .
     * @param appName     .
     * @return .
     */
    public WechatUserJudgeResponse getOpenid(String code, AppTypeEnum appTypeEnum, String appName) {
        AppInfo appInfo = getAppSecret(appName);
        String url = String.format(appTypeEnum.getUrlFormat(), appInfo.getAppId(), appInfo.getSecret(), code);
        String result = HttpClientUtils.getRequest(url);
        log.info("获取openid url：{}, result:{}", url, result);
        return JSON.parseObject(result, WechatUserJudgeResponse.class);
    }


    /**
     * 获取用户信息
     *
     * @param accessToken . accessToken
     * @param openid      .      openID
     * @return . .
     */
    public WechatUserInfoResponse getWechatH5UserInfo(String accessToken, String openid) {
        String url = String.format(AppTypeEnum.H5.getUserInfoUrlFormat(), accessToken, openid);
        String result = HttpClientUtils.getRequest(url);
        log.info("获取H5用户信息 url：{}, result:{}", url, result);
        return JSON.parseObject(result, WechatUserInfoResponse.class);

    }

    /**
     * 用户支付完成后，获取该用户的 UnionId，无需用户授权。本接口支持第三方平台代理查询。
     * 注意：调用前需要用户完成支付，且在支付后的五分钟内有效。
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public String getMiniPaidUnionId(String accessToken, String openId) {
        String url = String.format(AppTypeEnum.MIN_APP.getUserInfoUrlFormat(), accessToken, openId);
        String result = HttpClientUtils.getRequest(url);
        log.info("获取该用户的 UnionId url：{}, result:{}", url, result);

        JSONObject jsonObject = JSON.parseObject(result);
        if (null != jsonObject.getInteger("errcode")) {
            throw new RuntimeException(result);
        } else {
            return jsonObject.getString("unionid");
        }
    }


    /**
     * 获取H5全局唯一后台接口调用凭据
     *
     * @return .
     */
    public String getH5AccessToken() {
        String url = String.format(AppTypeEnum.H5.getAccessTokenUrlFormat(), wechatPayProperties.getAppId(), wechatPayProperties.getSecret());
        String result = HttpClientUtils.getRequest(url);
        log.info("获取H5全局唯一后台接口调用凭据 url：{}, result:{}", url, result);

        JSONObject jsonObject = JSON.parseObject(result);
        if (null != jsonObject.getInteger("errcode")) {
            throw new RuntimeException(result);
        } else {
            return jsonObject.getString("access_token");
        }
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据
     *
     * @param appName .
     * @return .
     */
    public String getMiniAccessToken(String appName) {
        AppInfo appInfo = getAppSecret(appName);
        String url = String.format(AppTypeEnum.MIN_APP.getAccessTokenUrlFormat(), appInfo.getAppId(), appInfo.getSecret());
        String result = HttpClientUtils.getRequest(url);
        log.info("获取小程序全局唯一后台接口调用凭据 url：{}, result:{}", url, result);

        JSONObject jsonObject = JSON.parseObject(result);
        if (null != jsonObject.getInteger("errcode")) {
            throw new RuntimeException(result);
        } else {
            return jsonObject.getString("access_token");
        }
    }

    /**
     * 统一支付订单
     *
     * @param wechatPayRequest .
     * @return .
     */
    public WechatPayResponse pay(WechatPayRequest wechatPayRequest, String appName) {

        AppInfo appInfo = getAppSecret(appName);

        wechatPayRequest.setAppid(appInfo.getAppId());
        wechatPayRequest.setMch_id(wechatPayProperties.getMchId());
        wechatPayRequest.setTrade_type(PaymentWayEnum.JSAPI.getType());
        wechatPayRequest.setNotify_url(wechatPayProperties.getNotifyUrl());

        WXPaySdkConfig wxPaySdkConfig = new WXPaySdkConfig(wechatPayProperties, appName);
        try {
            WXPay wxPay = new WXPay(wxPaySdkConfig);
            Map<String, String> requestMap = wechatPayRequest.toMap();
            Map<String, String> responseMap = wxPay.unifiedOrder(requestMap);
            WechatPayResponse wechatPayResponse = WechatPayResponse.mapToBean(responseMap);
            if (!"SUCCESS".equals(wechatPayResponse.getReturn_code())) {
                log.error("调用微信SDK创建微信预支付订单请求失败,{}", wechatPayResponse);
                throw new BizException(BizExceptionEnum.WXPREPAYERROR.getCode(), wechatPayResponse.getReturn_msg());
            }
            return wechatPayResponse;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用微信SDK创建微信预支付订单出现异常: {}", wechatPayRequest);
            throw new BizException(GlobalExceptionEnum.SERVER_ERROR);
        }
    }


    /**
     * 退款
     *
     * @param wechatRefundRequest .
     * @return .
     */
    public WechatRefundNotifyResponse refund(WechatRefundRequest wechatRefundRequest, String appName) {
        AppInfo appInfo = getAppSecret(appName);
        wechatRefundRequest.setAppid(appInfo.getAppId());
        wechatRefundRequest.setMch_id(wechatPayProperties.getMchId());
        wechatRefundRequest.setNotify_url(wechatPayProperties.getRefundNotifyUrl());

        WXPaySdkConfig wxPaySdkConfig = new WXPaySdkConfig(wechatPayProperties, appName);
        try {
            WXPay wxPay = new WXPay(wxPaySdkConfig);
            Map<String, String> responseMap = wxPay.refund(wechatRefundRequest.toMap());
            WechatRefundNotifyResponse wechatRefundNotifyResponse = WechatRefundNotifyResponse.mapToBean(responseMap);

            if (!"SUCCESS".equals(wechatRefundNotifyResponse.getReturn_code())) {
                log.error("调用微信支付生成退款订单请求失败,{}", wechatRefundNotifyResponse);
                throw new BizException(BizExceptionEnum.WXPREPAYERROR.getCode(), wechatRefundNotifyResponse.getReturn_msg());
            }
            return wechatRefundNotifyResponse;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用微信SDK创建微信退款订单出现异常: {}", wechatRefundRequest);
            throw new BizException(GlobalExceptionEnum.SERVER_ERROR);
        }

    }


    /**
     * 获取退款详情
     *
     * @param str .
     * @return .
     * @throws Exception
     */
    public WechatRefundNotifyDetailResponse decode(String str) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] strDecoded = decoder.decode(str);
        String keyMd5 = WXPayUtil.MD5(wechatPayProperties.getKey());

        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyMd5.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        String detail = new String(cipher.doFinal(strDecoded));

        return WechatRefundNotifyDetailResponse.mapToBean(WXPayUtil.xmlToMap(detail));
    }

    /**
     * @param appName .
     * @return .
     */
    private AppInfo getAppSecret(String appName) {
        AppInfo appInfo = new AppInfo();
        if (!StringUtils.isEmpty(appName)) {
            MiniAppProperties miniAppProperties = wechatPayProperties.getMiniApps().get(appName);
            if (null == miniAppProperties) {
                throw new RuntimeException("小程序配置错误");
            }
            appInfo.setAppId(miniAppProperties.getAppId());
            appInfo.setSecret(miniAppProperties.getSecret());
        } else {
            appInfo.setAppId(wechatPayProperties.getAppId());
            appInfo.setSecret(wechatPayProperties.getSecret());
        }
        return appInfo;
    }

    /**
     * 获取request中xml
     *
     * @param inputStream .
     * @return .
     * @throws IOException
     */
    public String readStreamString(InputStream inputStream) throws IOException {
        try (
                InputStreamReader input = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(input)
        ) {
            StringBuilder xml = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                xml.append(line);
                line = bufferedReader.readLine();
            }
            return xml.toString();
        }
    }


    /**
     * 签名验证
     *
     * @param xmlString .
     * @return .
     */
    public boolean checkSign(String xmlString) {
        WXPaySdkConfig wxPaySdkConfig = new WXPaySdkConfig(wechatPayProperties, null);
        try {
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xmlString);
            WXPay wxpay = new WXPay(wxPaySdkConfig);
            if (!wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                log.error("微信回调参数签名不正确: {}", xmlString);
                return false;
            }
            log.info("微信回调参数签名验证成功: {}", xmlString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信回调参数失败: {}", xmlString);
            return false;
        }
    }

}
