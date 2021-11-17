import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Parser {

    String[] currentCommand;
    BufferedReader myReader;
    File vmTranslate;
    String pathVM;
    private int currentLine = 0;
    private ArrayList<String> lines;
    public static final int ARITHMETIC = 0;
    public static final int PUSH = 1;
    public static final int POP = 2;
    public static final int LABEL = 3;
    public static final int GOTO = 4;
    public static final int IF = 5;
    public static final int FUNCTION = 6;
    public static final int RETURN = 7;
    public static final int CALL = 8;
    public static final ArrayList<String> arithmeticCmds = new ArrayList<String>();

    static {

        arithmeticCmds.add("add");
        arithmeticCmds.add("sub");
        arithmeticCmds.add("neg");
        arithmeticCmds.add("eq");
        arithmeticCmds.add("gt");
        arithmeticCmds.add("lt");
        arithmeticCmds.add("and");
        arithmeticCmds.add("or");
        arithmeticCmds.add("not");
    }

    public Parser(String pathVM) throws IOException {
        this.pathVM = pathVM;
        this.currentCommand = new String[3];
        this.lines = new ArrayList<String>();
        File vmCode = new File(this.pathVM);
        try {
            this.myReader = new BufferedReader(new FileReader(vmCode));
            String line = this.myReader.readLine();
            while (line != null) {
                line = line.replaceAll("//.+", "");
                if (line.equals("")) {
                    line = this.myReader.readLine();
                    continue;
                }
                this.lines.add(line);
                line = this.myReader.readLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean hasMoreCommands() {
        return currentLine <= this.lines.size() - 1;
    }

    public void advance() {
        this.currentCommand = this.lines.get(currentLine).trim().split(" ");
        currentLine++;
    }

    public int commandType() {

        String currentType = this.currentCommand[0];

        if (arithmeticCmds.contains(currentType)) {
            return ARITHMETIC;
        } else if (currentType.equals("push")) {
            return PUSH;
        } else if (currentType.equals("pop")) {
            return POP;
        } else if (currentType.equals("label")) {
            return LABEL;
        } else if (currentType.equals("goto")) {
            return GOTO;
        } else if (currentType.equals("if-goto")) {
            return IF;
        } else if (currentType.equals("function")) {
            return FUNCTION;
        } else if (currentType.equals("return")) {
            return RETURN;
        } else if (currentType.equals("call")) {
            return CALL;
        }

        System.out.println(currentType);
        return -1;
    }

    public String arg1() {

        if (this.commandType() == RETURN) {
            throw new IllegalStateException("RETURN command!");
        } else if (this.commandType() == ARITHMETIC) {
            return this.currentCommand[0];
        } else {
            return this.currentCommand[1];
        }

    }

    public int arg2() {

        if (this.commandType() == PUSH | this.commandType() == POP | this.commandType() == CALL
                | this.commandType() == FUNCTION) {
            return Integer.parseInt(this.currentCommand[2]);
        } else {
            throw new IllegalArgumentException("Ilegal command!");
        }
    }
}
