// useKakaoMap.jsx
import { useRef, useEffect } from "react";
import useMarker from "./useMarker";
// import useArea from "./useArea";
import useCircleWithOverlay from "./useCircleWithOverlay";

const { kakao } = window;

const useKakaoMap = (location, isLoading, areaCenter, areaRadius) => {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);

  useEffect(() => {
    if (!isLoading && mapRef.current && !mapInstanceRef.current) {
      const options = {
        center: new kakao.maps.LatLng(location.lat, location.lng),
        level: 1,
      };
      const mapInstance = new kakao.maps.Map(mapRef.current, options);
      mapInstanceRef.current = mapInstance;
    }
  }, [isLoading]);

  useMarker(mapInstanceRef.current, location);
  // useArea(mapInstanceRef.current, areaCenter, areaRadius);
  useCircleWithOverlay(mapInstanceRef.current, areaCenter, areaRadius);

  return mapRef;
};

export default useKakaoMap;
