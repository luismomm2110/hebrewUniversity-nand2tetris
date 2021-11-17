import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser(args[0]);
        CodeWriter codeWriter = new CodeWriter(args[0]);

        while (parser.hasMoreCommands()) {
            parser.advance();
            
            switch (parser.commandType()) {

            case 0:
                codeWriter.writeArithmetic(parser.arg1());
                break;

            case 1:
            case 2:
                System.out.println(parser.commandType() + parser.arg1() + parser.arg2());
                codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                break;

            case 3:
                System.out.println(parser.arg1());
                codeWriter.writeLabel(parser.arg1());
                break;

            case 4: 
                System.out.println(parser.arg1());
                codeWriter.writeGo(parser.arg1());
                break; 

            case 5: 
                System.out.println(parser.arg1());
                codeWriter.writeIf(parser.arg1());
                break; 

            default:
                System.out.println("TODO");
                return;
            }
        }
        codeWriter.close();
    }
}
