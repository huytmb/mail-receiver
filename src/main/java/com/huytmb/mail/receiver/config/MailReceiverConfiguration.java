package com.huytmb.mail.receiver.config;

import com.huytmb.mail.receiver.service.ReceiveMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.integration.mail.MailReceivingMessageSource;
import org.springframework.messaging.Message;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Configuration
@EnableIntegration
public class MailReceiverConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MailReceiverConfiguration.class);

    private final ReceiveMailService receiveMailService;

    public MailReceiverConfiguration(ReceiveMailService receiveMailService) {
        this.receiveMailService = receiveMailService;
    }

    @ServiceActivator(inputChannel = "receiveEmailChannel")
    public void receive(Message<?> message) {
        receiveMailService.handleReceivedMail((MimeMessage) message.getPayload());
    }

    @Bean("receiveEmailChannel")
    public DirectChannel defaultChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setDatatypes(javax.mail.internet.MimeMessage.class);
        return directChannel;
    }

    @Bean()
    @InboundChannelAdapter(
            channel = "receiveEmailChannel",
            poller = @Poller(fixedDelay = "5000", taskExecutor = "asyncTaskExecutor")
    )
    public MailReceivingMessageSource mailMessageSource(MailReceiver mailReceiver) {
        MailReceivingMessageSource mailReceivingMessageSource = new MailReceivingMessageSource(mailReceiver);
        return mailReceivingMessageSource;
    }

    @Bean
    public MailReceiver imapMailReceiver(@Value("imaps://${mail.imap.username}:${mail.imap.password}@${mail.imap.host}:${mail.imap.port}/inbox") String storeUrl) {
        log.info("IMAP connection url: {}", storeUrl);

        ImapMailReceiver imapMailReceiver = new ImapMailReceiver(storeUrl);
        imapMailReceiver.setShouldMarkMessagesAsRead(true);
        imapMailReceiver.setShouldDeleteMessages(false);
        imapMailReceiver.setMaxFetchSize(10);
        // imapMailReceiver.setAutoCloseFolder(true);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.imap.socketFactory.fallback", false);
        javaMailProperties.put("mail.store.protocol", "imaps");
        javaMailProperties.put("mail.debug", true);

        imapMailReceiver.setJavaMailProperties(javaMailProperties);

        return imapMailReceiver;
    }

}
