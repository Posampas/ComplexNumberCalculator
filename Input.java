import java.util.Scanner;

public class Input {

    private static Scanner scanner = new Scanner(System.in);

    Input() {

    }

    public String getData(){

        String input = null;
        while(true){
            input = scanner.nextLine();

            if(!input.equals("")){
                break;
            }
        }

        return input;

    }



}
