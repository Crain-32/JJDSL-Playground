package monkey.functions;

import core.annnotation.Source;
import core.engine.interfaces.EngineBacked;
import monkey.data.Token;

public interface LexerFunctions extends EngineBacked {

    @Source(value = "74d5fe2c7a857a07ef597f5f01364d3049999a74", order = 2)
    Token nextToken();
}
