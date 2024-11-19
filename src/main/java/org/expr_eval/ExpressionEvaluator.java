package org.expr_eval;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for evaluating mathematical expressions.
 * Supports basic arithmetic operations, functions, and variables.
 */
public class ExpressionEvaluator {
    private static final HashMap<String, Double> variableCache = new HashMap<>();

    /**
     * Evaluates a mathematical expression given as a string.
     *
     * @param expression string containing the mathematical expression
     * @return the result of the expression evaluation
     * @throws Exception if the expression contains a syntax error or an unexpected character
     */
    public static double evaluate(String expression) throws Exception {
        return new Object() {
            int pos = -1, ch;

            /**
             * Moves to the next character in the expression.
             */
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            /**
             * Checks if the current character is the specified character and moves to the next character if it is.
             *
             * @param charToEat character to check
             * @return true if the current character equals charToEat, otherwise false
             */
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            /**
             * Parses the expression and returns its value.
             *
             * @return the value of the expression
             * @throws Exception if the expression contains a syntax error or an unexpected character
             */
            double parse() throws Exception {
                nextChar();
                eat(' ');
                double x = parseExpression();
                if (pos < expression.length()) throw new Exception("Unexpected character: " + (char) ch);
                return x;
            }

            /**
             * Parses an expression consisting of additions and subtractions.
             *
             * @return the value of the expression
             * @throws Exception if the expression contains a syntax error
             */
            double parseExpression() throws Exception {
                eat(' ');
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) {
                        eat(' ');
                        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == ')') {
                            throw new Exception("Syntax error: unexpected operator");
                        }
                        x += parseTerm();
                    } else if (eat('-')) {
                        eat(' ');
                        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == ')') {
                            throw new Exception("Syntax error: unexpected operator");
                        }
                        x -= parseTerm();
                    } else {
                        return x;
                    }
                }
            }

            /**
             * Parses a term consisting of multiplications and divisions.
             *
             * @return the value of the term
             * @throws Exception if the expression contains a syntax error
             */
            double parseTerm() throws Exception {
                eat(' ');
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) {
                        eat(' ');
                        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == ')') {
                            throw new Exception("Syntax error: unexpected operator");
                        }
                        x *= parseFactor();
                    } else if (eat('/')) {
                        eat(' ');
                        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == ')') {
                            throw new Exception("Syntax error: unexpected operator");
                        }
                        x /= parseFactor();
                    } else {
                        return x;
                    }
                }
            }

            /**
             * Parses a factor, which can be a number, variable, function, or an expression in parentheses.
             *
             * @return the value of the factor
             * @throws Exception if the expression contains a syntax error
             */
            double parseFactor() throws Exception {
                double x = 0;
                eat(' ');
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                eat(' ');
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    eat(' ');
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expression.substring(startPos, this.pos);
                    if (func.equals("sqrt")) x = Math.sqrt(parseFactor());
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(parseFactor()));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(parseFactor()));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(parseFactor()));
                    else {
                        if (variableCache.containsKey(func)) {
                            x = variableCache.get(func);
                        } else {
                            System.out.println("Enter the value for variable " + func + ":");
                            x = Double.parseDouble(new Scanner(System.in).nextLine());
                            variableCache.put(func, x);
                        }
                    }
                }


                if (eat('^')) {
                    if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == ')') {
                        throw new Exception("Syntax error: unexpected operator");
                    }
                    x = Math.pow(x, parseFactor());
                }
                return x;
            }
        }.

                parse();
    }
}
