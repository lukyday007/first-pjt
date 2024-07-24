import { useState, useEffect } from "react";

// const harversineDistance = (lat1, lng1, lat2, lng2) => {
//   const R = 6371;
//   const toRadians = angle => angle * (Math.PI / 180);

//   const dLat = toRadians(lat2 - lat1);
//   const dLng = toRadians(lng2 - lng1);
//   const lat1Rad = toRadians(lat1);
//   const lat2Rad = toRadians(lat2);

//   const a =
//     Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//     Math.cos(lat1Rad) *
//       Math.cos(lat2Rad) *
//       Math.sin(dLng / 2) *
//       Math.sin(dLng / 2);
//   const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

//   const distance = R * c;
//   return (distance * 1000).toFixed(1); // 거리의 소수점 이하 1자리까지, 미터 단위
// };

const approximateDistance = (lat1, lng1, lat2, lng2) => {
  const R = 6371; // 지구의 반지름 (km)
  const toRadians = angle => angle * (Math.PI / 180);

  const dLat = toRadians(lat2 - lat1);
  const dLng = toRadians(lng2 - lng1);
  const lat1Rad = toRadians(lat1);
  const lat2Rad = toRadians(lat2);

  const avgLatRad = (lat1Rad + lat2Rad) / 2;
  const x = dLng * Math.cos(avgLatRad);
  const distance = Math.sqrt(dLat * dLat + x * x) * R;

  return (distance * 1000).toFixed(1); // 거리의 소수점 이하 1자리까지, 미터 단위
};

const coordToFixed = number => {
  return parseFloat(number.toFixed(5)); // 거리 연산에 위/경도의 소수점 5자리까지 사용
};

const useDistanceCalculator = (location, areaCenter) => {
  const [distance, setDistance] = useState(0);

  useEffect(() => {
    if (location && areaCenter) {
      const lat1 = coordToFixed(location.lat);
      const lng1 = coordToFixed(location.lng);
      const lat2 = coordToFixed(areaCenter.lat);
      const lng2 = coordToFixed(areaCenter.lng);

      const calculatedDistance = approximateDistance(lat1, lng1, lat2, lng2);
      setDistance(calculatedDistance);
    }
  }, [location, areaCenter]);

  return distance;
};

export default useDistanceCalculator;
