import java.io.FileOutputStream;
import java.io.IOException;

public class BinUpdater {
    public static void main(String[] args) {
        // Replace this with your desired binary value
        String binaryValue = "1101000100000000011000111001110011111000000000000000001110010100111110000000000010000011100100111111100000000001000000111001111010001011000111110000000000010011";

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
