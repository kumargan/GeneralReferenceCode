package pipe_from_stdin;

import java.util.Scanner;

public class ReadPipedMesseges {

	public static void main(String[] args) {
		System.out.println(" start reading from stdin");
		Scanner scanner = new Scanner(System.in);
		// Read and print out each line.
        while (scanner.hasNextLine()) {
            String lineOfInput = scanner.nextLine();
            System.out.println(lineOfInput);
        }
        System.out.println(" done reading from stdin");
	}

}
