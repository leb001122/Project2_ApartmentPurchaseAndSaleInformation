package persistence.dao;


import dto.ApartPriceIndicesDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.IApartPriceIndicesMapper;

import java.util.ArrayList;
import java.util.List;

public class ApartPriceIndicesDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public ApartPriceIndicesDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    // 아파트 매매 가격 지수 조회 (지역별 & 날짜(해당 날짜 이후)별)
    public ArrayList<ApartPriceIndicesDTO> selectByRegion(String region) {
        SqlSession session = sqlSessionFactory.openSession();
        IApartPriceIndicesMapper mapper = session.getMapper(IApartPriceIndicesMapper.class);
        try {
            ArrayList<ApartPriceIndicesDTO> dtoList = mapper.selectByRegion(region);
            session.close();
            return dtoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
