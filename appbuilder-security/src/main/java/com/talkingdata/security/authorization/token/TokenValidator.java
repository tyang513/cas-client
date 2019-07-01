package com.talkingdata.security.authorization.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkingdata.security.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * token验证
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenValidator {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);

    /**
     * cas url prefix, exp : http://cas.example.com/sso
     */
    private final String casUrlPrefix;

    public TokenValidator(String casUrlPrefix) {
        this.casUrlPrefix = casUrlPrefix;
    }

    public String validate(String token) {
        String url = casUrlPrefix + (casUrlPrefix.endsWith("/") ? "" : "/") +  "apb/token?token=" + token;
        logger.info("验证token正确性 token : {}", token);
        String validationMessage = CommonUtils.getResponseFromServer(url, "UTF-8");
        return validationMessage;
    }

    public static void main(String[] args) {
        TokenValidator validator = new TokenValidator("http://localhost:9097/sso");
        String s = validator.validate("eyJjdHkiOiJKV1QiLCJlbmMiOiJBMTkyQ0JDLUhTMzg0IiwiYWxnIjoiZGlyIn0..tc0NCXlc5qNaeNdheB3maQ.xPtWzDgofVE8E30kIhl3tQ9C8m1TZ9gyblfcwK_sj_8qOBHPgadEPLxtXoCK8MNv1iz8MB3fJoW8Ld-ODoWlKQ77l1pNtRA-jxp2Aly0k804mn-bNdGgBQPAIYJ65KDL8iFCWhKP5qnji98JD9w47N0NXHoqIIYhpZdROvtocSCv_Ipcs7MEJw-za6meFUM5mSyIUPjbZCXCmx0iWN3O-NrVo2_5HZga0-eurj6ddkQKi7_fvXsUUzrAwgtjuHUcvh9342b9vv1cEpc_irSAdTezCwu-Zrhga5ndg8PtVUCPLIsJdph_FF8qp6bC7JjBqjpN48qqAMTrYEjvbhO5m2XJdkRZ0s1sL8I-Ugc_HvjEisM5zfiHGXbZ-G2DCx6mfg4whd-m9KxHMUVinXw6knhFnisaSmlzw4Z0Y8OWMwX2_FiI4x2loL4-MP3_iLr5d5fqV4NcyVnoGxeWyIOYSRqOlvw8WzIPc-VqkoSfue-1xdG-ewTymUYvzUQpGUPEVT4dQBl112UMseXPDR6mQa87gd9EZl3h0uY8sLi9SVEN2pfZbWwD6vBqtnJ4ldjEeSun9FzdYq54LVIS3u8LtxXo94nuatJKo0KUfidpjYJ0FSUsxzlXGkv0jHjwh58oUNkIZCbiOfTLG6sWDd7lfOwQy6RiVelkNXftiK4e-a3ZEZkJ--oMdpg5nKjh_zS4s-3MRa8RW8fFkSznES9dSlX766iPHl204J7JmahLdNzb1Y_BKY7tHMa_KlZMZeMaMAdqlFOQXZ_qza3qgfq2WQ.q0PJr2xKnJ-Q0XnkTl69QL7NyPtCBoDU");
        System.out.println(s);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> returnMap = objectMapper.readValue(s, Map.class);
            System.out.println(returnMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

