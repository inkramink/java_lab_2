import org.expr_eval.ExpressionEvaluator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ExpressionEvaluatorTest {

    @Test
    public void testSimpleArithmetic() throws Exception {
        assertEquals(7.0, ExpressionEvaluator.evaluate("3 + 4"), 0.0001);
        assertEquals(1.0, ExpressionEvaluator.evaluate("3 - 2"), 0.0001);
        assertEquals(12.0, ExpressionEvaluator.evaluate("3 * 4"), 0.0001);
        assertEquals(2.0, ExpressionEvaluator.evaluate("8 / 4"), 0.0001);
        assertEquals(8.0, ExpressionEvaluator.evaluate("2 ^ 3"), 0.0001);
    }

    @Test
    public void testFunctions() throws Exception {
        assertEquals(2.0, ExpressionEvaluator.evaluate("sqrt(4)"), 0.0001);
        assertEquals(Math.sin(Math.toRadians(30)), ExpressionEvaluator.evaluate("sin(30)"), 0.0001);
        assertEquals(Math.cos(Math.toRadians(60)), ExpressionEvaluator.evaluate("cos(60)"), 0.0001);
        assertEquals(Math.tan(Math.toRadians(45)), ExpressionEvaluator.evaluate("tan(45)"), 0.0001);
    }

    @Test
    public void testVariables() throws Exception {
        String input = "5";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        try {
            assertEquals(5.0, ExpressionEvaluator.evaluate("a"), 0.0001);
            assertEquals(10.0, ExpressionEvaluator.evaluate("a + 5"), 0.0001);
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testSyntaxError() {
        Exception exception = assertThrows(Exception.class, () -> {
            ExpressionEvaluator.evaluate("3 + * 4");
        });
        assertEquals("Syntax error: unexpected operator", exception.getMessage());
    }

    @Test
    public void testUnexpectedCharacter() {
        Exception exception = assertThrows(Exception.class, () -> {
            ExpressionEvaluator.evaluate("3 + 4 abc");
        });
        assertEquals("Unexpected character: a", exception.getMessage());
    }
    @Test
    public void testComplexExpressions() throws Exception {
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream("5".getBytes()));
        System.setIn(new ByteArrayInputStream("16".getBytes()));

        try {
            assertEquals(5.0, ExpressionEvaluator.evaluate("a"), 0.0001);
            assertEquals(16.0, ExpressionEvaluator.evaluate("b"), 0.0001);
            assertEquals(21.0, ExpressionEvaluator.evaluate("b+a*sqrt(b)/4"), 0.0001);
            assertEquals(6.0, ExpressionEvaluator.evaluate("2+2*2"), 0.0001);
        } finally {
            System.setIn(originalIn);
        }
    }
}