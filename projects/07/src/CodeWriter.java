import java.io.*;
//import java.util.Scanner;

public class CodeWriter {

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
            this.assemblyWriter.write("@SP\n" + "AM=M-1\n" + "M=D\n" + "@SP\n" + "AM=M-1\n" + "M=M+D\n" + "@SP\n" + "M=M+1\n");
        }

        if (arg1.equals("sub")) {
            this.assemblyWriter.write("@SP\n" + "M=M-1\n" + "A=M\n" + "D=M\n" +
                    "@SP\n" + "M=M-1\n" + "A=M\n" + "D=D-M\n" + "@SP\n" + "A=M\n" + "M=D\n" +
                    "@SP\n" + "M=M+1\n");
        }

        if (arg1.equals("neg")) {

            this.assemblyWriter.write("@SP\n" + "M=M-1\n" + "A=M\n" + "D=M\n" + "D=-D\n" +
                     "@SP\n" + "A=M\n" + "M=D\n" +
                    "@SP\n" + "M=M+1\n");

        }

        if (arg1.equals("eq")) {
            this.assemblyWriter.write("@");
        }


    }

    public void writePushPop(int command, String segment, int c_index) throws IOException {
         if (command == 1) {
            if (segment.equals("constant")) {
                this.assemblyWriter.write("@" + Integer.toString(c_index) + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n");
            }
         }
    }

    public void close() throws IOException {
        this.assemblyWriter.write("(END)\n");
        this.assemblyWriter.write("@END\n");
        this.assemblyWriter.write("0;JMP\n");
        this.assemblyWriter.close();
    }
}