package com.zkyt.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author kings
 */
public class RSAUtils {
    //private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
//    private static SysConfig sysConfig = SpringContextUtils.getBean(SysConfig.class);

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDrKWklonbo4ZFgwLgE63wt+sHiB4Uq0Tl6V6g2Aoj5IPRd4TL8mJlMkaJu/c3G212jhjbUjYkWn5HOnrUwrVN1q+mJGGUCED6ruTNMiAc1Oe5gbLwGIeyk4IMuZFUVwq7XdTgVfFo9cVRAM8jIWftJrCm63ZrqqU/AdBoU4FSTQIDAQAB";
    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIOspaSWidujhkWDAuATrfC36weIHhSrROXpXqDYCiPkg9F3hMvyYmUyRom79zcbbXaOGNtSNiRafkc6etTCtU3Wr6YkYZQIQPqu5M0yIBzU57mBsvAYh7KTggy5kVRXCrtd1OBV8Wj1xVEAzyMhZ+0msKbrdmuqpT8B0GhTgVJNAgMBAAECgYBRXXDp2IANcem5sHApaN+1/OBFwi3cVcz4SRHJUFZLwZxW5oTvxbJZmAyQdc8YhWFJvhrMA+wi4KxhdViLelmvflUQ8OkiGPvvd8nFcHtrjLSI6DyMPdZzt9LwbNp24wpzW8H7viLF96jD1p+KnRlMPSZZY95UbBb8mwgb8cY0nQJBANqueW7KeFVpnFdk8IzKNTIFiB1Pm8S+7xzqb1RqPHyC5s7BqUTXiIXk+c9XGU7QcLOM2fhgsF3hmAFeBx4aSyMCQQCaJRkGQ6O/jBVcweT2mUj3iJ0xgPR918LFwG5O03ZC6ibWb5GDcLhgDi/dcROy8nTA9AhBjg1ptSchl4zO9LvPAkA/2VdtSMKPtbFHNxqhEN0tSL4wSLEjZdgs6iotTINym0nGrna8ue2LooppxnWGNIfjdMmjop98GcUnCQws9uJfAkAYFugcRIHYs4m7H0orB7WRibYXQHH1ILz9rp8OjpmSx4keRf1Vr04BJPZyAvPzKVT5uql3pT2PYZIl+8szpKedAkEArk2Dua/fdgnohm42gxkWTBupdALkNKCSOKKZCykAUXUo075L5fQ/kjchPdU63LdNODoOklfRGe5d9fdjuH/dDw==";

    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        //keyMap.put(0,publicKeyString);  //0表示公钥
        //keyMap.put(1,privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}