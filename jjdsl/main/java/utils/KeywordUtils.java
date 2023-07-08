package utils;

import monkey.data.Token;
import monkey.data.constants.TokenType;

public class KeywordUtils {
    private KeywordUtils() {
    }

    public static boolean isKeyword(String string) {
        return TokenType.KEYWORD_TOKENS.stream().map(Token::getLiteral).anyMatch(keyword -> keyword.equalsIgnoreCase(string));
    }

    public static TokenType getKeyword(String string) {
        return TokenType.KEYWORD_TOKENS.stream()
                .filter(token -> token.getLiteral().equalsIgnoreCase(string))
                .map(Token::getType)
                .findFirst()
                .orElse(TokenType.ILLEGAL);
    }
}
