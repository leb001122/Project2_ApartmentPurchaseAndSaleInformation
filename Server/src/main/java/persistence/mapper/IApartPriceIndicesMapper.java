package persistence.mapper;

import dto.ApartPriceIndicesDTO;
import org.apache.ibatis.annotations.*;
import persistence.sql.ApartPriceIndicesSql;

import java.util.ArrayList;
import java.util.List;

public interface IApartPriceIndicesMapper {

    @Results(id = "Results", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "date", column = "e_date"),
        @Result(property = "index", column = "jeonguk_indices"),
        @Result(property = "index", column = "seoul_indices"),
        @Result(property = "index", column = "busan_indices"),
        @Result(property = "index", column = "daegu_indices"),
        @Result(property = "index", column = "incheon_indices"),
        @Result(property = "index", column = "gwangju_indices"),
        @Result(property = "index", column = "daejeon_indices"),
        @Result(property = "index", column = "ulsan_indices"),
        @Result(property = "index", column = "sejong_indices"),
        @Result(property = "index", column = "Gyeonggi_indices"),
        @Result(property = "index", column = "Gangwon_indices"),
        @Result(property = "index", column = "Chungbuk_indices"),
        @Result(property = "index", column = "Chungnam_indices"),
        @Result(property = "index", column = "Jeonbuk_indices"),
        @Result(property = "index", column = "Jeonnam_indices"),
        @Result(property = "index", column = "Gyeongbuk_indices"),
        @Result(property = "index", column = "Gyeongnam_indices"),
        @Result(property = "index", column = "Jeju_indices")
    })

    // 아파트 매매 가격 지수 조회 (지역별 & 날짜(해당 날짜 이후)별)
    @SelectProvider(type = ApartPriceIndicesSql.class, method = "selectByRegion")
    ArrayList<ApartPriceIndicesDTO> selectByRegion(String region);
}
