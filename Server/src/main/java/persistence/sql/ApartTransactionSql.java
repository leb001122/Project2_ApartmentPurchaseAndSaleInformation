package persistence.sql;

import dto.ApartTransactionDTO;
import org.apache.ibatis.jdbc.SQL;

public class ApartTransactionSql {

    public static String selectByOption(ApartTransactionDTO dto) {
        return new SQL() {{
            // 가격, 층, 견축년도
            SELECT("*");
            FROM("apart_transaction");
            WHERE("si_gun_gu like CONCAT('%',#{siGunGu},'%')"); // 지역 옵션 필수
            if (dto.getTradeAmount() != 0) {        // 가격 옵션
                if (dto.getPriceOption() != null) {
                    if (dto.getPriceOption().equals("UP"))
                        WHERE("trade_amount >= #{tradeAmount}");
                    else
                        WHERE("trade_amount <= #{tradeAmount}");
                }
            }
            if (dto.getFloor() != 0) {              // 층 옵션
                if (dto.getFloorOption() != null) {
                    if (dto.getFloorOption().equals("UP"))
                        WHERE("floor >= #{floor}");
                    else
                        WHERE("floor <= #{floor}");
                }
            }
            if (dto.getBuildYear() != 0) {             // 건축년도 옵션
                if (dto.getBuildYearOption() != null) {
                    if (dto.getBuildYearOption().equals("UP"))
                        WHERE("build_year >= #{buildYear}");
                    else
                        WHERE("build_year <= #{buildYear}");
                }
            }
        }}.toString();
    }
}

