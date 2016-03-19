/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package texthack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.twilio.sdk.*; 

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class TextHackSpeechlet implements Speechlet, KeyVariables {
	private static final Logger log = LoggerFactory.getLogger(TextHackSpeechlet.class);

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session)
			throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
			throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session)
			throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;
		log.info("Intent Name: " + intentName);

		if ("textHackIntent".equals(intentName)) {

			Map<java.lang.String,Slot> slots = intent.getSlots();

			Slot groupSlot = slots.get("Group");
			Slot actionSlot = slots.get("Action");

			if(groupSlot != null && actionSlot != null)
			{
				String group = groupSlot.getValue();
				String action = actionSlot.getValue();
				log.info("Group: " + group);
				log.info("Action: " + action);

				if(action.equalsIgnoreCase("test"))
				{
					return getTestResponse(group);
				}
				else if(action.equalsIgnoreCase("home"))
				{
					return getHomeReponse(group);
				}
				else if(action.equalsIgnoreCase("order"))
				{
					return getOrderResponse(group);
				}
				else
				{
					return getClarificationResponse();
				}
			} else if ("HelpIntent".equals(intentName)) {
				return getHelpResponse();
			} else {
				throw new SpeechletException("Invalid Intent");
			}
		}
		else
		{
			log.info("Null Group Slot");
			log.info("Null Action Slot");
			return getClarificationResponse();
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session)
			throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Method to send text to housemate asking whether they are home
	 * @param group
	 * @return
	 */
	private SpeechletResponse getHomeReponse(String group) {
		try
		{
			String from = TWILLIO_REGISTERED_NUMBER;
			String msg = HOME_MESSAGE;
			TwillioClient testText = new TwillioClient();

			ArrayList<String> recipients = new ArrayList<String>();

			if(group.equalsIgnoreCase("Asia"))
			{
				recipients.add(JAPAN);
				recipients.add(VIETNAM);
				recipients.add(KENYA);
				recipients.add(CHINA);
				recipients.add(NEWYORK);

			}
			else if(group.equalsIgnoreCase("Vietnam"))
			{
				recipients.add(VIETNAM);
			}
			else if(group.equalsIgnoreCase("Japan"))
			{
				recipients.add(JAPAN);
			}
			else if(group.equalsIgnoreCase("China"))
			{
				recipients.add(CHINA);
			}
			else if(group.equalsIgnoreCase("Kenya"))
			{
				recipients.add(KENYA);
			}
			else if(group.equalsIgnoreCase("New York"))
			{
				recipients.add(NEWYORK);
			}

			for(String recipient : recipients)
			{
				String logText = testText.sendMessage(recipient, from, msg);
				log.info(logText);
			}

			/**
			 * Response for Alexa
			 */
			String speechText = "You have asked me to text "+ group +" to ask about home status.";
			String cardContent = "Are you home for Housemate";

			/**
			 *  Create the Simple card content.
			 */
			SimpleCard card = new SimpleCard();
			card.setTitle("Text Folks SKill - Home Status.");
			card.setContent(cardContent);

			/**
			 *  Create the plain text output.
			 */
			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText(speechText);

			return SpeechletResponse.newTellResponse(speech, card);
		}
		catch( TwilioRestException tre)
		{
			log.error("TwilioRestException - unable to send message");
			SimpleCard card = new SimpleCard();
			card.setTitle("Text Folks SKill");
			card.setContent("Failed to deliver message");

			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText("Failed to deliver message");

			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

	/**
	 * Method to send text to housemate asking whether they would like to order Chipotle
	 * @param group
	 * @return
	 */
	private SpeechletResponse getOrderResponse(String group) {

		try
		{
			String from = TWILLIO_REGISTERED_NUMBER;
			String msg = ORDER_FOOD_MESSAGE;
			TwillioClient testText = new TwillioClient();

			ArrayList<String> recipients = new ArrayList<String>();

			if(group.equalsIgnoreCase("Asia"))
			{
				recipients.add(JAPAN);
				recipients.add(VIETNAM);
				recipients.add(KENYA);
				recipients.add(CHINA);
				recipients.add(NEWYORK);

			}
			else if(group.equalsIgnoreCase("Vietnam"))
			{
				recipients.add(VIETNAM);
			}
			else if(group.equalsIgnoreCase("Japan"))
			{
				recipients.add(JAPAN);
			}
			else if(group.equalsIgnoreCase("China"))
			{
				recipients.add(CHINA);
			}
			else if(group.equalsIgnoreCase("Kenya"))
			{
				recipients.add(KENYA);
			}
			else if(group.equalsIgnoreCase("New York"))
			{
				recipients.add(NEWYORK);
			}

			for(String recipient : recipients)
			{
				String logText = testText.sendMessage(recipient, from, msg);
				log.info(logText);
			}

			/**
			 * Response for Alexa
			 */
			String speechText = "You have asked me to text "+ group +" about ordering Chipotle.";
			String cardContent = "Ordering with Housemate";

			/**
			 *  Create the Simple card content.
			 */
			SimpleCard card = new SimpleCard();
			card.setTitle("Text Folks SKill - Ordering.");
			card.setContent(cardContent);

			/**
			 *  Create the plain text output.
			 */
			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText(speechText);

			return SpeechletResponse.newTellResponse(speech, card);
		}
		catch( TwilioRestException tre)
		{
			log.error("TwilioRestException - unable to send message");
			SimpleCard card = new SimpleCard();
			card.setTitle("Text Folks SKill");
			card.setContent("Failed to deliver message");

			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText("Failed to deliver message");

			return SpeechletResponse.newTellResponse(speech, card);
		}

	}

	/**
	 * Method to send text to housemate for testing purposes
	 * @param group
	 * @return
	 */
	private SpeechletResponse getTestResponse(String group) {
		try
		{
			String from = TWILLIO_REGISTERED_NUMBER;
			String msg = TEST_MESSAGE;
			TwillioClient testText = new TwillioClient();

			ArrayList<String> recipients = new ArrayList<String>();

			if(group.equalsIgnoreCase("Asia"))
			{
				recipients.add(JAPAN);
				recipients.add(VIETNAM);
				recipients.add(KENYA);
				recipients.add(CHINA);
				recipients.add(NEWYORK);

			}
			else if(group.equalsIgnoreCase("Vietnam"))
			{
				recipients.add(VIETNAM);
			}
			else if(group.equalsIgnoreCase("Japan"))
			{
				recipients.add(JAPAN);
			}
			else if(group.equalsIgnoreCase("China"))
			{
				recipients.add(CHINA);
			}
			else if(group.equalsIgnoreCase("Kenya"))
			{
				recipients.add(KENYA);
			}
			else if(group.equalsIgnoreCase("New York"))
			{
				recipients.add(NEWYORK);
			}

			for(String recipient : recipients)
			{
				String logText = testText.sendMessage(recipient, from, msg);
				log.info(logText);
			}

			/**
			 * Response for Alexa
			 */
			String speechText = "You have asked me to text "+ group +" as a test.";
			String cardContent = "Testing with Housemate";

			/**
			 *  Create the Simple card content.
			 */
			SimpleCard card = new SimpleCard();
			card.setTitle("Text Folks SKill - Test.");
			card.setContent(cardContent);

			/**
			 *  Create the plain text output.
			 */
			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText(speechText);

			return SpeechletResponse.newTellResponse(speech, card);
		}
		catch( TwilioRestException tre)
		{
			log.error("TwilioRestException - unable to send message");
			SimpleCard card = new SimpleCard();
			card.setTitle("textHack");
			card.setContent("Failed to deliver message");

			PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
			speech.setText("Failed to deliver message");

			return SpeechletResponse.newTellResponse(speech, card);
		}
	}


	private SpeechletResponse getClarificationResponse() {
		String speechText = "I am confused. Please try again.";

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt);
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Hey there, what do you want to text?";
		String cardContent = "Text Folks Skill here. What do you want to text?";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Text Folks Skill Welcome");
		card.setContent(cardContent);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "You can use this app to text your housemates for random things";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Text Folks Help.");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}
}
