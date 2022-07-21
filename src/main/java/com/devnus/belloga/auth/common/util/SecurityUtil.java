package com.devnus.belloga.auth.common.util;

import com.devnus.belloga.auth.common.exception.error.EncryptException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;

@Component
public class SecurityUtil {

    /**
     * static 메서드에서 접근할 수 있도록 static 변수로 사용
     */
    private static String secretKey;

    /**
     * static 변수에 @Value어노테이션이 적용 되도록하는 setter
     */
    @Value("${app.jwt.payload.secret-key}")
    public void setSecretKey(String secretKey) {
        SecurityUtil.secretKey = secretKey;
    }

    /**
     * SHA-256으로 암호화
     */
    public static String encryptSHA256(String text) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            byte[] bytes = md.digest();
            StringBuilder builder = new StringBuilder();
            for(byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("지원하지 않는 암호화 알고리즘 사용",e);
        }
    }

    /**
     * AES-256 암호화에 사용될 Key
     */
    private static Key getAES256Key() {
        Key key = null;
        try{
            byte[] keyBytes = new byte[16];
            byte[] secretKeyBytes = secretKey.getBytes("UTF-8");
            int len = secretKeyBytes.length;

            if(len > keyBytes.length){
                len = keyBytes.length;
            }
            System.arraycopy(secretKeyBytes,0,keyBytes,0, len);
            key = new SecretKeySpec(keyBytes,"AES");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException(e);
        }
        return key;
    }

    /**
     * AES-256으로 암호화
     * 토큰 payload의 UserId 암호화에 사용
     * 복호화는 API Gateway에 연결된 Authorizer에서 진행
     */
    public static String encryptAES256(String text) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Key key = getAES256Key();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(secretKey.substring(0,16).getBytes()));
        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(encrypted));
    }
}
