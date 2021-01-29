/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cmcgeemac.norm;

import java.util.stream.StreamSupport;
import org.junit.Assert;
import org.junit.Test;


public class TestNormStatementWithResult {

    @Test
    public void testInlineConstruction() throws Exception {
        class p implements Parameters {

            int baz = 100;
        }

        class r implements Result {

            int foo;
        }

        try (CloseableIterable<r> rs
        = new @SQL("SELECT foo "
                + "FROM bar WHERE "
                + "bar.baz = :baz;") NormStatementWithResult<p, r>() {
                }.execute(null)) {
            for (r r : rs) {
                System.out.println(r.foo);
            }
        }

    }

    @Test
    public void testInlineConstructionWithMultiLineStatement() throws Exception {
        class p implements Parameters {

            int baz;
        }

        class r implements Result {

            int foo;
        }

        try (CloseableIterable<r> rs = new @SQL(
                "SELECT foo "
                + "FROM bar "
                + "WHERE bar.baz = :baz;") NormStatementWithResult<p, r>() {
        }.execute(null)) {
            for (r r : rs) {
                System.out.println(r.foo);
            }
        }

    }

    @Test
    public void testStreaming() throws Exception {
        class p implements Parameters {

            int baz;
        }

        class r implements Result {

            int foo;
        }

        Integer foo;

        try (CloseableIterable<r> rs = new @SQL(
                "SELECT foo "
                + "FROM bar "
                + "WHERE bar.baz = :baz;") NormStatementWithResult<p, r>() {
        }.execute(null)) {
            foo = StreamSupport.stream(rs
                    .spliterator(), false)
                    .map(l -> l.foo)
                    .findFirst()
                    .get();
        }

        Assert.assertNotNull(foo);
    }

    private static class QueryParameters implements Parameters {

        int baz;
    }

    private static class QueryResult implements Result {

        int foo;
    }

    private static final NormStatementWithResult<QueryParameters, QueryResult> QUERY = new @SQL(
            "SELECT foo "
            + "FROM bar "
            + "WHERE bar.baz = :baz;") NormStatementWithResult<QueryParameters, QueryResult>() {
    };

    @Test
    public void testReusableStatement() throws Exception {
        try ( CloseableIterable<QueryResult> rs = QUERY.execute(null)) {
            rs.forEach(r -> System.out.println(r.foo));
        }
    }

}