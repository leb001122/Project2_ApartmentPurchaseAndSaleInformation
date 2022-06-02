package persistence.mapper;

import dto.UserDTO;
import org.apache.ibatis.annotations.*;
import persistence.sql.UserSql;

import java.util.List;

public interface IUserMapper {

    @Results(id = "userResultSet", value = {
            @Result(property = "id", column = "user_id"),
            @Result(property = "password", column = "user_password"),
            @Result(property = "name", column = "user_name"),
            @Result(property = "email", column = "user_email"),
    })

    @Select("SELECT user_id, user_name, user_email FROM USER where user_id = #{id} and user_password = #{password}")
    UserDTO selectUserByIdAndPw(UserDTO userDTO);

    @Select("SELECT * FROM USER WHERE USER_ID = #{id}")
    @ResultMap("userResultSet")
    UserDTO selectUserById(@Param("id") String id) ;

    @InsertProvider(type = UserSql.class, method = "insertUser")
    void insertUser(UserDTO user);

    @UpdateProvider(type = UserSql.class, method = "updateUser")
    void updateUser(UserDTO user);

    @DeleteProvider(type = UserSql.class, method = "deleteUser")
    void deleteUser(String id);

}
