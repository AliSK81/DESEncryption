package main.abstractions;

import main.implementations.Bits;

public interface EncryptionMode {
    Bits encrypt(Bits plaintext, Bits key, Bits iv);

    Bits decrypt(Bits ciphertext, Bits key, Bits iv);
}
