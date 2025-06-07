package http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse{

    private String responseCode;

    private HashMap<String, String> reqHashMap;

    private String body;

    private HttpResponse(HttpResponseBuilder builder){
        this.responseCode = builder.responseCode;
        this.reqHashMap = builder.reqHashMap;
        this.body = builder.body;
    }

    public String getResponse(){
        StringBuilder sb = new StringBuilder();
        sb.append(responseCode);
        
        try{
            StringBuilder headers = new StringBuilder();
            for(Map.Entry<String, String> e : reqHashMap.entrySet()){
                headers.append(e.getKey() + " " + e.getValue() + "\r\n");
            }
            headers.append("\r\n");

            //adding headers to the main response
            // System.out.println(headers.toString() + ".....printing headers");
            sb.append(headers);
        }catch(NullPointerException e){
            sb.append("\r\n");
        }

        try{
            // System.out.println(body + "....printing body");
            if(body != null){
                sb.append(body);
            }
        }catch(NullPointerException e){

        }
        
        return sb.toString();
    }

    public static class HttpResponseBuilder{

        private String responseCode;
            
        private HashMap<String, String> reqHashMap;
        private String body;
    
        public HttpResponseBuilder(String responseCode){
            this.responseCode = responseCode;
        }

        public HttpResponseBuilder setReqHashMap(HashMap<String,String> reqHashMap){
            this.reqHashMap = reqHashMap;
            return this;
        }

        public HttpResponseBuilder setBody(String body){
            this.body = body;
            return this;
        }

        public HttpResponse build(){
            return new HttpResponse(this);
        }
    } 

}