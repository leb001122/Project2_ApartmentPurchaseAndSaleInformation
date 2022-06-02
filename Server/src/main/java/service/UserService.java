package service;

import dto.UserDTO;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dao.UserDAO;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;


    public UserService(SqlSessionFactory sqlSessionFactory) {
        this.userDAO = new UserDAO(sqlSessionFactory);
    }

    // 로그인
    public UserDTO login(UserDTO dto) {
        UserDTO user = userDAO.selectUserByIdAndPw(dto);
        return user;
    }


    public UserDTO retrieveUser(String id) {
        UserDTO user = userDAO.selectUserById(id);
        return user;
    }

    public void createUser(UserDTO dto) throws Exception {
        userDAO.create(dto);
    }


    // 회원정보 수정
    public void updateUser(UserDTO dto) throws Exception {
        userDAO.updateUser(dto);
    }

    // 회원탈퇴
    public void deleteUser(String id) throws Exception {
        userDAO.deleteUser(id);
    }
}

