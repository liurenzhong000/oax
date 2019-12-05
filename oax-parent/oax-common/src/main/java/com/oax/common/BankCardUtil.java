package com.oax.common;

import com.oax.common.json.JsonHelper;
import com.oax.common.json.TypeReferences;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡工具类
 */
@Slf4j
public class BankCardUtil {

    private static StringBuffer bankCodeInfoStringBuffer = new StringBuffer();

    private static Map<String, String> bankCodeInfoMap = new HashMap<>();

    private final static String LOGO_URL = "https://apimg.alipay.com/combo.png?d=cashier&t={CODE}";

    private final static String VALIDATE_URL = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo={CARD_NO}&cardBinCheck=true";

    static {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new ClassPathResource("bank_code_info.txt").getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = bufferedReader.readLine();
                if (StringUtils.isBlank(line)) {
                    break;
                }
                bankCodeInfoStringBuffer.append(line);
            }
            bankCodeInfoMap = JsonHelper.readValue(bankCodeInfoStringBuffer.toString(), TypeReferences.REF_MAP_STRING);
        } catch (Exception e) {
            log.error("初始化银行卡代码失败。", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static final String banks[] = {
            "中国银行","中国农业银行","中国建设银行","中国工商银行","招商银行","交通银行","平安银行","上海浦东发展银行","广东发展银行","中国邮政储蓄银行","中国民生银行","中信银行","兴业银行","中国光大银行","北京银行"
    };

    public static List<String> getAllBank() {
        List<String> bankList = Arrays.asList(banks);
        return bankList;
    }

    public static String getLogoByCode(String code) {
        return LOGO_URL.replace("{CODE}", code);
    }

    /**验证银行卡是否正常*/
    public static void validateBankCard(String cardNo, String bankName) {
        String[] url = VALIDATE_URL.replace("{CARD_NO}", cardNo).split("\\?");
        String requestStr = HttpRequestUtil.sendGet(url[0], url[1]);
        Map<String, Object> map = JsonHelper.readValue(requestStr, TypeReferences.REF_MAP_OBJECT);
        boolean validate = (boolean) map.get("validated");
        AssertHelper.isTrue(validate, "请填写正确的银行卡号");
        String bankCode = (String) map.get("bank");
        String name = bankCodeInfoMap.get(bankCode);
        AssertHelper.isTrue(name.equals(bankName), "银行卡号不属于该银行");
        if (!getAllBank().contains(bankName)) {
            AssertHelper.isTrue(false, "暂不支持"+bankName);
        }
    }

    /**
     * 获取银行卡code
     */
    public static String getCodeByCardNo(String cardNo) {
        String[] url = VALIDATE_URL.replace("{CARD_NO}", cardNo).split("\\?");
        String requestStr = HttpRequestUtil.sendGet(url[0], url[1]);
        Map<String, Object> map = JsonHelper.readValue(requestStr, TypeReferences.REF_MAP_OBJECT);
        boolean validate = (boolean) map.get("validated");
        if (validate) {
            return (String) map.get("bank");
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(bankCodeInfoStringBuffer);
//        Map<String, String> jsonMap = JsonHelper.readValue(bankCodeInfoStringBuffer.toString(), TypeReferences.REF_MAP_STRING);
        System.out.println(bankCodeInfoMap.keySet());
        System.out.println(getLogoByCode("TCCB"));
        validateBankCard("6221506020009066385", "中国邮政储蓄银行");
        System.out.println(getCodeByCardNo("6221506020009066384"));
    }

}
