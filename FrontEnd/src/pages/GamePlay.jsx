import React, { useContext, useEffect } from "react";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";
import useWebSocket from "@/hooks/Map/useWebSocket";

const GamePlay = () => {
  const {
    gameStatus,
    myLocation,
    setGameStatus,
    setTargetLocation,
    setAreaCenter,
    setAreaRadius,
    gameRoomId,
    targetId,
  } = useContext(GameContext);

  const { sendLocation } = useWebSocket({
    gameRoomId,
    targetId,
    setGameStatus,
    setTargetLocation,
    setAreaCenter,
    setAreaRadius,
  });

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        sendLocation({ lat: myLocation.lat, lng: myLocation.lng });
      }, 1000); // 1초마다 위치 전송

      return () => clearInterval(locationInterval); // 컴포넌트 unmount 시 interval 클리어
    }
  }, [gameStatus, myLocation]);

  return (
    <>
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
