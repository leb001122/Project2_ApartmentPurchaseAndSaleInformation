package persistence.mapper;

import dto.ApartTransactionDTO;
import org.apache.ibatis.annotations.*;
import persistence.sql.ApartTransactionSql;

import java.util.ArrayList;

public interface IApartTransactionMapper {

    @Results(id = "Results", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "siGunGu", column = "si_gun_gu"),
            @Result(property = "roadName", column = "road_name"),
            @Result(property = "danjiName", column = "danji_name"),
            @Result(property = "area", column = "area"),
            @Result(property = "contract_y_m", column = "contract_y_m"),
            @Result(property = "tradeAmount", column = "trade_amount"),
            @Result(property = "buildYear", column = "build_year"),
            @Result(property = "floor", column = "floor")
    })

    // 아파트 거래정보 조회
    @SelectProvider(type = ApartTransactionSql.class, method = "selectByOption")
    ArrayList<ApartTransactionDTO> selectByOption(ApartTransactionDTO dto);
}
