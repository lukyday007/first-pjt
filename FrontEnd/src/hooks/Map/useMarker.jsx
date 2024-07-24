import { useEffect, useRef } from "react";

const { kakao } = window;

const useMarker = (mapInstance, location) => {
  const markerRef = useRef(null);

  // 지도 최초 생성 시, 마커 생성 후 지도에 표시
  useEffect(() => {
    if (mapInstance && !markerRef.current) {
      const marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(location.lat, location.lng),
        map: mapInstance,
      });
      markerRef.current = marker;
    }
  }, [mapInstance]);

  // 내 위치 변동에 따라 마커를 새로 set
  useEffect(() => {
    if (markerRef.current) {
      const newPosition = new kakao.maps.LatLng(location.lat, location.lng);
      markerRef.current.setPosition(newPosition);
    }
  }, [location]);

  return markerRef;
};

export default useMarker;
