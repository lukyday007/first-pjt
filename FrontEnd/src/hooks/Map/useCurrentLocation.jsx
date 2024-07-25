import { useState, useEffect } from "react";
import { throttle } from "lodash";

const useCurrentLocation = () => {
  const [location, setLocation] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (navigator.geolocation) {
      // 위치를 가져오는 함수
      const fetchLocation = () => {
        navigator.geolocation.getCurrentPosition(
          position => {
            const { latitude, longitude } = position.coords;
            setLocation({ lat: latitude, lng: longitude });
            setIsLoading(false);
          },
          () => {
            setIsLoading(false);
            document.getElementById("errorMsg").innerText =
              "getCurrentPosition: useCurrentLocation";
          }
        );
      };

      // throttle을 사용하여 fetchLocation을 1초마다 실행
      // 1초 내에 여러 번 fetchLocation 함수가 실행되더라도 1번만 실행되도록 보장해 줌
      // throttle 자체로는 주기적인 호출을 설정해줄 수 없음
      const throttledFetchLocation = throttle(fetchLocation, 1000);

      // 주기적으로 위치를 가져오기 위해 setInterval 사용
      // setInterval만으로 위치 정보를 가져오는 데는 충분하기 때문에, throttle이 필요하지 않을 수 있음
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
