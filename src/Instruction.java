import java.util.List;

public class Instruction {

    private String mnemonic;
    private String opcode;
    private String format;

    public Instruction(String mnemonic, String opcode, String format) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.format = format;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getOpcode() {
        return opcode;
    }

    public String getFormat() {
        return format;
    }

    public int[] getOpcodeBits() {
        return convertStringToBits(opcode);
    }

    private int[] convertStringToBits(String opcode) {
        int[] bits = new int[opcode.length()];
        for (int i = 0; i < opcode.length(); i++) {
            bits[i] = Character.getNumericValue(opcode.charAt(i));
        }
        return bits;
    }

    @Override
    public String toString() {
        return "OpcodeEntry{" +
                "mnemonic='" + mnemonic + '\'' +
                ", opcode='" + opcode + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}


