import Objects.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonParse {
    public static void main(String[] args) throws JsonProcessingException {
        String json = "{ \"name\":\"David\", \"position\":\"SOFTWARE_ENGINEER\", \"skilltree\":[ \"Java\", \"Python\", \"JavaScript\" ], \"address\":{ \"street\":\"Street\", \"streetNo\":\"123\" } }";
        System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();

        Employee employee = objectMapper.readValue(json, Employee.class);

        System.out.println(employee);
//        System.out.println(employee.getName());
    }
}
