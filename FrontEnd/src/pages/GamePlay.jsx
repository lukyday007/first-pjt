import React, { useContext, useEffect } from "react";
import { useParams } from "react-router-dom";

import GameHeader from "@/components/GameHeader";
import MapComponent from "@/components/MapComponent";

import { GameContext } from "@/context/GameContext";
import useFirebase from "@/hooks/Map/useFirebase";
import useTimer from "@/hooks/Map/useTimer";

const GamePlay = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const {
    setGameRoomId,
    gameStatus,
    setGameStatus,
    myLocation,
    areaRadius,
    distance,
    username,
  } = useContext(GameContext);
  const { sendGPS } = useFirebase();
  const { decreaseTime } = useTimer();

  useEffect(() => {
    setGameRoomId(paramGameRoomId);
  }, [paramGameRoomId, setGameRoomId]);

  useEffect(() => {
    return () => setGameStatus(false);
  }, [setGameStatus]);

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        sendGPS(username, myLocation.lat, myLocation.lng); // 1초마다 위치 전송

        if (distance > areaRadius) {
          decreaseTime(); // 1초마다 영역 이탈 여부 체크해 시간 감소
        }
      }, 1000);

      return () => clearInterval(locationInterval);
    }
  }, [gameStatus, myLocation, sendGPS, username, decreaseTime]);

  return (
    <>
      {/* <GameHeader /> */}
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
