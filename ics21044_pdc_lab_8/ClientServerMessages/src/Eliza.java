import java.util.HashMap;
import java.util.Map;

public class Eliza {
    private Map<String, String> responses = new HashMap<>();

    public Eliza() {
        initResponses();
    }

    private void initResponses() {
        responses.put("hello", "Hi there! How can I help you today?");
        responses.put("hi", "Hi there! How can I help you today?");
        responses.put("how are you", "I'm just a computer program, but I'm doing well. How can I assist you?");
        responses.put("goodbye", "Goodbye! Have a nice day!");
        responses.put("bye", "Goodbye! Have a nice day!");
    }

    public String respond(String message) {
        String response = responses.get(message.toLowerCase());

        if (response == null) response = "I don't understand that. Can you please elaborate?";

        return response;
    }
}
