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
        try {
            this.assemblyWriter = new FileWriter(this.assemblyTranslate);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void writeArithmetic(String arg1) throws IOException {

        this.assemblyWriter.write("//" +  arg1 + "\n");
        if (arg1.equals("add")) {
            this.assemblyWriter.write(backSP() + "M=D\n" + backSP() + "M=M+D\n" + advSP());
        }

        if (arg1.equals("sub")) {
            this.assemblyWriter.write(backSP() + "D=M\n" + backSP() + "M=M-D\n" + advSP());
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

        this.assemblyWriter.write("//" + Integer.toString(command) + " " + segment + " " + Integer.toString(c_index) + "\n");

        if (command == 1) {
             //push constant
            if  (segment.equals("constant")) {
                this.assemblyWriter.write("@" + Integer.toString(c_index) + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());
            //push local, static, this, that
            } else if (!chooseSeg(segment, c_index).isBlank()) {
                this.assemblyWriter.write(chooseSeg(segment, c_index) + "A=A+D\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());
            //push temp
            } else if (segment.equals("temp")) {
                this.assemblyWriter.write("@R" + Integer.toString(5 + c_index) + "\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" +  advSP());
            } else if (segment.equals("pointer")) {
                this.assemblyWriter.write("@" + Integer.toString(3 + c_index) + "\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + advSP());
            } else {
                this.assemblyWriter.write("ERROR IN COMMAND PUSH");
            }
         } else if (command == 2) {
                //pop temp
                if (segment.equals("temp")) {
                    this.assemblyWriter.write(backSP() + "D=M\n" + "@R" + Integer.toString(5 + c_index) + "\n" + "M=D\n");
                } //pop local, static, this, that
                 else if (!chooseSeg(segment, c_index).isBlank()) {
                    this.assemblyWriter.write(chooseSeg(segment, c_index) + "D=A+D\n" + "@adress\n" + "M=D\n" + backSP() + "D=M\n" +
                    "@adress\n" + "A=M\n" + "M=D\n");
                //pop pointer
                } else if (segment.equals("pointer")) {
                    this.assemblyWriter.write(backSP() + "D=M\n" + "@" + Integer.toString(3 + c_index) + "\n" + "M=D\n");
                } else {
                    this.assemblyWriter.write("ERROR IN COMMAND POP");
                }
         }
    }

    private String chooseSeg(String segment, int c_index) {
        String seg = "";
        
        switch(segment) {
            case ("local"):
            seg = "@LCL";
            break;

            case("argument"):
            seg =  "@ARG";
            break;

            case("this"):
            seg = "@THIS";
            break;

            case("that"):
            seg = "@THAT";
            break;

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