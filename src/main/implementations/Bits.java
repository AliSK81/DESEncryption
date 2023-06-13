package main.implementations;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
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

    public static Bits empty() {
        return new Bits(0);
    }

    public static BitSet bytesToBitSet(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);
        return BitSet.valueOf(bigInt.toByteArray());
    }

    public static Bits fromTxt(String text) {
        String reversedText = new StringBuilder(text).reverse().toString();
        byte[] bytes = reversedText.getBytes(StandardCharsets.UTF_8);
        BitSet bitSet = bytesToBitSet(bytes);
        return new Bits(bitSet, text.length() * 8).convertToMSB();
    }

    public static Bits fromByteArray(byte[] bytes) {
        Bits bits = new Bits(bytes.length * 8);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            for (int j = 0; j < 8; j++) {
                if (((b >> (7 - j)) & 1) == 1) {
                    bits.set(i * 8 + j);
                }
            }
        }
        return bits;
    }

    public static Bits fromHex(String hex) {
        BigInteger bigInt = new BigInteger(hex, 16);
        byte[] bytes = bigInt.toByteArray();
        return fromByteArray(bytes);
    }

    public static Bits fromBin(String binary) {
        Bits bits = new Bits(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bits.set(i);
            }
        }
        return bits;
    }

    public String toTxt() {
        String reverseText = new String(toByteArray(), StandardCharsets.UTF_8);
        return new StringBuilder(reverseText).reverse().toString();
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

    public List<Bits> split(int blockSize) {
        List<Bits> blocks = new ArrayList<>();
        for (int i = 0; i < size(); i += blockSize) {
            int endIndex = Math.min(i + blockSize, size());
            blocks.add(get(i, endIndex));
        }
        return blocks;
    }

    public Bits pad(int blockSize) {
        int padLength = (blockSize - (size() % blockSize)) / 8;
        char padChar = (char) padLength;
        Bits padding = Bits.fromTxt(String.valueOf(padChar).repeat(padLength));
        return concat(padding);
    }

    public Bits trim() {
        int padLength = get(size() - 8, size()).toTxt().charAt(0);
        return get(0, size() - padLength * 8);
    }

    public void xor(Bits bits) {
        data.xor(bits.data);
    }

    public void or(Bits bits) {
        data.or(bits.data);
    }

    public String toBinString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    public String toHexString() {
        BigInteger bigInteger = new BigInteger(toBinString(), 2);
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
        return String.format("Bits{ Data: %s\t Binary: %s\t Hex: %s }",
                data.toString(), toBinString(), toHexString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bits bits = (Bits) o;
        return nbits == bits.nbits && Objects.equals(data, bits.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}