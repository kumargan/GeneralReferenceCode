package com.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.*")
public class Application implements RequestHandler<SQSEvent,String>{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

   @Override
    public String handleRequest(SQSEvent input, Context context) {
        String[] args = new String[1];
        args[0] = "abc";
        main(args);
        String output = " finished ";
        context.getLogger().log(output);
        return output;
    }
}
