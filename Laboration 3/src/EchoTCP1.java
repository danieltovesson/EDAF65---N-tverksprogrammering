import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoTCP1 {

	public static void main(String[] args) {

		try {

			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = new Socket("cs.lth.se", 30000);
				input = socket.getInputStream();
				output = socket.getOutputStream();
			} catch (UnknownHostException e) {
				System.out.println(e);
				System.exit(1);
			} catch (IOException e) {
				System.out.println(e);
				System.exit(1);
			}

			// Protocol echo (e), quit (q)
			
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
