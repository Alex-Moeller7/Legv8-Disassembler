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
        Instruction matchedInstruction = null;
        String Mnem = null;
        String instructionType = null;
        int bitsRead = 0;

        try (InputStream is = new FileInputStream(fileLocation)) {
            int currentByte;
            while (true) {
                currentByte = is.read();
                if (currentByte == -1) {
                    break; // Exit the loop when the end of the file is reached
                }

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
                                        int OPsize = matchedInstruction.getOpcodeSize(); // number of bits for remaining fields
                                        List<Integer> remainingInts = bitList.subList(OPsize, 32);
                                        List<Integer> RmArr; //11111
                                        List<Integer> shamtArr; //000000
                                        List<Integer> RnArr; //00000
                                        List<Integer> RdArr; //10100

                                        RmArr = remainingInts.subList(0,5);
                                        int Rm = binaryToDecimal(combineElements(RmArr));

                                        shamtArr = remainingInts.subList(5,11);
                                        int shamt = binaryToDecimal(combineElements(shamtArr));

                                        RnArr = remainingInts.subList(11,16);
                                        int Rn = binaryToDecimal(combineElements(RnArr));

                                        RdArr = remainingInts.subList(16,21);
                                        int Rd = binaryToDecimal(combineElements(RdArr));

                                        /**
                                         * All R type instructions to be handled for printing (30)
                                         * UMULH, UDIV, SUBS, SUB, SMULH, SDIV, PRNT, PRNL, ORR, MUL
                                         * LSR, LSL, HALT, FSUBS, FSUBD, FMULS, FMULD, FDIVS, FDIVD, FCMPS, FCMPD,
                                         * FADDD, FADDS, EOR, DUMP, BR, ANDS, AND, ADDS, ADD
                                         */
                                        if(Mnem.equals("PRNT")){
                                            System.out.println(Mnem + " X" + Rd);
                                        }else if(Mnem.equals("PRNL") || Mnem.equals("DUMP") || Mnem.equals("HALT")){
                                            System.out.println(Mnem);
                                        }else if(Mnem.equals("LSL") || Mnem.equals("LSR")){
                                            System.out.println(Mnem + " X" + Rd + ", X" + Rn + ", #" + shamt);
                                        }else{
                                            //replace X31 with XZR
                                            if(Rm == 31){
                                                String XZR = "XZR";
                                                System.out.println(Mnem + " X" + Rd + ", X" + Rn + ", " + XZR);
                                            }else {
                                                System.out.println(Mnem + " X" + Rd + ", X" + Rn + ", X" + Rm);
                                            }
                                        }

                                        break;

                                    /*
                                     * EX: ADDI (I type)
                                     * 10 bit opcode - 1001000100
                                     * 12 bit ALU
                                     * 5 bit Rn
                                     * 5 bit Rd
                                     */
                                    case "I":
                                        int OPsizeI = matchedInstruction.getOpcodeSize(); // number of bits for remaining fields
                                        List<Integer> remainingIntsI = bitList.subList(OPsizeI, 32);
                                        //System.out.println(remainingIntsI);
                                        List<Integer> AluArr; //000000000001
                                        List<Integer> RnArrI; //00000
                                        List<Integer> RdArrI; //01001

                                        AluArr = remainingIntsI.subList(0,12);
                                        int Alu = binaryToDecimal(combineElements(AluArr));

                                        RnArrI = remainingIntsI.subList(12,17);
                                        int RnI = binaryToDecimal(combineElements(RnArrI));

                                        RdArrI = remainingIntsI.subList(17,22);
                                        int RdI = binaryToDecimal(combineElements(RdArrI));

                                        //replace X28 with SP
                                        if(RnI == 28 && RdI == 28){
                                            String SP = "SP";
                                            System.out.println(Mnem + " " + SP + ", " + SP  + ", #" + Alu);
                                        } else if(RdI == 28){
                                            String SP = "SP";
                                            System.out.println(Mnem + " " + SP + ", X" + RnI  + ", #" + Alu);
                                        }else if(RnI == 28){
                                            String SP = "SP";
                                            System.out.println(Mnem + " X" + RdI + ", " + SP  + ", #" + Alu);
                                        } else {
                                            System.out.println(Mnem + " X" + RdI + ", X" + RnI  + ", #" + Alu);
                                        }

                                        //Switch case to print based on the instruction
                                        /**
                                         * All I type instructions (8)
                                         * ADDI, ADDIS, ANDI, ANDIS, EORI, ORRI, SUBIS, SUBI
                                         */


                                        break;

                                    /* EX: BL (B type)
                                     * 6 bit opcode - 100101
                                     * 26 bit BR_Address
                                     */
                                    case "B":
                                        int OPsizeB = matchedInstruction.getOpcodeSize(); // number of bits for remaining fields
                                        List<Integer> remainingIntsB = bitList.subList(OPsizeB, 32);
                                        //System.out.println(remainingIntsB);
                                        List<Integer> BR_Arr; //

                                        BR_Arr = remainingIntsB;
                                        System.out.println(combineElements(BR_Arr));
                                        int BR_A = binaryToDecimal(combineElements(BR_Arr));

                                        /**
                                         * All B type instructions for printing (2)
                                         * B, BL
                                         */
                                        System.out.println("BL " + BR_A);
                                        break;

                                    /* EX: LDUR (D type)
                                     * 11 bit opcode - 11111000010
                                     * 9 bit DT-address
                                     * 2 bit op
                                     * 5 bit Rn
                                     * 5 bit Rd
                                     */
                                    case "D":
                                        int OPsizeD = matchedInstruction.getOpcodeSize(); // number of bits for remaining fields
                                        List<Integer> remainingIntsD = bitList.subList(OPsizeD, 32);
                                        //System.out.println(remainingIntsD);
                                        List<Integer> DTArr; //000001000
                                        List<Integer> opArr; //00
                                        List<Integer> RnArrD; //11100
                                        List<Integer> RdArrD; //10011

                                        DTArr = remainingIntsD.subList(0,9);
                                        int dt = binaryToDecimal(combineElements(DTArr));

                                        opArr = remainingIntsD.subList(9,11);
                                        //System.out.println(combineElements(opArr));
                                        int op = binaryToDecimal(combineElements(opArr));

                                        RnArrD = remainingIntsD.subList(11,16);
                                        //System.out.println(combineElements(RnArrD));
                                        int RnD = binaryToDecimal(combineElements(RnArrD));

                                        RdArrD = remainingIntsD.subList(16,21);
                                        //System.out.println(combineElements(RdArrI));
                                        int RdD = binaryToDecimal(combineElements(RdArrD));

                                        /**
                                         * All D type instructions for printing (12)
                                         * LDUR, LDURB, LDURD, LDURH, LDURS, LDURSW,
                                         * STUR, STRUB, STRUD, STRUH, STRUS, STURSW
                                         */

                                        System.out.println(Mnem + " X" + RdD + ", [X" + RnD + ", #" + dt + "]");
                                        break;

                                    /* EX: CBZ (CB type)
                                     * 8 bit opcode - 10110100
                                     * 19 bit COND_BR_Address
                                     * 5 bit Rt
                                     */
                                    case "CB":
                                        int OPsize_CB = matchedInstruction.getOpcodeSize(); // number of bits for remaining fields
                                        List<Integer> remainingInts_CB = bitList.subList(OPsize_CB, 32);
                                        //System.out.println(remainingInts_CB);
                                        List<Integer> COND_Arr; //0000000000000000010
                                        List<Integer> Rt_Arr; //01001
                                        String condString = null;

                                        COND_Arr = remainingInts_CB.subList(0,19);
                                        //System.out.println(combineElements(COND_Arr));
                                        int Cond = binaryToDecimal(combineElements(COND_Arr));

                                        Rt_Arr = remainingInts_CB.subList(19,24);
                                        //System.out.println(combineElements(Rt_Arr));
                                        int Rt_CB = binaryToDecimal(combineElements(Rt_Arr));

                                        /**
                                         * All CB instructions for printing variations (3)
                                         * B., CBNZ, CBZ
                                         */
                                        //if the instruction is a B.cond, determine which cond.
                                        if(Mnem.equals("B.")){
                                            switch(Rt_CB){
                                                case 0:
                                                    condString = "EQ";
                                                case 1:
                                                    condString = "NE";
                                                case 2:
                                                    condString = "HS";
                                                case 3:
                                                    condString = "LO";
                                                case 4:
                                                    condString = "MI";
                                                case 5:
                                                    condString = "PL";
                                                case 6:
                                                    condString = "VS";
                                                case 7:
                                                    condString = "VC";
                                                case 8:
                                                    condString = "HI";
                                                case 9:
                                                    condString = "LS";
                                                case 10:
                                                    condString = "GE";
                                                case 11:
                                                    condString = "LT";
                                                case 12:
                                                    condString = "GT";
                                                case 13:
                                                    condString = "LE";
                                                default:
                                            }
                                            System.out.println(Mnem + condString + " " + Cond);

                                            //if instruction is not B.cond
                                        } else {
                                            System.out.println(Mnem + " X" + Rt_CB + ", " + Cond);
                                        }

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

    /**
     * Takes elements from an array list and converts them to an integer of the same values.
     * Ex:  Integer List [1, 0, 1, 1, 0, 1, 0], outputs: 1011010
     * @param elements - A list of integers in the form of binary 1 or 0's (Ex: [1, 0, 0, 1, 1, 0])
     * @return result - an integer in the form of binary (Ex: 101001)
     */
    public static int combineElements(List<Integer> elements) {
        int result = 0;
        int multiplier = 1;

        for (int i = elements.size() - 1; i >= 0; i--) {
            result += elements.get(i) * multiplier;
            multiplier *= 10; // Adjust this multiplier for different bases (e.g., 2 for binary)
        }

        return result;
    }

    /**
     * Converts a binary number to a decimal value.
     * Used to convert an integer in the format 1010100 to a decimal number (register number).
     * Ex: 0110 outputs: 6
     * Ex: 101100 outputs: 36
     *
     * @param binaryNumber - an integer in the format 101100
     * @return decimal - a value in decimal
     */
    public static int binaryToDecimal(int binaryNumber) {
        int decimal = 0;
        int power = 0;

        while (binaryNumber > 0) {
            int bit = binaryNumber % 10;
            decimal += bit * Math.pow(2, power);
            binaryNumber /= 10;
            power++;
        }

        return decimal;
    }

}
