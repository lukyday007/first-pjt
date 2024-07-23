import { useState, useEffect, useCallback } from 'react';
import { throttle } from 'lodash';

const approximateDistance = (lat1, lng1, lat2, lng2) => {
  const R = 6371; // 지구의 반지름 (km)
  const toRadians = angle => angle * (Math.PI / 180);

  const dLat = toRadians(lat2 - lat1);
  const dLng = toRadians(lng2 - lng1);
  const avgLatRad = (toRadians(lat1) + toRadians(lat2)) / 2;
  const x = dLng * Math.cos(avgLatRad);
  const distance = Math.sqrt(dLat * dLat + x * x) * R;

  return (distance * 1000).toFixed(2); // 거리의 소수점 이하 2자리까지, 미터 단위
};

const useGeolocation = (throttleInterval = 2000) => {
  const [isLoading, setIsLoading] = useState(true);
  const [location, setLocation] = useState(null);
  const [areaCenter, setAreaCenter] = useState(null);
  const [distance, setDistance] = useState(0);

  const updateDistance = useCallback(() => {
    if (location && areaCenter) {
      setDistance(
        approximateDistance(
          location.lat,
          location.lng,
          areaCenter.lat,
          areaCenter.lng
        )
      );
    }
  }, [areaCenter]);

  const throttledGetCurrentPosition = useCallback(
    throttle(callback => {
      navigator.geolocation.getCurrentPosition(
        position => callback(position),
        error => console.log(error),
        { enableHighAccuracy: true, maximumAge: 10000 }
      );
    }, throttleInterval),
    [throttleInterval]
  );

  useEffect(() => {
    const getPos = position => {
      const { latitude, longitude } = position.coords;

      if (areaCenter === null) {
        setAreaCenter({
          lat: parseFloat(latitude.toFixed(5)),
          lng: parseFloat(longitude.toFixed(5)),
        });
      }
    };

    throttledGetCurrentPosition(getPos);
  }, []);

  useEffect(() => {
    if (areaCenter) {
      const handlePosition = position => {
        const { latitude, longitude } = position.coords;

        setLocation({
          lat: parseFloat(latitude.toFixed(5)),
          lng: parseFloat(longitude.toFixed(5)),
        });
        if (isLoading) {
          setIsLoading(false);
        }
      };

      throttledGetCurrentPosition(handlePosition);
    }
  }, [areaCenter, throttledGetCurrentPosition]);

  useEffect(() => {
    updateDistance();
  }, [location]);

  return { isLoading, location, areaCenter, distance };
};

export default useGeolocation;
