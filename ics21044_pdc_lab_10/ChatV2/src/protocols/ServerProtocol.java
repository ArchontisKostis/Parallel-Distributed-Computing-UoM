package protocols;

// Η κλαση αυτη χρησιμοποιείται για την αλληλεπίδραση με τον χρήστη και τον χειρισμό των μηνυμάτων απο την μεριά του server.
public class ServerProtocol {

	public String processRequest(String theInput) {
		System.out.println("Received message from client: " + theInput);
		String theOutput = theInput;
		System.out.println("Send message to client: " + theOutput);
		return theOutput;
	}
}