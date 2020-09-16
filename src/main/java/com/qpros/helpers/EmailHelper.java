package com.qpros.helpers;

import com.qpros.common.Base;
import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.search.SubjectTerm;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class EmailHelper extends Base {



    /**
     * Utility for interacting with an Email application
     */
        //public static EmailHelper emailHelper;
        private final Folder folder;

        public enum EmailFolder {
            INBOX("INBOX"),
            SPAM("SPAM");

            private final String text;

             EmailFolder(String text){
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }

        /**
         * Connects to email server with credentials provided to read from a given folder of the email application
         */
        public EmailHelper() throws MessagingException, GeneralSecurityException {

            EmailFolder emailFolder = EmailFolder.INBOX;
            Properties props = System.getProperties();
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.setProperty("mail.imaps.host", "imap.gmail.com");
            props.setProperty("mail.imaps.user", "qprosautomation@gmail.com");
            props.setProperty("mail.imaps.password", "QPros@123");
            props.setProperty("mail.imaps.port", "993");
            props.setProperty("mail.imaps.auth", "true");
            //props.setProperty("mail.debug", "true");
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com","qprosautomation@gmail.com", "QPros@123");
            folder = store.getFolder(emailFolder.getText());
            folder.open(Folder.READ_WRITE);
        }


        public void openEmail(Message message) throws Exception{
            message.getContent();
        }

        public int getNumberOfMessages() throws MessagingException {
            return folder.getMessageCount();
        }

        public int getNumberOfUnreadMessages()throws MessagingException {
            return folder.getUnreadMessageCount();
        }

        /**
         * Gets a message by its position in the folder. The earliest message is indexed at 1.
         */
        public Message getMessageByIndex(int index) throws MessagingException {
            return folder.getMessage(index);
        }

        public Message getLatestMessage() throws MessagingException{
            return getMessageByIndex(getNumberOfMessages());
        }

        /**
         * Gets all messages within the folder
         */
        public Message[] getAllMessages() throws MessagingException {
            return folder.getMessages();
        }

        /**
         * @param maxToGet maximum number of messages to get, starting from the latest. For example, enter 100 to get the last 100 messages received.
         */
        public Message[] getMessages(int maxToGet) throws MessagingException {
            Map<String, Integer> indices = getStartAndEndIndices(maxToGet);
            return folder.getMessages(indices.get("startIndex"), indices.get("endIndex"));
        }

        /**
         * Searches for messages with a specific subject
         * @param subject Subject to search messages for
         * @param unreadOnly Indicate whether to only return matched messages that are unread
         * @param maxToSearch maximum number of messages to search, starting from the latest. For example, enter 100 to search through the last 100 messages.
         */
        public Message[] getMessagesBySubject(String subject, boolean unreadOnly, int maxToSearch) throws Exception{
            Map<String, Integer> indices = getStartAndEndIndices(maxToSearch);

            @SuppressWarnings("CStyleArrayDeclaration") Message[] messages = folder.search(
                    new SubjectTerm(subject),
                    folder.getMessages(indices.get("startIndex"), indices.get("endIndex")));

            if(unreadOnly){
                List<Message> unreadMessages = new ArrayList<>();
                for (Message message : messages) {
                    if(isMessageUnread(message)) {
                        unreadMessages.add(message);
                    }
                }
                messages = unreadMessages.toArray(new Message[]{});
            }
            List listOfProducts = Arrays.asList(messages);
            Collections.reverse(listOfProducts);
            return (Message[])listOfProducts.toArray(messages);
        }

        /**
         * Returns HTML of the email's content
         */
        public String getMessageContent(Message message) throws Exception {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }

        /**
         * Returns all urls from an email message with the linkText specified
         */
        public List<String> getUrlsFromMessage(Message message, String linkText) throws Exception{
            String html = getMessageContent(message);
            List<String> allMatches = new ArrayList<>();
            Matcher matcher = Pattern.compile("(<a [^>]+>)"+linkText+"</a>").matcher(html);
            while (matcher.find()) {
                String aTag = matcher.group(1);
                allMatches.add(aTag.substring(aTag.indexOf("http"), aTag.indexOf("\">")));
            }
            return allMatches;
        }

        private Map<String, Integer> getStartAndEndIndices(int max) throws MessagingException {
            int endIndex = getNumberOfMessages();
            int startIndex = endIndex - max;

            //In event that maxToGet is greater than number of messages that exist
            if(startIndex < 1){
                startIndex = 1;
            }

            Map<String, Integer> indices = new HashMap<>();
            indices.put("startIndex", startIndex);
            indices.put("endIndex", endIndex);

            return indices;
        }

        /**
         * Gets text from the end of a line.
         * In this example, the subject of the email is 'Authorization Code'
         * And the line to get the text from begins with 'Authorization code:'
         * Change these items to whatever you need for your email. This is only an example.
         */

        public String getVerificationCode(String emailName, String search, int lettersToRead) throws Exception {
            Message email = getMessagesBySubject(emailName, false, 5)[0];
            BufferedReader reader = new BufferedReader(new InputStreamReader(email.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(search)) {
                    System.out.println(line);
                    System.out.println();
                    return line.substring(line.indexOf(search) + search.length() , line.indexOf(search) + search.length() + lettersToRead);
                }
            }
            return null;
        }



        //************* BOOLEAN METHODS *******************

        /**
         * Searches an email message for a specific string
         */
        public boolean isTextInMessage(Message message, String text) throws Exception {
            String content = getMessageContent(message);

            //Some Strings within the email have whitespace and some have break coding. Need to be the same.
            content = content.replace("&nbsp;", " ");
            return content.contains(text);
        }

        public boolean isMessageInFolder(String subject, boolean unreadOnly) throws Exception {
            int messagesFound = getMessagesBySubject(subject, unreadOnly, getNumberOfMessages()).length;
            return messagesFound > 0;
        }

        public boolean isMessageUnread(Message message) throws Exception {
            return !message.isSet(Flags.Flag.SEEN);
        }
    public void sendMail(String recipient, String attachmentPath) {

        // Recipient's email ID needs to be mentioned.

        // Sender's email ID needs to be mentioned
        String from = "QProsAutomation2@q-pros.net";

        // Assuming you are sending email from through gmails smtp
        String host = "qpros-net0c.mail.protection.outlook.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        //properties.put("mail.smtp.port", "993");
        properties.put("mail.smtp.ssl.enable", "true");
        //properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("qprosautomation@gmail.com", "QPros@123");

            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress( recipient ));

            // Set Subject: header field
            message.setSubject("Automation Report");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

                File f =new File(attachmentPath);

                attachmentPart.attachFile(f);
                textPart.setText("This is QPros automated email. IT WORKS!");
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
    public static void main(String[] args) throws MessagingException, GeneralSecurityException {
        EmailHelper testHelper = new EmailHelper();
        try {
            System.out.println(testHelper.getVerificationCode("ADEK Online - Registration invitation", "HEIAuthorizationSystem/Client/registration?token=", 38));

        } catch (Exception e){
            e.printStackTrace();

        }

    }

}


