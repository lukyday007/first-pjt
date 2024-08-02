import React, { createContext, useState, useEffect } from "react";

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
  const [gameRoomId, setGameRoomId] = useState(() => {
    return localStorage.getItem("gameRoomId") || "";
  }); // 게임 방 번호, localStorage 관리
  const [gameRoomUsers, setGameRoomUsers] = useState([]); // 참여자 목록
  const [gameStatus, setGameStatus] = useState(false); // 게임 플레이 여부 (웹소켓 메시지에 따라 true로 전환되고, 이후 게임 종료 조건에 따라 false로 전환)
  const [areaCenter, setAreaCenter] = useState(() => {
    const savedCenter = localStorage.getItem("areaCenter");
    if (savedCenter) {
      const parsedCenter = JSON.parse(savedCenter);
      return {
        lat: parseFloat(parsedCenter.lat),
        lng: parseFloat(parsedCenter.lng),
      };
    }
    return { lat: 0, lng: 0 };
  }); // 영역 중심, localStorage 관리
  const [areaRadius, setAreaRadius] = useState(() => {
    const savedRadius = localStorage.getItem("areaRadius");
    return savedRadius !== null ? parseFloat(savedRadius) : null;
  }); // 영역 반경, localStorage 관리
  const [myLocation, setMyLocation] = useState({ lat: 0, lng: 0 }); // 내 위치
  const [targetId, setTargetId] = useState(() => {
    const savedTargetId = localStorage.getItem("targetId");
    return savedTargetId !== null ? savedTargetId : null;
  }); // 타겟 ID(닉네임), localStorage 관리
  const [targetLocation, setTargetLocation] = useState(null); // 타겟 위치
  const [distance, setDistance] = useState(null); // 사용자와 영역 중심 간 거리
  const [distToTarget, setDistToTarget] = useState(null); // 사용자와 타겟 간 거리
  const username = localStorage.getItem("username"); // sendGPS 함수에서 활용 (useFirebase.jsx)

  // 내 위치를 잡고, 거리를 계산하는 함수
  const fetchLocation = () => {
    navigator.geolocation.getCurrentPosition(
      position => {
        const { latitude, longitude } = position.coords;
        const newLocation = coordToFixed(latitude, longitude);
        setMyLocation(newLocation);

        if (areaCenter) {
          setDistance(
            approximateDistance(
              newLocation.lat,
              newLocation.lng,
              areaCenter.lat,
              areaCenter.lng
            )
          );
        }

        if (targetLocation) {
          setDistToTarget(
            approximateDistance(
              newLocation.lat,
              newLocation.lng,
              targetLocation.lat,
              targetLocation.lng
            )
          );
        }
      },
      error => console.log(error)
    );
  };

  // gameRoomId 값에 변동이 있다면 localStorage에 저장
  // 기본적으로 /room 접속 시 useParams 활용해 gameRoomId를 세팅하나, 새로고침 등을 대비해 localStorage에 저장
  useEffect(() => {
    if (gameRoomId) {
      localStorage.setItem("gameRoomId", gameRoomId);
    }
  }, [gameRoomId]);

  // 게임 상태가 "started"일 때만 위치 정보를 가져오는 로직
  // 내 위치를 실시간으로 변경
  // - 한편 위치 전송은 useFirebase에서 수행하고 GamePlay에서 사용
  useEffect(() => {
    if (!gameStatus || !navigator.geolocation) return;

    const intervalId = setInterval(fetchLocation, 1000); // 1초마다 내 위치 및 거리 계산 함수 실행

    return () => clearInterval(intervalId);
  }, [gameStatus, areaCenter]);

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
        distToTarget,
        setDistToTarget,
        username,
      }}
    >
      {children}
    </GameContext.Provider>
  );
};
