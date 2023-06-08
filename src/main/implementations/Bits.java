package main.implementations;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Objects;

public class Bits implements Serializable {
    private final BitSet data;
    private final int nbits;

    public Bits(int nbits) {
        this.data = new BitSet(nbits);
        this.nbits = nbits;
    }

    private Bits(BitSet bitSet, int nbits) {
        this.data = bitSet;
        this.nbits = nbits;
    }

    public Bits(String bits) {
        this(bits.length());
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                data.set(i);
            }
        }
    }

    public static BitSet bytesToBitSet(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);
        return BitSet.valueOf(bigInt.toByteArray());
    }

    public static Bits fromText(String text) {
        BitSet bitSet = bytesToBitSet(text.getBytes());
        return new Bits(bitSet, bitSet.size()).convertToMSB();
    }

    public String toText() {
        return new String(toByteArray(), StandardCharsets.US_ASCII);
    }

    public int size() {
        return nbits;
    }

    public boolean get(int bitIndex) {
        return data.get(bitIndex);
    }

    public Bits get(int fromIndex, int toIndex) {
        BitSet bitSet = data.get(fromIndex, toIndex);
        return new Bits(bitSet, toIndex - fromIndex);
    }

    public Bits getFirstHalf() {
        return get(0, size() / 2);
    }

    public Bits getSecondHalf() {
        return get(size() / 2, size());
    }

    public void set(int bitIndex) {
        data.set(bitIndex);
    }

    public void set(int fromIndex, int toIndex) {
        data.set(fromIndex, toIndex);
    }

    public void set(int fromIndex, boolean value) {
        data.set(fromIndex, value);
    }

    public void set(int fromIndex, int toIndex, Bits bits) {
        for (int i = 0, j = fromIndex; j < toIndex; i++, j++) {
            data.set(j, bits.get(i));
        }
    }

    public Bits concat(Bits other) {
        Bits concatenated = new Bits(size() + other.size());
        concatenated.set(0, size(), this);
        concatenated.set(size(), concatenated.size(), other);
        return concatenated;
    }

    public void xor(Bits bits) {
        data.xor(bits.data);
    }

    public void or(Bits bits) {
        data.or(bits.data);
    }

    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    public String toHexString() {
        BigInteger bigInteger = new BigInteger(toBinaryString(), 2);
        return bigInteger.toString(16).toUpperCase();
    }

    public void swap(int fromIndex1, int toIndex1, int fromIndex2, int toIndex2) {
        if (toIndex1 - fromIndex1 != toIndex2 - fromIndex2) {
            throw new IllegalArgumentException("Ranges must have the same length");
        }
        Bits temp = get(fromIndex1, toIndex1);
        set(fromIndex1, toIndex1, get(fromIndex2, toIndex2));
        set(fromIndex2, toIndex2, temp);
    }

    public void swapHalves() {
        swap(0, size() / 2, size() / 2, size());
    }

    public void circularShiftLeft(int shiftsCount) {
        if (shiftsCount < 0) {
            throw new IllegalArgumentException("Shifts count must be non-negative.");
        }
        shiftsCount %= size();
        if (shiftsCount == 0) {
            return;
        }
        BitSet temp = (BitSet) data.clone();
        for (int i = 0; i < size(); i++) {
            int newIndex = (i - shiftsCount + size()) % size();
            data.set(newIndex, temp.get(i));
        }
    }

    private Bits convertToMSB() {
        Bits msbBits = new Bits(nbits);
        for (int i = 0; i < nbits; i++) {
            msbBits.set(i, data.get(nbits - i - 1));
        }
        return msbBits;
    }

    public byte[] toByteArray() {
        return convertToMSB().data.toByteArray();
    }

    @Override
    public String toString() {
        return String.format("Bits{\ndata: %s\nbinary: %s\nhex: %s\n}", data.toString(), toBinaryString(), toHexString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bits bits = (Bits) o;
        return Objects.equals(data, bits.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}