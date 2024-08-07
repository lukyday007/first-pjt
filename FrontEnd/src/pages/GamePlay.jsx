import React, { useState, useEffect, useContext } from "react";

import GameHeader from "@/components/GameHeader";
import MapComponent from "@/components/MapComponent";
import CamChattingComponent from "@/components/CamChattingComponent";
import { GameContext } from "@/context/GameContext";
import { WebSocketContext } from "@/context/WebSocketContext";

import useSendGPS from "@/hooks/Map/useSendGPS";
import GameTime from "@/components/GameTime";
import CatchTargetButton from "@/components/CatchTargetButton";
import useCatchTarget from "@/hooks/Map/useCatchTarget";
import CheckMyItemButton from "@/components/CheckMyItemButton";
import CamChattingButton from "@/components/CamChattingButton";
import GiveUpButton from "@/components/GiveUpGameButton";

const GamePlay = () => {
  const { gameStatus } = useContext(GameContext);
  const { disconnect } = useContext(WebSocketContext);
  const { isAbleToCatchTarget, handleOnClickCatchTarget } = useCatchTarget();
  const [camChatting, setCamChatting] = useState(false); // camChatting 상태 초기화

  const toggleCamChatting = () => {
    setCamChatting(prevState => !prevState); // camChatting 상태 토글 함수
  };

  useSendGPS();

  useEffect(() => {
    return () => {
      if (!gameStatus) {
        disconnect();
      }
    };
  }, [disconnect]);

  return (
    <>
      <GameHeader />
      {camChatting ? (
        <>
          <GameTime />
          <CamChattingComponent />
        </>
      ) : (
        <>
          <MapComponent />
          <GameTime />
          <div className="flex justify-between">
            <div />
            <div />
            <div id="catch-button" className="flex justify-center">
              <CatchTargetButton
                onClick={handleOnClickCatchTarget}
                isDisabled={!isAbleToCatchTarget}
              />
            </div>
            <div />
            <div id="mini-buttons" className="mx-3 flex flex-col">
              <CheckMyItemButton />
              <CamChattingButton onClick={toggleCamChatting} />
              <GiveUpButton />
            </div>
            <div />
          </div>
        </>
      )}
    </>
  );
};

export default GamePlay;
