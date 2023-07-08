package monkey.interpreter;

import core.fields.interfaces.GetSetField;
import monkey.data.LexerData;
import monkey.functions.LexerFunctions;

public interface Lexer extends LexerFunctions, GetSetField {
    void lexerData(LexerData data);

    LexerData lexerData();
}
