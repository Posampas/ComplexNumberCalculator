import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsher {
    private ArrayDeque<String> parshingStack;
    private String inputString;
    private HashMap <Integer, Double[]> equationVariables;
    private int complexNumCount;

    public Parsher(String inputString) {
        parshingStack = new ArrayDeque<>();
        this.inputString = inputString;
        complexNumCount = 0;
        equationVariables = new HashMap<>();

    }

    public void parsheEquation() {

//        1. Sprawdza poprawność danych wejściowych

        inputString = replaceAllUnnecessary(inputString);

//        System.out.println(inputString);

//        2. Robzbija na atomy

        parshingStack = atomizeEquation(inputString);


//        3. Translate to RPN and witch complex numbers as variables.
        parshingStack = tralslateToRPNandAddVariavles();

//        print(parshingStack);




    }
    private ArrayDeque tralslateToRPNandAddVariavles(){
        ArrayDeque <String> signStack = new ArrayDeque<>();
        ArrayDeque <String> result = new ArrayDeque<>();

        while (!parshingStack.isEmpty()) {

            if (parshingStack.getFirst().matches("(\\(-?[0-9]+[+\\-]i?[0-9]+i?\\))" +
                                                                "|(\\(-?i?[0-9]+i?[+\\-][0-9]+\\))|" +
                                                                "(\\(-?[0-9]+\\))|(\\(-?i?[0-9]+i?\\))")) {
                result.offerLast(saveAsVariable(parshingStack.pop()));
            } else if (parshingStack.getFirst().matches("[-+*/]")) {
                if (signStack.isEmpty() || signStack.getFirst().equals("(")) {
                    signStack.push(parshingStack.pop());
                } else {
                    if (getSignPriority(parshingStack.getFirst())
                            < getSignPriority(signStack.getFirst())) {
                        signStack.push(parshingStack.pop());
                    } else {
                        result.offerLast(signStack.pop());
                    }
                }
            } else if (parshingStack.getFirst().equals("(")) {
                signStack.push(parshingStack.pop());
            } else if (parshingStack.getFirst().equals(")")) {
                parshingStack.pop();
                while (!signStack.getFirst().equals("(")) {
                    result.offerLast(signStack.pop());
                }
                signStack.pop();
            }
        }
        while (!signStack.isEmpty()) {
            result.offerLast(signStack.pop());
        }

        return result;

    }
    private String saveAsVariable(String str){

        double realPart =0;
        double imaginaryPart = 0;
        str = str.substring(1,str.length()-1);


        if(str.contains("+")) {
            String[] arr = str.split("[+]");
            if (arr[0].contains("i")) {
                StringBuilder sb = new StringBuilder(arr[0]);
                if(sb.length()>1) {
                    sb.deleteCharAt(sb.indexOf("i"));
                }else {
                    sb.deleteCharAt(sb.indexOf("i"));
                    sb.append(1);
                }
                imaginaryPart = Double.parseDouble(sb.toString());
                realPart = Double.parseDouble(arr[1]);
            } else {
                realPart = Double.parseDouble(arr[0]);
                StringBuilder sb = new StringBuilder(arr[1]);
                if(sb.length()>1) {
                    sb.deleteCharAt(sb.indexOf("i"));
                }else {
                    sb.deleteCharAt(sb.indexOf("i"));
                    sb.append(1);
                }
                imaginaryPart = Double.parseDouble(sb.toString());
            }

        }else if(str.substring(1).contains("-")){
            String[] arr = new String[2];
            arr[0] = str.substring(0,str.lastIndexOf("-"));
            arr[1] = str.substring(str.lastIndexOf("-"));
            if (arr[0].contains("i")) {
                StringBuilder sb = new StringBuilder(arr[0]);
                if(sb.length()>1) {
                    sb.deleteCharAt(sb.indexOf("i"));
                }else {
                    sb.deleteCharAt(sb.indexOf("i"));
                    sb.append(1);
                }
                imaginaryPart = Double.parseDouble(sb.toString());
                realPart = Double.parseDouble(arr[1]);
            } else {
                realPart = Double.parseDouble(arr[0]);
                StringBuilder sb = new StringBuilder(arr[1]);
                if(sb.length()>1) {
                    sb.deleteCharAt(sb.indexOf("i"));
                }else {
                    sb.deleteCharAt(sb.indexOf("i"));
                    sb.append(1);
                }
                imaginaryPart = Double.parseDouble(sb.toString());
            }
        }else{
            if(str.contains("i")){
                StringBuilder sb = new StringBuilder(str);
                if(sb.length()>1) {
                    sb.deleteCharAt(sb.indexOf("i"));
                }else {
                    sb.deleteCharAt(sb.indexOf("i"));
                    sb.append(1);
                }
                imaginaryPart= Double.parseDouble(sb.toString());
            }else {
                realPart = Double.parseDouble(str);
            }
        }

        Double[] complexNumArray = new Double[2];

        complexNumArray[0] = realPart;
        complexNumArray[1] = imaginaryPart;
        addVariable(complexNumArray);

        return String.valueOf(equationVariables.size());
    }

    private int getSignPriority(String singn) {
        if (singn.equals("+")) {
            return 2;
        } else if (singn.equals("-")) {
            return 2;
        } else if (singn.equals("/")) {
            return 1;
        } else {
            return 1;
        }
    }

    void addVariable(Double[] complexNum){
        if(equationVariables.isEmpty()) {
            equationVariables.put(1, complexNum);
        }else {
            equationVariables.put((equationVariables.size()+1), complexNum);
        }

    }




    private void print(ArrayDeque stack) {
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + "     ");
        }
    }


    private ArrayDeque<String> atomizeEquation(String inputString) {

        ArrayDeque<String> tmp = new ArrayDeque<>();
        String pat = "(\\(-?[0-9]+[+\\-]i?[0-9]+i?\\))|(\\(-?i?[0-9]+i?[+\\-][0-9]+\\))|(-)|(\\+)|(=)|(\\*)|(/)|(\\(-?[0-9]*\\))|(\\(-?i?[0-9]*i?\\))|([()])";



        Matcher split = Pattern.compile(pat).matcher(inputString);


        while (split.find()) {


            if (split.group(1) != null) {
                tmp.offerLast(split.group(1));
            } else if (split.group(2) != null) {
                tmp.offerLast(split.group(2));
            } else if (split.group(3) != null) {
                tmp.offerLast(split.group(3));
            } else if (split.group(4) != null) {
                tmp.offerLast(split.group(4));
            } else if (split.group(5) != null) {
                tmp.offerLast(split.group(5));
            } else if (split.group(6) != null) {
                tmp.offerLast(split.group(6));
            } else if (split.group(7) != null) {
                tmp.offerLast(split.group(7));
            } else if (split.group(8) != null) {
                tmp.offerLast(split.group(8));
            }else if (split.group(9) != null) {
                tmp.offerLast(split.group(9));
            }else if (split.group(10) != null) {
                tmp.offerLast(split.group(10));
            }
        }
        return tmp;
    }

    private String replaceAllUnnecessary(String equation) {
        boolean twoSigninrow = true;

        while (twoSigninrow) {

            equation = equation.replaceAll("\\+-", "-")
                    .replaceAll("-\\+", "-")
                    .replaceAll("\\++", "+")
                    .replaceAll("--", "+")
                    .replaceAll("  ", " ")
                    .replaceAll("=\\+ ", "=")
                    .replaceAll("(\\(\\))|(\\( +\\))", "(0)");
            twoSigninrow = false;
            for (int i = 0; i < equation.length() - 1; i++) {
                if (((equation.charAt(i) == '+') && (equation.charAt(i + 1) == '-'))) {
                    twoSigninrow = true;
                    break;
                }
                if (((equation.charAt(i) == '-') && (equation.charAt(i + 1) == '+'))) {
                    twoSigninrow = true;
                    break;
                }
                if (((equation.charAt(i) == '-') && (equation.charAt(i + 1) == '-'))) {
                    twoSigninrow = true;
                    break;
                }
                if (((equation.charAt(i) == ' ') && (equation.charAt(i + 1) == ' '))) {
                    twoSigninrow = true;
                    break;
                }
            }
        }
        return equation;
    }

    public ArrayDeque<String> getParshingStack() {
        return parshingStack;
    }

    public HashMap<Integer, Double[]> getEquationVariables() {
        return equationVariables;
    }
}
