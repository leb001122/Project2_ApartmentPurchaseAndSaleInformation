package persistence.sql;

import dto.UserDTO;
import org.apache.ibatis.jdbc.SQL;

public class UserSql {

    public static String insertUser(UserDTO user) {
        return new SQL() {{
            INSERT_INTO("user");
            VALUES("user_id", "#{id}");
            VALUES("user_password", "#{password}");
            VALUES("user_name", "#{name}");
            VALUES("user_email", "#{email}");
        }}.toString();
    }

    // 사용자 정보 수정
    public static String updateUser(UserDTO user) {
        return new SQL() {{
            UPDATE("user");
            // 아이디는 변경 불가
            if (user.getPassword() != null)
                SET("user_password = #{password}");
            if (user.getName() != null)
                SET("user_name = #{name}");
            if (user.getEmail() != null)
                SET("user_email = #{email}");
            WHERE("user_id = #{id}");
        }}.toString();
    }

    // 삭제
    public static String deleteUser(String id) {
        return new SQL() {{
            DELETE_FROM("user");
            WHERE("user_id = #{id}");
        }}.toString();
    }
}
