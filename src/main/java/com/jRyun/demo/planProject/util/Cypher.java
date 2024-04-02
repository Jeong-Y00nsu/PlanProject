package com.jRyun.demo.planProject.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Cypher {

    // 양방향 대칭키 암호화
    // AES 암호 알고리즘에 사용
    static final private String aesStringKey = "aeskey12345678987654321asekey987"; //256
    static final private String aesIv = "aesiv12345678912";

    private SecretKeySpec secretKey;
    private IvParameterSpec IV;

    /*메소드명: Aes
     * 기 능: 생성자(키 생성, IV 생성)
     * 입 력: 키의 베이스가 될 문자열과 IV의 베이스가 될 iv
     * 출 력: 없음
     * */
    public void initAes() throws UnsupportedEncodingException, NoSuchAlgorithmException{
        // 바이트 배열로부터 ScretKey 생성
        this.secretKey = new SecretKeySpec(aesStringKey.getBytes("UTF-8"), "AES");
        // 바이트 배열로부터 IV 생성
        this.IV= new IvParameterSpec(aesIv.getBytes());
    }

    /*메소드명: aesCBCEncode
     * 기 능: AES CBC PKCS5Padding 암호화 함수
     * 입 력: 평문
     * 출 력: 16진수 암호문 */
    public String aesCBCEncode(String plainText) throws Exception{
        //Cipher 객체 인스턴스화(Java에서는 PKCS#5 = PKCS#7이랑 동일)
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //Cipher 객체 초기화
        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
        //암호화
        byte[] encrpytionByte = c.doFinal(plainText.getBytes("UTF-8"));
        //Hex Encode
        return Hex.encodeHexString(encrpytionByte);
    }

    /*메소드명: aesCBCDecode
     * 기 능: AES CBC PKCS5Padding 복호화 함수
     * 입 력: 암호문 문자열
     * 출 력: 평문 문자열 */
    public String aesCBCDecode(String encodeText) throws Exception{
        //Cipher 객체 인스턴스화(Java에서는 PKCS#5 = PKCS#7이랑 동일)
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //Cipher c = Cipher.getInstance("AES/CBC/noPadding"); //복호화될 때 뒤에 깨진 글자가 같이 출력됨
        //Cipher 객체 초기화
        c.init(Cipher.DECRYPT_MODE, secretKey, IV);
        //Decode Hex
        byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());
        //복호화
        return new String(c.doFinal(decodeByte),"UTF-8");
    }

    // 단방향 암호화
    //SHA256 알고리즘
    public String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
