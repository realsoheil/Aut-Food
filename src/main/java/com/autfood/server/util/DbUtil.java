package com.autfood.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * تنها نقطهٔ ایجاد Connection به SQLite.
 * مسیر فایل: ./data/data.db   (اگر نبود ایجاد می‌شود)
 */
public final class DbUtil {

    private static final String URL = "jdbc:sqlite:data/data.db";
    private static Connection connection;

    /** برگرداندن اتصال بازِ singleton */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    /** ساخت جدول‌های اولیه در اولین اجرا */
    public static void initSchema() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT
                UNIQUE NOT NULL,
                            email TEXT UNIQUE NO
                       password_hash TEXT NOT
                NULL,
                            salt TEXT NOT NULL,
            role TEXT NOT NULL
            
            """;
        try (Statement st = getConnection().createStatement()) {
            st.execute(sql);
        }
        String sql2 = """
 CREATE TABLE IF NOT EXISTS restaurants(
   id          INTEGER PRIMARY KEY AUTOINCREMENT,
   owner_id    INTEGER NOT NULL,
   name        TEXT    NOT NULL,
   status      TEXT    NOT NULL,
   description TEXT,
   FOREIGN KEY(owner_id) REFERENCES users(id)
 );
""";
        try (Statement st = getConnection().createStatement()) {
            st.execute(sql);
            st.execute(sql2);
        }
        String sql3 = """
CREATE TABLE IF NOT EXISTS food_items(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  restaurant_id INTEGER NOT NULL,
  name TEXT NOT NULL,
  price REAL NOT NULL,
  discount REAL DEFAULT 0,
  status TEXT NOT NULL,
  image_path TEXT,
  FOREIGN KEY(restaurant_id) REFERENCES restaurants(id)
);
""";
        st.execute(sql3);

    }

    private DbUtil() { }
}
