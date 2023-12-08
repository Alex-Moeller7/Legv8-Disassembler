import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileParser {

    public static List<Instruction> parseInstructions(String fileLocation) {
        List<Instruction> instructionList = new ArrayList<>();
        List<Integer> bitList = new ArrayList<>();
        Instruction matchedInstruction = null;
        String Mnem = null;
        String instructionType = null;
        int bitsRead = 0;

        try (InputStream is = new FileInputStream(fileLocation)) {
            int currentByte;
            while ((currentByte = is.read()) != -1) {
                for (int i = 7; i >= 0; i--) {
                    int bit = (currentByte >> i) & 1;
                    bitList.add(bit);
                    bitsRead++;

                    if (bitsRead >= 6) {
                        if (matchedInstruction == null) {
                            matchedInstruction = findMatchingInstruction(bitList);

                            if (matchedInstruction != null) {
                                instructionList.add(matchedInstruction);
                                Mnem = matchedInstruction.getMnemonic();
                                instructionType = matchedInstruction.getFormat();
                            }
                        }

                        if (matchedInstruction != null) {
                            int totalBitsForInstruction = 32; // Total bits for a 4-byte instruction

                            if (bitsRead >= totalBitsForInstruction) {
                                // Extract the remaining bits for the complete instruction
                                // ... Extract and process the remaining bits based on instruction type
                                // Reset variables for the next instruction

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
                                        int size = 32 - 11; // number of bits for remaining fields
                                        List<Integer> Rm;
                                        List<Integer> shamt;
                                        List<Integer> Rn;
                                        List<Integer> Rd;

                                        Rm = bitList.subList(0,4);
                                        shamt = bitList.subList(5,10);
                                        Rn = bitList.subList(11,15);
                                        Rd = bitList.subList(16,20);


                                        System.out.println(Mnem + " X" + Rm + " X" + shamt + " X" + Rn + " " + Rd);
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

                                bitList.clear();
                                matchedInstruction = null;
                                Mnem = null;
                                instructionType = null;
                                bitsRead = 0;
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

    // Method to convert bitList to integer value
    private static int convertBitListToInt(List<Integer> bitList) {
        int value = 0;
        int power = bitList.size() - 1;
        for (int bit : bitList) {
            value += bit * Math.pow(2, power);
            power--;
        }
        return value;
    }
}
