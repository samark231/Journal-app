package com.samar.Journal_app.serviceTest;

import com.samar.Journal_app.scheduler.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


public class UserSchedulerTests {
    @Autowired
    private UserScheduler userScheduler;


    public void testFetchUserAndSendMail(){
        userScheduler.fetchUsersAndSendMail();
    }

}
