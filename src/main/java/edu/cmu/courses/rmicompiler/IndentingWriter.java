package edu.cmu.courses.rmicompiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Note: this file is stolen from sun.rmi.rmic.IdentingWriter
 *
 * IndentingWriter is a BufferedWriter subclass that supports automatic
 * indentation of lines of text written to the underlying Writer.
 *
 * Methods are provided for compact, convenient indenting, writing text,
 * and writing lines in various combinations.
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public class IndentingWriter extends BufferedWriter {

    /** true if the next character written is the first on a line */
    private boolean beginningOfLine = true;

    /** current number of spaces to prepend to lines */
    private int currentIndent = 0;

    /** number of spaces to change indent when indenting in or out */
    private int indentStep = 4;

    /** number of spaces to convert into tabs. Use MAX_VALUE to disable */
    private int tabSize = 8;

    /**
     * Create a new IndentingWriter that writes indented text to the
     * given Writer.  Use the default indent step of four spaces.
     */
    public IndentingWriter(Writer out) {
        super(out);
    }

    /**
     * Create a new IndentingWriter that writes indented text to the
     * given Writer and uses the supplied indent step.
     */
    public IndentingWriter(Writer out, int step) {
        this(out);

        if (indentStep < 0)
            throw new IllegalArgumentException("negative indent step");

        indentStep = step;
    }

    /**
     * Create a new IndentingWriter that writes indented text to the
     * given Writer and uses the supplied indent step and tab size.
     */
    public IndentingWriter(Writer out, int step, int tabSize) {
        this(out);

        if (indentStep < 0)
            throw new IllegalArgumentException("negative indent step");

        indentStep = step;
        this.tabSize = tabSize;
    }

    /**
     * Write a single character.
     */
    public void write(int c) throws IOException {
        checkWrite();
        super.write(c);
    }

    /**
     * Write a portion of an array of characters.
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len > 0) {
            checkWrite();
        }
        super.write(cbuf, off, len);
    }

    /**
     * Write a portion of a String.
     */
    public void write(String s, int off, int len) throws IOException {
        if (len > 0) {
            checkWrite();
        }
        super.write(s, off, len);
    }

    /**
     * Write a line separator.  The next character written will be
     * preceded by an indent.
     */
    public void newLine() throws IOException {
        super.newLine();
        beginningOfLine = true;
    }

    /**
     * Check if an indent needs to be written before writing the next
     * character.
     *
     * The indent generation is optimized (and made consistent with
     * certain coding conventions) by condensing groups of eight spaces
     * into tab characters.
     */
    protected void checkWrite() throws IOException {
        if (beginningOfLine) {
            beginningOfLine = false;
            int i = currentIndent;
            while (i >= tabSize) {
                super.write('\t');
                i -= tabSize;
            }
            while (i > 0) {
                super.write(' ');
                -- i;
            }
        }
    }

    /**
     * Increase the current indent by the indent step.
     */
    protected void indentIn() {
        currentIndent += indentStep;
    }

    /**
     * Decrease the current indent by the indent step.
     */
    protected void indentOut() {
        currentIndent -= indentStep;
        if (currentIndent < 0)
            currentIndent = 0;
    }

    /**
     * Indent in.
     */
    public void pI() {
        indentIn();
    }

    /**
     * Indent out.
     */
    public void pO() {
        indentOut();
    }

    /**
     * Write string.
     */
    public void p(String s) throws IOException {
        write(s);
    }

    /**
     * End current line.
     */
    public void pln() throws IOException {
        newLine();
    }

    /**
     * Write string; end current line.
     */
    public void pln(String s) throws IOException {
        p(s);
        pln();
    }

    /**
     * Write string; end current line; indent in.
     */
    public void plnI(String s) throws IOException {
        p(s);
        pln();
        pI();
    }

    /**
     * Indent out; write string.
     */
    public void pO(String s) throws IOException {
        pO();
        p(s);
    }

    /**
     * Indent out; write string; end current line.
     */
    public void pOln(String s) throws IOException {
        pO(s);
        pln();
    }

    /**
     * Indent out; write string; end current line; indent in.
     *
     * This method is useful for generating lines of code that both
     * end and begin nested blocks, like "} else {".
     */
    public void pOlnI(String s) throws IOException {
        pO(s);
        pln();
        pI();
    }

    /**
     * Write Object.
     */
    public void p(Object o) throws IOException {
        write(o.toString());
    }
    /**
     * Write Object; end current line.
     */
    public void pln(Object o) throws IOException {
        p(o.toString());
        pln();
    }

    /**
     * Write Object; end current line; indent in.
     */
    public void plnI(Object o) throws IOException {
        p(o.toString());
        pln();
        pI();
    }

    /**
     * Indent out; write Object.
     */
    public void pO(Object o) throws IOException {
        pO();
        p(o.toString());
    }

    /**
     * Indent out; write Object; end current line.
     */
    public void pOln(Object o) throws IOException {
        pO(o.toString());
        pln();
    }

    /**
     * Indent out; write Object; end current line; indent in.
     *
     * This method is useful for generating lines of code that both
     * end and begin nested blocks, like "} else {".
     */
    public void pOlnI(Object o) throws IOException {
        pO(o.toString());
        pln();
        pI();
    }

}

