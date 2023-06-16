package test.eve;

import main.implementations.Bits;
import main.implementations.eve.EVEAttacker;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EVEAttackerTest {
    private final EVEAttacker EVEAttacker = new EVEAttacker();
    private static final Map<String, String> plaintextToCiphertext = new HashMap<>();

    @Test
    public void testAttack() {

        plaintextToCiphertext.put("kootahe", "6E2F7B25307C3144");
        plaintextToCiphertext.put("Zendegi", "CF646E7170632D45");
        plaintextToCiphertext.put("Edame", "D070257820560746");
        plaintextToCiphertext.put("Dare", "5574223505051150");
        plaintextToCiphertext.put("JolotYe", "DB2E393F61586144");
        plaintextToCiphertext.put("Daame", "D175257820560746");
        plaintextToCiphertext.put("DaemKe", "D135603D1A705746");
        plaintextToCiphertext.put("Mioftan", "D83C6F7321752A54");
        plaintextToCiphertext.put("Toosh", "413A2B666D024747");
        plaintextToCiphertext.put("HattaMo", "5974216034186B44");
        plaintextToCiphertext.put("khayeSa", "EA29302D74463545");
        plaintextToCiphertext.put("05753jj", "B1203330722B7A04");
        plaintextToCiphertext.put("==j95697", "38693B6824232D231D1C0D0C4959590D");

        var key = Bits.fromHex("4355262724562343");

        var actualPBox = EVEAttacker.attack(plaintextToCiphertext, key);

        int[] exceptedPBox = new int[]{
                6, 26, 20, 28, 29, 12, 21, 17,
                31, 15, 23, 10, 8, 18, 2, 16,
                7, 5, 24, 14, 19, 27, 1, 9,
                32, 13, 30, 4, 11, 22, 25, 3
        };

        assertArrayEquals(exceptedPBox, actualPBox);
    }
}
