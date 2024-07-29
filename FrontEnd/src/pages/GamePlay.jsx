import React, { useContext, useEffect } from "react";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";
import useWebSocket from "@/hooks/Map/useWebSocket";
import useFirebase from "@/hooks/Map/useFirebase";

const GamePlay = () => {
  const { gameStatus, setGameStatus, userId, myLocation } =
    useContext(GameContext);
  const { sendGPS } = useFirebase();
  useWebSocket();

  useEffect(() => {
    setGameStatus(true);

    return () => setGameStatus(false);
  }, [setGameStatus]);

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        sendGPS(userId, myLocation.lat, myLocation.lng);
      }, 1000); // 1초마다 위치 전송

      return () => clearInterval(locationInterval); // 컴포넌트 unmount 시 interval 클리어
    }
  }, [gameStatus, myLocation, sendGPS, userId]);

  return (
    <>
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
