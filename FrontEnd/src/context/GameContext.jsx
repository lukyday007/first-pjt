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
  // /room/:gameRoomId 입장 시 userId, gameRoomId 설정
  // WebSocket 통신 msg 통해 gameStatus를 true로 전환, 이후 게임 종료 조건에 따라 false로 전환
  // 게임 시작 시 BE단에서 areaCenter, areaRadius 수신해 설정
  // 이후 targetId, targetLocation 수신해 설정 및 지속 감시
  // myLocation은 gameStatus===true일 때 getCurrentPosition을 사용해 매초 변경, Firebase를 통한 위치 전송 함수는 useFirebase.jsx에서 정의되고 GamePlay.jsx에서 사용
  // myLocation 변경에 따라 distance 변경

  const [userId, setUserId] = useState(() => {
    return localStorage.getItem("username");
  }); // 사용자 ID(닉네임) - sendGPS 함수에서 활용 (useFirebase.jsx)
  const [gameRoomId, setGameRoomId] = useState(() => {
    return localStorage.getItem("gameRoomId") || "";
  }); // 게임 방 번호, localStorage 관리
  const [gameRoomUsers, setGameRoomUsers] = useState([]); // 게임 방에 참여 중인 인원 목록
  const [gameStatus, setGameStatus] = useState(false); // 게임 플레이 상태 여부, true: 게임 중, false: 게임 중이 아님
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
  }); // 영역 중심 정보, localStorage 관리
  const [areaRadius, setAreaRadius] = useState(() => {
    const savedRadius = localStorage.getItem("areaRadius");
    return savedRadius !== null ? parseFloat(savedRadius) : null;
  }); // 영역 반경 정보, localStorage 관리
  const [myLocation, setMyLocation] = useState({ lat: 0, lng: 0 }); // 내 위치 정보
  const [targetId, setTargetId] = useState(() => {
    const savedTargetId = localStorage.getItem("targetId");
    return savedTargetId !== null ? savedTargetId : null;
  }); // 타겟 ID(닉네임), localStorage 관리
  const [targetLocation, setTargetLocation] = useState(null); // 타겟 위치 정보
  const [distance, setDistance] = useState(null); // 사용자와 게임 영역 중심 간 거리
  const [distToTarget, setDistToTarget] = useState(null); // 사용자와 타겟 간 거리

  // gameRoomId 값에 변동이 있다면 localStorage에 저장
  // 기본적으로 /room 접속 시 useParams 활용해 gameRoomId를 세팅하나, 새로고침 등을 대비해 localStorage에 저장
  useEffect(() => {
    if (gameRoomId) {
      localStorage.setItem("gameRoomId", gameRoomId);
    }
  }, [gameRoomId]);

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

  // 게임 상태가 "started"일 때만 위치 정보를 가져오는 로직
  useEffect(() => {
    if (!gameStatus || !navigator.geolocation) return;

    const intervalId = setInterval(fetchLocation, 1000); // 1초마다 내 위치 및 거리 계산 함수 실행

    return () => clearInterval(intervalId); // 컴포넌트 unmount 시 interval 클리어
  }, [gameStatus, areaCenter]); // gameStatus가 변경 시 실행

  return (
    <GameContext.Provider
      value={{
        userId,
        setUserId,
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
      }}
    >
      {children}
    </GameContext.Provider>
  );
};
