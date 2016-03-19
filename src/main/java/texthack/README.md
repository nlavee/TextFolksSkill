#My TextHack for Alexa
This is my attempt to build something for my Amazon Echo Speaker. I plan to set up a way for my to easily text my housemates without physically touching my phone.

## Concepts
This project shows how to create a Lambda function for handling Alexa Skill requests that:

- Correctly text to a specific groups of people
- Correctly text regarding a specific task

**(The below is adapted from the Amazon Alexa Skill Kit's README.md file.)**

## Get Necessary API Setup
1. You would need to set up Twillio API. When you have received the API Keys (Auth Token & Account SID), go to KeyVariables.java and change it to what you have
2. [FOR FREE VERSION] Before you could text anyone, you would need to have them as registered phone number. Please go to [Twillio Account Phone Numbers Verified](https://www.twilio.com/user/account/phone-numbers/verified) and follow the instructions

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
1. Go to the AWS Console and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda.
2. Click on the Create a Lambda Function or Get Started Now button.
3. Skip the blueprint
4. Name the Lambda Function "text-hack".
5. Select the runtime as Java 8
6. Go to the the root directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" in the target directory.
7. Select Code entry type as "Upload a .ZIP file" and then upload the "text-folk-skill-kit-0.1-jar-with-dependencies.jar" file from the build directory to Lambda
8. Set the Handler as texthack.TextHackSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip).
9. Create a basic execution role and click create.
10. Leave the Advanced settings as the defaults.
11. Click "Next" and review the settings then click "Create Function"
12. Click the "Event Sources" tab and select "Add event source"
13. Set the Event Source type as Alexa Skills kit and Enable it now. Click Submit.
14. Copy the ARN from the top right to be used later in the Alexa Skill Setup.

### Alexa Skill Setup
1. Go to the [Alexa Console](https://developer.amazon.com/edw/home.html) and click Add a New Skill.
2. Set "Text Hack" as the skill name and "text" as the invocation name, this is what is used to activate your skill.
3. Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
4. Copy the custom slot types from the customSlotTypes folder. Each file in the folder represents a new custom slot type. The name of the file is the name of the custom slot type, and the values in the file are the values for the custom slot.
5. Copy the Intent Schema from the included IntentSchema.json.
6. Copy the Sample Utterances from the included SampleUtterances.txt. Click Next.
7. Go back to the skill Information tab and copy the appId. Paste the appId into the KeyVariables.java file for the variable supportedApplicationIds,
   then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
8. You are now able to start testing your sample skill! You should be able to go to the [Echo webpage](http://echo.amazon.com/#skills) and see your skill enabled.
9. In order to test it, try to say some of the Sample Utterances from the Examples section below.
10. Your skill is now saved and once you are finished testing you can continue to publish your skill.

## Examples
### Dialog model:
    User: "Alexa, open text hack."
    Alexa: "Hey there, what do you want to text?"
    User: "Ask Asia about Mexican Food"
    Alexa: "You have asked me to text Asia about ordering Chipotle."
