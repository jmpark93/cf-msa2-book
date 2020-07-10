package com.jmworks.book.controller;

import com.jmworks.book.payload.UserCommand;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class MessageController {

    @StreamListener(target = Sink.INPUT)
    public void processMessage(UserCommand usrCmd)
    {
        if( "DEL".equalsIgnoreCase( usrCmd.getOper() )) {
            System.out.println( "User(" + usrCmd.getId() + ") will be deleted ... ");
        }
    }
}
