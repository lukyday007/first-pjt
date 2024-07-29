import React, { createContext, useState, useEffect, useCallback } from "react";

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

const coordToFixed = (getLat, getLng) => {
  return {
    lat: getLat.toFixed(5), // 거리 연산에 위/경도의 소수점 5자리까지 사용
    lng: getLng.toFixed(5), // 거리 연산에 위/경도의 소수점 5자리까지 사용
  };
};

export const GameContext = createContext();

export const GameProvider = ({ children }) => {
  const [userId, setUserId] = useState(null); // 사용자 ID(닉네임)
  const [gameRoomId, setGameRoomId] = useState(1); // 게임 방 번호, 임시값
  const [targetId, setTargetId] = useState(null); // 타겟 ID(닉네임)
  const [gameStatus, setGameStatus] = useState(false); // 게임 플레이 상태 여부, true: 게임 중, false: 게임 중이 아님
  const [myLocation, setMyLocation] = useState({ lat: 0, lng: 0 }); // 내 위치 정보
  const [targetLocation, setTargetLocation] = useState(null); // 타겟 위치 정보
  const [areaCenter, setAreaCenter] = useState({ lat: 36.356, lng: 127.354 }); // 영역 중심 정보, 임시값
  const [areaRadius, setAreaRadius] = useState(200); // 영역 반경 정보, 임시값
  const [distance, setDistance] = useState(null); // 사용자와 게임 영역 중심 간 거리

  // 게임 상태가 "started"일 때만 위치 정보를 가져오는 로직
  useEffect(() => {
    if (!gameStatus) return;
    if (gameStatus && navigator.geolocation) {
      const fetchLocation = () => {
        navigator.geolocation.getCurrentPosition(
          position => {
            const { latitude, longitude } = position.coords;
            setMyLocation(coordToFixed(latitude, longitude));
          },
          error => console.log(error)
        );
      };
      const intervalId = setInterval(fetchLocation, 1000); // 1초마다 내 위치를 갱신 및 거리 계산

      return () => clearInterval(intervalId); // 컴포넌트 unmount 시 interval 클리어
    }
  }, [gameStatus]); // gameStatus가 변경될 때마다 useEffect 실행

  useEffect(() => {
    if (!gameStatus) return;
    if (myLocation && areaCenter) {
      setDistance(
        approximateDistance(
          myLocation.lat,
          myLocation.lng,
          areaCenter.lat,
          areaCenter.lng
        )
      );
    }
  }, [myLocation, areaCenter]); // 내 위치가 변경될 때마다 거리 계산

  return (
    <GameContext.Provider
      value={{
        gameRoomId,
        setGameRoomId,
        targetId,
        setTargetId,
        gameStatus,
        setGameStatus,
        myLocation,
        setMyLocation,
        targetLocation,
        setTargetLocation,
        areaCenter,
        areaRadius,
        setAreaCenter,
        setAreaRadius,
        distance,
        setDistance,
      }}
    >
      {children}
    </GameContext.Provider>
  );
};
