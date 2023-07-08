package monkey.data.constants;

import monkey.data.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TokenType {
    ILLEGAL,
    EOF,
    IDENT,
    INT,
    COMMA,
    SEMI,
    LPAREN,
    RPAREN,
    LSQIRLY,
    RSQIRLY,
    ASSIGN,
    PLUS,
    MINUS,
    BANG,
    ASTERISK,
    SLASH,
    LT,
    GT,
    EQUAL,
    NOT_EQUAL,
    FUNC,
    LET,
    TRUE,
    FALSE,
    IF,
    ELSE,
    RETURN;

    public static final List<Token> KEYWORD_TOKENS = Collections.unmodifiableList(Arrays.asList(new Token(TokenType.FUNC, "fn"), new Token(TokenType.LET, "let")));
}
