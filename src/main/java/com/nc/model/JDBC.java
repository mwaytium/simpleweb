package com.nc.model;

import com.nc.entity.UserEntity;
import com.nc.entity.UserGroupEntity;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC {
    private DataSource ds  = null;
    private Connection con = null;

    public JDBC() {
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/OracleDS");
            if (ds != null) {
                con = ds.getConnection();
            }
            Statement statement = con.createStatement();
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_SEQUENCES where sequence_name = 'USERGROUP_ID_SEQ'; if c = 0 then execute immediate 'CREATE SEQUENCE USERGROUP_ID_SEQ'; end if; end;");
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_SEQUENCES where sequence_name = 'USERS_ID_SEQ'; if c = 0 then execute immediate 'CREATE SEQUENCE USERS_ID_SEQ'; end if; end;");
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_TABLES where table_name='USERGROUP'; if c = 0 then execute immediate 'CREATE TABLE usergroup ( id NUMBER(10) NOT NULL, name VARCHAR2(100) NOT NULL, CONSTRAINT usergroup_pk PRIMARY KEY (id))'; end if; end;");
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_TABLES where table_name='USERS'; if c = 0 then execute immediate 'CREATE TABLE users ( id NUMBER(10) NOT NULL, first_name VARCHAR2(100), last_name VARCHAR2(100), usergroup_id NUMBER(10), CONSTRAINT user_pk PRIMARY KEY (id), CONSTRAINT fk_usergroup FOREIGN KEY (usergroup_id) REFERENCES usergroup(id))'; end if; end;");
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public void addUser(UserEntity user) {
        try (final PreparedStatement statement = this.con.prepareStatement(
                "insert into users(id, first_name, last_name, usergroup_id) values " +
                        "(USERS_ID_SEQ.nextval, (?), (?), (?))")) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, String.valueOf(user.getUserGroupId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGroup(UserGroupEntity group) {
        try (final PreparedStatement statement = this.con.prepareStatement(
                "insert into usergroup(id, name) values " +
                        "(USERGROUP_ID_SEQ.nextval, (?))")) {
            statement.setString(1, group.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(int id) {
        try (final PreparedStatement statement = this.con.prepareStatement(
                "delete from users where id = (?)")) {
            statement.setString(1, String.valueOf(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeGroup(int id) {
        try (final PreparedStatement statement = this.con.prepareStatement(
                "begin " +
                        "delete from users where usergroup_id = (?); " +
                        "delete from usergroup where id = (?); " +
                        "commit; " +
                        "end;")) {
            statement.setString(1, String.valueOf(id));
            statement.setString(2, String.valueOf(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(UserGroupEntity group) {
        try (final PreparedStatement statement = this.con.prepareStatement (
                "update usergroup set name = (?) where id = (?)")) {
            statement.setString(1, group.getName());
            statement.setString(2, String.valueOf(group.getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(UserEntity user) {
        try (final PreparedStatement statement = this.con.prepareStatement (
                "update users set first_name = (?), last_name = (?), usergroup_id = (?)  where id = (?)")) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, String.valueOf(user.getUserGroupId()));
            statement.setString(4, String.valueOf(user.getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserGroupEntity> findAllGroups() {
        final List<UserGroupEntity> groups = new ArrayList<>();
        try (final Statement statement = this.con.createStatement();
             final ResultSet rs = statement.executeQuery(
                     "select u.id, u.first_name, u.last_name, ug.id as usergroup_id, ug.name from usergroup ug " +
                     "left join users u on (u.usergroup_id = ug.id) " +
                     "order by usergroup_id")) {
            while (rs.next()) {
                if (rs.getInt("id") == 0){
                    UserGroupEntity group = new UserGroupEntity(
                            rs.getInt("usergroup_id"),
                            rs.getString("name")
                    );
                    groups.add(group);
                } else if ((groups.size() != 0) &&
                        (rs.getInt("usergroup_id") == groups.get(groups.size() - 1).getId())) {
                    groups.get(groups.size() - 1).getUsers().add(new UserEntity(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("usergroup_id")
                    ));
                } else {
                    UserGroupEntity group = new UserGroupEntity(
                            rs.getInt("usergroup_id"),
                            rs.getString("name"),
                            new UserEntity(
                                    rs.getInt("id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getInt("usergroup_id")
                            )
                    );
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public UserGroupEntity findGroupById(int id) {
        final UserGroupEntity group = new UserGroupEntity();
        try (final PreparedStatement statement = this.con.prepareStatement (
                "select u.id, u.first_name, u.last_name, ug.id as usergroup_id, ug.name from usergroup ug " +
                        "left join users u on (u.usergroup_id = ug.id) " +
                        "where ug.id = (?)")) {
            statement.setString(1, String.valueOf(id));

            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    if (rs.isFirst()) {
                        group.setId(rs.getInt("usergroup_id"));
                        group.setName(rs.getString("name"));
                        group.getUsers().add(new UserEntity(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getInt("usergroup_id")
                        ));
                    } else {
                        group.getUsers().add(new UserEntity(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getInt("usergroup_id")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public List<UserEntity> findAllUsers() {
        final List<UserEntity> users = new ArrayList<>();
        try (final Statement statement = this.con.createStatement();
             final ResultSet rs = statement.executeQuery("select * from users")) {
            while (rs.next()) {
                users.add(new UserEntity(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("usergroup_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public UserEntity findUserById(int id) {
        final UserEntity user = new UserEntity();
        try (final PreparedStatement statement = this.con.prepareStatement (
                "select * from users where id = (?)")) {
            statement.setString(1, String.valueOf(id));

            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUserGroupId(rs.getInt("usergroup_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UserGroupEntity> findByUserAndGroup(String gn, String fn, String ln) {
        gn = (gn.equals("")) ? "%" : gn;
        fn = (fn.equals("")) ? "%" : fn;
        ln = (ln.equals("")) ? "%" : ln;
        Integer id;
        Integer usergroupId;
        String firstName;
        String lastName;
        String groupName;
        List<UserGroupEntity> groups = new ArrayList<>();
        boolean flag;

        try (final PreparedStatement statement = this.con.prepareStatement(
                "select id, first_name, last_name, usergroup_id, (select name " +
                        "from usergroup " +
                        "where u.usergroup_id = id) as group_name " +
                        "from users u " +
                        "where usergroup_id in (select id " +
                        "from usergroup ug " +
                        "where ug.name like (?)) and u.first_name like (?) and u.last_name like (?)"
        )
        ) {
            statement.setString(1, gn);
            statement.setString(2, fn);
            statement.setString(3, ln);
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    flag = false;
                    id = rs.getInt("id");
                    usergroupId = rs.getInt("usergroup_id");
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                    groupName = rs.getString("group_name");

                    if (groups.size() != 0) {
                        for (UserGroupEntity group : groups) {
                            if (group.getId() == usergroupId) {
                                group.getUsers().add(new UserEntity(id, firstName, lastName, usergroupId));
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (groups.size() == 0 || !flag) {
                        groups.add(new UserGroupEntity(
                                        usergroupId,
                                        groupName,
                                        new UserEntity(id, firstName, lastName, usergroupId)
                                )
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}
