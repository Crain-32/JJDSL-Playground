package monkey.data;

import monkey.data.constants.TokenType;

public class Token {

    private String literal;
    private TokenType type;

    public Token(TokenType type, String literal) {
        this.literal = literal;
        this.type = type;
    }

    public Token() {
    }

    public String getLiteral() {
        return literal;
    }

    public TokenType getType() {
        return type;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public void setType(TokenType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return String.format("Type: %s \nLiteral: %s", this.type, this.literal);
    }
}
