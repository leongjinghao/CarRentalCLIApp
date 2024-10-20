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

    Connection connection;

    private static final Lock lock = new ReentrantLock();

    private static MemberRepository instance;

    private MemberRepositoryImpl() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
    }

    public static MemberRepository getInstance() {
        lock.lock();

        if (instance == null) {
            instance = new MemberRepositoryImpl();
        }

        lock.unlock();

        return instance;
    }

    @Override
    public Member findById(int id) {
        Member member = null;

        try {
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

        try {
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
        lock.lock();

        int id = member.getId();
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
            id = resultSet.getInt(1);
        }

        lock.unlock();

        return id;
    }

    @Override
    public void delete(int id) {
        lock.lock();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM member WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lock.unlock();
    }
}
