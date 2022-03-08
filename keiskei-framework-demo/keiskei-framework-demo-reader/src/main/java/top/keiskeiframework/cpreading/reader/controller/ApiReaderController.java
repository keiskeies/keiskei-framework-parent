package top.keiskeiframework.cpreading.reader.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.HttpClientUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.cpreading.feeling.dto.*;
import top.keiskeiframework.cpreading.reader.entity.Reader;
import top.keiskeiframework.cpreading.reader.service.IReaderService;
import top.keiskeiframework.cpreading.reader.config.WechatPayProperties;
import top.keiskeiframework.cpreading.reader.dto.wechat.WechatUserInfoResponse;
import top.keiskeiframework.cpreading.reader.dto.wechat.WechatUserJudgeResponse;
import top.keiskeiframework.cpreading.reader.enums.AppTypeEnum;
import top.keiskeiframework.cpreading.reader.sdk.WXPayUtil;
import top.keiskeiframework.cpreading.reader.support.WechatPaySupportService;
import top.keiskeiframework.cpreading.reader.util.SHA1Util;
import top.keiskeiframework.cpreading.reader.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * <p>
 * 微信用户 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/api/v2/cpreading/reader")
@Slf4j
@Api(tags = "碎片阅读 - 微信用户")
public class ApiReaderController {

    @Autowired
    private WechatPaySupportService wechatPaySupportService;
    @Autowired
    private WechatPayProperties wechatPayProperties;
    @Autowired
    private CacheStorageService cacheStorageService;
    @Autowired
    private IReaderService readerService;

    private volatile static String APP_NAME = null;
    private final static int GIVE_NUM = 1000;


    @ApiOperation("微信H5登录")
    @PostMapping("/wechatWeb/login")
    @Lockable(lockName = "VIP_LOGIN_LOCK_INSERT", key = "#request.wxCode")
    public R<VipResponse> loginH5(
            @Validated({Insert.class}) @RequestBody VipRequest request,
            HttpServletRequest httpServletRequest
    ) {
        WechatUserJudgeResponse wechatUserJudge = wechatPaySupportService.getOpenid(request.getWxCode(), AppTypeEnum.H5, null);
        if (!StringUtils.isEmpty(wechatUserJudge.getReturn_code())) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        WechatUserInfoResponse wchatUserInfo = wechatPaySupportService.getWechatH5UserInfo(wechatUserJudge.getAccess_token(), wechatUserJudge.getOpenid());
        if (!StringUtils.isEmpty(wchatUserInfo.getErrcode())) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        Reader reader = readerService.findByColumn("wechatWebOpenid", wechatUserJudge.getOpenid());
        if (null == reader) {
            if (!StringUtils.isEmpty(wchatUserInfo.getUnionid())) {
                reader = readerService.findByColumn("unionid", wchatUserInfo.getUnionid());
            }
            if (null == reader) {
                reader = new Reader();
            }
        }
        reader.setAvatar(wchatUserInfo.getHeadimgurl());
        reader.setName(wchatUserInfo.getNickname());
        reader.setCountry(wchatUserInfo.getCountry());
        reader.setPrivilege(String.join(",", wchatUserInfo.getPrivilege()));
        reader.setCity(wchatUserInfo.getCity());
        reader.setSex(wchatUserInfo.getSexEnum());
        reader.setUnionid(wchatUserInfo.getUnionid());
        reader.setWechatWebOpenid(wechatUserJudge.getOpenid());
        readerService.save(reader);

        VipResponse vipResponse = new VipResponse();
        vipResponse.setId(reader.getId());
        vipResponse.setName(reader.getName());
        vipResponse.setNickName(reader.getNikeName());
        vipResponse.setAvatar(reader.getAvatar());
        vipResponse.setPhone(reader.getPhone());
        vipResponse.setWechatOpenid(reader.getWechatWebOpenid());
        vipResponse.setWxCode(request.getWxCode());
        vipResponse.setAppName(null);

        vipResponse.setToken(httpServletRequest.getSession().getId());
        httpServletRequest.getSession().setAttribute("user", vipResponse);
        return R.ok(vipResponse);
    }

    @ApiOperation("微信小程序获取openid")
    @PostMapping("/wechatMini/openid")
    @Lockable(lockName = "VIP_LOGIN_LOCK_INSERT", key = "#request.wxCode")
    public R<WechatUserJudgeResponse> getOpenid(
            @Validated({Insert.class}) @RequestBody VipMiniAppOpenidRequest request
    ) {
        String appName = getAppName();
        WechatUserJudgeResponse wechatUserJudge = wechatPaySupportService.getOpenid(request.getWxCode(), AppTypeEnum.MIN_APP, appName);
        if (!StringUtils.isEmpty(wechatUserJudge.getReturn_code())) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        cacheStorageService.save("CACHE:MINIAPP:WXCODE:" + request.getWxCode(), wechatUserJudge.getOpenid(), 60L, TimeUnit.SECONDS);
        return R.ok(wechatUserJudge);
    }

