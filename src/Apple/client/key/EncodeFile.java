package Apple.client.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class EncodeFile {

    private FileInputStream fin;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Signature signature;

    public EncodeFile() {
        try {
            signature = Signature.getInstance("DSA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密檔案
     */
    public void encodeFile(PrivateKey privateKey, File file, File enFile) {
        try {

            fin = new FileInputStream(file);
            byte[] xml = new byte[fin.available()];
            fin.read(xml);
            fin.close();

            signature.initSign(privateKey);
            signature.update(xml);
            byte[] signed = signature.sign();

            out = new ObjectOutputStream(new FileOutputStream(enFile));
            out.writeObject(xml);
            out.writeObject(signed);
            out.close();

            System.out.println("加密成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密檔案
     */
    public void decodeFile(PublicKey publicKey, File enFile, File deFile) {
        try {
            fin = new FileInputStream(enFile);
            in = new ObjectInputStream(fin);

            byte[] xml = (byte[]) in.readObject();
            byte[] signed = (byte[]) in.readObject();

            fin.close();
            in.close();

            signature.initVerify(publicKey);
            signature.update(xml);

            if (signature.verify(signed)) {
                FileOutputStream fout = new FileOutputStream(deFile);
                fout.write(xml);
                fout.close();
                System.out.print("解密成功");

            } else {
                System.out.print("解密失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
