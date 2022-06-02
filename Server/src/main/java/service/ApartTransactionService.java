package service;

import dto.ApartTransactionDTO;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dao.ApartTransactionDAO;

import java.util.ArrayList;
import java.util.List;

public class ApartTransactionService {

    private final ApartTransactionDAO apartTransactionDAO;

    public ApartTransactionService(SqlSessionFactory sqlSessionFactory) {
        apartTransactionDAO = new ApartTransactionDAO(sqlSessionFactory);
    }


    // 아파트 거래 정보 조회
    public ArrayList<ApartTransactionDTO> readApartTransaction(ApartTransactionDTO dto) {
        return apartTransactionDAO.selectByOption(dto);
    }
}
