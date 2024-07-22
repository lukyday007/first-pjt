import { useRef, useState, useEffect } from 'react';

const { kakao } = window;

const useKakaoMap = (location, isLoading) => {
  // 1. state
  // const [areaCenter, setAreaCenter] = useState();
  // const [distance, setDistance] = useState(0);

  // 2. constant
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const markerRef = useRef(null);

  // 3. handler

  // 4. useEffect

  // // useEffect - 방장이라면 중심 위치 결정 (수정 필요)
  // useEffect(() => {
  //   if (!isLoading && !!areaCenter) {
  //     const { latitude, longitude } = location;
  //     const roundedLat = parseFloat(latitude.toFixed(5));
  //     const roundedLng = parseFloat(longitude.toFixed(5));

  //     setAreaCenter({
  //       lat: roundedLat,
  //       lng: roundedLng,
  //     });
  //   }
  // });

  // useEffect - Kakao Map 초기화
  useEffect(() => {
    if (!isLoading && mapRef.current && !mapInstanceRef.current) {
      const options = {
        center: new kakao.maps.LatLng(location.lat, location.lng),
        level: 1,
      };
      const mapInstance = new kakao.maps.Map(mapRef.current, options);
      mapInstanceRef.current = mapInstance;

      const marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(location.lat, location.lng),
        map: mapInstance,
      });
      markerRef.current = marker;
    }
  }, [isLoading]);

  // useEffect - 사용자 위치 업데이트
  useEffect(() => {
    // if (!isLoading) {
    //   // 중심으로부터 거리 계산
    //   setDistance(
    //     harversineDistance(
    //       location.lat,
    //       location.lng,
    //       areaCenter.lat,
    //       areaCenter.lng
    //     )
    //   );
    // }

    if (mapInstanceRef.current && markerRef.current) {
      const newPosition = new kakao.maps.LatLng(location.lat, location.lng);
      // mapInstanceRef.current.setCenter(newPosition);
      markerRef.current.setPosition(newPosition);
    }
  }, [location]);

  return mapRef;
};

export default useKakaoMap;
