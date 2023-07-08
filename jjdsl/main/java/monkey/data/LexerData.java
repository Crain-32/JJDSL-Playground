package monkey.data;

public class LexerData {
    public String input;
    public int position;
    public int readPosition;

    public LexerData(String input) {
        this.input = input;
        this.position = 0;
        this.readPosition = 0;
    }
}
