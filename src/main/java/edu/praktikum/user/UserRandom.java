package edu.praktikum.user;

import edu.praktikum.models.UserCreate;

import static edu.praktikum.utils.RandomCores.randomString;
    public class UserRandom {
        private static final String EMAIL_DOMAIN = "@yandex.ru";
        public static UserCreate randomUserCreate() {
            return new UserCreate()
                    .withEmail(randomString(15) + EMAIL_DOMAIN)
                    .withPassword(randomString(16))
                    .withName(randomString(12));
        }
}
