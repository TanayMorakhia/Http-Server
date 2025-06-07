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
        System.out.println(sb.toString());
        
        try{
            StringBuilder headers = new StringBuilder();
            for(Map.Entry<String, String> e : reqHashMap.entrySet()){
                headers.append(e.getKey() + " " + e.getValue() + "\r\n");
                System.out.println(sb.toString());
            }
            headers.append("\r\n");
            System.out.println(sb.toString());
            
            //adding headers to the main response
            sb.append(headers);
            System.out.println(sb.toString());
        }catch(NullPointerException e){
            sb.append("\r\n");
            System.out.println(sb.toString());
        }
        
        try{
            sb.append(body);
            System.out.println(sb.toString());
        }catch(NullPointerException e){
            
        }
        
        System.out.println(sb.toString());
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