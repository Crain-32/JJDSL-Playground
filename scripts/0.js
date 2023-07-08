function nextToken(lexer) {
    var data = lexer.lexerData();
    var token = lexerA(data);
    lexerE(data, lexerF);
    return token;
}

function lexerA(data) {
    var nextChar = data.input.charAt(data.position);
    if (nextChar === '=') {
        return new Token(TokenType.ASSIGN, nextChar);
    } else if (nextChar === ';') {
        return new Token(TokenType.SEMI, nextChar);
    } else if (nextChar === '(') {
        return new Token(TokenType.LPAREN, nextChar);
    } else if (nextChar === ')') {
        return new Token(TokenType.RPAREN, nextChar);
    } else if (nextChar === ',') {
        return new Token(TokenType.COMMA, nextChar);
    } else if (nextChar === '+') {
        return new Token(TokenType.PLUS, nextChar);
    } else if (nextChar === '{') {
        return new Token(TokenType.LSQIRLY, nextChar);
    } else if (nextChar === '}') {
        return new Token(TokenType.RSQIRLY, nextChar);
    } else {
        return lexerD(data);
    }
}

function lexerB(data) {
    var initialPos = data.position;
    var tempPosition = data.position;
    while (lexerC(data.input.charCodeAt(tempPosition))) {
        tempPosition += 1;
    }
    data.position = (tempPosition - 1);
    return data.input.substring(initialPos, tempPosition);
}

function lexerC(charVal) {
    if (charVal === undefined || charVal === null) {
        return false;
    }
    return ((0x41 <= charVal && charVal <= 0x5A) || (0x61 <= charVal && charVal <= 0x7A) || charVal === 0x5f);
}

function lexerD(data) {
    var nextCharCode = data.input.charCodeAt(data.position);
    if (lexerC(nextCharCode)) {
        var tokenLiteral = lexerB(data);
        if (!KeywordUtils.isKeyword(tokenLiteral)) {
            return new Token(TokenType.IDENT, tokenLiteral);
        } else {
            return new Token(KeywordUtils.getKeyword(tokenLiteral), tokenLiteral);
        }
    } else if (lexerG(data.input.charAt(data.position))) {
        var initialPosition = data.position;
        lexerE(data, lexerG);
        data.position -= 1;
        return new Token(TokenType.INT, data.input.substring(initialPosition, data.position + 1));
    } else if (data.input.length <= data.position) {
        return new Token(TokenType.EOF, "");
    } else {
        return null;
    }
}

function lexerE(data, lexerFunc) {
    if (data.position < data.input.length) {
        data.position += 1;
        while ((data.position < data.input.length) && lexerFunc(data.input.charAt(data.position))) {
            data.position += 1;
        }
    }
}

function lexerF(charLit) {
    if (charLit === undefined || charLit === null) {
        return false;
    }
    return (' ' === charLit || '\n' === charLit || '\t' === charLit || '\r' === charLit);
}

function lexerG(charLit) {
    if (charLit === undefined || charLit === null) {
        return false;
    }
    return ('0' === charLit || '1' === charLit || '2' === charLit || '3' === charLit || '4' === charLit || '5' === charLit || '6' === charLit || '7' === charLit || '8' === charLit || '9' === charLit);
}