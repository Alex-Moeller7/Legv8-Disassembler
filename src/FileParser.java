import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public static List<Instruction> parseInstructions(String fileLocation) {
        List<Instruction> instructionList = new ArrayList<>();
        List<Integer> bitList = new ArrayList<>();

        try (InputStream is = new FileInputStream(fileLocation)) {
            int currentByte;
            while ((currentByte = is.read()) != -1) {
                for (int i = 7; i >= 0; i--) {
                    int bit = (currentByte >> i) & 1;
                    bitList.add(bit);

                    // min is 6 bits
                    if (bitList.size() >= 6) {
                        Instruction matchedInstruction = findMatchingInstruction(bitList);
                        if (matchedInstruction != null) {
                            instructionList.add(matchedInstruction);

                            // clear for next opcode
                            bitList.clear();
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("No file, No Hoes: " + fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instructionList;
    }

    private static Instruction findMatchingInstruction(List<Integer> bitList) {
        return InstructionSet.findInstruction(bitList);
    }
}
