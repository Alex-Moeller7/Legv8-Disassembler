import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No File Noe Hoes.");
        } else {
            //while loop for constant reading of bytes here?
            List<Instruction> instructionList = FileParser.parseInstructions(args[0]);
            //System.out.println(instructionList);
            extractAndPrintMnemonics(instructionList);
        }
    }

    private static void extractAndPrintMnemonics(List<Instruction> instructionList) {
        for (Instruction instruction : instructionList) {
            String mnemonic = instruction.getMnemonic();
            //System.out.println("Mnemonic: " + mnemonic);
        }
    }
}
