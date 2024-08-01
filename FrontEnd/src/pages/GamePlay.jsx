import React, { useContext, useEffect } from "react";
import { useParams } from "react-router-dom";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";
import useFirebase from "@/hooks/Map/useFirebase";
import useTimer from "@/hooks/Map/useTimer";

INITIAL_SAFETY_TIME = 60; // 영역 이탈 가능 시간 60초 초기 세팅 (localStorage의 값과 비교해 사용)

const GamePlay = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const {
    setGameRoomId,
    gameStatus,
    setGameStatus,
    userId,
    myLocation,
    areaRadius,
    distance,
  } = useContext(GameContext);
  const { sendGPS } = useFirebase();
  const { decreaseTime } = useTimer(INITIAL_SAFETY_TIME);

  useEffect(() => {
    setGameRoomId(paramGameRoomId);
  }, [paramGameRoomId, setGameRoomId]);

  useEffect(() => {
    return () => setGameStatus(false);
  }, [setGameStatus]);

  useEffect(() => {
    if (gameStatus && myLocation) {
      const locationInterval = setInterval(() => {
        sendGPS(userId, myLocation.lat, myLocation.lng); // 1초마다 위치 전송

        if (distance > areaRadius) {
          decreaseTime(); // 1초마다 영역 이탈 여부 체크해 시간 감소
        }
      }, 1000);

      return () => clearInterval(locationInterval); // 컴포넌트 unmount 시 interval 클리어
    }
  }, [gameStatus, myLocation, sendGPS, userId, decreaseTime]);

  return (
    <>
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
