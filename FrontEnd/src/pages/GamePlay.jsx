// src/pages/GamePlay.jsx
import React, { useContext, useEffect } from "react";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";

const GamePlay = () => {
  const { gameStatus } = useContext(GameContext);

  useEffect(() => {
    if (gameStatus === true) {
      // 게임이 시작되었을 때 실행할 로직
    }
  }, [gameStatus]);

  return (
    <>
      <div>GamePlay Page</div>
      <MapComponent />
    </>
  );
};

export default GamePlay;
