package com.projekt.dbs.tools;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;

public class MyTrigger implements Trigger {
    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) throws SQLException {

    }

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        Trigger.super.init(connection, s, s1, s2, b, i);
    }

    @Override
    public void close() throws SQLException {
        Trigger.super.close();
    }

    @Override
    public void remove() throws SQLException {
        Trigger.super.remove();
    }
}
