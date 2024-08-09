import React, { useState, useEffect, useContext } from "react";

import GameHeader from "@/components/GameHeader";
import MapComponent from "@/components/MapComponent";
import CamChattingComponent from "@/components/CamChattingComponent";
import { GameContext } from "@/context/GameContext";
import { WebSocketContext } from "@/context/WebSocketContext";

import useSendGPS from "@/hooks/Map/useSendGPS";
import GameTime from "@/components/GameTime";
import useCatchTarget from "@/hooks/Map/useCatchTarget";
import GiveUpButton from "@/components/GiveUpGameButton";
import { Button } from "@/components/ui/Button";

import catchButton from "@/assets/gameplay-icon/catch-button.png";

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
            <img
              src={catchButton}
              alt="catch button"
              onClick={handleOnClickCatchTarget}
              className={`w-60 ${isAbleToCatchTarget ? "" : "cursor-not-allowed opacity-50"}`}
            />
            <div id="mini-buttons" className="mx-3 flex flex-col">
              <Button
                id="item-button"
                className="m-1 h-[8vh] w-[8vh] rounded-full border-2 border-black bg-white text-black"
              >
                Item
              </Button>
              <Button
                id="camchatting-button"
                onClick={toggleCamChatting}
                className="m-1 h-[8vh] w-[8vh] rounded-full border-2 border-black bg-white text-black"
              >
                Cam
              </Button>
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
