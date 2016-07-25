import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RbstUtil {
    public static boolean debugLog = false;

    public RbstUtil() {}

    public static void rbst_log(String s) {
        if(debugLog == true) {
            String[] tokens = s.split("\n");

            for(int i = 0; i < tokens.length; i++) {
                System.out.print(tokens[i] + '\n');
            }

            if(tokens.length == 0) {
                System.out.print(s + '\n');
            }
        }
    }

   public static void rbst_log_no_nl(String s) {
        if(debugLog == true) {
            String[] tokens = s.split("\n");

            for(int i = 0; i < tokens.length; i++) {
                if(i != tokens.length - 1) {
                    System.out.print(tokens[i] + '\n');
                }
                else {
                    System.out.print(tokens[i]);
                }
            }

            if(tokens.length == 0) {
                System.out.print(s + '\n');
            }
        }
    }

    public static void rbst_log_final(String s) {
        String[] tokens = s.split("\n");

        for(int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i] + '\n');
        }

        if(tokens.length == 0) {
            System.out.print(s + '\n');
        }
    }

    public static void rbst_log_final(int s) {
        System.out.print(s);
        System.out.print('\n');
    }

    public static boolean createDir(String dirPath) {
        File dir = new File(dirPath);

        if(!dir.exists()) {
            if(!dir.mkdir()) {
                rbst_log("<createDir> Can't create dirPath");
                return false;
            }
        }
        else {
            rbst_log("<createDir> directory " + dirPath + " already exists");
        }
        return true;
    }

    public static void sendMessage(ObjectOutputStream out, String msg) {
        try {
            //stream write the message
            out.writeObject(msg);
            out.flush();
            rbst_log("<sendMessage: > Send message: " + msg);
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void sendByteStream(ObjectOutputStream out, byte[] msg) {
        try  {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(msg.length);
            dos.write(msg);
            out.flush();
            dos.flush();
            rbst_log("<sendByteStream: byte[] > message sent to Client ");
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void readWrite(RandomAccessFile raf, 
                        BufferedOutputStream bOf, int noOfBytes) 
            throws IOException {
        byte[] buf = new byte[noOfBytes];
        
        int ret = raf.read(buf);
        if(ret != -1) {
            bOf.write(buf);
        }
    }

    public static void writeToOutputStream(byte[] buf, BufferedOutputStream bOf)
            throws IOException {
            bOf.write(buf);
    }
}

