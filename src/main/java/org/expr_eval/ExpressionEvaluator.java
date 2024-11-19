package org.expr_eval;

import java.util.HashMap;
import java.util.Scanner;

public class ExpressionEvaluator {
    private static final HashMap<String, Double> variableCache = new HashMap<>();

    public static double evaluate(String expression) throws Exception {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() throws Exception {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new Exception("Неожиданный символ: " + (char) ch);
                return x;
            }

            double parseExpression() throws Exception {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() throws Exception {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() throws Exception {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x = 0;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expression.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else {
                        if (variableCache.containsKey(func)) {
                            x = variableCache.get(func);
                        } else {
                            System.out.println("Введите значение для переменной " + func + ":");
                            x = Double.parseDouble(new Scanner(System.in).nextLine());
                            variableCache.put(func, x);
                        }
                    }
                }
                if (eat('^')) {
                    x = Math.pow(x, parseFactor());
                }
                return x;
            }
        }.parse();
    }
}