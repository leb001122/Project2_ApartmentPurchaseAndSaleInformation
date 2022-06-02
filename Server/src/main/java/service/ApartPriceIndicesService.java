package service;

import dto.ApartPriceIndicesDTO;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dao.ApartPriceIndicesDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class ApartPriceIndicesService {
    private ApartPriceIndicesDAO apartPriceIndicesDAO;
    private static final HashMap<String, String> regionMap;

    static {
        regionMap = new HashMap<>();
        regionMap.put("전국", "jeonguk_indices");
        regionMap.put("서울", "seoul_indices");
        regionMap.put("부산", "busan_indices");
        regionMap.put("대구", "daegu_indices");
        regionMap.put("인천", "incheon_indices");
        regionMap.put("광주", "gwangju_indices");
        regionMap.put("대전", "daejeon_indices");
        regionMap.put("울산", "ulsan_indices");
        regionMap.put("세종", "sejong_indices");
        regionMap.put("경기", "Gyeonggi_indices");
        regionMap.put("강원", "Gangwon_indices");
        regionMap.put("충북", "Chungbuk_indices");
        regionMap.put("충남", "Chungnam_indices");
        regionMap.put("전북", "Jeonbuk_indices");
        regionMap.put("전남", "Jeonnam_indices");
        regionMap.put("경북", "Gyeongbuk_indices");
        regionMap.put("경남", "Gyeongnam_indices");
        regionMap.put("제주", "Jeju_indices");
    }

    public ApartPriceIndicesService(SqlSessionFactory sqlSessionFactory) {
        apartPriceIndicesDAO = new ApartPriceIndicesDAO(sqlSessionFactory);
    }


    public ArrayList<ApartPriceIndicesDTO> selectByDateAndRegion(ApartPriceIndicesDTO dto) {
        // 사용자가 요청한 지역에 해당하는 모든 매매지수 -> list
        ArrayList<ApartPriceIndicesDTO> list = apartPriceIndicesDAO.selectByRegion(regionMap.get(dto.getRegion()));

        // 사용자가 요청한 날짜 이후의 매매지수 -> newList
        ArrayList<ApartPriceIndicesDTO> newList = new ArrayList<>();

        String reqDate = dto.getDate(); // 클라이언트가 요청할 떈 2003과 같은 년도만 요청됨

        // 해당 날짜 이후 리스트만 뽑기
        for (ApartPriceIndicesDTO item : list) {
            String changedDate = changeDateFormat(item.getDate());
            System.out.println(changedDate);

            if (changedDate.compareTo(reqDate) >= 0) {
                newList.add(item);
            }
        }

        // 삽입 정렬을 위해 배열로 변경
//        ApartPriceIndicesDTO [] resultArr = new ApartPriceIndicesDTO[newList.size()];
//        for (int i = 0; i < resultArr.length; i++) {
//            resultArr[i] = list.get(i);
//        }
//        ApartPriceIndicesDTO [] sortedArr =  insertionSort(resultArr);

        return newList;
    }

    /** 데이터베이스에 날짜가 11월-03과 같은 형식으로 저장되어 있기 떄문에
         정상적으로 날짜를 비교 가능하도록, 사용자가 보기 편하도록 2003/11의 형식으로 변경
     **/
    private String changeDateFormat(String date) {
        String token = "-";
        String[] splitResult = date.split(token);
        String year = "20" + splitResult[1];
        return year ;
    }

    // TODO 날짜별로 정렬
    private static ApartPriceIndicesDTO[] insertionSort(ApartPriceIndicesDTO[] arr) {

        for(int i = 1; i < arr.length; i++) {
            // 타겟 날짜
            String target = arr[i].getDate();

            int j = i - 1;

            // 타겟이 이전 원소보다 크기 전 까지 반복
            while(j >= 0 && target.compareTo(arr[j].getDate()) < 0) {
                arr[j + 1] = arr[j];	// 이전 원소를 한 칸씩 뒤로 미룬다.
                j--;
            }

            arr[j + 1] = arr[i];
        }
        return arr;
    }


    // TODO 로지스틱 회귀 이용하여 예측 지수까지 포함하여 리턴

}
