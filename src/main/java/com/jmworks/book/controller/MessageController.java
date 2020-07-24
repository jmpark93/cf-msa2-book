package com.jmworks.book.controller;

import com.jmworks.book.payload.UserCommand;
import com.jmworks.book.repository.BookRepository;
import com.jmworks.book.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class MessageController {

    private final S3Service s3Service;
    private final BookRepository bookRepository;

    public MessageController(S3Service s3Service, BookRepository bookRepository) {
        this.s3Service = s3Service;
        this.bookRepository = bookRepository;
    }

    @StreamListener(target = Sink.INPUT)
    public void processMessage(UserCommand usrCmd)
    {
        if( "DEL".equalsIgnoreCase( usrCmd.getOper() )) {
            System.out.println( "User(" + usrCmd.getId() + ") will be deleted ... ");

            bookRepository.deleteAllByUserID(usrCmd.getId());
            s3Service.deleteFiles( usrCmd.getId() + "-book");
        }
    }
}
