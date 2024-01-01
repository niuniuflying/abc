package com.nac.abc.service.impl;

import com.nac.abc.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;//爆红不影响

    @Value("${spring.mail.username}")
    private String from;//发送人

    @Override
    public void sendCode(String email,String code) {
        try{
            MimeMessage message=javaMailSender.createMimeMessage();
            //true 允许发送附件
            MimeMessageHelper helper=new MimeMessageHelper(message,true);

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("AICAMPUS平台");
            //true 解析HTML
            helper.setText("AICAMPUS注册验证码："+code+"(五分钟内有效)",true);
            //添加附件
//            File file=new File("C:\\B\\heimaspringboot2\\6、2021新版SpringBoot2全套\\4.原理篇\\原理篇-资料\\springboot_23_mail\\src\\main\\resources\\logo.png");
            //给附件命名
//            helper.addAttachment("a.png",file);
            //发送
            javaMailSender.send(message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
