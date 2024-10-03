package protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Η κλαση χρησιμοποιείται για την αλληλεπίδραση με τον χρήστη και τον χειρισμό των μηνυμάτων.
public class ChatClientProtocol {
	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String sendMessage() throws IOException {
		String theOutput = user.readLine();
		return theOutput;
	}

	public String receiveMessage(String theInput) throws IOException {
		System.out.println(theInput);
		return theInput;
	}
}
