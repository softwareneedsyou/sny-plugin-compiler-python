package fr.sny.plugins.compile.python;
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;

import fr.sny.plugins.compile.python.CompilerExDeclare;

import static org.junit.Assert.*;

import java.util.UUID;

public class LibraryTest {
    @Test public void testSomeLibraryMethod() {
        CompilerExDeclare classUnderTest = new CompilerExDeclare();
        assertTrue("yours tests here", UUID.fromString("00000000-0000-0000-0000-000000000000").compareTo(classUnderTest.getID()) == 0);
    }
}