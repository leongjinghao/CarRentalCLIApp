package com.carrentalproj.repository;

import com.carrentalproj.databaseconnection.DatabaseConnection;
import com.carrentalproj.entity.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemberRepositoryImpl implements MemberRepository {

    private final DatabaseConnection databaseConnection;

    private static final Lock lock = new ReentrantLock();

    private static MemberRepository instance;

    private MemberRepositoryImpl() {
        databaseConnection = DatabaseConnection.getInstance();
    }

    public static MemberRepository getInstance() {
        try {
            lock.lock();

            if (instance == null) {
                instance = new MemberRepositoryImpl();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    @Override
    public Member findById(int id) {
        Member member = null;

        try (Connection connection = databaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM member WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                member = new Member(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
            } else {
                throw new NoSuchElementException("No member with ID=" + id + " found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM member");

            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public int save(Member member) throws SQLException {
        int idOnCreate = 0;

        try {
            lock.lock();

            try (Connection connection = databaseConnection.getConnection()) {
                PreparedStatement preparedStatement;

                // Update
                try {
                    findById(member.getId());

                    preparedStatement = connection.prepareStatement("""
                            UPDATE member
                            SET firstName = ?, lastName = ?
                            WHERE id = %d
                            """.formatted(member.getId()));
                }
                // Create
                catch (NoSuchElementException e) {
                    preparedStatement = connection.prepareStatement("""
                            INSERT INTO member(firstName, lastName)
                            VALUES
                            (?, ?)
                            """);
                }

                if (preparedStatement != null) {
                    preparedStatement.setString(1, member.getFirstName());
                    preparedStatement.setString(2, member.getLastName());
                    preparedStatement.executeUpdate();

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
                    resultSet.next();
                    idOnCreate = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }

        return idOnCreate;
    }

    @Override
    public void delete(int id) {
        try {
            lock.lock();

            try (Connection connection = databaseConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM member WHERE id = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }
}
