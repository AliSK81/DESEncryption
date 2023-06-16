package test.eve;

import main.implementations.eve.EVEAttack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EVEAttackTest {
    private final EVEAttack EVEAttack = new EVEAttack();

    @Test
    public void testAttack() {
        EVEAttack.attack();
    }
}
