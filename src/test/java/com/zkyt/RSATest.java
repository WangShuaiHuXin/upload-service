package com.zkyt;

import static com.zkyt.util.RSAUtils.decrypt;
import static com.zkyt.util.RSAUtils.encrypt;

public class RSATest {
    public static void main(String[] args) throws Exception {
        //加密字符串
//		String message = "123456";
//		//System.out.println("随机生成的公钥为:" + publicKey);
//		//System.out.println("随机生成的私钥为:" + privateKey);
//		String messageEn = encrypt(message);
//		System.out.println(message + "\t加密后的字符串为:" + messageEn);
//		String messageDe = decrypt(messageEn);
//		System.out.println("还原后的字符串为:" + messageDe);
//        String encrypt = encrypt("NNaSazdVs1Qb/WmtSr3uu2Huysnz85SsAayJLZV1QIM=");
//        String encode = URLEncoder.encode(encrypt, "UTF-8");
//        System.out.println(encrypt);
//        System.out.println(encode);
//        String s = "KIyyV94V8d5KfsOeomDNR3L31UIQjH7b0W0z3Yzy5wWpG9fTUm20hElCbYUmCE5MDjOKmtvKPkwtDc6VYNvJMoLqKWOySh5Pb1yPV5IqSidn2QHMjhLpuLSv06UuL65jOTyXt4qFf+7+PEZgsC8A0gL16dfSueweCr+fUtzPLYI=";
//        String decrypt = decrypt(s);
//        System.out.println(decrypt);
        System.out.println(encrypt("123"));
        System.out.println(decrypt("fTk9PtYiu2zrcTRoHAbNjYzqEavlKSwStvoz6jRKB/y73vzycnknuGJZeEXXddQme59M+y/NGX0c2p9162FbjpxkYRYIhpGV1Eio60IYbpQxaXzqDr7V5AB0b7PhE//V8vx3OzNHwGUaK80JWfkju5eVwIHVkOnnBCKe/2Bf9p8="));
    }

}
