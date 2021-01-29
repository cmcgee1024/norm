/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cmcgeemac.norm;

import org.junit.Test;


public class TestNormStatement {

    @Test
    public void testInlineConstruction() throws Exception {
        class p implements Parameters {
            int bar = 1;
            int newBaz = 100;
        }

        if (new @SQL("UPDATE foo SET bar = :newBar WHERE foo.bar = :bar;") NormStatement<p>() {
        }.executeUpdate(null) != 1) {
            System.out.println("Incorrect number of rows updated. Please try again.");
        }
    }

    @Test
    public void testInlineConstructionWithMultiLineStatement() throws Exception {
        class p implements Parameters {

            int baz;
        }

        if (new @SQL(""
                + "UPDATE foo "
                + "SET bar = :newBar "
                + "WHERE foo.bar = :bar;") NormStatement<p>() {
        }.executeUpdate(null) != 1) {
            System.out.println("Incorrect number of rows updated. Please try again.");
        }

    }

    private static class UpdateStatementParameters implements Parameters {

        int baz;
    }

    private static final NormStatement<UpdateStatementParameters> UPDATE_STATEMENT = new @SQL(
            "SELECT foo "
            + "FROM bar "
            + "WHERE bar.baz = :baz;") NormStatement<UpdateStatementParameters>() {
    };

    @Test
    public void testReusableStatement() throws Exception {
        UPDATE_STATEMENT.executeUpdate(null);
    }

}