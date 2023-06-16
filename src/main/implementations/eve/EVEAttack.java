package main.implementations.eve;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.*;
import main.tables.DESTables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EVEAttack {
    private static final Map<String, String> plaintextToCiphertext = new HashMap<>();
    private final static int BLOCK_SIZE = 64;
    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;
    private final Encryptor encryptor;
    PBox initialPBox = new EVEInitialPBox();
    PBox finalReversedPBox = new EVEFinalReversePBox();

    public EVEAttack() {
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

        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new EVEIdentityStraightPBox(), sBoxes);
        PBox initialPBox = new EVEInitialPBox();
        PBox finalPBox = new EVEIdentityFinalPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        encryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);
    }

    public void attack() {
        var key = Bits.fromHex("4355262724562343");

        Map<String, String> outputByInput = new HashMap<>();

        for (Map.Entry<String, String> entry : plaintextToCiphertext.entrySet()) {
            Bits plainText = Bits.fromTxt(entry.getKey());
            Bits cipherText = Bits.fromHex(entry.getValue());

            Bits paddedPlaintext = plainText.pad(BLOCK_SIZE);

            List<Bits> plainBlocks = paddedPlaintext.split(BLOCK_SIZE);
            List<Bits> cipherBlocks = cipherText.split(BLOCK_SIZE);

            for (int i = 0; i < plainBlocks.size(); i++) {

                Bits plainBlock = plainBlocks.get(i);
                Bits exceptedCipherBlock = cipherBlocks.get(i);

                Bits actualCipherBlock = encryptor.encrypt(plainBlock, key);

                Bits leftPermuted = initialPBox.permute(plainBlock).getFirstHalf();

                // find straightPBox input
                Bits straightPBoxInput = leftPermuted.copy();
                straightPBoxInput.xor(actualCipherBlock.getFirstHalf());

                // find straightPBox output
                Bits straightPBoxOutput = finalReversedPBox.permute(exceptedCipherBlock).getFirstHalf();
                straightPBoxOutput.xor(leftPermuted);

                System.out.println(plainBlock.toTxt());
                System.out.println(actualCipherBlock.toHexString());
                System.out.println(straightPBoxInput.toHexString());
                System.out.println(straightPBoxOutput.toHexString());
                System.out.println();

                outputByInput.put(straightPBoxInput.toBinString(), straightPBoxOutput.toBinString());
            }
        }

        System.out.println(PBoxFinder.findPBox(outputByInput));
    }
}
