import useWebSocket from "@/hooks/Map/useWebSocket";
import React, { createContext, useState, useEffect } from "react";

// 게임 상태와 관련된 모든 상태를 관리하는 Context
export const GameContext = createContext();

export const GameProvider = ({ children }) => {
  const [gameRoomId, setGameRoomId] = useState(null); // 게임 방 번호
  const [targetId, setTargetId] = useState(null); // 타겟 ID(닉네임)

  // const [gameStatus, setGameStatus] = useState(false); // 게임 플레이 상태 여부, true: 게임 중, false: 게임 중이 아님
  const [gameStatus, setGameStatus] = useState(true); // 게임 플레이 상태 여부, true: 게임 중, false: 게임 중이 아님

  const [myLocation, setMyLocation] = useState(null); // 내 위치 정보, { lat: 0.0, lng: 0.0 }
  const [targetLocation, setTargetLocation] = useState(null); // 타겟 위치 정보
  const [areaCenter, setAreaCenter] = useState({ lat: 0, lng: 0 }); // 영역 중심 정보
  const [areaRadius, setAreaRadius] = useState(null); // 영역 반경 정보

  const { sendLocation } = useWebSocket();

  // 게임 상태가 "started"일 때만 위치 정보를 가져오는 로직
  // 게임이 시작되면(false => true) myLocation 정보가 생성됨
  useEffect(() => {
    if (gameStatus && navigator.geolocation) {
      const fetchLocation = () => {
        navigator.geolocation.getCurrentPosition(
          position => {
            const { latitude, longitude } = position.coords;
            setMyLocation({ lat: latitude, lng: longitude });
          },
          error => console.log(error)
        );
      };
      const intervalId = setInterval(fetchLocation, 500); // 1초마다 위치를 가져옴

      return () => clearInterval(intervalId); // 컴포넌트 unmount 시 inerval 클리어
    }
  }, [gameStatus]);

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        // 1초마다 내 위치 정보를 서버에 전송
        sendLocation({ lat: myLocation.lat, lng: myLocation.lng });
      }, 1000); // 1초

      // 컴포넌트 unmount 시 setInterval 종료
      return () => clearInterval(locationInterval);
    }
  }, [gameStatus, myLocation, sendLocation]);

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
      }}
    >
      {children}
    </GameContext.Provider>
  );
};
