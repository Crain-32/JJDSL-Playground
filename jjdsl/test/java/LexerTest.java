import core.factory.ProxyFactory;
import core.source.constant.SourceCheckType;
import monkey.data.LexerData;
import monkey.data.Token;
import monkey.data.constants.TokenType;
import monkey.interpreter.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EngineCacheUtils;

import java.util.Arrays;
import java.util.List;

public class LexerTest {

    private static Lexer lexer;

    @BeforeAll
    static void loadFunctionsAndEngine() throws Exception {
        EngineCacheUtils.checkSourceAndCreateSetEngine(SourceCheckType.ASSUME_CACHE);
        // If the Proxy is performance issue, maybe could convert the Lexer into a Managed Proxy?
        // Not sure about the Performance/Threading considerations there, might be worth investigating.
        lexer = ProxyFactory.createUnmanagedProxy(Lexer.class);
    }

    @Test
    void itShould_LexBasicStuff() throws Exception {
        try {
            final String INITIAL_TEST = "=+(){},;";
            final LexerData data = new LexerData(INITIAL_TEST);
            lexer.lexerData(data);
            final List<Token> expectedResults = Arrays.asList(
                    new Token(TokenType.ASSIGN, "="),
                    new Token(TokenType.PLUS, "+"),
                    new Token(TokenType.LPAREN, "("),
                    new Token(TokenType.RPAREN, ")"),
                    new Token(TokenType.LSQIRLY, "{"),
                    new Token(TokenType.RSQIRLY, "}"),
                    new Token(TokenType.COMMA, ","),
                    new Token(TokenType.SEMI, ";")
            );
            for (Token token : expectedResults) {
                Token lexerToken = lexer.nextToken();
                System.out.println(lexerToken);
                Assertions.assertAll(
                        () -> Assertions.assertEquals(token.getLiteral(), lexerToken.getLiteral()),
                        () -> Assertions.assertEquals(token.getType(), lexerToken.getType())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    void itShould_CheckCharactersRight() {
        try {
            final String WHITESPACE_STRING = "= \n\t\r}";
            final LexerData lexerData = new LexerData(WHITESPACE_STRING);
            lexer.lexerData(lexerData);
            lexer.nextToken();
            Assertions.assertEquals(5, lexerData.position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void itShould_LexSimpleStatements() throws Exception {
        try {
            final String BASIC_INPUT = "" +
                    "let five = 5;" +
                    "let ten = 10;" +
                    "" +
                    "let add = fn(x, y) {" +
                    "x + y;" +
                    "};" +
                    "" +
                    "let result = add(five, ten);";
            final LexerData data = new LexerData(BASIC_INPUT);
            lexer.lexerData(data);
            final List<Token> expectedResults = Arrays.asList(
                    new Token(TokenType.LET, "let"),
                    new Token(TokenType.IDENT, "five"),
                    new Token(TokenType.ASSIGN, "="),
                    new Token(TokenType.INT, "5"),
                    new Token(TokenType.SEMI, ";"),
                    new Token(TokenType.LET, "let"),
                    new Token(TokenType.IDENT, "ten"),
                    new Token(TokenType.ASSIGN, "="),
                    new Token(TokenType.INT, "10"),
                    new Token(TokenType.SEMI, ";"),
                    new Token(TokenType.LET, "let"),
                    new Token(TokenType.IDENT, "add"),
                    new Token(TokenType.ASSIGN, "="),
                    new Token(TokenType.FUNC, "fn"),
                    new Token(TokenType.LPAREN, "("),
                    new Token(TokenType.IDENT, "x"),
                    new Token(TokenType.COMMA, ","),
                    new Token(TokenType.IDENT, "y"),
                    new Token(TokenType.RPAREN, ")"),
                    new Token(TokenType.LSQIRLY, "{"),
                    new Token(TokenType.IDENT, "x"),
                    new Token(TokenType.PLUS, "+"),
                    new Token(TokenType.IDENT, "y"),
                    new Token(TokenType.SEMI, ";"),
                    new Token(TokenType.RSQIRLY, "}"),
                    new Token(TokenType.SEMI, ";"),
                    new Token(TokenType.LET, "let"),
                    new Token(TokenType.IDENT, "result"),
                    new Token(TokenType.ASSIGN, "="),
                    new Token(TokenType.IDENT, "add"),
                    new Token(TokenType.LPAREN, "("),
                    new Token(TokenType.IDENT, "five"),
                    new Token(TokenType.COMMA, ","),
                    new Token(TokenType.IDENT, "ten"),
                    new Token(TokenType.RPAREN, ")"),
                    new Token(TokenType.SEMI, ";"),
                    new Token(TokenType.EOF, "")
            );
            for (Token token : expectedResults) {
                Token lexerToken = lexer.nextToken();
                System.out.println(lexerToken);
                Assertions.assertAll(
                        () -> Assertions.assertEquals(token.getLiteral(), lexerToken.getLiteral()),
                        () -> Assertions.assertEquals(token.getType(), lexerToken.getType())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
