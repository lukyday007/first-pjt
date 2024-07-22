// import { useState, useEffect } from 'react';

// const harversineDistance = (lat1, lng1, lat2, lng2) => {
//   if (Math.abs((lat2 - lat1) * (lng2 - lng1)) < 1e-8) {
//     return 0;
//   }

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
//   return distance.toFixed(2);
// };

// const useGeolocation = () => {
//   // 1. state
//   const [isLoading, setIsLoading] = useState(true);
//   const [location, setLocation] = useState({ lat: 0, lng: 0 });

//   const [areaCenter, setAreaCenter] = useState(false);
//   const [distance, setDistance] = useState(0);

//   // 2. constant

//   // 3. handler

//   // 4. useEffect
//   useEffect(() => {
//     const getPos = position => {
//       const { latitude, longitude } = position.coords;

//       // 방장이라면 중심 위치 결정 (추후 기능 추가)
//       setAreaCenter({
//         lat: parseFloat(latitude.toFixed(5)),
//         lng: parseFloat(longitude.toFixed(5)),
//       });

//       setLocation({
//         lat: parseFloat(latitude.toFixed(5)),
//         lng: parseFloat(longitude.toFixed(5)),
//       });
//       setIsLoading(false);
//     };

//     const watchPos = position => {
//       const { latitude, longitude } = position.coords;

//       setLocation({
//         // 소수점 이하 5자리까지 사용 - 1m 단위까지 연산에 사용
//         lat: parseFloat(latitude.toFixed(5)),
//         lng: parseFloat(longitude.toFixed(5)),
//       });
//       setDistance(
//         harversineDistance(
//           location.lat,
//           location.lng,
//           areaCenter.lat,
//           areaCenter.lng
//         )
//       );

//       // setIsLoading(false);
//     };

//     const failure = error => {
//       console.log(error);
//       setIsLoading(false);
//     };

//     navigator.geolocation.getCurrentPosition(getPos, failure);

//     const watchId = navigator.geolocation.watchPosition(watchPos, failure, {
//       enableHighAccuracy: true,
//       maximumAge: 10000,
//     });

//     return () => navigator.geolocation.clearWatch(watchId);
//   }, []);

//   return { isLoading, location, areaCenter, distance };
// };

// export default useGeolocation;

import { useState, useEffect } from 'react';

const harversineDistance = (lat1, lng1, lat2, lng2) => {
  if (Math.abs((lat2 - lat1) * (lng2 - lng1)) < 1e-8) {
    return 0;
  }

  const R = 6371; // 지구의 반지름 (km)
  const toRadians = angle => angle * (Math.PI / 180);

  const dLat = toRadians(lat2 - lat1);
  const dLng = toRadians(lng2 - lng1);
  const lat1Rad = toRadians(lat1);
  const lat2Rad = toRadians(lat2);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1Rad) *
      Math.cos(lat2Rad) *
      Math.sin(dLng / 2) *
      Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  const distance = R * c;
  return distance.toFixed(2); // 거리의 소수점 이하 2자리까지
};

const useGeolocation = () => {
  // 1. state
  const [isLoading, setIsLoading] = useState(true);
  const [location, setLocation] = useState({ lat: 0, lng: 0 });
  const [areaCenter, setAreaCenter] = useState(null); // 초기값을 null로 설정
  const [distance, setDistance] = useState(0);

  // 2. useEffect
  useEffect(() => {
    const getPos = position => {
      const { latitude, longitude } = position.coords;

      if (areaCenter === null) {
        // areaCenter가 null일 때만 설정
        setAreaCenter({
          lat: parseFloat(latitude.toFixed(5)),
          lng: parseFloat(longitude.toFixed(5)),
        });
      }

      setLocation({
        lat: parseFloat(latitude.toFixed(5)),
        lng: parseFloat(longitude.toFixed(5)),
      });
      setIsLoading(false);
    };

    const watchPos = position => {
      const { latitude, longitude } = position.coords;

      setLocation({
        lat: parseFloat(latitude.toFixed(5)),
        lng: parseFloat(longitude.toFixed(5)),
      });
    };

    const failure = error => {
      console.log(error);
      setIsLoading(false);
    };

    navigator.geolocation.getCurrentPosition(getPos, failure);

    const watchId = navigator.geolocation.watchPosition(watchPos, failure, {
      enableHighAccuracy: true,
      maximumAge: 10000,
    });

    return () => navigator.geolocation.clearWatch(watchId);
  }, [areaCenter]);

  useEffect(() => {
    if (location.lat && areaCenter && areaCenter.lat) {
      setDistance(
        harversineDistance(
          location.lat,
          location.lng,
          areaCenter.lat,
          areaCenter.lng
        )
      );
    }
  }, [location, areaCenter]);

  return { isLoading, location, areaCenter, distance };
};

export default useGeolocation;
