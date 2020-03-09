package com.example.securitybasicpract;


import org.apache.commons.codec.digest.DigestUtils;

public class CrackingDigestAuthentication {

    public static void main(String[] args) {

        //Digest username="admin", realm="admin-digest-realm",
        // nonce="MTU4Mzc4MjIxNzU5Nzo2ZWJlOWRjODNmZjBhNmViMDViNGRlZTk2NzM1YjZiNw==",
        // uri="/test", response="4dcf30550edaa318e21e85195b435c9c",
        // qop=auth, nc=00000001, cnonce="d25e0235578d3e18"
      /*

      HA1 =MD5(username:realm:password) //password
        HA2 = MD5(method:digestURI)
        response=MD5(HA1:nonce:nonceCount:cnonce:qop:HA2)*/


       /* String username = "admin";
        String realm = "admin-digest-realm";*/
        String method = "GET";
        String digestUri = "/test";
        String nonce = "MTU4Mzc4Mjc1Njc5MTozYWM0ZmIzNzNhMTE3ZGUwY2EwYWIwNTVkMWUyZTY5OQ==";

        //stolenHa1 from data base its md5  hash
        String stolenHa1 = "550b281ad5cb0ab6db9a3e2c72b2f0fc";

        String ha2 = HA2(method, digestUri);
        String digest = generateDigest(stolenHa1, nonce, ha2);
        System.out.println(digest);
    }

   /* public static String HA1(String username, String realm, String password) {
        String prepare = username + ":" + realm + ":" + password;
        return generateMd5Hex(prepare);
    }*/

    public static String HA2(String method, String digestUri) {
        String prepare = method + ":" + digestUri;
        return generateMd5Hex(prepare);
    }

    public static String generateDigest(String ha1, String nonce, String ha2) {
        String prepare = ha1 + ":" + nonce + ":" + ha2;
        return generateMd5Hex(prepare);
    }

    public static String generateMd5Hex(String rawString) {
        return DigestUtils.md5Hex(rawString);
    }

    /**
     * Digest username="admin", realm="admin-digest-realm", nonce="MTU4Mzc4Mjc1Njc5MTozYWM0ZmIzNzNhMTE3ZGUwY2EwYWIwNTVkMWUyZTY5OQ==", uri="/test", response="da6dc2c60369a1a4f1ca51bb16efbc59"
     */
}
