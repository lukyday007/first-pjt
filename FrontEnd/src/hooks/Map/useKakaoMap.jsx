import { useRef, useEffect, useCallback, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useMarker from "./useMarker";
import useCircleWithOverlay from "./useCircleWithOverlay";

const { kakao } = window;

const useKakaoMap = () => {
  const { gameStatus, areaCenter } = useContext(GameContext);

  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);

  useEffect(() => {
    if (gameStatus && mapRef.current && !mapInstanceRef.current) {
      const options = {
        center: new kakao.maps.LatLng(areaCenter.lat, areaCenter.lng),
        level: 1,
      };
      const mapInstance = new kakao.maps.Map(mapRef.current, options);
      mapInstanceRef.current = mapInstance;
    }
  }, [gameStatus]);
  
  useCircleWithOverlay(mapInstanceRef.current); // 플레이 영역을 표시
  useMarker(mapInstanceRef.current);  // 내 위치를 실시간으로 마커 표시

  // 내 위치로 이동하는 버튼 함수
  const panToMyLocation = useCallback((lat, lng) => {
    if (mapInstanceRef.current) {
      mapInstanceRef.current.panTo(new kakao.maps.LatLng(lat, lng));
    }
  }, [gameStatus]);

  return { mapRef, panToMyLocation };
};

export default useKakaoMap;
