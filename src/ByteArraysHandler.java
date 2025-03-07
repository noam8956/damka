import java.io.*;

public class ByteArraysHandler {

    public static byte[] convert2DArrayToByteArray(String[][] array) throws IOException {
        // Create a ByteArrayOutputStream to hold the bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create an ObjectOutputStream to write objects to the byte stream
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            // Write the 2D array to the ObjectOutputStream (it will be serialized)
            objectOutputStream.writeObject(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Return the byte array representation of the 2D array
        return byteArrayOutputStream.toByteArray();
    }

    public static String[][] convertByteArrayTo2DArray(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            // Deserialize the byte array back to a 2D array
            return (String[][]) objectInputStream.readObject();
        }
    }
}
