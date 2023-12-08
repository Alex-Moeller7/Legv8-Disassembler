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
                //for (int i = 7; i >= 0; i--) {
                for (int i = 0; i < 32; i++) {
                    int bit = (currentByte >> i) & 1;
                    bitList.add(bit);

                    // min is 6 bits
                    if (bitList.size() >= 6) {
                        Instruction matchedInstruction = findMatchingInstruction(bitList);
                        if(matchedInstruction != null){
                            System.out.println(matchedInstruction.getOpcode());
                            System.out.println(bitList);
                        }

                        if (matchedInstruction != null) {
                            //we matched an instruction
                            instructionList.add(matchedInstruction);
                            String Mnem = matchedInstruction.getMnemonic();
                            String instructionType = matchedInstruction.getFormat();
                            System.out.println(Mnem);
                            System.out.println(instructionType);

                            switch(instructionType){
                                /*
                                EX: ADD (R type)
                                    * 11 bit opcode - 10001011000
                                    * 5 bit Rm
                                    * 6 bit shamt
                                    * 5 bit Rn
                                    * 5 bit Rd
                                 */
                                case "R":

                                    bitList.clear(); //clear bitlist to fill with new options
                                    break;

                                /*
                                 * EX: ADDI (I type)
                                 * 10 bit opcode - 1001000100
                                 * 12 bit ALU
                                 * 5 bit Rn
                                 * 5 bit Rd
                                 */
                                case "I":
                                    bitList.clear();
                                    break;

                                /* EX: BL (B type)
                                 * 6 bit opcode - 100101
                                 * 26 bit BR_Address
                                 */
                                case "B":
                                    bitList.clear();
                                    break;

                                /* EX: LDUR (D type)
                                 * 11 bit opcode - 11111000010
                                 * 9 bit DT-address
                                 * 2 bit op
                                 * 5 bit Rn
                                 * 5 bit Rd
                                 */
                                case "D":
                                    bitList.clear();
                                    break;

                                /* EX: CBZ (CB type)
                                 * 8 bit opcode - 10110100
                                 * 19 bit COND_BR_Address
                                 * 5 bit Rt
                                 */
                                case "CB":
                                    bitList.clear();
                                    break;

                                default:
                            }

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
