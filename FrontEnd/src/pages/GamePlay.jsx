import React, { useContext, useEffect } from "react";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";
import useFirebase from "@/hooks/Map/useFirebase";
import { useParams } from "react-router-dom";

const GamePlay = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const { setGameRoomId, gameStatus, setGameStatus, username, myLocation } =
    useContext(GameContext);
  const { sendGPS } = useFirebase();

  useEffect(() => {
    setGameRoomId(paramGameRoomId);
  }, [paramGameRoomId, setGameRoomId]);

  useEffect(() => {
    return () => setGameStatus(false);
  }, [setGameStatus]);

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        sendGPS(username, myLocation.lat, myLocation.lng);
      }, 1000); // 1초마다 위치 전송

      return () => clearInterval(locationInterval); // 컴포넌트 unmount 시 interval 클리어
    }
  }, [gameStatus, myLocation, sendGPS, username]);

  return (
    <>
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
