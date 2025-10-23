package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for managing test data
 */
public class DataUtils {
    private static final Faker faker = new Faker();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generates a unique email address
     * @return Unique email string
     */
    public static String uniqueEmail() {
        long t = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "user_" + t + "_" + r + "@testmail.com";
    }

    /**
     * Generates a random full name using JavaFaker
     * @return Random full name
     */
    public static String randomName() {
        return faker.name().fullName();
    }

    /**
     * Generates a random password
     * @return Random password string
     */
    public static String randomPassword() {
        return faker.internet().password(8, 16, true, true);
    }

    /**
     * Reads test data from JSON file
     * @param filename Name of the JSON file in testdata directory
     * @return List of maps containing test data
     */
    public static List<Map<String, Object>> readJsonData(String filename) {
        try (InputStream is = DataUtils.class.getClassLoader().getResourceAsStream("testdata/" + filename)) {
            if (is == null) {
                throw new IOException("File not found: testdata/" + filename);
            }
            return objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data from " + filename, e);
        }
    }

    /**
     * Reads test data from JSON file and returns as Object[][]
     * @param filename Name of the JSON file in testdata directory
     * @return Object[][] suitable for TestNG DataProvider
     */
    public static Object[][] readJsonDataProvider(String filename) {
        List<Map<String, Object>> data = readJsonData(filename);
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }
}
