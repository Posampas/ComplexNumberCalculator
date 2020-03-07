import java.util.ArrayDeque;
import java.util.HashMap;

public class Calcuator {

    private ArrayDeque<String> stack;
    private String result;
    private HashMap<Integer, Double[]> variables;
    private ArrayDeque<String> resultStack;

    Calcuator(ArrayDeque stack, HashMap<Integer, Double[]> variables) {
        this.stack = stack;
        this.variables = variables;
        resultStack = new ArrayDeque();
    }

    void performCalculation() {

        String[] compute = new String[3];
        boolean freshNumbesInArray = false;

        while (!stack.isEmpty()) {
            if (stack.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    compute[i] = stack.pop();
                    freshNumbesInArray = true;
                }
                while (!goodForCalcuation(compute)) {
                    if (!stack.isEmpty()) {
                        resultStack.offerLast(compute[0]);
                        compute[0] = compute[1];
                        compute[1] = compute[2];
                        compute[2] = stack.pop();
                    } else {
                        break;
                    }
                }

                if (goodForCalcuation(compute)) {
                    resultStack.offerLast(smallCalculate(compute));
                    freshNumbesInArray = false;

                }

                if (freshNumbesInArray) {
                    for (int i = 0; i < 3; i++) {
                        resultStack.offerLast(compute[i]);
                    }
                    freshNumbesInArray = false;
                }
            } else {
                while (!stack.isEmpty()) {
                    resultStack.offerLast(stack.pop());
                }

            }


            if (stack.isEmpty() && resultStack.size() == 1) {
                break;
            }


            if (stack.isEmpty()) {
                stack = resultStack.clone();
                resultStack.clear();
            }

        }
        Integer resultVariable = Integer.parseInt(resultStack.pop());

        result = "(" + variables.get(resultVariable)[0] + " + " + variables.get(resultVariable)[1] + "i)";

        System.out.println(result);


    }


    private String smallCalculate(String[] calcArr) {
        int firstNum = Integer.parseInt(calcArr[0]);
        int secondNum = Integer.parseInt(calcArr[1]);

        if (calcArr[2].equals("+")) {

            return addComNum(firstNum, secondNum);

        } else if (calcArr[2].equals("-")) {
            return substractComNum(firstNum, secondNum);

        } else if (calcArr[2].equals("*")) {
            return multipyComNum(firstNum, secondNum);
        } else {
            return divideComNum(firstNum, secondNum);
        }


    }
    private String divideComNum(Integer keyOne, Integer keyTwo){
        Double[] firstNumber = variables.get(keyOne);
        Double[] secondNumber = variables.get(keyTwo);
        Double[] secondNumberComplexConjugate = new Double[2];
        System.out.println("(" + secondNumber[0] + " + " + secondNumber[1] + "i)");
        secondNumberComplexConjugate[0] = secondNumber[0];
        secondNumberComplexConjugate[1] = secondNumber[1]*(-1);

        System.out.println("(" + secondNumberComplexConjugate[0] + " + " + secondNumberComplexConjugate[1] + "i)");

        Double[] nominator = new Double[2];
        Double denominatior = 0.0 ;

        nominator[0] = firstNumber[0] * secondNumberComplexConjugate[0]+ (firstNumber[1] * secondNumberComplexConjugate[1])*(-1);
        nominator[1] = firstNumber[0] * secondNumberComplexConjugate[1] + firstNumber[1] * secondNumberComplexConjugate[0];
        denominatior = secondNumber[0] * secondNumberComplexConjugate[0]+ (secondNumber[1] * secondNumberComplexConjugate[1])*(-1);

        Double[] result = new Double[2];

        result[0] = nominator[0] / denominatior;
        result[1] = nominator[1] / denominatior;

        addVariable(result);
        return String.valueOf(variables.size());
    }
    private String multipyComNum(Integer keyOne, Integer keyTwo){
        Double[] firstNumber = variables.get(keyOne);
        Double[] secondNumber = variables.get(keyTwo);

        Double[] result = new Double[2];

        result[0] = firstNumber[0] * secondNumber[0]+ (firstNumber[1] * secondNumber[1])*(-1);
        result[1] = firstNumber[0] * secondNumber[1] + firstNumber[1] * secondNumber[0];
        addVariable(result);
        return String.valueOf(variables.size());

    }
    private String substractComNum(Integer keyOne, Integer keyTwo){
        Double[] firstNumber = variables.get(keyOne);
        Double[] secondNumber = variables.get(keyTwo);

        Double[] result = new Double[2];

        result[0] = firstNumber[0] - secondNumber[0];
        result[1] = firstNumber[1] - secondNumber[1];
        addVariable(result);
        return String.valueOf(variables.size());
    }

    private String addComNum(Integer keyOne, Integer keyTwo) {

        Double[] firstNumber = variables.get(keyOne);
        Double[] secondNumber = variables.get(keyTwo);

        Double[] result = new Double[2];

        result[0] = firstNumber[0] + secondNumber[0];
        result[1] = firstNumber[1] + secondNumber[1];
        addVariable(result);
        return String.valueOf(variables.size());

    }

    void addVariable(Double[] complexNum) {
        variables.put(variables.size() + 1, complexNum);

    }


    private boolean goodForCalcuation(String[] arr) {
        if (arr[0].matches("-?[0-9]+"))
            if (arr[1].matches("-?[0-9]+"))
                if (arr[2].matches("[-+/*]"))
                    return true;
        return false;

    }

}
