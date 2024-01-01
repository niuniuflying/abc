package com.nac.abc.service.impl;

import com.nac.abc.service.ISendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

public class SendMailServiceImpl implements ISendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;//发送人

    @Override
    public void sendCode(String email) {

    }


}
