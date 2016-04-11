package com.brother.yckx.control.activity.uppay;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import android.util.Base64;

public class RSAUtil {

    public static final String RSA = "RSA";
    public static final String RSA_PADDING_MODE = "RSA";
    public static final String ALGORITHM_RSA_SIGN = "SHA1withRSA";
    private static final String RSA_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
    private static final String RSA_NOPADDING = "RSA/ECB/NoPadding";
    public static final int RSAKEYLEN = 2048;

    /** key_lable:key_lable */
    public static final String KEY_LABEL = "key_label";

    /** data:data */
    public static final String DATA = "data";

    /** text:text */
    public static final String TEXT = "text";

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static PublicKey clientPublicKey;

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * RSA加密运算�?
     * 
     * @param data
     * @param publicKey
     * @return 加密结果
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_PADDING_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA解密运算�?
     * 
     * @param data
     * @param privateKey
     * @return 解密成功则返回解密结�?,否则返回null.
     */
    public static byte[] decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_PADDING_MODE);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA解密运算
     * 
     * @param priKey
     * @param data
     * @param padding
     * @return
     */
    public static byte[] decrypt(PrivateKey priKey, byte[] data,
            String padding, String provider) {
        try {
            Cipher cipher = Cipher.getInstance(padding, provider);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据模数和公钥指数生成公�?
     * 
     * @param modulus
     * @param publicExponent
     * @return 公钥
     */
    // public static PublicKey generateRSAPublicKey(String modulus,
    // String publicExponent) {
    // try {
    // KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    // RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
    // modulus), new BigInteger(publicExponent));
    //
    // PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
    // return publicKey;
    // // return keyFactory.generatePublic(pubKeySpec);
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * 根据字节流产生公�?
     * 
     * @param key
     * @return 公钥
     */
    public static PublicKey generateRSAPublicKey(byte[] key) {
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据字节流产生私�?
     * 
     * @param key
     * @return 私钥
     */
    public static PrivateKey generateRSAPrivateKey(byte[] key) {
        try {
            PKCS8EncodedKeySpec pkcs8keyspec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8keyspec);
            return priKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据模和指数生成私钥
     * 
     * @param modulus
     * @param privateExponent
     * @return 私钥
     */
    public static PrivateKey generateRSAPrivateKey(String modulus,
            String privateExponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPrivateKeySpec pubKeySpec = new RSAPrivateKeySpec(
                    new BigInteger(modulus), new BigInteger(privateExponent));
            return keyFactory.generatePrivate(pubKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用公钥对数据进行加密，并返回byte[]类型
     * 
     * @param publicKey
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptDataBytes(PublicKey publicKey, byte[] data)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
                    : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i
                            * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i
                            * blockSize, raw, i * outputSize);
                }
                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static PrivateKey getPrivateKey(String priKeyData) throws Exception {
        /*
         * n:512 e:512 d:512 p:256 q:256 dmp1:256 dmq1:256 iqmp:256
         */
        BigInteger modulus = new BigInteger(priKeyData.substring(8, 512 + 8),
                16);
        BigInteger publicExponent = new BigInteger(priKeyData.substring(
                512 + 8, 512 + 8 + 512), 16);
        BigInteger privateExponent = new BigInteger(priKeyData.substring(
                512 + 8 + 512, 512 + 8 + 512 + 512), 16);
        BigInteger primeP = new BigInteger(priKeyData.substring(
                512 + 8 + 512 + 512, 512 + 8 + 512 + 512 + 256), 16);
        BigInteger primeQ = new BigInteger(priKeyData.substring(512 + 8 + 512
                + 512 + 256, 512 + 8 + 512 + 512 + 256 + 256), 16);
        BigInteger primeExponentP = new BigInteger(
                priKeyData.substring(512 + 8 + 512 + 512 + 256 + 256, 512 + 8
                        + 512 + 512 + 256 + 256 + 256), 16);
        BigInteger primeExponentQ = new BigInteger(priKeyData.substring(512 + 8
                + 512 + 512 + 256 + 256 + 256, 512 + 8 + 512 + 512 + 256 + 256
                + 256 + 256), 16);
        BigInteger crtCoefficient = new BigInteger(priKeyData.substring(512 + 8
                + 512 + 512 + 256 + 256 + 256 + 256, 512 + 8 + 512 + 512 + 256
                + 256 + 256 + 256 + 256), 16);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(
                modulus, publicExponent, privateExponent, primeP, primeQ,
                primeExponentP, primeExponentQ, crtCoefficient);
        return keyFactory.generatePrivate(rsaPrivateKeySpec);
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        BigInteger bigIntModulus = new BigInteger(modulus, 16);

        BigInteger bigIntPublicExponent = new BigInteger(publicExponent, 16);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPublicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 根据模数和公钥指数生成公�?
     * 
     * @param modulus
     * @param publicExponent
     * @return 公钥
     */
    public static PublicKey generateRSAPublicKey(String modulus,
            String publicExponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
                    modulus), new BigInteger(publicExponent));
            return keyFactory.generatePublic(pubKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKeyPM() {
        // 请将此处的module换成PM环境商户验签的公钥模�?
        String modulus = "23648629510357402173669374843546537318532861396089478651610490265597426690711092692490012429464861104676801339474220894685964389750254240882066338437712341498313076007251358899488346743554156067576120095739341094220657657611893755799646325194641430110114613586989866468748149428464174345443169749235358776080247588710246733575431530477273705811466095207773188767974550742707293785661521305267533098997705930724499157184797236612324838287379798375903922360666026664942383548006246201656190964746068225967889145661249463716565124050082767382345820178584568857820200627919768134084891356188058390460707236118612628845159";
        String publicExponent = "65537";
        PublicKey publicKey = RSAUtil.generateRSAPublicKey(modulus,
                publicExponent);
        return publicKey;
    }

    public static PublicKey getPublicKeyProduct() {
        // 请将此处的module换成生产环境商户验签的公钥模�?
        String modulus = "24882698307025187401768229621661046262584590315978248721358993520593720674589904440569546585666019820242051570504151753011145804842286060932917913063481673780509705461614953345565639235206110825500286080970112119864280897521494849627888301696007067301658192870705725665343356870712277918685009799388229000694331337917299248049043161583425309743997726880393752539043378681782404204317246630750179082094887254614603968643698185220012572776981256942180397391050384441191238689965500817914744059136226832836964600497185974686263216711646940573711995536080829974535604890076661028920284600607547181058581575296480113060083";
        String publicExponent = "65537";
        PublicKey publicKey = RSAUtil.generateRSAPublicKey(modulus,
                publicExponent);
        return publicKey;
    }

    public static boolean verifyPM(byte[] message, byte[] signature)
            throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublicKeyPM());
        sig.update(message);
        return sig.verify(signature);
    }

    public static boolean verifyProduct(byte[] message, byte[] signature)
            throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublicKeyProduct());
        sig.update(message);
        return sig.verify(signature);
    }

    public static String sha1(byte[] raw) {

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(raw);
            byte[] bytes = messageDigest.digest();
            return bytesToHex(bytes);
        } catch (Exception e) {

            return null;
        }
    }

    public static boolean verify(String msg, String sign64, String mode) {
        boolean ret = false;
        try {
            if ("01".equals(mode)) {
                ret = RSAUtil.verifyPM(RSAUtil.sha1(msg.getBytes()).getBytes(),
                        Base64.decode(sign64, Base64.NO_WRAP));
            } else if ("00".equals(mode)) {
                ret = RSAUtil.verifyProduct(RSAUtil.sha1(msg.getBytes())
                        .getBytes(), Base64.decode(sign64, Base64.NO_WRAP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * �?16进制的字符串转换成bytes
     * 
     * @param hex
     * @return 转化后的byte数组
     */
    public static byte[] hexToBytes(String hex) {
        return hexToBytes(hex.toCharArray());
    }

    /**
     * �?16进制的字符数组转换成byte数组
     * 
     * @param hex
     * @return 转换后的byte数组
     */
    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127) {
                value -= 256;
            }
            raw[i] = (byte) value;
        }
        return raw;
    }

    /**
     * 将byte数组转换�?16进制字符�?
     * 
     * @param bytes
     * @return 16进制字符�?
     */
    public static String bytesToHex(byte[] bytes) {
        String hexArray = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int bi = b & 0xff;
            sb.append(hexArray.charAt(bi >> 4));
            sb.append(hexArray.charAt(bi & 0xf));
        }
        return sb.toString();
    }

    public static String publicDecrypt(PublicKey key, byte[] enc) {
        Cipher cipher = null;
        String decText = "";

        if (null == enc) {
            return decText;
        }

        try {
            cipher = Cipher.getInstance(RSA_NOPADDING);
            // byte[] data = PBOCUtils.hexStringToBytes(message);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] enBytes = cipher.doFinal(enc);
            decText = bytesToHex(enBytes);
            // decText = new String(enBytes);
            // Log.e("RSA", "encText:" + decText);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decText;

    }
}
