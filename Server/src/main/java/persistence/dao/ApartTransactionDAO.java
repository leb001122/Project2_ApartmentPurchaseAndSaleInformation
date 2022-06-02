package persistence.dao;

import dto.ApartTransactionDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.IApartTransactionMapper;

import java.util.ArrayList;
import java.util.List;

public class ApartTransactionDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public ApartTransactionDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public ArrayList<ApartTransactionDTO> selectByOption(ApartTransactionDTO dto){

        SqlSession session = sqlSessionFactory.openSession();
        IApartTransactionMapper mapper = session.getMapper(IApartTransactionMapper.class);
        ArrayList<ApartTransactionDTO> list = mapper.selectByOption(dto);
        session.close();
        return list;

    }
}
