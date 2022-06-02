package persistence.sql;

import org.apache.ibatis.jdbc.SQL;

public class ApartPriceIndicesSql {


    public static String selectByRegion(String region) {
        return new SQL() {{
            SELECT("e_date," + region);
            FROM("Apart_price_indices");
        }}.toString();
    }

}
