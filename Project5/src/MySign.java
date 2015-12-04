import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Chris on 12/3/15.
 */
public class MySign {
    public static void main(String[] args)throws FileNotFoundException, NoSuchAlgorithmException, IOException{
        if(args.length !=2 ){
            System.out.println("Please use appropriate flags ");
            return;
        }
        String flag = args[0];
        String filename = args[1];

        if(flag.equals("s")){
            s(filename);
        }
        else if(flag.equals("v")){
            v(filename);
        }
    }
    public static void s(String file)throws FileNotFoundException, NoSuchAlgorithmException, IOException{
        File f = new File(file);
        Scanner s = new Scanner(f);
        ArrayList<String> fileLines = new ArrayList<>();
        String fileString= "";
        while(s.hasNext()){
            String str = s.nextLine();
            fileLines.add(str);
            fileString+=str;

        }
        MessageDigest m = MessageDigest.getInstance("SHA-256");

        BigInteger hash = new BigInteger(m.digest(fileString.getBytes()));
        System.out.println(hash);
        File priv = new File("privkey.rsa");
        s = new Scanner(priv);
        BigInteger d = new BigInteger(s.nextLine());
        BigInteger n = new BigInteger(s.nextLine());

        BigInteger decrpyt = hash.modPow(d,n);
        System.out.println(decrpyt);


        FileWriter writer = new FileWriter(file+".signed");
        writer.write(decrpyt + "\n");
        for(String line: fileLines){
            writer.write(line + "\n");
        }
        writer.close();
    }
    public static void v(String file) throws FileNotFoundException, NoSuchAlgorithmException{
        File f = new File(file);
        Scanner s = new Scanner(f);
        ArrayList<String> fileLines = new ArrayList<>();
        String fileString= "";
        BigInteger decrypted = new BigInteger(s.nextLine());
        while(s.hasNext()){
            String str = s.nextLine();
            fileLines.add(str);
            fileString+=str;
        }
        MessageDigest m = MessageDigest.getInstance("SHA-256");

        BigInteger hash = new BigInteger(m.digest(fileString.getBytes()));
        System.out.println(hash);
        File pub = new File("pubkey.rsa");
        Scanner scan = new Scanner(pub);
        String s1 = scan.nextLine();
        //System.out.println(s1);
        String s2 = scan.nextLine();

        //System.out.println(s2);
        BigInteger e = new BigInteger(s1);
        BigInteger n = new BigInteger(s2);

        BigInteger encrpt = hash.modPow(e,n);


        System.out.println(encrpt);
        System.out.println(encrpt.equals(decrypted));



    }
}
