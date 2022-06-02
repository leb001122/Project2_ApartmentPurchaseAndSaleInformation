package persistence.dao;

import dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.domain.User;
import persistence.mapper.IUserMapper;

import java.util.List;

public class UserDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public UserDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public UserDTO selectUserByIdAndPw(UserDTO userDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        IUserMapper mapper = session.getMapper(IUserMapper.class);
        UserDTO user = mapper.selectUserByIdAndPw(userDTO);
        session.close();
        return user;
    }

    public UserDTO selectUserById(String id) {
        SqlSession session = sqlSessionFactory.openSession();
        IUserMapper mapper = session.getMapper(IUserMapper.class);
        UserDTO user = mapper.selectUserById(id);
        session.close();
        return user;
    }

    public void create(UserDTO dto) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            mapper.insertUser(dto);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public void updateUser(UserDTO userDTO) throws Exception{
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            mapper.updateUser(userDTO);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteUser(String id) throws Exception{
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            mapper.deleteUser(id);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
