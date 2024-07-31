import React, { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { WebSocketContext } from "@/context/WebSocketContext";
import { GameContext } from "@/context/GameContext";
import { useParams } from "react-router-dom";

const Room = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const { gameStatus, gameRoomId, setGameRoomId } = useContext(GameContext);
  const { connect, disconnect } = useContext(WebSocketContext);
  const navigate = useNavigate();

  // 방에 접속 시 userId, gameRoomId 설정 및 WebSocket 연결
  useEffect(() => {
    setGameRoomId(paramGameRoomId);
    connect();

    return () => {
      disconnect();
    };
  }, [connect, disconnect, paramGameRoomId, setGameRoomId]);

  useEffect(() => {
    // WebSocketContext.jsx에서 메시지 수신 여부에 따라 gameStatus true로 변경
    // gameStatus 변동 시 /game-play로 이동하도록 함
    if (gameStatus && gameRoomId) {
      navigate(`/game-play/${gameRoomId}`);
    }
  }, [navigate, gameStatus, gameRoomId]);

  return (
    <div className="flex h-screen flex-col items-center justify-center bg-white">
      <div>1. 랜덤 생성 QR code</div>

      <div>2. 랜덤 생성 방 번호</div>

      <div>3. 현재 참가자 목록</div>
    </div>
  );
};

export default Room;
