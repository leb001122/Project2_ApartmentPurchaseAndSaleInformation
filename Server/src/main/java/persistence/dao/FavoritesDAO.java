package persistence.dao;

import dto.ApartTransactionDTO;
import dto.FavoritesDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

public class FavoritesDAO {

    private SqlSessionFactory sqlSessionFactory;

    public FavoritesDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public ArrayList<ApartTransactionDTO> selectAll(String memberId) throws Exception {
        SqlSession session = sqlSessionFactory.openSession();
        List<ApartTransactionDTO> list = session.selectList("mapper.favoritesMapper.selectAll", memberId);
        session.close();
        return (ArrayList<ApartTransactionDTO>) list;
    }

    public void create(FavoritesDTO dto) throws Exception {
        SqlSession session = null;

        try {
            session = sqlSessionFactory.openSession();
            session.insert("mapper.favoritesMapper.create", dto);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }

    public void delete(FavoritesDTO dto) throws Exception {
        SqlSession session = null;

        try {
            session = sqlSessionFactory.openSession();
            session.delete("mapper.favoritesMapper.delete", dto);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }
}
