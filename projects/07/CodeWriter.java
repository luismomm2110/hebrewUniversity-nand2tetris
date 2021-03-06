import java.io.*;

public class CodeWriter {
    private static Integer numJump;
    private String pathAssembly;
    private File assemblyTranslate;
    public FileWriter assemblyWriter;

    public CodeWriter(String pathAssembly) throws IOException {

        this.pathAssembly = pathAssembly;
        CodeWriter.numJump = 0;
        this.assemblyTranslate = new File(pathAssembly.replace(".vm", ".asm"));
        System.out.println(pathAssembly.replace(".vm", ".asm"));
        try {
            this.assemblyWriter = new FileWriter(this.assemblyTranslate);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void writeArithmetic(String arg1) throws IOException {

        this.assemblyWriter.write("//" + arg1 + "\n");

        switch (arg1) {

        case "add":
            this.assemblyWriter.write(backSP() + "M=D\n" + backSP() + "M=M+D\n" + advSP());
            break;

        case "sub":
            this.assemblyWriter.write(backSP() + "D=M\n" + backSP() + "M=M-D\n" + advSP());
            break;

        case "neg":
            this.assemblyWriter.write(backSP() + "M=-M\n" + advSP());
            break;

        case "eq":
            this.assemblyWriter.write(compCommands("eq"));
            break;

        case "gt":
            this.assemblyWriter.write(compCommands("gt"));
            break;

        case "lt":
            this.assemblyWriter.write(compCommands("lt"));
            break;

        case "and":
            this.assemblyWriter.write(backSP() + "D=M\n" + backSP() + "M=M&D\n" + advSP());
            break;

        case "or":
            this.assemblyWriter.write(backSP() + "D=M\n" + backSP() + "M=M|D\n" + advSP());
            break;

        case "not":
            this.assemblyWriter.write(backSP() + "M=!M\n" + advSP());
            break;

        default:
            System.out.println("Error parsing arithmetic command");
            return;
        }

    }

    public void writePushPop(int command, String segment, int c_index) throws IOException {

        this.assemblyWriter
                .write("//" + Integer.toString(command) + " " + segment + " " + Integer.toString(c_index) + "\n");

        if (command == 1) {
            handlePush(segment, c_index);
        } else if (command == 2) {
            handlePop(segment, c_index);
        }
    }

    public void writeLabel(String segment) throws IOException {
        this.assemblyWriter.write("($null" + segment + ")\n");
    }

    public void writeIf(String segment) throws IOException {
        this.assemblyWriter.write("@SP\n" + "M=M-1\n" + "A=M\n" + "@null$" + segment + "\n" + "D;JME\n");
    }

    public void writeGo(String segment) throws IOException {
        this.assemblyWriter.write("@null$" + segment + "\n" + "0;JMP\n");
    }

    private String chooseSeg(String segment, int c_index) {
        String seg = "";

        switch (segment) {
        case ("local"):
            seg = "@LCL";
            break;

        case ("argument"):
            seg = "@ARG";
            break;

        case ("this"):
            seg = "@THIS";
            break;

        case ("that"):
            seg = "@THAT";
            break;

        case ("temp"):
            seg = "@5";
            return (seg + "\n" + "D=A\n" + "@" + Integer.toString(c_index) + "\n");

        default:
            return "";
        }
        return (seg + "\n" + "D=M\n" + "@" + Integer.toString(c_index) + "\n");
    }

    private String compCommands(String type) {
        String comparison = "\n";

        if (type.equals("eq")) {
            comparison = "D;JNE\n";
        } else if (type.equals("gt")) {
            comparison = "D;JLE\n";
        } else if (type.equals("lt")) {
            comparison = "D;JGE\n";
        }

        numJump++;

        return this.backSP() + "D=M\n" + backSP() + "D=M-D\n" + "@FALSE" + numJump + "\n" + comparison + "@SP\n"
                + "A=M\n" + "M=-1\n" + "@CONTINUE" + numJump + "\n" + "D;JMP\n" + "(FALSE" + numJump + ")\n" + "@SP\n"
                + "A=M\n" + "M=0\n" + "(CONTINUE" + numJump + ")\n" + advSP();
    }

    private String staticName(int index) {
        String name = this.assemblyTranslate.getName().replace(".asm", ("." + Integer.toString(index)));
        return "@" + name + "\n";
    }

    private String backSP() {
        return "@SP\n" + "AM=M-1\n";
    }

    private String advSP() {
        return "@SP\n" + "M=M+1\n";
    }

    private void handlePush(String segment, int c_index) throws IOException {
        // push constant
        if (segment.equals("constant")) {
            this.assemblyWriter
                    .write("@" + Integer.toString(c_index) + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());

            // push local, static, this, that
        } else if (!chooseSeg(segment, c_index).isBlank()) {
            this.assemblyWriter
                    .write(chooseSeg(segment, c_index) + "A=A+D\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());

            // push temp
        } else if (segment.equals("temp")) {
            this.assemblyWriter.write(
                    "@R" + Integer.toString(5 + c_index) + "\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());

            // push pointer
        } else if (segment.equals("pointer")) {
            this.assemblyWriter.write(
                    "@" + Integer.toString(3 + c_index) + "\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());

            // push static
        } else if (segment.equals("static")) {
            this.assemblyWriter.write(staticName(c_index) + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());

        } else {
            this.assemblyWriter.write("ERROR IN COMMAND PUSH");
        }
    }

    private void handlePop(String segment, int c_index) throws IOException {

        this.assemblyWriter.write(chooseSeg(segment, c_index) + "D=A+D\n" + "@R13\n" + "M=D\n" + backSP() + "A=M\n" + "D=A\n"
                + "@R13\n" + "A=M\n" + "M=D\n");

    }

    public void close() throws IOException {
        this.assemblyWriter.write("(END)\n");
        this.assemblyWriter.write("@END\n");
        this.assemblyWriter.write("0;JMP\n");
        this.assemblyWriter.close();
    }
}