package org.yannapu;
import java.util.Scanner;
public class AppInterface {
    private ProcessingEngine processingEngine;

    public AppInterface(ProcessingEngine processingEngine) {
        this.processingEngine = processingEngine;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("File Retrieval Engine");
        System.out.println("Available commands:");
        System.out.println("index <path>  - Enter relative path with the index");
        System.out.println("search <query> - Enter the valid query");
        System.out.println("quit  - Exit the program");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            if (command.equals("quit")) {
                System.out.println("Exiting File Retrieval Engine.");
                break;
            } else if (command.startsWith("index ")) {
                String path = command.substring(6);
                processingEngine.indexFiles(path);
            } else if (command.startsWith("search ")) {
                String query = command.substring(7);
                processingEngine.searchFiles(query);
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }

        scanner.close();
    }
    public static void main(String[] args) {
        ProcessingEngine processingEngine = new ProcessingEngine();
        AppInterface appInterface = new AppInterface(processingEngine);
        appInterface.start();
    }
}
