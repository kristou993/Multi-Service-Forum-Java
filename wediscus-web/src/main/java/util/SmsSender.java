package util;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class SmsSender {

    public static final String ACCOUNT_SID 		= "AC0b755c74af611737c70d946cde2679ed";
    public static final String AUTH_TOKEN  		= "d7e86737ee664a8cc7e768e37aeb9204";
    public static final String API_PHONE   		= "+16193049387";

    public static void SendSMS(String to, String body){
        
        Message message = Message
                .creator(new PhoneNumber(to),  // to
                         new PhoneNumber(API_PHONE),  // from
                         body)
                .create();
    }
}
