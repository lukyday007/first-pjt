// import { useState, useEffect } from 'react';

// // const harversineDistance = (lat1, lng1, lat2, lng2) => {
// //   if (Math.abs((lat2 - lat1) * (lng2 - lng1)) < 1e-8) {
// //     return 0;
// //   }

// //   const R = 6371;
// //   const toRadians = angle => angle * (Math.PI / 180);

// //   const dLat = toRadians(lat2 - lat1);
// //   const dLng = toRadians(lng2 - lng1);
// //   const lat1Rad = toRadians(lat1);
// //   const lat2Rad = toRadians(lat2);

// //   const a =
// //     Math.sin(dLat / 2) * Math.sin(dLat / 2) +
// //     Math.cos(lat1Rad) *
// //     Math.cos(lat2Rad) *
// //     Math.sin(dLng / 2) *
// //     Math.sin(dLng / 2);
// //   const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

// //   const distance = R * c * 1000;
// //   return distance.toFixed(2);
// // };

// const approximateDistance = (lat1, lng1, lat2, lng2) => {

//   const R = 6371; // 지구의 반지름 (km)
//   const toRadians = angle => angle * (Math.PI / 180);

//   const dLat = toRadians(lat2 - lat1);
//   const dLng = toRadians(lng2 - lng1);
//   const lat1Rad = toRadians(lat1);
//   const lat2Rad = toRadians(lat2);

//   const avgLatRad = (lat1Rad + lat2Rad) / 2;
//   const x = dLng * Math.cos(avgLatRad);
//   const distance = Math.sqrt(dLat * dLat + x * x) * R;

//   return (distance * 1000).toFixed(2); // 거리의 소수점 이하 2자리까지, 미터 단위
// };

// const useGeolocation = () => {
//   // 1. state
//   const [isLoading, setIsLoading] = useState(true);
//   const [location, setLocation] = useState(null);

//   const [areaCenter, setAreaCenter] = useState(null);
//   const [distance, setDistance] = useState(0);

//   // 2. constant

//   // 3. handler

//   // 4. useEffect
//   useEffect(() => {
//     const getPos = position => {
//       const { latitude, longitude } = position.coords;

//       // 방장이라면 중심 위치 결정 (추후 기능 추가)
//       if (areaCenter === null) {
//         setAreaCenter({
//           lat: parseFloat(latitude.toFixed(5)),
//           lng: parseFloat(longitude.toFixed(5)),
//         });
//       }
//     };

//     const failure = error => {
//       console.log(error);
//       setIsLoading(false);
//     };

//     navigator.geolocation.getCurrentPosition(getPos, failure);
//   }, []);

//   useEffect(() => {
//     // const getPos = position => {
//     //   const { latitude, longitude } = position.coords;

//     //   setLocation({
//     //     lat: parseFloat(latitude.toFixed(5)),
//     //     lng: parseFloat(longitude.toFixed(5)),
//     //   });
//     //   setIsLoading(false);
//     // };

//     const watchPos = position => {
//       const { latitude, longitude } = position.coords;

//       setLocation({
//         // 소수점 이하 5자리까지 사용 - 1m 단위까지 연산에 사용
//         lat: parseFloat(latitude.toFixed(5)),
//         lng: parseFloat(longitude.toFixed(5)),
//       });
//       setIsLoading(false);
//     };

//     const failure = error => {
//       console.log(error);
//       setIsLoading(false);
//     };

//     // navigator.geolocation.getCurrentPosition(getPos, failure);

//     const watchId = navigator.geolocation.watchPosition(watchPos, failure, {
//       enableHighAccuracy: false,
//       maximumAge: 10000,
//     });

//     return () => navigator.geolocation.clearWatch(watchId);
//   }, [areaCenter]);

//   useEffect(() => {
//     if (location && areaCenter) {
//       setDistance(
//         approximateDistance(
//           location.lat,
//           location.lng,
//           areaCenter.lat,
//           areaCenter.lng
//         )
//       );
//     }
//   }, [location, areaCenter]);

//   return { isLoading, location, areaCenter, distance };
// };

// export default useGeolocation;


//////////////////////////////////////////////
import { useState, useEffect, useCallback } from 'react';

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

const useGeolocation = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [location, setLocation] = useState(null);
  const [areaCenter, setAreaCenter] = useState(null);
  const [distance, setDistance] = useState(0);

  const updateDistance = useCallback(() => {
    if (location && areaCenter) {
      setDistance(approximateDistance(location.lat, location.lng, areaCenter.lat, areaCenter.lng));
    }
  }, [location, areaCenter]);

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

    const failure = error => {
      console.log(error);
      setIsLoading(false);
    };

    navigator.geolocation.getCurrentPosition(getPos, failure);
  }, [areaCenter]);

  useEffect(() => {
    if (areaCenter) {
      const watchPos = position => {
        const { latitude, longitude } = position.coords;

        setLocation({
          lat: parseFloat(latitude.toFixed(5)),
          lng: parseFloat(longitude.toFixed(5)),
        });
        setIsLoading(false);
      };

      const failure = error => {
        console.log(error);
        setIsLoading(false);
      };

      const watchId = navigator.geolocation.watchPosition(watchPos, failure, {
        enableHighAccuracy: false,
        maximumAge: 10000,
      });

      return () => navigator.geolocation.clearWatch(watchId);
    }
  }, [areaCenter]);

  useEffect(() => {
    updateDistance();
  }, [location, areaCenter, updateDistance]);

  return { isLoading, location, areaCenter, distance };
};

export default useGeolocation;
