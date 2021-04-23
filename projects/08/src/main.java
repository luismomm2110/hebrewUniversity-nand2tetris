import java.io.IOException;

public class main {

    public static void main (String[] args) throws IOException {
        
        Parser parser = new Parser(args[0]);
        CodeWriter codeWriter = new CodeWriter(args[0]);

         while (parser.hasMoreCommands()) {
            parser.advance();

            switch (parser.commandType())  {
                
                case 0:
                codeWriter.writeArithmetic(parser.arg1());
                break;

                case 1, 2:
                codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                break;

                case 3:
                codeWriter.writeLabel(parser.arg1());
                break;
                
                case 4:
                codeWriter.writeGoTo(parser.arg1());

                case 5:
                codeWriter.writeIf(parser.arg1());
                break;

                case 6: 
                codeWriter.writeFunction(parser.arg1(), parser.arg2());
                break;

                case 7:
                codeWriter.writeReturn();
                break;

                default:
                System.out.println("TODO");
                return;
            }
        }
        codeWriter.close();
    }
}
