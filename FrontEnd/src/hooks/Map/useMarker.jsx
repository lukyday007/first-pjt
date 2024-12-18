import { useEffect, useRef, useContext } from "react";
import { GameContext } from "@/context/GameContext";

const { kakao } = window;

const useMarker = mapInstance => {
  const markerRef = useRef(null);
  const { myLocation } = useContext(GameContext);
  const myLocationRef = useRef(myLocation);

  // 내 위치 변동에 따라 마커를 새로 set
  useEffect(() => {
    myLocationRef.current = myLocation;
    if (markerRef.current) {
      const newPosition = new kakao.maps.LatLng(
        myLocationRef.current.lat,
        myLocationRef.current.lng
      );
      markerRef.current.setPosition(newPosition);
    }
  }, [myLocation]);

  // 지도 최초 생성 시, 마커 생성 후 지도에 표시
  useEffect(() => {
    if (mapInstance && myLocation && !markerRef.current) {
      const marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(
          myLocationRef.current.lat,
          myLocationRef.current.lng
        ),
        map: mapInstance,
      });
      markerRef.current = marker;
    }
  }, [mapInstance]);

  return markerRef;
};

export default useMarker;
