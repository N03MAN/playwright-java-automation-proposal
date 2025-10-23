package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

/**
 * Test Data Manager - Single Responsibility: Test Data Management
 * Handles all test data operations: loading, generation, and transformation
 */
public class TestDataManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Faker faker = new Faker();
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    
    // Cache for loaded test data
    private static final Map<String, List<Map<String, Object>>> dataCache = new HashMap<>();
    
    /**
     * Load test data from JSON file with caching
     * @param filename JSON file name (without path)
     * @return List of test data maps
     */
    public static List<Map<String, Object>> loadTestData(String filename) {
        // Check cache first
        if (dataCache.containsKey(filename)) {
            return dataCache.get(filename);
        }
        
        try {
            File file = Paths.get(TEST_DATA_PATH, filename).toFile();
            List<Map<String, Object>> data = objectMapper.readValue(
                file,
                objectMapper.getTypeFactory().constructCollectionType(
                    List.class, Map.class
                )
            );
            
            // Process data placeholders
            data = processDataPlaceholders(data);
            
            // Cache the data
            dataCache.put(filename, data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from: " + filename, e);
        }
    }
    
    /**
     * Get specific test data by index
     * @param filename JSON file name
     * @param index Data index in the file
     * @return Test data map at specified index
     */
    public static Map<String, Object> getTestDataAt(String filename, int index) {
        List<Map<String, Object>> dataList = loadTestData(filename);
        if (index < 0 || index >= dataList.size()) {
            throw new IllegalArgumentException(
                String.format("Invalid index %d for file %s (size: %d)", 
                    index, filename, dataList.size())
            );
        }
        return dataList.get(index);
    }
    
    /**
     * Convert test data to TestNG DataProvider format
     * @param filename JSON file name
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] toDataProvider(String filename) {
        List<Map<String, Object>> dataList = loadTestData(filename);
        Object[][] result = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            result[i][0] = dataList.get(i);
        }
        return result;
    }
    
    /**
     * Process placeholders in test data
     * @param data Raw test data
     * @return Processed test data with placeholders replaced
     */
    private static List<Map<String, Object>> processDataPlaceholders(List<Map<String, Object>> data) {
        List<Map<String, Object>> processedData = new ArrayList<>();
        
        for (Map<String, Object> entry : data) {
            Map<String, Object> processedEntry = new HashMap<>();
            
            for (Map.Entry<String, Object> field : entry.entrySet()) {
                Object value = field.getValue();
                
                // Process string placeholders
                if (value instanceof String string) {
                    value = processStringPlaceholder(string);
                }
                
                processedEntry.put(field.getKey(), value);
            }
            
            processedData.add(processedEntry);
        }
        
        return processedData;
    }
    
    /**
     * Process string placeholders
     * @param value String value that may contain placeholders
     * @return Processed string value
     */
    private static String processStringPlaceholder(String value) {
        return switch (value) {
            case "<generated>", "<unique_email>" -> generateUniqueEmail();
            case "<random_name>" -> generateRandomName();
            case "<random_password>" -> generateRandomPassword();
            case "<timestamp>" -> String.valueOf(System.currentTimeMillis());
            default -> value;
        };
    }
    
    /**
     * Generate unique email address
     * @return Unique email string
     */
    public static String generateUniqueEmail() {
        return "test_" + System.currentTimeMillis() + "@testmail.com";
    }
    
    /**
     * Generate random name using Faker
     * @return Random full name
     */
    public static String generateRandomName() {
        return faker.name().fullName();
    }
    
    /**
     * Generate random secure password
     * @return Random password meeting security requirements
     */
    public static String generateRandomPassword() {
        return faker.internet().password(10, 16, true, true, true) + "1!";
    }
    
    /**
     * Generate random data for a specific field type
     * @param fieldType Type of field to generate data for
     * @return Generated random value
     */
    public static Object generateRandomData(String fieldType) {
        return switch (fieldType.toLowerCase()) {
            case "email" -> generateUniqueEmail();
            case "name" -> generateRandomName();
            case "password" -> generateRandomPassword();
            case "phone" -> faker.phoneNumber().cellPhone();
            case "address" -> faker.address().fullAddress();
            case "company" -> faker.company().name();
            case "city" -> faker.address().city();
            case "state" -> faker.address().state();
            case "zipcode" -> faker.address().zipCode();
            case "country" -> faker.address().country();
            default -> faker.lorem().word();
        };
    }
    
    /**
     * Clear the data cache
     */
    public static void clearCache() {
        dataCache.clear();
    }
}
