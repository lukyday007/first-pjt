// import { useEffect } from "react";
import useAreaInfo from "@/hooks/Map/useAreaInfo";
import useCurrentLocation from "@/hooks/Map/useCurrentLocation";
import useDistanceCalculator from "@/hooks/Map/useDistanceCalculator";
import useKakaoMap from "@/hooks/Map/useKakaoMap";
// import useWebSocket from "@/hooks/Map/useWebSocket";

const MapComponent = () => {
  // areaCenter: 플레이 지역 중심
  // areaRadius: 플레이 지역 반경
  // location: 디바이스 사용자(나)의 위치
  // isLoading: 내 위치(location)를 가져오는 getCurrentPosition 실행 이후 페이지 렌더링 변경
  // distance: 플레이 지역 중심과 나의 위치 간 거리
  // mapRef: 지도
  const { areaCenter, areaRadius } = useAreaInfo(); // 플레이 지역 중심 및 반경을 결정하는 hook
  const { location, isLoading } = useCurrentLocation(); // 디바이스의 현재 위치를 잡는 hook
  const distance = useDistanceCalculator(location, areaCenter); // 플레이 지역 중심과 디바이스 현재 위치 간 거리를 계산하는 hook
  const mapRef = useKakaoMap(location, isLoading, areaCenter, areaRadius); // 지도를 그리는 hook

  // const roomId = '12345'  // 임시
  // const { otherLocations, sendLocation } = useWebSocket(roomId);

  // useEffect(() => {
  //   if (!isLoading && location) {
  //     sendLocation(location);
  //   }
  // }, [isLoading, location, sendLocation]);

  // 개발 중 임시로 지도 상단에 중심 위치, 내 위치, 거리 표시
  return isLoading ? (
    <div>
      <h1>Loading...</h1>
      <div id="errorMsg"></div>
    </div>
  ) : (
    <div>
      <h3>
        Cnt: {areaCenter.lat}º {areaCenter.lng}º
      </h3>
      <h3>
        Loc: {location.lat}º {location.lng}º
      </h3>
      <h3>Distance: {distance}m</h3>

      <div
        id="map"
        ref={mapRef}
        style={{ width: "100%", height: "500px", border: "1px solid black" }}
      />
    </div>
  );
};

export default MapComponent;
