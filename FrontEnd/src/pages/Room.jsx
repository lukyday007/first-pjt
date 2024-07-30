import React, { useContext, useEffect } from "react";
import { WebSocketContext } from "@/context/WebSocketContext";
import { GameContext } from "@/context/GameContext";
import GameSettingDialog from "@components/GameSettingDialog";

const Room = () => {
  const { setUserId, setGameRoomId } = useContext(GameContext);
  const { connect, disconnect } = useContext(WebSocketContext);

  // 방에 접속 시 userId, gameRoomId

  useEffect(() => {
    connect();

    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  return (
    // 방 접속 시 gameRoomId를 받아야 함

    <div className="flex h-screen flex-col items-center justify-center bg-white">
      <div>1. 랜덤 생성 QR code</div>

      <div>2. 랜덤 생성 방 번호</div>

      <div>3. 현재 참가자 목록</div>
    </div>
  );
};

export default Room;
