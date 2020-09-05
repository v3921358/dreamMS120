package Apple.client.key;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI
 */
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.io.File;

public class GenKeys {

    public static void main(String[] args) throws Exception {
        //加密
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
            EncodeFile ff = new EncodeFile();
            ff.encodeFile(priv, new File("D:\\jar\\tms120-netty.jar"), new File("D:\\jar\\tms120-netty_.jar"));
            //ff.decodeFile(pub, new File("E:\\Temp\\新增資料夾\\加密後.doc"), new File("E:\\Temp\\新增資料夾\\解密後.doc"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        //加密結束
    }
}
