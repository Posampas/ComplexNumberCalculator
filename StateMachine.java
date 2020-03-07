import java.util.ArrayDeque;
import java.util.HashMap;

public class StateMachine {
    private boolean inputState;
    private boolean parshing;
    private boolean computation;
    private boolean work;
    private String str;
    private ArrayDeque stack;
    private HashMap <Integer, Double[]>equationVariables;

    public StateMachine() {
        startState();
        work = true;


    }

    private void startState() {
        this.inputState = true;
        this.parshing = false;
        this.computation = false;
    }

    public void workingLoop() {

        while (work) {

            startState();


            if (inputState) {

                Input in = new Input();
                str = in.getData();
                inputState = false;
                parshing = true;
            }

            if (parshing) {

                Parsher parsher = new Parsher(str);
                parsher.parsheEquation();
                stack = parsher.getParshingStack();
                equationVariables = parsher.getEquationVariables();
                parshing = false;
                computation = true;
            }

            if(computation){

                Calcuator calc = new Calcuator(stack,equationVariables);
                calc.performCalculation();
                computation= false;
            }

        }
    }

}
