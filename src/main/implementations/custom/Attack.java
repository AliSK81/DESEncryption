package main.implementations.custom;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Attack {
    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;

    private static final Map<String, String> plaintextToCiphertext = new HashMap<>();

    private final EncryptionMode encryptionMode;

    public Attack() {
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

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new CustomStraightPBox(), sBoxes);
        PBox initialPBox = new CustomInitialPBox();
        PBox finalPBox = new CustomFinalIdentityPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        DESEncryptor desEncryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);

        encryptor = desEncryptor;
        encryptionMode = new ECBEncryptionMode(desEncryptor);
    }

    private final Encryptor encryptor;

    private final static int BLOCK_SIZE = 64;

    public void attack() {
        var key = Bits.fromHex("4355262724562343");
        PBox initialPBox = new CustomInitialPBox();

        Map<String, String> io = new HashMap<>();


        for (Map.Entry<String, String> entry : plaintextToCiphertext.entrySet()) {
            Bits plainText = Bits.fromTxt(entry.getKey());
            Bits cipherText = Bits.fromHex(entry.getValue());

            Bits paddedPlaintext = plainText.pad(BLOCK_SIZE);

            List<Bits> plainBlocks = paddedPlaintext.split(BLOCK_SIZE);
            List<Bits> cipherBlocks = cipherText.split(BLOCK_SIZE);

//            assertEquals(plainBlocks.size(), cipherBlocks.size());

            System.out.println(paddedPlaintext.toTxt());
            System.out.println(paddedPlaintext.size());

            System.out.println(cipherText.toHexString());
            System.out.println(cipherText.size());

            System.out.println("plain blocks");
            for (int i = 0; i < plainBlocks.size(); i++) {
                System.out.println(plainBlocks.get(i).toHexString());
            }

            System.out.println("cipher blocks");
            for (int i = 0; i < cipherBlocks.size(); i++) {
                System.out.println(cipherBlocks.get(i).toHexString());
            }

            System.out.println();
            System.out.println();

//            for (int i = 0; i < plainBlocks.size(); i++) {
//
//                Bits plainBlock = plainBlocks.get(i);
//                Bits exceptedCipherBlock = cipherBlocks.get(i);
//
//                Bits actualCipherBlock = encryptor.encrypt(plainBlock, key);
//
//                Bits leftPermuted = initialPBox.permute(plainBlock).getFirstHalf();
//
//                leftPermuted.xor(actualCipherBlock.getFirstHalf());
//
//                Bits straightPBoxInput = leftPermuted;
//
//                // ----
//                Bits cipherReversedLeft = initialPBox.permute(exceptedCipherBlock).getFirstHalf();
//                cipherReversedLeft.xor(leftPermuted);
//                Bits straightPBoxOutput = cipherReversedLeft;
//
//                System.out.println(straightPBoxInput.toBinString());
//                System.out.println(straightPBoxOutput.toBinString());
//                System.out.println(straightPBoxInput.countOnes());
//                System.out.println(straightPBoxOutput.countOnes());
//                System.out.println();
//
////                io.put(straightPBoxInput.toBinString(), straightPBoxOutput.toBinString());
//            }

        }

//        System.out.println(Arrays.toString(findPbox(io)));
    }

    public int[] findPbox(Map<String, String> outputByInput) {
        // Step 1: Determine the size of the input and output of the P-box.
        int inputSize = outputByInput.keySet().iterator().next().length();
        int outputSize = outputByInput.values().iterator().next().length();

        // Step 2: Create an empty P-box array.
        int[] pbox = new int[outputSize];

        // Step 3: Fill in the P-box array.
        for (int i = 0; i < outputSize; i++) {
            StringBuilder inputBuilder = new StringBuilder();
            for (String input : outputByInput.keySet()) {
                inputBuilder.append(input.charAt(i));
            }
            String inputColumn = inputBuilder.toString();
            String outputColumn = outputByInput.get(inputColumn);
            for (int j = 0; j < inputSize; j++) {
                if (outputColumn.charAt(j) == '1') {
                    pbox[i] = j;
                    break;
                }
            }
        }

        // Step 4: Return the P-box array.
        return pbox;
    }
}
