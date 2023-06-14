package test.custom;

import main.abstractions.*;
import main.implementations.*;
import main.implementations.custom.Attack;
import main.implementations.custom.CustomFinalPBox;
import main.implementations.custom.CustomInitialPBox;
import main.implementations.custom.CustomStraightPBox;
import main.implementations.des.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomTest {
    private Attack attack = new Attack();

    @Test
    public void testAttack() {
        attack.attack();
    }
}