    @ApiOperation("微信小程序登录")
    @PostMapping("/wechatMini/login")
    @Lockable(lockName = "VIP_LOGIN_LOCK_INSERT", key = "#request.wxCode")
    public R<VipResponse> loginMini(
            @Validated({Insert.class}) @RequestBody VipMiniAppRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Object openid = cacheStorageService.get("CACHE:MINIAPP:WXCODE:" + request.getWxCode());
        if (StringUtils.isEmpty(openid) || !request.getOpenid().equals(openid)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        String appName = getAppName();
        Reader reader = readerService.findByColumn("wechatMiniOpenid", request.getOpenid());
        if (!StringUtils.isEmpty(request.getUnionid())) {
            reader = readerService.findByColumn("unionid", request.getUnionid());
        }
        if (null == reader) {
            reader = new Reader();
            reader.setAppName(appName);
        }
        reader.setAvatar(request.getAvatar());
        reader.setName(request.getName());
        reader.setCountry(request.getCountry());
        reader.setCity(request.getCity());
        reader.setSex(request.getSexEnum());
        reader.setUnionid(request.getUnionid());
        reader.setWechatMiniOpenid(request.getOpenid());

        readerService.save(reader);

        VipResponse vipResponse = new VipResponse();
        vipResponse.setId(reader.getId());
        vipResponse.setName(reader.getName());
        vipResponse.setNickName(reader.getNikeName());
        vipResponse.setAvatar(reader.getAvatar());
        vipResponse.setPhone(reader.getPhone());
        vipResponse.setWechatOpenid(request.getOpenid());
        vipResponse.setWxCode(request.getWxCode());
        vipResponse.setToken(httpServletRequest.getSession().getId());
        vipResponse.setAppName(appName);

        httpServletRequest.getSession().setAttribute("user", vipResponse);
        return R.ok(vipResponse);
    }


    @ApiOperation("获取微信信息")
    @PostMapping("/wechatWeb/getWxInfo")
    public R<Map<String, String>> getWxInfo(@Validated @RequestBody TicketRequest request) throws Exception {

        String timestamp = (System.currentTimeMillis() / 1000) + "";
        String nonceStr = WXPayUtil.generateNonceStr();
        String ticket = getJSAPITicket();

        Map<String, String> wxInfo = new HashMap<>();

        wxInfo.put("jsapi_ticket", ticket);
        wxInfo.put("timestamp", timestamp);
        wxInfo.put("noncestr", nonceStr);
        wxInfo.put("url", request.getUrl());

        String signature = generateJsApiSignature(wxInfo);
        wxInfo.put("appId", wechatPayProperties.getAppId());
        wxInfo.put("signature", signature);
        return R.ok(wxInfo);
    }

    public static String generateJsApiSignature(final Map<String, String> data) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k.toLowerCase()).append("=").append(data.get(k).trim()).append("&");
            }
        }
        return SHA1Util.encode(sb.toString().substring(0, sb.toString().length() - 1));
    }


    /**
     * 获取微信access_token
     *
     * @return .
     */
    public String getAccessToken() {
        String token = (String) cacheStorageService.get("accessToken");
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        token = wechatPaySupportService.getMiniAccessToken(null);
        cacheStorageService.save("accessToken", token);
        return token;
    }

    public String getJSAPITicket() {
        String ticket = (String) cacheStorageService.get("jsapiTicket");
        if (!StringUtils.isEmpty(ticket)) {
            return ticket;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + getAccessToken();
        String result = HttpClientUtils.getRequest(url);
        log.info("调用微信SDK获取JSAPI_TICKET开始,调用结果:{}", result);
        JSONObject resultJson = JSON.parseObject(result);
        ticket = resultJson.getString("ticket");
        if (!StringUtils.isEmpty(ticket)) {
            cacheStorageService.save("jsapiTicket", ticket, 7000L, TimeUnit.SECONDS);
            return ticket;
        }
        throw new BizException(BizExceptionEnum.ERROR);
    }


    private String getAppName() {
        if (null == APP_NAME) {
            synchronized (ApiReaderController.class) {
                if (null == APP_NAME) {
                    APP_NAME = String.join("", wechatPayProperties.getMiniApps().keySet());
                }
            }
        }
        return APP_NAME;
    }
}
