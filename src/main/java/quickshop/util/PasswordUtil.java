package quickshop.util;

import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Argon2;

public class PasswordUtil {
    Argon2Function argon2 = Argon2Function.getInstance(14, 20, 1, 32, Argon2.ID);

    public static String hashPass(String password) {
        Hash hash = Password.hash(password)
                .addRandomSalt(32)
                .withArgon2();

        return hash.getResult();
    }

    public static boolean verifyPass(String password, String hashedPassword) {
        return Password
                .check(password, hashedPassword)
                .withArgon2();
    }
}
