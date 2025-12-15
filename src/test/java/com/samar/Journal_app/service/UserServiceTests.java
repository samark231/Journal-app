package com.samar.Journal_app.service;

import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAdd(){
        assertEquals(4, 2+2);

    }

    @Test
    public void testFindByUserName(){
        User user = userRepository.findByUsername("samar");
        assertFalse(user.getJournalEntries().isEmpty());
    }

    //parameterized test.
    @ParameterizedTest()
    @CsvSource({"1,3,4",
                "2,1,4",
                "3,6,9"
    })
    public void testParameters(int a , int b, int expected){
        assertEquals(expected, a+b, "failed for a= "+a +", b="+b+", expected = "+expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"samar", "amit", "admin"})
    public void testUserPassword(String username){
        User user = userRepository.findByUsername(username);
        assertFalse(user.getPassword().isEmpty());
    }

    //custom source
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testSaveNewUser(User user){
        assertTrue(userService.saveUser(user));

    }
}
