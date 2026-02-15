package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

/**
 * Stmt Grammar:
 *
 * program        → declaration* EOF ;
 * declaration    → varDecl
 *                | statement ;
 * varDecl        → "var" IDENTIFIER ( "=" expression )? ";" ;
 * statement      → exprStmt
 *                | printStmt ;
 * exprStmt       → expression ";" ;
 * printStmt      → "print" expression ";" ;
 *
 *
 *
 * Expression Grammar:
 *
 *
 * expression     → assignment ;
 * assignment     → IDENTIFIER "=" assignment
 *                | equality ;
 * equality       → comparison ( ( "!=" | "==" ) comparison )* ;
 * comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
 * term           → factor ( ( "-" | "+" ) factor )* ;
 * factor         → unary ( ( "/" | "*" ) unary )* ;
 * unary          → ( "!" | "-" ) unary
 *                | primary ;
 * primary        → NUMBER | STRING | "true" | "false" | "nil"
 *                | "(" expression ")" ;
 *                | IDENTIFIER ;
 *
 *
 *
 * see https://craftinginterpreters.com/parsing-expressions.html
 */
class Parser {

    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    private Expr expression() {
        return equality();
    }

    private Stmt declaration() {
        try {
            if (match(VAR)) return varDeclaration();

            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Stmt.Var(name, initializer);
    }

    private Stmt statement() {
        if (match(PRINT)) return printStatement();

        return expressionStatement();
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    /**
     * equality → comparison ( ( "!=" | "==" ) comparison )* ;
     */
    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
     */
    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * unary   → ( "!" | "-" ) unary
     *         | primary ;
     */
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    /**
     * primary  → NUMBER | STRING | "true" | "false" | "nil"
     *          | "(" expression ")" ;
     */
    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(IDENTIFIER)) {
            return new Expr.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    /**
     * The error() method returns the error instead of throwing it because we want to let the calling method inside the parser decide whether to unwind or not. Some parse errors occur in places where the parser isn’t likely to get into a weird state and we don’t need to synchronize. In those places, we simply report the error and keep on truckin’.
     */
    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    /**
     * To synchronize, means we want to discard tokens
     * until we’re right at the beginning of the next statement
     *
     * After catching a ParseError, we’ll call this and then we are hopefully back in sync
     */
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            // After a semicolon, we’re probably finished with a statement
            if (previous().type == SEMICOLON) return;

            // Most statements start with a keyword—for, if, return, var, etc. When the next token is any of those, we’re probably about to start a statement
            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }



    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    // returns the current token we have yet to consume
    private Token peek() {
        return tokens.get(current);
    }

    // returns the most recently consumed token
    private Token previous() {
        return tokens.get(current - 1);
    }


}
