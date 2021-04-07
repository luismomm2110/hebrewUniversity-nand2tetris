import java.io.*;

public class CodeWriter {

    Integer numJump = 0;
    String pathAssembly;
    File assemblyTranslate;
    FileWriter assemblyWriter;

    public CodeWriter(String pathAssembly) throws IOException {

        this.pathAssembly = pathAssembly;
        this.assemblyTranslate = new File(pathAssembly.replace(".vm", ".asm"));
        try {
            this.assemblyWriter = new FileWriter(this.assemblyTranslate);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void writeArithmetic(String arg1) throws IOException {

        if (arg1.equals("add")) {
            this.assemblyWriter.write(backSP() + "M=D\n" + backSP() + "M=M+D\n" + advSP());
        }

        if (arg1.equals("sub")) {
            this.assemblyWriter.write(backSP() + "M=D\n" + backSP() + "M=M-D\n" + advSP());
        }

        if (arg1.equals("neg")) {
            this.assemblyWriter.write(backSP() + "M=-M\n" + advSP());
        }

        if (arg1.equals("eq")) {
            this.assemblyWriter.write(compCommands("eq"));
        }

        if (arg1.equals("gt")) {
            this.assemblyWriter.write(compCommands("gt"));
        }   
        
        if (arg1.equals("lt")) {
            this.assemblyWriter.write(compCommands("lt"));
        }

        if (arg1.equals("and")) {
            this.assemblyWriter.write(backSP() +"D=M\n" + backSP() + "M=M&D\n" + advSP());
        }

        if (arg1.equals("or")) {
            this.assemblyWriter.write(backSP() +"D=M\n" + backSP() + "M=M|D\n" + advSP());
        }
        
        if (arg1.equals("not")) {
            this.assemblyWriter.write(backSP() +"M=!M\n" + advSP());
        }

    }

    public void writePushPop(int command, String segment, int c_index) throws IOException {
         if (command == 1) {
            if (segment.equals("constant")) {
                this.assemblyWriter.write("@" + Integer.toString(c_index) + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n");
            }
         }
    }

    private String compCommands(String type) {
        String comparison = "\n";

        if (type.equals("eq")) {
            comparison = "D;JNE\n";
        } else if (type.equals("gt")) {
            comparison  = "D;JLE\n";
        } else if (type.equals("lt")) {
            comparison = "D;JGE\n";
        }

        numJump++;

        return this.backSP() + "D=M\n" + backSP() + "D=M-D\n" + "@FALSE" + numJump + "\n" + comparison + "@SP\n" + "A=M\n" + "M=-1\n" + "@CONTINUE" + numJump
         + "\n" + "D;JMP\n" + "(FALSE" + numJump + ")\n" + "@SP\n" + "A=M\n" + "M=0\n" + "(CONTINUE" + numJump + ")\n" + advSP();
    }

    private String backSP () {
        return "@SP\n" + "AM=M-1\n";
    }

    private String advSP() {
        return "@SP\n" + "M=M+1\n";
    }

    public void close() throws IOException {
        this.assemblyWriter.write("(END)\n");
        this.assemblyWriter.write("@END\n");
        this.assemblyWriter.write("0;JMP\n");
        this.assemblyWriter.close();
    }
}