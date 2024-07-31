import React, { useContext, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { WebSocketContext } from "@/context/WebSocketContext";
import { GameContext } from "@/context/GameContext";
import axiosInstance from "@/api/axiosInstance.js";

import { Button } from "@components/ui/Button";

const Room = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const { gameStatus, myLocation, gameRoomId, setGameRoomId } =
    useContext(GameContext);
  const { connect, disconnect } = useContext(WebSocketContext);
  const navigate = useNavigate();

  // GameSettingDialog 컴포넌트에서 보낸 state에서 QR과 방 코드를 추출
  const location = useLocation();
  const { qrCode, gameCode } = location.state || {};

  const handleStartGame = async () => {
    try {
      const response = await axiosInstance.post(
        "/gameroom/${gameRoomId}/start",
        {
          centerLat: myLocation.lat,
          centerLng: myLocation.lng,
        }
      );

      if (response.status == 200) {
        navigate();
      } else {
        setError("게임 시작에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (err) {
      setError(
        "서버와 통신하는 중에 문제가 발생했습니다. 나중에 다시 시도해주세요."
      );
    }
  };

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
      <div>1. QR code</div>
      {qrCode && <img src={qrCode} alt={qrCode} className="h-40 w-40" />}

      <div>2. 방 코드</div>
      {gameCode}

      <div>3. 현재 참가자 목록</div>
      <Button
        className="mb-8 bg-theme-color-1 font-bold"
        onClick={handleStartGame}
      >
        게임 시작
      </Button>
    </div>
  );
};

export default Room;
