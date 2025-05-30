package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor {
    private GZIPOutputStream gzipOs;
    private ByteArrayOutputStream bOs;

    public byte[] gzipCompression(byte[] uncompressedData){

        try{
            bOs = new ByteArrayOutputStream();
            gzipOs = new GZIPOutputStream(bOs);
    
            gzipOs.write(uncompressedData);
            gzipOs.close();
            
            return bOs.toByteArray();
        }catch(IOException e){
            e.printStackTrace();
            return new byte[]{};
        }
    }
    
}
     