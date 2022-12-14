package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.jaxb.User;

public class UserUtilForTest {

    public static User getNewUserTest() {
        User user = new User();
        user.setId(1);
        user.setFirstName("FirstName");
        return user;
    }
}
