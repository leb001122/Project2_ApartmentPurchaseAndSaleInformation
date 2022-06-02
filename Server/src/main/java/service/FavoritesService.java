package service;

import dto.ApartTransactionDTO;
import dto.FavoritesDTO;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dao.FavoritesDAO;

import java.util.ArrayList;
import java.util.List;

public class FavoritesService {

    private FavoritesDAO favoritesDAO;

    public FavoritesService(SqlSessionFactory sqlSessionFactory) {
        favoritesDAO = new FavoritesDAO(sqlSessionFactory);
    }

    public ArrayList<ApartTransactionDTO> selectAll(String memberId) throws Exception{
        return favoritesDAO.selectAll(memberId);
    }

    public void create(FavoritesDTO dto) throws Exception{
        favoritesDAO.create(dto);
    }


    public void delete(FavoritesDTO dto) throws Exception{
        favoritesDAO.delete(dto);
    }
}
