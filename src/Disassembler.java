import java.util.List;

public class Disassembler {
    public static void disassembleInstructions(List<Instruction> instructionList) {
        for (Instruction instruction : instructionList) {
            String disassembled = disassembleInstruction(instruction);
            System.out.println(disassembled);
        }
    }

    private static String disassembleInstruction(Instruction instruction) {
        // based on instruction set
        //  decode  opcode, format, fields
        // return the disassembled instruction as a string

        String mnemonic = instruction.getMnemonic();
        String disassembled = "Disassembled: " + mnemonic;
        return disassembled;
    }
}
