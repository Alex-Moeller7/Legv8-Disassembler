import java.io.FileOutputStream;
import java.io.IOException;

public class BinUpdater {
    public static void main(String[] args) {
        // Replace this with your desired binary value
        String binaryValue = "100010110001111111111111010110000110100100011010010001101001000";
        /* R, I, CB, B, D,
         *
         * EX: ADD (R type)
         * 11 bit opcode - 10001011000
         * 5 bit Rm
         * 6 bit shamt
         * 5 bit Rn
         * 5 bit Rd
         *
         * EX: ADDI (I type)
         * 10 bit opcode - 1001000100
         * 12 bit ALU
         * 5 bit Rn
         * 5 bit Rd
         *
         * EX: LDUR (D type)
         * 11 bit opcode - 11111000010
         * 9 bit DT-address
         * 2 bit op
         * 5 bit Rn
         * 5 bit Rd
         *
         * EX: BL (B type)
         * 6 bit opcode - 100101
         * 26 bit BR_Address
         *
         * EX: CBZ (CB type)
         * 8 bit opcode - 10110100
         * 19 bit COND_BR_Address
         * 5 bit Rt
         */

        // Specify the file path
        String filePath = "src/test.bin";

        try {
            byte[] byteArray = convertBinaryStringToByteArray(binaryValue);

            // Write the byte array to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(byteArray);
            }

            System.out.println("File updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] convertBinaryStringToByteArray(String binaryString) {
        int length = binaryString.length();
        int paddedLength = (length + 7) / 8 * 8; // Round up to the nearest multiple of 8
        byte[] byteArray = new byte[paddedLength / 8];

        // Pad the binary string with zeros
        StringBuilder paddedBinaryString = new StringBuilder(binaryString);
        while (paddedBinaryString.length() < paddedLength) {
            paddedBinaryString.append('0');
        }

        for (int i = 0; i < paddedLength; i += 8) {
            String byteString = paddedBinaryString.substring(i, i + 8);
            byteArray[i / 8] = (byte) Integer.parseInt(byteString, 2);
        }

        return byteArray;
    }
}
