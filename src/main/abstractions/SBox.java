package main.abstractions;

import main.implementations.Bits;


public interface SBox {
    Bits substitute(Bits data);
}