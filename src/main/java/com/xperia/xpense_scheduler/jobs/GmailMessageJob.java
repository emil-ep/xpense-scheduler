package com.xperia.xpense_scheduler.jobs;

import com.xperia.xpense_scheduler.client.InternalClient;
import com.xperia.xpense_scheduler.jobs.scheduler.ScheduledJob;
import com.xperia.xpense_scheduler.kafka.XpenseProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xperia.client.GoogleClient;
import org.xperia.models.SharedMailDetails;
import org.xperia.models.SharedUserSetting;
import org.xperia.models.UserOauthToken;
import org.xperia.models.XpenseKafkaTopics;
import org.xperia.models.google.GoogleHistoryMessagesAddedModel;
import org.xperia.models.google.GoogleHistoryResponse;

import java.util.List;

@Component("GmailMessageJob")
public class GmailMessageJob implements ScheduledJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmailMessageJob.class);
    private final XpenseProducer<String, String> kafkaProducer;
    private final InternalClient internalClient;
    private final GoogleClient googleClient;

    @Autowired
    public GmailMessageJob(@Qualifier("mailMessageIdProducer") XpenseProducer<String, String> kafkaProducer,
                           GoogleClient googleClient,
                           InternalClient internalClient){
        this.internalClient = internalClient;
        this.kafkaProducer = kafkaProducer;
        this.googleClient = googleClient;
    }

    @Override
    public String getName() {
        return "GmailMessageJob";
    }

    @Override
    public void execute() {

        List<UserOauthToken> tokens = this.internalClient.findAllAuthTokens();
        for (UserOauthToken token : tokens){
            SharedUserSetting setting = this.internalClient.findUserSettingByType(token.getUserEmail(), "mailLabelId");
            SharedMailDetails mailDetails = this.internalClient.findMailDetailsOfUser(token.getUserEmail());
            Long currentTimestamp = System.currentTimeMillis();
            try{
                if (token.getExpireTimestamp() <= currentTimestamp){
                    token = this.internalClient.refreshOauth2Token(token.getUserEmail());
                }
                GoogleHistoryResponse historyResponse = googleClient
                        .fetchHistory(
                                token.getAccessToken(),
                                mailDetails.getHistoryId(),
                                String.valueOf(setting.getPayload().get("mailLabelId").asText()));
                for (int i = 0; i < historyResponse.history().size(); i++){
                    List<GoogleHistoryMessagesAddedModel> items = historyResponse.history().get(i).messagesAdded();
                    GoogleHistoryMessagesAddedModel messagesAdded = items.get(i);
                    String messageId = messagesAdded.message().id();
                    this.kafkaProducer.send(XpenseKafkaTopics.MAIL_MESSAGE_ID.getName(), messageId, messageId);
                }
            } catch (Exception ex){
                LOGGER.error("Error fetching messages for the user : {}", token.getUserEmail(), ex);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
