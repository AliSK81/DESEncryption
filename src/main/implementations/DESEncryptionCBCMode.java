package main.implementations;

import main.abstractions.DESEncryptionMode;

import java.util.List;

public class DESEncryptionCBCMode implements DESEncryptionMode {

    private static final int BLOCK_SIZE = 64;
    private final DESEncryptor desEncryptor;

    public DESEncryptionCBCMode(DESEncryptor desEncryptor) {
        this.desEncryptor = desEncryptor;
    }

    @Override
    public Bits encrypt(Bits plaintext, Bits key, Bits iv) {
        Bits paddedPlaintext = plaintext.pad(BLOCK_SIZE);

        List<Bits> plaintextBlocks = paddedPlaintext.split(BLOCK_SIZE);

        Bits ciphertext = Bits.empty();
        Bits previousBlock = iv;

        for (Bits block : plaintextBlocks) {
            block.xor(previousBlock);

            Bits encryptedBlock = desEncryptor.encrypt(block, key);

            ciphertext = ciphertext.concat(encryptedBlock);

            previousBlock = encryptedBlock;
        }

        return ciphertext;
    }

    @Override
    public Bits decrypt(Bits ciphertext, Bits key, Bits iv) {
        List<Bits> ciphertextBlocks = ciphertext.split(BLOCK_SIZE);

        Bits plaintext = Bits.empty();
        Bits previousBlock = iv;

        for (Bits block : ciphertextBlocks) {
            Bits decryptedBlock = desEncryptor.decrypt(block, key);

            decryptedBlock.xor(previousBlock);

            plaintext = plaintext.concat(decryptedBlock);

            previousBlock = block;
        }

        return plaintext.trim();
    }
}