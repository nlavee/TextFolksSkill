package texthack;

import java.util.*; 

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 
import com.twilio.sdk.resource.list.*;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
 
public class TwillioClient implements KeyVariables { 
	
	public TwillioClient()
	{
		 // empty constructor
	}

	/**
	 * Method to send message from registered phone number to another phone number
	 * @param to
	 * @param from
	 * @param msg
	 * @return
	 * @throws TwilioRestException
	 */
	public String sendMessage(String to, String from, String msg) throws TwilioRestException
	{
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		
		// Build the parameters 
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("To", to)); 
		params.add(new BasicNameValuePair("From", from)); 
		params.add(new BasicNameValuePair("Body", msg));   

		MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		Message message = messageFactory.create(params); 
		return message.getSid();
	} 
	
}
