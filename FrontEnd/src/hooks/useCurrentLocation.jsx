import { useState, useEffect } from 'react';
import { throttle } from 'lodash';

const useCurrentLocation = () => {
  const [location, setLocation] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (navigator.geolocation) {
      // 위치를 가져오는 함수
      const fetchLocation = () => {
        navigator.geolocation.getCurrentPosition(
          position => {
            console.log(position.coords);
            const { latitude, longitude } = position.coords;
            setLocation({ lat: latitude, lng: longitude });
            setIsLoading(false);
          },
          () => {
            setIsLoading(false);
          }
        );
      };

      // throttle을 사용하여 fetchLocation을 1초마다 실행
      const throttledFetchLocation = throttle(fetchLocation, 1000);

      // 주기적으로 위치를 가져오기 위해 setInterval 사용
      const intervalId = setInterval(throttledFetchLocation, 1000);

      // 컴포넌트가 언마운트될 때 interval을 클리어
      return () => {
        clearInterval(intervalId);
      };
    } else {
      setIsLoading(false);
    }
  }, []);

  return { location, isLoading };
};

export default useCurrentLocation;
