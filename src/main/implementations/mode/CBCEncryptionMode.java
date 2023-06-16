package main.implementations.mode;

import main.abstractions.EncryptionMode;
import main.abstractions.Encryptor;
import main.implementations.Bits;

import java.util.List;

public class CBCEncryptionMode implements EncryptionMode {

    private static final int BLOCK_SIZE = 64;
    private final Encryptor encryptor;

    public CBCEncryptionMode(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public Bits encrypt(Bits plaintext, Bits key, Bits iv) {
        Bits paddedPlaintext = plaintext.pad(BLOCK_SIZE);

        List<Bits> plaintextBlocks = paddedPlaintext.split(BLOCK_SIZE);

        Bits ciphertext = Bits.empty();
        Bits previousBlock = iv;

        for (Bits block : plaintextBlocks) {
            block.xor(previousBlock);

            Bits encryptedBlock = encryptor.encrypt(block, key);

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
            Bits decryptedBlock = encryptor.decrypt(block, key);

            decryptedBlock.xor(previousBlock);

            plaintext = plaintext.concat(decryptedBlock);

            previousBlock = block;
        }

        return plaintext.trim();
    }
}