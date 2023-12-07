import java.util.Arrays;
import java.util.List;

public class InstructionSet {

    public static final List<Instruction> opcodeList = Arrays.asList(
            new Instruction("ADD", "10001011000", "R"),
            new Instruction("ADDI", "1001000100", "I"),
            new Instruction("ADDIS", "1011000100", "I"),
            new Instruction("ADDS", "10101011000", "R"),
            new Instruction("AND", "10001010000", "R"),
            new Instruction("ANDI", "1001001000", "I"),
            new Instruction("ANDIS", "1111001000", "I"),
            new Instruction("ANDS", "1110101000", "R"),
            new Instruction("B", "000101", "B"),
            new Instruction("BL", "100101", "B"),
            new Instruction("BR", "11010110000", "R"),
            new Instruction("CBNZ", "10110101", "CB"),
            new Instruction("CBZ", "10110100", "CB"),
            new Instruction("DUMP", "11111111110", "R"),
            new Instruction("EOR", "11001010000", "R"),
            new Instruction("EORI", "1101001000", "I"),
            new Instruction("FADDD", "00011110011", "R"),
            new Instruction("FADDS", "00011110001", "R"),
            new Instruction("FCMPD", "00011110011", "R"),
            new Instruction("FCMPS", "00011110001", "R"),
            new Instruction("FDIVD", "00011110011", "R"),
            new Instruction("FDIVS", "00011110001", "R"),
            new Instruction("FMULD", "00011110011", "R"),
            new Instruction("FMULS", "00011110001", "R"),
            new Instruction("FSUBD", "00011110011", "R"),
            new Instruction("FSUBS", "00011110001", "R"),
            new Instruction("HALT", "11111111111", "R"),
            new Instruction("LDUR", "11111000010", "D"),
            new Instruction("LDURB", "00111000010", "D"),
            new Instruction("LDURD", "11111100010", "D"),
            new Instruction("LDURH", "01111000010", "D"),
            new Instruction("LDURS", "10111100010", "D"),
            new Instruction("LDURSW", "10111000100", "D"),
            new Instruction("LSL", "11010011011", "R"),
            new Instruction("LSR", "11010011010", "R"),
            new Instruction("MUL", "10011011000", "R"),
            new Instruction("ORR", "10101010000", "R"),
            new Instruction("ORRI", "1011001000", "I"),
            new Instruction("PRNL", "11111111100", "R"),
            new Instruction("PRNT", "11111111101", "R"),
            new Instruction("SDIV", "10011010110", "R"),
            new Instruction("SMULH", "10011011010", "R"),
            new Instruction("STUR", "11111000000", "D"),
            new Instruction("STURB", "00111000000", "D"),
            new Instruction("STURD", "11111100000", "D"),
            new Instruction("STURH", "01111000000", "D"),
            new Instruction("STURS", "10111100000", "D"),
            new Instruction("STURSW", "10111000000", "D"),
            new Instruction("SUB", "11001011000", "R"),
            new Instruction("SUBI", "1101000100", "I"),
            new Instruction("SUBIS", "1111000100", "I"),
            new Instruction("SUBS", "11101011000", "R"),
            new Instruction("UDIV", "10011010110", "R"),
            new Instruction("UMULH", "10011011110", "R")
    );

    public static Instruction findInstruction(List<Integer> opcodeBits) {

        for (Instruction entry : opcodeList) {
            if (matchesOpcode(opcodeBits, entry.getOpcodeBits())) {
                return entry;
            }
        }
        return null;
    }

    private static boolean matchesOpcode(List<Integer> opcodeBits, int[] entryBits) {
        if (opcodeBits.size() != entryBits.length) {
            return false;
        }

        for (int i = 0; i < opcodeBits.size(); i++) {
            if (opcodeBits.get(i) != entryBits[i]) {
                return false;
            }
        }

        return true;
    }
}
