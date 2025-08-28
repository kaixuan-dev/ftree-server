package org.example.ftree.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @param content 待加密字符串
     * @param salt    加密盐
     * @return
     */
    public static String encrypt(String content, String salt) {
        try {
            //获得密码的字节数组
            byte[] raw = salt.getBytes();
            //根据密码生成AES密钥
            SecretKeySpec skey = new SecretKeySpec(raw, KEY_ALGORITHM);
            //根据指定算法ALGORITHM自成密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥 16位
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            //获取加密内容的字节数组(设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_content = content.getBytes("utf-8");
            //密码器加密数据
            byte[] encode_content = cipher.doFinal(byte_content);
            //将加密后的数据转换为Base64编码的字符串返回
            return Base64.encodeBase64String(encode_content);
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        return null;
    }

    /**
     * @param content 待解密字符串
     * @param salt    解密盐
     * @return
     */
    public static String decrypt(String content, String salt) {
        try {
            //获得密码的字节数组
            byte[] raw = salt.getBytes();
            //根据密码生成AES密钥
            SecretKeySpec skey = new SecretKeySpec(raw, KEY_ALGORITHM);
            //根据指定算法ALGORITHM自成密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            cipher.init(Cipher.DECRYPT_MODE, skey);
            //把密文字符串Base64转回密文字节数组
            byte[] encode_content = Base64.decodeBase64(content);
            //密码器解密数据
            byte[] byte_content = cipher.doFinal(encode_content);
            //将解密后的数据转换为字符串返回
            return new String(byte_content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("aes decrypt error", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String salt = UUID.randomUUID().toString().replace("-", "");
        String str = encrypt("13100000000", salt);
        System.out.println(str);

        String str1 = decrypt(str, salt);
        System.out.println(str1);

    }

}
