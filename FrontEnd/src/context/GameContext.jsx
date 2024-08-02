import React, { createContext, useState, useEffect, useCallback } from "react";

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
  const [gameRoomId, setGameRoomId] = useState(
    () => localStorage.getItem("gameRoomId") || ""
  );
  const [gameRoomUsers, setGameRoomUsers] = useState([]); // 참여자 목록
  const [gameStatus, setGameStatus] = useState(false); // 게임 플레이 여부 (웹소켓 메시지에 따라 true로 전환되고, 이후 게임 종료 조건에 따라 false로 전환)
  const [areaCenter, setAreaCenter] = useState({ lat: 0, lng: 0 }); // 영역 중심
  const [areaRadius, setAreaRadius] = useState(null); // 영역 반경
  const [myLocation, setMyLocation] = useState({ lat: 0, lng: 0 }); // 내 위치
  const [targetId, setTargetId] = useState(null); // 타겟 ID(닉네임)
  const [targetLocation, setTargetLocation] = useState(null); // 타겟 위치
  const [distance, setDistance] = useState(null); // 사용자와 영역 중심 간 거리

  // sendGPS 함수에서 활용 (useFirebase.jsx)
  const username = localStorage.getItem("username");

  // gameRoomId 값에 변동이 있다면 localStorage에 저장
  // 기본적으로 /room 접속 시 useParams 활용해 gameRoomId를 세팅하나, 새로고침 등을 대비해 localStorage에 저장
  useEffect(() => {
    if (gameRoomId) {
      localStorage.setItem("gameRoomId", gameRoomId);
    }
  }, [gameRoomId]);

  // 내 위치를 실시간으로 변경
  // - 한편 위치 전송은 useFirebase에서 수행하고 GamePlay에서 사용
  useEffect(() => {
    if (!gameStatus || !navigator.geolocation) return;

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

    return () => clearInterval(intervalId);
  }, [gameStatus]);

  // 내 위치가 변경될 때마다 거리를 계산해, 영역에서 벗어나는지 판단
  // - 개인별 영역 이탈 시간 측정 관련 변수와 로직은 추후 추가 예정
  useEffect(() => {
    if (!gameStatus || !myLocation || !areaCenter) return;

    setDistance(
      approximateDistance(
        myLocation.lat,
        myLocation.lng,
        areaCenter.lat,
        areaCenter.lng
      )
    );
  }, [myLocation, areaCenter]);

  return (
    <GameContext.Provider
      value={{
        gameRoomId,
        setGameRoomId,
        gameRoomUsers,
        setGameRoomUsers,
        targetId,
        setTargetId,
        gameStatus,
        setGameStatus,
        myLocation,
        setMyLocation,
        targetLocation,
        setTargetLocation,
        areaCenter,
        setAreaCenter,
        areaRadius,
        setAreaRadius,
        distance,
        setDistance,
        username,
      }}
    >
      {children}
    </GameContext.Provider>
  );
};
