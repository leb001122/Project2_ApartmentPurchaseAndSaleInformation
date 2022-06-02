package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApartTransactionDTO implements Serializable {

    private int id;
    private String siGunGu;
    private String roadName;
    private String danjiName;
    private float area;
    private String contract_y_m;
    private int tradeAmount;
    private int floor;
    private int buildYear;

    private String priceOption;
    private String floorOption;
    private String buildYearOption;

    // TODO toString 재정의

}
