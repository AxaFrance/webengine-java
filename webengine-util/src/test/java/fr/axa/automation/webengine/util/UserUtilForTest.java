package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.jaxb.withoutpackageinfo.UserWithoutPackageInfo;
import fr.axa.automation.webengine.jaxb.withpackageinfo.UserWithPackageInfo;

public class UserUtilForTest {

    public static UserWithPackageInfo getNewUserWithPackageInfo() {
        UserWithPackageInfo user = new UserWithPackageInfo();
        user.setId(1);
        user.setFirstName("Ramesh");
        user.setLastName("Fadatare");
        user.setAge(25);
        user.setGender("Male");
        return user;
    }

    public static UserWithoutPackageInfo getNewUserWithoutPackageInfo() {
        UserWithoutPackageInfo user = new UserWithoutPackageInfo();
        user.setId(1);
        user.setFirstName("Ramesh");
        user.setLastName("Fadatare");
        user.setAge(25);
        user.setGender("Male");
        return user;
    }
}
