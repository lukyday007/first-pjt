import { useState, useEffect } from "react";

const useAreaInfo = () => {
  const [areaCenter, setAreaCenter] = useState({ lat: 0, lng: 0 }); // 영역 중심 정보에 관한 로직은 추후 변경
  const [areaRadius, setAreaRadius] = useState(200); // 반경 정보에 관한 로직은 추후 변경

  useEffect(() => {
    if (navigator.geolocation) {
      const success = position => {
        const { latitude, longitude } = position.coords;
        setAreaCenter({
          lat: latitude,
          lng: longitude,
        });
      };
      const error = err => {
        console.error("Error getting location:", err);
        document.getElementById("errorMsg").innerText =
          "getCurrentPosition: useAreaInfo";
      };

      // 위치 정보를 받는 데 성공한 경우 중심 위치로 설정
      // 로직은 추후 변경 예정
      navigator.geolocation.getCurrentPosition(success, error);
    } else {
      console.log("Geolocation is not supported by this browser.");
    }
  }, []);

  return { areaCenter, areaRadius };
};

export default useAreaInfo;
