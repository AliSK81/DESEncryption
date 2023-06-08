package main.abstractions;

import main.implementations.Bits;

public interface Encryptor {
    Bits encrypt(Bits plaintext, Bits key);

    Bits decrypt(Bits ciphertext, Bits key);
}