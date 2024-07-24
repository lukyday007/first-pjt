import { useEffect, useRef } from "react";

const { kakao } = window;

const useArea = (mapInstance, areaCenter, areaRadius) => {
  const circleRef = useRef(null);
  const centerPointRef = useRef(null);

  useEffect(() => {
    if (mapInstance && !circleRef.current) {
      const circle = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(areaCenter.lat, areaCenter.lng),
        radius: areaRadius,
        strokeWeight: 5,
        strokeColor: "#75B8FA",
        strokeOpacity: 1,
        strokeStyle: "solid",
        fillColor: "#CFE7FF",
        fillOpacity: 0.25,
      });

      const centerPoint = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(areaCenter.lat, areaCenter.lng),
        radius: 0.25,
        strokeWeight: 5,
        strokeColor: "#000000",
        fillcolor: "#000000",
      });

      circle.setMap(mapInstance);
      centerPoint.setMap(mapInstance);
      circleRef.current = circle;
      centerPointRef.current = centerPoint;
    }
  }, [mapInstance]);

  useEffect(() => {
    if (circleRef.current) {
      // const newCenter = new kakao.maps.LatLng(areaCenter.lat, areaCenter.lng);
      // circleRef.current.setPosition(newCenter);
      circleRef.current.setRadius(areaRadius);
      // centerPointRef.current.setPosition(newCenter);
    }
  }, [areaCenter, areaRadius]);

  return { circleRef, centerPointRef };
};

export default useArea;
